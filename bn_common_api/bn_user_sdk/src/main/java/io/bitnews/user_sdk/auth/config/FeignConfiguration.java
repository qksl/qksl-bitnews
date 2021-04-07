package io.bitnews.user_sdk.auth.config;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestTemplate;

import feign.RequestInterceptor;

@Configuration
public class FeignConfiguration implements RequestInterceptor {
//	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void apply(RequestTemplate template) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(attributes == null) {
//			logger.error("attributes is null");
			return;
		}
		HttpServletRequest request = attributes.getRequest();
		if(request == null) {
//			logger.error("request is null");
			return;
		}
		Enumeration<String> headerNames = request.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String name = headerNames.nextElement();
				String values = request.getHeader(name);
				template.header(name, values);

			}
//			logger.info("feign interceptor header:{}", template);
		}
	}
}
