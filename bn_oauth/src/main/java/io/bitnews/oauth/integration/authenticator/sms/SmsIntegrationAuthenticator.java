package io.bitnews.oauth.integration.authenticator.sms;

import io.bienews.common.helper.exception.CommonException;
import io.bitnews.model.passport.po.TUser;
import io.bitnews.oauth.dao.TUserDao;
import io.bitnews.oauth.integration.IntegrationAuthentication;
import io.bitnews.oauth.integration.authenticator.AbstractPreparableIntegrationAuthenticator;
import io.bitnews.oauth.integration.authenticator.SysUserAuthentication;
import io.bitnews.oauth.integration.authenticator.sms.event.SmsAuthenticateBeforeEvent;
import io.bitnews.oauth.integration.authenticator.sms.event.SmsAuthenticateSuccessEvent;
import io.bitnews.oauth.integration.validate.SmsVerificationCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SmsIntegrationAuthenticator extends AbstractPreparableIntegrationAuthenticator implements ApplicationEventPublisherAware {

    @Autowired
    private TUserDao tUserDao;

    @Autowired
    private SmsVerificationCode smsVerificationCode;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ApplicationEventPublisher applicationEventPublisher;

    private final static String SMS_AUTH_TYPE = "sms";

    @Override
    public SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication) {

        //获取密码，实际值是验证码
        String password = integrationAuthentication.getAuthParameter("password");
        //获取用户名，实际值是手机号
        String mobile = integrationAuthentication.getUsername();
        log.info("验证手机用户:{},验证码为：{}", mobile, password);
        //发布事件，可以监听事件进行自动注册用户
        this.applicationEventPublisher.publishEvent(new SmsAuthenticateBeforeEvent(integrationAuthentication));
        //通过手机号码查询用户
        TUser tUser = tUserDao.findByMobile(mobile);
        SysUserAuthentication sysUserAuthentication = new SysUserAuthentication();
        if (tUser != null) {
            //将密码设置为验证码
            BeanUtils.copyProperties(tUser,sysUserAuthentication);
            sysUserAuthentication.setPassword(passwordEncoder.encode(password));
            //发布事件，可以监听事件进行消息通知
            this.applicationEventPublisher.publishEvent(new SmsAuthenticateSuccessEvent(integrationAuthentication));
        }
        sysUserAuthentication.setAuthorities(AuthorityUtils.createAuthorityList("USER"));
        sysUserAuthentication.setUsername(tUser.getId().toString());
        return sysUserAuthentication;
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {
        String smsCode = integrationAuthentication.getAuthParameter("password");
        String username = integrationAuthentication.getAuthParameter("username");
        boolean result = smsVerificationCode.validate(smsCode, username);
        if (!result) {
            throw new CommonException("验证码错误或已过期");
        }
        log.info("短信验证码验证成功");
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return SMS_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}