package io.bitnews.oauth.integration.authenticator;

import io.bitnews.model.admin.po.TAdmin;
import io.bitnews.model.passport.po.TUser;
import io.bitnews.oauth.dao.TUserDao;
import io.bitnews.oauth.integration.IntegrationAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.SQLManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

@Component
@Primary
@Slf4j
public class AdminAuthenticator extends AbstractPreparableIntegrationAuthenticator {

    private final static String VERIFICATION_CODE_AUTH_TYPE = "mg";

    @Autowired
    @Qualifier(value = "adminSqlManager")
    private SQLManager sqlManager;

    @Override
    public SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication) {
        //查找获得用户
        TAdmin tAdmin =
                sqlManager.query(TAdmin.class).andEq("username", integrationAuthentication.getUsername()).single();
        SysUserAuthentication sysUserAuthentication = new SysUserAuthentication();
        BeanUtils.copyProperties(tAdmin,sysUserAuthentication);
        sysUserAuthentication.setAuthorities(AuthorityUtils.createAuthorityList("ADMIN"));
        return sysUserAuthentication;
    }

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {
        log.info("账户密码模式--用户名模式");
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return VERIFICATION_CODE_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }
}