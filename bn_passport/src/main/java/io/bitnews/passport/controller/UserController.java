package io.bitnews.passport.controller;

import io.bienews.common.helper.StringUtil;
import io.bienews.common.helper.TimeUtil;
import io.bienews.common.helper.exception.CommonException;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.RedisConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.common.model.LoginDTO;
import io.bitnews.framework.file.AliyunOosFileStorage;
import io.bitnews.framework.mail.MailSender;
import io.bitnews.framework.mail.MailSenderParams;
import io.bitnews.framework.redis.RedisManager;
import io.bitnews.model.external.*;
import io.bitnews.model.internal.UserDataVo;
import io.bitnews.model.internal.UserVo;
import io.bitnews.model.passport.po.TUser;
import io.bitnews.passport.service.TokenService;
import io.bitnews.passport.service.UserService;
import io.bitnews.passport.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.RandomStringUtils;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by ywd on 2019/7/11.
 */
@Slf4j
@Api(tags = "????????????????????????")
@RestController
@RequestMapping("/v1/passport/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private AliyunOosFileStorage aliyunOosFileStorage;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/registry")
    @ApiOperation(value = "????????????")
    public BNResponse<String> registry(@RequestBody UserRegistry userRegistry) {
        String type = userRegistry.getType();
        String username = userRegistry.getUsername();
        String password = userRegistry.getPassword();
        String code = userRegistry.getCode();
        Assert.notNull(type, "type????????????");
        Assert.notNull(username, "username????????????");
        Assert.notNull(code, "code????????????");
        TUser user = userService.findByName(username);
        Assert.isNull(user, "?????????????????????????????????????????????????????????");
        String key = RedisConstant.LOGIN_KEY + username;
        VerifyCode verifyCode = (VerifyCode) redisManager.get(key);
        Assert.notNull(verifyCode, "????????????");
        long currentTime = new Date().getTime() / 1000;
        Assert.isTrue(verifyCode.getExpire() > currentTime, "???????????????");
        Assert.isTrue(verifyCode.getCode().equals(code), "???????????????");
        if (CommonConstant.CONTACT_EMAIL.equalsIgnoreCase(type)) {
            if (!userRegistry.isSetPassword()) {
                //?????????????????????????????????????????????????????????code????????????
                return new BNResponse<>();
            }
            if (StringUtil.isEmpty(password)) {
                throw new CommonException("????????????????????????????????????");
            }
        }
        userService.registry(userRegistry);
        return new BNResponse(true, UserSdkErrorCode.SUCCESS_REGISTRY, true);
    }

    @PostMapping("send/code")
    @ApiOperation(value = "???????????????")
    public BaseResponse sendCode(@RequestBody SendCode sendCode) {
        String type = sendCode.getType();
        String username = sendCode.getUsername();
        Assert.notNull(type, "type????????????");
        Assert.notNull(username, "username????????????");
        String cacheKey = RedisConstant.LOGIN_KEY + sendCode.getUsername();
        VerifyCode verifyCode = new VerifyCode();
        long nowTime = new Date().getTime() / 1000;
        if (redisManager.hasKey(cacheKey)) {
            verifyCode = (VerifyCode) redisManager.get(cacheKey);
            int count = verifyCode.getCount();
            log.info(username + "????????????????????????" + count);
            Assert.isTrue(count <= 10, "?????????????????????????????????????????????");
            verifyCode.setCount(count + 1);
            log.info("????????????" + redisManager.getExpire(cacheKey));
        } else {
            verifyCode.setUsername(sendCode.getUsername());
            verifyCode.setCount(1);
        }
        String code = RandomStringUtils.randomNumeric(5);
        verifyCode.setCode(code);
        verifyCode.setExpire(nowTime + CommonConstant.TIME_15_MINUTE);
        userService.sendCode(sendCode, code);
        redisManager.set(cacheKey, verifyCode, TimeUtil.getLastSeconds());
        log.info("?????????{}???????????????{}???", cacheKey, verifyCode.getCode());
        return new BNResponse(true, UserSdkErrorCode.SUCCESS_SEND_CODE, true);
    }

    @PostMapping("password/login")
    @ApiOperation(value = "??????????????????")
    public BNResponse<LoginDTO> login(@RequestBody PasswordLogin passwordLogin) {
        String password = passwordLogin.getPassword();
        String username = passwordLogin.getUsername();
        Assert.notNull(username, "?????????????????????");
        Assert.notNull(password, "??????????????????");
        TUser user = userService.findByName(username);
        Assert.notNull(user, "???????????????");
        Assert.isTrue(!CommonConstant.USER_STATUS_BAN.equals(user.getStatus()), "?????????????????????");
        Assert.isTrue(!CommonConstant.NOT_PASSWORD.equals(user.getPassword()), "???????????????");
        LoginDTO loginDTO = userService.loginByPassword(user, password);
        return new BNResponse<>(loginDTO);
    }

    @PostMapping("fast/login")
    @ApiOperation(value = "??????????????????")
    public BNResponse<LoginDTO> fastLogin(@RequestBody FastLogin fastLogin) {
        String mobile = fastLogin.getMobile();
        String code = fastLogin.getCode();
        Assert.notNull(mobile, "?????????????????????");
        Assert.notNull(code, "?????????????????????");
        TUser user = userService.findByMobile(mobile);
        Assert.notNull(user, "??????????????????");
        Assert.isTrue(!CommonConstant.USER_STATUS_BAN.equals(user.getStatus()), "?????????????????????");
        String cacheKey = RedisConstant.LOGIN_KEY + mobile;
        VerifyCode verifyCode = (VerifyCode) redisManager.get(cacheKey);
        Assert.notNull(verifyCode, "????????????");
        long currentTime = new Date().getTime() / 1000;
        Assert.isTrue(verifyCode.getExpire() > currentTime, "???????????????");
        Assert.isTrue(code.equals(verifyCode.getCode()), "???????????????");
        LoginDTO loginDTO = userService.loginFast(user, code);
        return new BNResponse<>(loginDTO);

    }

    @PostMapping("modify/password")
    @ApiOperation(value = "????????????")
    public BaseResponse password(@RequestBody ModifyPasswordVo modifyPasswordVo, Principal principal) {
        if (null == principal || null == modifyPasswordVo.getNewPassword()) {
            return new BaseResponse(UserSdkErrorCode.INTERNAL_SERVER_ERROR);
        }
        Assert.notNull(modifyPasswordVo.getNewPassword(), "????????????????????????");
        Assert.isTrue(modifyPasswordVo.getNewPassword().length() > 4, "??????????????????");
        userService.modifyPassword(principal.getName(), modifyPasswordVo.getNewPassword());

        return new BaseResponse();
    }

    @PostMapping("modify/nickname")
    @ApiOperation(value = "??????????????????")
    public BaseResponse username(@RequestBody ModifyUserNameVo modifyUserNameVo, Principal principal) {
        if (null == principal || null == modifyUserNameVo.getNewUserName()) {
            return new BaseResponse(UserSdkErrorCode.INTERNAL_SERVER_ERROR);
        }
        String userId = principal.getName();
        userService.modifyNickname(userId, modifyUserNameVo.getNewUserName());
        return new BaseResponse();
    }

    @PostMapping("login-out")
    @ApiOperation(value = "???????????????token?????????")
    public BaseResponse loginOut(Principal principal) {
        userService.loginOut(principal.getName());
        return new BaseResponse();
    }

    @GetMapping("current")
    public BNResponse<UserVo> userPrincipal(Principal principal) {
        String userId = principal.getName();
        TUser tUser = userService.findById(userId);
        if (null == tUser) {
            return new BNResponse(UserSdkErrorCode.DB_SQL_FAILURE);
        }
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(tUser, vo);
        BNResponse<BigDecimal> response = tokenService.queryTokenAmount(userId);
        if (response.isToast()) {
            vo.setToken(response.getBody());
        }
        return new BNResponse<>(vo);
    }

    @GetMapping("find/id")
    public BNResponse<UserVo> findById(@RequestParam("userId") String userId) {
        TUser tUser = userService.findById(userId);
        if (null == tUser) {
            return new BNResponse(UserSdkErrorCode.DB_SQL_FAILURE);
        }
        UserVo vo = new UserVo();
        vo.setId(tUser.getId());
        vo.setUsername(tUser.getNickName());
        vo.setTxPicture(tUser.getTxPicture());
        return new BNResponse<>(vo);
    }

    @PostMapping("modify/info")
    @ApiOperation(value = "??????????????????")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "signature", value = "??????", dataType = "String"),
            @ApiImplicitParam(paramType = "form", name = "nickName", value = "??????", dataType = "String")}
    )
    public BaseResponse issue(@RequestParam(value = "signature", required = false) String signature,
                              @RequestParam(value = "nickName", required = false) String nickName,
                              HttpServletRequest request,
                              Principal principal) {
        String userId = principal.getName();
        TUser tUser = userService.findById(userId);
        if (StringUtil.isNotEmpty(signature)) {
            tUser.setTxSignature(signature);
        }
        if (StringUtil.isNotEmpty(nickName)) {
            tUser.setNickName(nickName);
        }
        MultipartFile image = null;
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            MultipartHttpServletRequest multipartRequest = WebUtils.getNativeRequest(request,
                    MultipartHttpServletRequest.class);
            image = multipartRequest.getFile("image");
            if (null != image) {
                //????????????
                String originalFilename = image.getOriginalFilename();
                InputStream inputStream = null;
                try {
                    inputStream = image.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (inputStream != null) {
                    aliyunOosFileStorage.store(inputStream, originalFilename);
                } else {
                    log.warn("??????????????????");
                }
                tUser.setTxPicture(aliyunOosFileStorage.getFileProperties().getGetUrl() + CommonConstant.SLASH + originalFilename);
            }

        }
        userService.modifyInfo(tUser);
        return new BaseResponse();
    }

    @PostMapping("update/contract")
    @ApiOperation(value = "??????????????????")
    public BaseResponse modifyContract(@RequestBody UpdateVerifyCode updateVerifyCode, Principal principal) {
        String userId = principal.getName();
        if (StringUtil.isEmpty(updateVerifyCode.getCode())) {
            throw new CommonException("?????????????????????");
        }
        //??????????????????????????????
        userService.updateAccountContact(Long.parseLong(userId), updateVerifyCode);
        return new BaseResponse();
    }

}
