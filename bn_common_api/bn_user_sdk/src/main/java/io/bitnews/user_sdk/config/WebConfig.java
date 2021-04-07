package io.bitnews.user_sdk.config;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import io.bitnews.user_sdk.web.filter.AccessFilter;
import io.bitnews.user_sdk.web.filter.CspFilter;
import io.bitnews.user_sdk.web.filter.XssFilter;

@Configuration
public class WebConfig {

	private static final String ALLOW_ALL_CORS = "*";
	private static final String ALLOW_ALL_PATH_CORS = "/**";

	@Value("#{'${cors.allow.orgin}'.split(';')}")
	private String[] allowOrigin;

	@Value("${server.session.cookie.path}")
	private String cookiePath;

	@Value("${server.session.cookie.domain}")
	private String cookieDomain;

	@Value("${server.session.cookie.max-age}")
	private int cookieMaxAge;

	@Value("${server.session.cookie.http-only}")
	private boolean useHttpOnlyCookie;

	@Value("${server.session.cookie.secure}")
	private boolean useSecureCookie;

	@Value("${server.session.timeout}")
	private Integer maxInactiveIntervalInSeconds;

	@Bean
	public LocaleResolver localeResolver() {
		AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
		List<Locale> supportedLocales = new ArrayList<Locale>(3);
		supportedLocales.add(Locale.SIMPLIFIED_CHINESE);
		supportedLocales.add(Locale.TRADITIONAL_CHINESE);
		supportedLocales.add(Locale.ENGLISH);
		localeResolver.setDefaultLocale(Locale.ENGLISH);
		localeResolver.setSupportedLocales(supportedLocales);
		return localeResolver;
	}

	@Bean
	public RestTemplate restTemplate() {
		RestTemplate tpl = new RestTemplate();
		return tpl;
	}


	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		for (String origin : allowOrigin) {
			configuration.addAllowedOrigin(origin);
		}
		configuration.addAllowedHeader(ALLOW_ALL_CORS);
		configuration.addAllowedMethod(ALLOW_ALL_CORS);
		source.registerCorsConfiguration(ALLOW_ALL_PATH_CORS, configuration);
		return new CorsFilter(source);
	}

	
	@Bean
	public AccessFilter accessFilter(){
		return new AccessFilter();
	}
	
	@Bean
	public XssFilter xssFilter(){
		return new XssFilter();
	}
	
	@Bean
	public CspFilter cspFilter(){
		return new CspFilter();
	}

	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setCookiePath(cookiePath);
		serializer.setDomainName(cookieDomain);
		serializer.setCookieMaxAge(cookieMaxAge);
		serializer.setUseHttpOnlyCookie(useHttpOnlyCookie);
		serializer.setUseSecureCookie(useSecureCookie);
		return serializer;
	}

}
