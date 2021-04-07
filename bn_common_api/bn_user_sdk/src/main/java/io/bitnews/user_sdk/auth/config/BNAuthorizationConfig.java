package io.bitnews.user_sdk.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import io.bitnews.user_sdk.auth.interceptor.BNAuthorizationInterceptor;

@Configuration
public class BNAuthorizationConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private BNAuthorizationInterceptor authorizationInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authorizationInterceptor);
	}
}
