package io.bitnews.user_sdk.auth.interceptor;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.bitnews.user_sdk.auth.BNAuthorization;
import io.bitnews.user_sdk.auth.BNTokenManager;
import io.bitnews.user_sdk.constant.LoginSource;
import io.bitnews.user_sdk.constant.Platform;
import io.bitnews.user_sdk.constant.UserSdkConstant;
import io.bitnews.user_sdk.constant.UserSdkErrorCode;
import io.bitnews.user_sdk.constant.UserStatus;
import io.bitnews.user_sdk.exception.BNException;
import io.bitnews.user_sdk.util.Utility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BNAuthorizationInterceptor extends HandlerInterceptorAdapter {

	private static final int DURATION_THRESHOLD = 3000;

	@Autowired
	private BNTokenManager tokenManager;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		request.setAttribute(UserSdkConstant.REQ_START_TIME, System.currentTimeMillis());
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		BNAuthorization annotation = method.getAnnotation(BNAuthorization.class);
		if (annotation != null) {
			checkAnnotation(request, annotation);
		}
		return true;
	}

	private void checkAnnotation(HttpServletRequest request, BNAuthorization annotation) {
		LoginSource loginSource = Utility.getLoginSource(request);
		// 403 check
		if ((loginSource == LoginSource.WEB && annotation.onlyApp())
				|| (loginSource != LoginSource.WEB && annotation.onlyWeb())) {
			throw new BNException(UserSdkErrorCode.FORBIDDEN);
		}
		// 获取Token
		String token = request.getHeader(UserSdkConstant.HEADER_TOKEN);
		if (log.isDebugEnabled()) {
			log.debug("loginSource: " + loginSource + ", token: " + token + ", requestUri: " + request.getRequestURI());
			Enumeration<String> names = request.getHeaderNames();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				log.debug("header: " + name + " -->> " + request.getHeader(name));
			}
		}
		if (loginSource == LoginSource.WEB) {
			token = getWebToken(request);
		} else {
			throw new BNException(UserSdkErrorCode.FORBIDDEN);
		}
		Integer userId = tokenManager.getUserIdByToken(token, loginSource);
		if (userId == null) {
			throw new BNException(UserSdkErrorCode.UNAUTHORIZED);
		}
		UserStatus status = tokenManager.getUserStatus(userId);
		if (status == UserStatus.DELETE) {
			throw new BNException(UserSdkErrorCode.USER_IS_DELETED);
		}
		if (annotation.requireKyc()) {
			if (tokenManager.getUserStatus(userId) != UserStatus.KYC_PASS) {
				throw new BNException(UserSdkErrorCode.KYC_REQUIRED);
			}
		}
		request.setAttribute(UserSdkConstant.REQ_AUTH_TOKEN, token);
		request.setAttribute(UserSdkConstant.REQ_AUTH_ID, userId);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		request.setAttribute(UserSdkConstant.REQ_NORMAL_EXIT, true);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		Long startTime = (Long) request.getAttribute(UserSdkConstant.REQ_START_TIME);
		if (startTime != null) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			String beanName = handlerMethod.getBean().getClass().toString();
			String methodName = handlerMethod.getMethod().getName();
			Map<String, String[]> pramMap = request.getParameterMap();
			String requestURI = request.getRequestURI();
			boolean run = request.getAttribute(UserSdkConstant.REQ_NORMAL_EXIT) != null;
			long duration = System.currentTimeMillis() - startTime;
			log.info("threadName={},beanName={},methodName={},url={},pramMap={},exit={},timeTaken={}",
					Thread.currentThread().getName(), beanName, methodName, requestURI, Utility.objectToJson(pramMap), run, duration);
		}
	}

	private String getWebToken(HttpServletRequest request) {
		String csrfToken = request.getHeader(UserSdkConstant.HEADER_TOKEN);
		String token = Utility.getAuthorization(request, csrfToken);
		if (token == null) {
			throw new BNException(UserSdkErrorCode.UNAUTHORIZED);
		}
		return token;
	}

}
