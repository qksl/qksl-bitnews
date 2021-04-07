package io.bitnews.oauth.integration.authenticator;

import io.bitnews.model.passport.po.TUser;
import io.bitnews.oauth.dao.TUserDao;
import io.bitnews.oauth.integration.IntegrationAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
@Primary
@Slf4j
public class UsernamePasswordAuthenticator extends AbstractPreparableIntegrationAuthenticator {

    @Autowired
    private TUserDao tUserDao;

    @Override
    public SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication) {
        //查找获得用户
        log.info("用户密码登录验证:{}", integrationAuthentication.getUsername());
        TUser tUser = tUserDao.findByname(integrationAuthentication.getUsername());
        SysUserAuthentication sysUserAuthentication = new SysUserAuthentication();
        BeanUtils.copyProperties(tUser,sysUserAuthentication);
        sysUserAuthentication.setAuthorities(AuthorityUtils.createAuthorityList("USER"));
        sysUserAuthentication.setUsername(tUser.getId().toString());
        return sysUserAuthentication;
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {
        log.info("账户密码模式-邮件登录");
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return StringUtils.isEmpty(integrationAuthentication.getAuthType());
    }
}