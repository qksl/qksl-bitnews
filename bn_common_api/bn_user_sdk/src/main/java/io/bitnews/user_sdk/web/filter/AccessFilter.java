package io.bitnews.user_sdk.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import io.bitnews.user_sdk.auth.BNTokenManager;
import io.bitnews.user_sdk.constant.LoginSource;
import io.bitnews.user_sdk.util.Utility;

public class AccessFilter extends OncePerRequestFilter {

	@Autowired
	private BNTokenManager tokenManager;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String token = Utility.getAuthorization(request);
		final LoginSource loginSource = Utility.getLoginSource(request);
		
		// Automatically add token expiration for web
		if (StringUtils.isNotBlank(token) && loginSource == LoginSource.WEB) {
			if (tokenManager.refreshToken(token, loginSource)) {
				filterChain.doFilter(request, response);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}
}
