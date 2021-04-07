package io.bitnews.passport.config;

import io.bitnews.passport.filter.CustomerAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

//@Order(6)
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    @Autowired
    @Qualifier("tokenStore")
    TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId("passport").tokenStore(tokenStore);
    }


    @Autowired
    private CustomerAuthFilter customerAuthFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/v1/passport/user/registry").permitAll()
                .antMatchers("/v1/passport/user/send/code").permitAll()
                .antMatchers("/v1/passport/user/fast/login").permitAll()
                .antMatchers("/v1/passport/user/password/login").permitAll()
                .antMatchers("/v1/passport/user/email/codeValidation").permitAll()
                .antMatchers("/v1/passport/news/**").permitAll()
                .antMatchers("/v1/passport/quotes/**").permitAll()
                .antMatchers("/v1/passport/guess/query/**").permitAll()
                .antMatchers("/v1/passport/fear/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/**").authenticated();

        http.addFilterAfter(customerAuthFilter, BasicAuthenticationFilter.class);
    }


}