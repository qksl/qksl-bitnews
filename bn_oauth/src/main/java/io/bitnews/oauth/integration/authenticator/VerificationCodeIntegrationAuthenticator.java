package io.bitnews.oauth.integration.authenticator;

import io.bitnews.oauth.integration.IntegrationAuthentication;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

@Component
public class VerificationCodeIntegrationAuthenticator extends UsernamePasswordAuthenticator {

    private final static String VERIFICATION_CODE_AUTH_TYPE = "vc";

//    @Autowired
//    private VerificationCodeClient verificationCodeClient;

    @Override
    public void prepare(IntegrationAuthentication integrationAuthentication) {
        String vcToken = integrationAuthentication.getAuthParameter("vc_token");
        String vcCode = integrationAuthentication.getAuthParameter("vc_code");
        //验证验证码
        boolean rs = true;
        if (!rs) {
            throw new OAuth2Exception("验证码错误");
        }
    }

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return VERIFICATION_CODE_AUTH_TYPE.equals(integrationAuthentication.getAuthType());
    }
}