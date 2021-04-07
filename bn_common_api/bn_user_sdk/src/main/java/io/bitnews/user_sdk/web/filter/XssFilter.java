package io.bitnews.user_sdk.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.HtmlUtils;

public class XssFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		XssRequestWrapper xssRequest = new XssRequestWrapper(request);
		filterChain.doFilter(xssRequest, response);
	}

	static class XssRequestWrapper extends HttpServletRequestWrapper {

		public XssRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		@Override
		public String getHeader(String name) {
			String value = super.getHeader(name);
			return HtmlUtils.htmlEscape(value);
		}

		@Override
		public String getParameter(String name) {
			String value = super.getParameter(name);
			return HtmlUtils.htmlEscape(value);
		}

		@Override
		public String[] getParameterValues(String name) {
			String[] values = super.getParameterValues(name);
			if (values != null) {
				int length = values.length;
				String[] escapseValues = new String[length];
				for (int i = 0; i < length; i++) {
					escapseValues[i] = HtmlUtils.htmlEscape(values[i]);
				}
				return escapseValues;
			}
			return super.getParameterValues(name);
		}

	}
}
