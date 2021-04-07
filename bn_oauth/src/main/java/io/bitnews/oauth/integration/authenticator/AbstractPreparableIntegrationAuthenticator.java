package io.bitnews.oauth.integration.authenticator;


import io.bitnews.oauth.integration.IntegrationAuthentication;

public abstract class AbstractPreparableIntegrationAuthenticator implements IntegrationAuthenticator{

    @Override
    public abstract SysUserAuthentication authenticate(IntegrationAuthentication integrationAuthentication);

    @Override
    public abstract void prepare(IntegrationAuthentication integrationAuthentication);

    @Override
    public abstract boolean support(IntegrationAuthentication integrationAuthentication);

    @Override
    public void complete(IntegrationAuthentication integrationAuthentication) {

    }
}