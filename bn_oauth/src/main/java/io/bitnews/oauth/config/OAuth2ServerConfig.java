package io.bitnews.oauth.config;

import io.bitnews.common.constants.CommonConstant;
import io.bitnews.oauth.integration.IntegrationAuthenticationFilter;
import io.bitnews.oauth.service.IntegrationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
public class OAuth2ServerConfig {


    @Configuration
    @EnableAuthorizationServer
    protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

        @Autowired
        @Qualifier("authenticationManagerBean")
        AuthenticationManager authenticationManager;

        @Autowired
        JwtAccessTokenConverter jwtTokenEnhancer;

        @Autowired
        private IntegrationAuthenticationFilter integrationAuthenticationFilter;

        @Autowired
        private IntegrationUserDetailsService integrationUserDetailsService;

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            //配置两个客户端,一个用于password认证一个用于client认证
            clients.inMemory().withClient("bn-oauth")
                    .authorizedGrantTypes("implicit","refresh_token", "password", "authorization_code")
                    .scopes("service")
                    .authorities("client")
                    .secret("$2a$10$ENiDVN6dX9mSlJWjYZC0LOPIpDnDZdFYW2rvndYoxdbK73YCgEJu6")
                    .accessTokenValiditySeconds(CommonConstant.SEVEN_DAY);//24小时过期
        }

        //    配置授权服务器端点
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    //指定token存储位置
                    .tokenStore(tokenStore())
                    .allowedTokenEndpointRequestMethods(HttpMethod.GET,HttpMethod.POST)
                    .accessTokenConverter(jwtTokenEnhancer)
                    //指定认证管理器
                    .authenticationManager(authenticationManager)
                    .reuseRefreshTokens(false)
                    .userDetailsService(integrationUserDetailsService);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security
                    .tokenKeyAccess("permitAll()")
                    .checkTokenAccess("isAuthenticated()")
                    .addTokenEndpointAuthenticationFilter(integrationAuthenticationFilter);
        }

        @Bean
        public TokenStore tokenStore() {
            return new JwtTokenStore(jwtTokenEnhancer);
        }

        @Bean
        protected JwtAccessTokenConverter jwtTokenEnhancer() {
            KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("bit-news.jks"), "bitnew321".toCharArray());
            JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
            converter.setKeyPair(keyStoreKeyFactory.getKeyPair("bitnews"));
            return converter;
        }

    }

}