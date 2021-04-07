package io.bitnews.passport.service;

import com.alibaba.fastjson.JSONObject;
import io.bienews.common.helper.PageUtil;
import io.bienews.common.helper.StringUtil;
import io.bienews.common.helper.exception.CommonException;
import io.bitnews.client.news.NewsServiceClient;
import io.bitnews.client.oauth.AuthServiceClient;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.RedisConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.*;
import io.bitnews.framework.mail.MailSender;
import io.bitnews.framework.mail.MailSenderParams;
import io.bitnews.framework.redis.RedisManager;
import io.bitnews.framework.sms.SmsParameter;
import io.bitnews.framework.sms.SmsSendResult;
import io.bitnews.framework.sms.SmsSender;
import io.bitnews.framework.sms.properties.SmsProperties;
import io.bitnews.model.external.SendCode;
import io.bitnews.model.external.UpdateVerifyCode;
import io.bitnews.model.external.UserRegistry;
import io.bitnews.model.external.VerifyCode;
import io.bitnews.model.internal.UserDataVo;
import io.bitnews.model.internal.UserVo;
import io.bitnews.model.passport.po.TUser;
import io.bitnews.passport.dao.TUserDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.beetl.sql.core.db.KeyHolder;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/7/11.
 */
@Service
@CacheConfig(cacheNames = "user")
@Slf4j
public class UserService {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    private TUserDao tUserDao;

    @Autowired
    private AuthServiceClient authServiceClient;

    @Autowired
    SmsSender smsSender;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TaskService taskService;

    @Autowired
    RedisManager redisManager;

    @Autowired
    SmsProperties smsProperties;

    @Autowired
    private MailSender mailSender;

    private Random random = new Random(37);

    private final static String PATH = "http://bitnewstest.oss-cn-hangzhou.aliyuncs.com/tx_default_";
    private final static String PATH_TAIL = ".jpg";

    @Transactional
    public BNResponse createByEmail(String email, String password) {
        TUser tUser = createUser(email, null, password);
        tUser.setNickName(createNickName());
        tUserDao.insertTemplate(tUser);
        return new BNResponse();
    }

    /**
     * 分页返回管理员
     *
     * @return
     */
    public BNPageResponse<UserVo> queryByPage(PageQuery<TUser> query) {
        tUserDao.selectByPage(query);
        List<TUser> tList = query.getList();
        List<UserVo> vList = tList.stream().map(ta -> {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(ta, userVo);
            BNResponse<BigDecimal> response = tokenService.queryTokenAmount(userVo.getId().toString());
            if (response.isToast()) {
                userVo.setToken(response.getBody());
            }
            return userVo;
        }).collect(Collectors.toList());
        return PageUtil.createPage(vList,
                query.getPageNumber(), query.getPageSize(), query.getTotalPage(), query.getTotalRow());
    }

    /**
     * 修改密码
     *
     * @param newPassword
     */
    @Caching(put = {
            @CachePut(value = "user", key = "#userId", unless = "#result == null")
    })
    public void modifyPassword(String userId, String newPassword) {
        TUser tUser = tUserDao.single(userId);
        tUser.setPassword(encoder.encode(newPassword));
        tUserDao.updateById(tUser);
        //请重新登录
        loginOut(userId);
    }

    /**
     * 修改用户名
     *
     * @param newUsername
     * @return
     */
    @Caching(put = {
            @CachePut(value = "user", key = "#userId", unless = "#result == null")
    })
    public TUser modifyNickname(String userId, String newUsername) {
        TUser tUser = tUserDao.single(userId);
        tUser.setNickName(newUsername);
        tUserDao.updateById(tUser);
        return tUser;
    }

    private TUser createUser(String email, String mobile, String password) {
        TUser tUser = new TUser();
        if (null != email) {
            tUser.setUsername(email);
            tUser.setEmail(email);

        }
        if (null != mobile) {
            tUser.setUsername(mobile);
            tUser.setPhoneNumber(mobile);
        }
        if (null != password && !CommonConstant.NOT_PASSWORD.equals(password)) {
            tUser.setPassword(encoder.encode(password));
        }else {
            tUser.setPassword(password);
        }
        tUser.setType(CommonConstant.USER_COMMON);
        tUser.setCreateTime(new Date());
        tUser.setStatus(CommonConstant.USER_STATUS);
        tUser.setFirstLogin(CommonConstant.IS_FIRST_LOGIN);
        tUser.setTxPicture(PATH + (10000 + random.nextInt(20)) + PATH_TAIL);
        return tUser;
    }

    private LoginDTO createLoginDTO(TUser tUser, JWT jwt) {
        LoginDTO loginDTO = new LoginDTO();
        BeanUtils.copyProperties(tUser, loginDTO);
        jwt.setToken_type("Bearer");
        loginDTO.setToken(jwt);
        tUser.setLastLoginTime(new Date());
        tUserDao.updateById(tUser);
        return loginDTO;
    }

    @Cacheable(value = "user", key = "#id", unless = "#result == null")
    public TUser findById(String id) {
        return tUserDao.single(Long.parseLong(id));
    }

    public TUser findByMobile(String mobile) {
        return tUserDao.findByMobile(mobile);
    }


    public TUser findByName(String name) {
        return tUserDao.findByUsername(name);
    }

    @Caching(put = {
            @CachePut(value = "user", key = "#tUser.id", unless = "#result == null")
    })
    public TUser modifyInfo(TUser tUser) {
        tUserDao.updateById(tUser);
        return tUser;
    }

    private void saveTokenToRedis(String name, String token) {
        String key = RedisConstant.LOGIN_KEY + name + RedisConstant.COLON_SEPARATE + token;
        redisManager.set(key, name, CommonConstant.SEVEN_DAY);
    }

    public void loginOut(String userId) {
        String keyPrex = RedisConstant.LOGIN_KEY + userId + RedisConstant.COLON_SEPARATE_MATCH;
        redisManager.deleteByPrex(keyPrex);
    }

    private String createNickName() {
        return "BN-" + RandomStringUtils.randomNumeric(5);
    }

    public void registry(UserRegistry userRegistry) {
        TUser user;
        if (CommonConstant.CONTACT_EMAIL.equalsIgnoreCase(userRegistry.getType())) {
            user = createUser(userRegistry.getUsername(), null, userRegistry.getPassword());
        } else {
            if (userRegistry.isSetPassword()) {
                user = createUser(null, userRegistry.getUsername(), userRegistry.getPassword());
            }else {
                user = createUser(null, userRegistry.getUsername(), CommonConstant.NOT_PASSWORD);
                user.setStatus(CommonConstant.USER_STATUS_NOTPASSWORD);
            }
        }
        user.setNickName(createNickName());
        tUserDao.insertTemplate(user);
    }

    public void sendCode(SendCode sendCode, String code) {
        if (CommonConstant.CONTACT_EMAIL.equalsIgnoreCase(sendCode.getType())) {
            try {
                sendMailCode(sendCode.getUsername(), code);
            } catch (Exception e) {
                log.error("发送邮件失败"+sendCode.getUsername(), e);
                throw new CommonException("邮件发送失败");
            }
        }else {
            SmsSendResult smsSendResult = sendSmsCode(sendCode.getUsername(), code);
            if (!smsSendResult.isSuccess()) {
                throw new CommonException("短信发送失败" + sendCode.getUsername());
            }
        }
    }

    private SmsSendResult sendSmsCode(String mobile, String code) {
        JSONObject object = new JSONObject();
        object.put("code", code);
        SmsParameter smsParameter = new SmsParameter();
        smsParameter.setPhoneNumber(mobile);
        smsParameter.setParams(object.toJSONString());
        return smsSender.send(smsParameter);
    }

    private void sendMailCode(String mail, String code) throws Exception {
        MailSenderParams em = new MailSenderParams();
        em.setSubject("邮件注册验证码");
        em.setContent(String.format(CommonConstant.SMS_EMAIL_CODE, CommonConstant.APP_NAME, CommonConstant.APP_NAME,
                code));
        em.setEmail(new String[]{mail});
        mailSender.send(em);
    }

    public LoginDTO loginByPassword(TUser tUser, String password) {
        if (!encoder.matches(password, tUser.getPassword())) {
            throw new CommonException(UserSdkErrorCode.USER_MAIL_CHECK_PASSWORD_FAILURE.getMessageZh());
        }
        JWT jwt = authServiceClient.getToken("Basic Ym4tb2F1dGg6MzIxNDU2", "password", tUser.getUsername(), password, null);
        // 获得用户菜单
        if (null == jwt) {
            throw new CommonException(UserSdkErrorCode.USER_MAIL_GET_TOKEN_FAIL.getMessageZh());
        }
        saveTokenToRedis(tUser.getId().toString(), jwt.getAccess_token());
        LoginDTO loginDTO = createLoginDTO(tUser, jwt);
        //判断是否为第一登录
        if (CommonConstant.IS_FIRST_LOGIN.equals(tUser.getFirstLogin())) {
            //发放积分
            loginDTO.setFirst(true);
            BaseResponse response = taskService.firstLogin(tUser.getId().toString());
            if (!response.isToast()) {
                throw new CommonException("发放积分");
            }
            tUser.setFirstLogin(CommonConstant.NOT_FIRST_LOGIN);
            tUserDao.updateById(tUser);
        }
        return loginDTO;
    }

    public LoginDTO loginFast(TUser tUser, String code) {
        JWT jwt = authServiceClient.getToken("Basic Ym4tb2F1dGg6MzIxNDU2", "password", tUser.getUsername(), code,
                CommonConstant.SMS_AUTH_TYPE);
        // 获得用户菜单
        if (null == jwt) {
            throw new CommonException(UserSdkErrorCode.USER_MAIL_GET_TOKEN_FAIL.getMessageZh());
        }
        saveTokenToRedis(tUser.getId().toString(), jwt.getAccess_token());
        LoginDTO loginDTO = createLoginDTO(tUser, jwt);
        if (CommonConstant.IS_FIRST_LOGIN.equals(tUser.getFirstLogin())) {
            //发放积分
            loginDTO.setFirst(true);
            BaseResponse response = taskService.firstLogin(tUser.getId().toString());
            if (!response.isToast()) {
                throw new CommonException("发放积分");
            }
            tUser.setFirstLogin(CommonConstant.NOT_FIRST_LOGIN);
            tUserDao.updateById(tUser);
        }
        return loginDTO;
    }

    public void updateAccountContact(Long userId, UpdateVerifyCode updateVerifyCode) {
        TUser tUser = new TUser();
        tUser.setId(userId);
        String username = "";
        if (CommonConstant.CONTACT_EMAIL.equals(updateVerifyCode.getType())) {
            tUser.setEmail(updateVerifyCode.getMail());
            username = updateVerifyCode.getMail();
        } else if (CommonConstant.CONTACT_MOBILE.equals(updateVerifyCode.getType())) {
            tUser.setPhoneNumber(updateVerifyCode.getPhone());
            username = updateVerifyCode.getPhone();
        }
        if (StringUtil.isEmpty(username)) {
            throw new CommonException("参数异常");
        }
        TUser check = findByName(username);
        Assert.isNull(check, "该账户已被注册");
        tUser.setUsername(username);
        String key = RedisConstant.LOGIN_KEY+ username;
        log.info("获取{}的验证码",key);
        VerifyCode verifyCode = (VerifyCode) redisManager.get(key);
        Assert.notNull(verifyCode, "验证失败");
        long currentTime = new Date().getTime()/1000;
        Assert.isTrue(verifyCode.getExpire()>currentTime, "验证码过期");
        Assert.isTrue(verifyCode.getCode().equals(updateVerifyCode.getCode()), "验证码错误");
        tUserDao.updateTemplateById(tUser);
    }
}
