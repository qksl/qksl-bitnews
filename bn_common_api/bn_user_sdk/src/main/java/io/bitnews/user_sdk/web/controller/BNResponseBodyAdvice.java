package io.bitnews.user_sdk.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.bitnews.user_sdk.util.Utility;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class BNResponseBodyAdvice implements ResponseBodyAdvice<Object> {
	@Override
	public boolean supports(final MethodParameter returnType,
			final Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		if (log.isDebugEnabled()) {
			if (body instanceof ResponseEntity && request instanceof HttpServletRequest) {
				ResponseEntity responseEntity = (ResponseEntity) body;
				String requestURI = ((HttpServletRequest) request).getRequestURI();
				try {
					log.debug("threadName={},url={},response={},", Thread.currentThread().getName(), requestURI,
							Utility.objectToJson(responseEntity));
				} catch (JsonProcessingException e) {
					log.error("fail to convert response body", responseEntity);
				}
			}
		}
		return body;
	}
}