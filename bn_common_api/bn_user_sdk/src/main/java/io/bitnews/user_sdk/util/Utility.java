package io.bitnews.user_sdk.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Base64Utils;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vdurmont.emoji.EmojiParser;

import io.bitnews.user_sdk.constant.LoginSource;
import io.bitnews.user_sdk.constant.UserSdkConstant;

public abstract class Utility {

	private static final int STAR_CODE_LENGTH = 6;
	private static final int INVITATION_CODE_LENGTH = 8;
	private static final int ONTIME_CODE_LENGTH = 10;
	private static final int START_INDEX = 0;
	private static final String EMPTY = "";
	private static final String UUID_SEP = "-";
	private static final char[] SPECIAL_CHARS = { '`', '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',' ',
			'=', '+', '[', ']', '{', '}', '\\', '|', ';', ':', '\'', '"', ',', '<', '.', '>', '/', '?' };
	private static final String BN = "BN";
	private static final int PASSWORD_AUTO_LENGTH = 5;
	private static final String LOCALHOST = "localhost";
	private static final int PORT_80 = 80;
	private static final int PORT_443 = 443;
	private static final String HTTP = "http";
	private static final String HTTPS = "https";

	private static final int HTTP_EXPIRED = 0;
	private static final String HTTP_NO_CACHE = "no-cache";
	private static final String HTTP_PRAGMA = "Pragma";
	private static final String HTTP_NO_CACHES = "no-cache, no-store, max-age=0, must-revalidate";
	private static final String HTTP_CACHE_CONTROL = "Cache-Control";
	private static final String HTTP_EXPIRES = "Expires";

	public static final String PASSWORD_SALT = Base64Utils.encodeToString("wan".getBytes(StandardCharsets.UTF_8));
	public static final String OPENID_SALT = Base64Utils.encodeToString("open".getBytes(StandardCharsets.UTF_8));
	/**
	 * 十七位数字本体码权重
	 */
	private static final int[] ID_WEIGHT = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
	/**
	 * mod11,对应校验码字符值
	 */
	private static final char[] ID_VALIDATE = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
	private static final int IDMOD11_LENGTH = 11;
	private static final int ID18_LENGTH = 18;
	private static final int ID17_LENGTH = 17;

	public static Date now() {
		return new Date();
	}

	public static boolean checkID18(String idStr) {
		if (StringUtils.isEmpty(idStr) || idStr.length() != ID18_LENGTH) {
			return false;
		}
		// the first 17 characters should be number
		if (!StringUtils.isNumeric(idStr.substring(0, ID17_LENGTH))) {
			return false;
		}
		int sum = 0;
		for (int i = 0; i < ID17_LENGTH; i++) {
			sum = sum + Integer.parseInt(String.valueOf(idStr.charAt(i))) * ID_WEIGHT[i];
		}
		int mode = sum % IDMOD11_LENGTH;
		return idStr.charAt(ID17_LENGTH) == ID_VALIDATE[mode];
	}

	public static long randomUniqueID() {
		return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
	}

	public static String randomUniqueName() {
		return BN + UUID.randomUUID().toString().replace(UUID_SEP, EMPTY);
	}

	public static String onetimeInvitationCode() {
		return UUID.randomUUID().toString().replace(UUID_SEP, EMPTY).substring(START_INDEX, ONTIME_CODE_LENGTH);
	}

	public static String invitationCode() {
		return UUID.randomUUID().toString().replace(UUID_SEP, EMPTY).substring(START_INDEX, INVITATION_CODE_LENGTH);
	}

	public static String starInvitationCode() {
		return UUID.randomUUID().toString().replace(UUID_SEP, EMPTY).substring(START_INDEX, STAR_CODE_LENGTH);
	}

	public static String randomPassword() {
		String text = new RandomStringGenerator.Builder().withinRange('a', 'z').build().generate(PASSWORD_AUTO_LENGTH);
		String number = new RandomStringGenerator.Builder().withinRange('0', '9').build()
				.generate(PASSWORD_AUTO_LENGTH);
		return text + number;
	}

	public static String toHash(String str) {
		if (str == null) {
			return null;
		}
		return String.valueOf(str.hashCode());
	}

	public static String randomNonce(int length) {
		String text = new RandomStringGenerator.Builder().withinRange('a', 'z').build().generate(length);
		return text;
	}

	public static String openidDigest(String src) {
		if (src == null) {
			return null;
		}
		return DigestUtils.md5Hex(src.toLowerCase() + OPENID_SALT);
	}

	public static String passwordEncode(String src) {
		if (src == null) {
			return null;
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(DigestUtils.md5Hex(src));
	}
	
	public static boolean passwordMatches(String src, String dest) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(DigestUtils.md5Hex(src), dest);
	}
	
	public static String getAppLoginDigest(String str) {
		return DigestUtils.sha256Hex(str);
	}

	public static String batchNo() {
		return UUID.randomUUID().toString();
	}

	public static String encodeBase64(String message) {
		return Base64Utils.encodeToString(message.getBytes(StandardCharsets.UTF_8));
	}

	public static String decodeBase64(String message) {
		byte[] encodeBase64 = Base64Utils.decodeFromString(message);
		return new String(encodeBase64, StandardCharsets.UTF_8);
	}

	public static String objectToJson(Object o) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		mapper.disable(SerializationFeature.WRITE_NULL_MAP_VALUES);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.writeValueAsString(o);
	}

	public static <T> T jsonToObject(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(json, clazz);
	}

	public static boolean containsAlphaNumberSpecialChar(final CharSequence cs) {
		if (StringUtils.isEmpty(cs)) {
			return false;
		}
		boolean containsLetter = false;
		boolean containsDigit = false;
		boolean containsSpecialChar = false;
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			Character cr = cs.charAt(i);

			boolean letter = CharUtils.isAsciiAlpha(cr);
			if (letter && !containsLetter) {
				containsLetter = true;
			}

			boolean digit = CharUtils.isAsciiNumeric(cr);
			if (digit && !containsDigit) {
				containsDigit = true;
			}

			boolean specChar = isSpecialChar(cr);
			if (specChar && !containsSpecialChar) {
				containsSpecialChar = true;
			}
			// only letters
			if (!letter && !digit && !specChar) {
				return false;
			}
		}
		return containsLetter && containsDigit && containsSpecialChar;
	}
	
	public static String removeSpecialChar(final CharSequence cs) {
		if (cs == null) {
			return null;
		}
		final int sz = cs.length();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < sz; i++) {
			Character cr = cs.charAt(i);
			boolean specChar = isSpecialChar(cr);
			if (!specChar) {
				builder.append(cr);
			}
		}
		return builder.toString();
	}

	public static boolean isSpecialChar(Character cr) {
		for (Character sc : SPECIAL_CHARS) {
			if (sc.equals(cr)) {
				return true;
			}
		}
		return false;
	}

	public static String removeEmoji(String str) {
		if (str == null) {
			return null;
		}
		return EmojiParser.removeAllEmojis(str);
	}

	public static String buildServerPath(HttpServletRequest request, String servletPath) {
		return buildServerPath(request, servletPath, false);
	}

	public static String buildServerPath(HttpServletRequest request, String servletPath, boolean ignoreDefaultPort) {
		return buildServerPath(request, servletPath, null, ignoreDefaultPort, false);
	}

	public static String buildServerPath(HttpServletRequest request, String servletPath, String queryString,
			boolean ignoreDefaultPort) {
		return buildServerPath(request, servletPath, queryString, ignoreDefaultPort, false);
	}

	public static String buildServerPath(HttpServletRequest request, String servletPath, String queryString,
			boolean ignoreDefaultPort, boolean userLocalhost) {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(request.getScheme());
		builder.host(userLocalhost ? LOCALHOST : request.getServerName());
		if (ignoreDefaultPort) {
			boolean condition = (HTTPS.equals(request.getScheme()) && request.getServerPort() != PORT_443)
					|| (HTTP.equals(request.getScheme()) && request.getServerPort() != PORT_80);
			if (condition) {
				builder.port(request.getServerPort());
			}
		} else {
			builder.port(request.getServerPort());
		}
		builder.path(request.getContextPath() + servletPath);
		if (queryString != null) {
			builder.query(queryString);
		}
		return builder.build(false).toUriString();
	}

	public static void queryParams(UriComponentsBuilder ub, Map<String, String> map) {
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			ub.queryParam(entry.getKey(), entry.getValue());
		}
	}

	public static String getAuthorization(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(UserSdkConstant.HEADER_AUTHORIZATION)) {
					if (cookie.getValue() != null) {
						return cookie.getValue();
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 通过user-agent获取请求来源
	 * 
	 * @param request
	 * @return
	 */
	public static LoginSource getLoginSource(HttpServletRequest request) {
		String requestFrom = request.getHeader(UserSdkConstant.HEADER_REQUEST_FROM);
		if (StringUtils.isNotBlank(requestFrom)
				&& UserSdkConstant.HEADER_REQUEST_FROM_HASHKEY_WALLET.equals(requestFrom)) {
			// 默认WEB访问
			return LoginSource.HASHKEY;
		}
		String agent = request.getHeader(UserSdkConstant.HEADER_USER_AGENT);
		if (StringUtils.isBlank(agent)) {
			// 默认WEB访问
			return LoginSource.WEB;
		}
		if (agent.contains(UserSdkConstant.HEADER_USER_AGENT_bitnews)) {
			return LoginSource.bitnews;
		}
		if (agent.contains(UserSdkConstant.HEADER_USER_AGENT_HASHKEYWALLET)) {
			return LoginSource.HASHKEY;
		}
		if (agent.contains(UserSdkConstant.HEADER_USER_AGENT_OKHTTP)) {
			return LoginSource.bitnews;
		}
		return LoginSource.WEB;
	}

	public static String getAuthorization(HttpServletRequest request, String csrfToken) {
		Cookie[] cookies = request.getCookies();
		if (StringUtils.isNotEmpty(csrfToken) && cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(UserSdkConstant.HEADER_AUTHORIZATION)) {
					// crsf token must be equals to cookie value
					if (cookie.getValue() != null && csrfToken.equals(Utility.toHash(cookie.getValue()))) {
						return cookie.getValue();
					}
				}
			}
		}
		return null;
	}

	public static String getUriServer(String str) throws URISyntaxException {
		URI uri = new URI(str);
		if (uri.getPort() < 0 || LOCALHOST.equalsIgnoreCase(uri.getHost())) {
			return uri.getHost();
		}
		return uri.getHost();
	}

	public static boolean inAllowedUri(String str, String[] whitelist) throws URISyntaxException {
		String domain = Utility.getUriServer(str);
		for (String allowed : whitelist) {
			if (allowed != null) {
				allowed = allowed.trim();
				if (allowed.equals(domain) || domain.endsWith(allowed)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void addNoHeaderCache(HttpServletResponse response) {
		response.setDateHeader(HTTP_EXPIRES, HTTP_EXPIRED);
		response.setHeader(HTTP_CACHE_CONTROL, HTTP_NO_CACHES);
		response.setHeader(HTTP_PRAGMA, HTTP_NO_CACHE);
	}

	public static String[] splitUri(String uri, String splitToken) {
		String baseUri = null;
		int foundIndex = uri.indexOf(splitToken);
		String param = null;
		if (foundIndex != -1) {
			baseUri = uri.substring(0, foundIndex);
			param = uri.substring(foundIndex + 1);
			return new String[] { baseUri, param };
		}
		return new String[] { uri, null };
	}

	public static String[] splitUriWithAnchor(String uri) {
		return splitUri(uri, UserSdkConstant.URI_ANCHOR);
	}

	public static String removeQueryParams(String queryParams, String key) {
		return removeQueryParams(queryParams, new String[] { key });
	}

	public static String removeQueryParams(String queryParams, String[] keys) {
		if (queryParams == null || keys == null) {
			return queryParams;
		}
		StringTokenizer st = new StringTokenizer(queryParams, UserSdkConstant.QUERY_PARAM_CONCAT);
		StringBuilder builder = new StringBuilder();
		int count = st.countTokens();
		int i = 0;
		while (st.hasMoreTokens()) {
			i++;
			String token = st.nextToken();
			int foundPair = token.indexOf(UserSdkConstant.QUERY_PARAM_ASSIGN_SIGN);
			if (foundPair > 0) {
				boolean foundKey = findToken(keys, token.substring(0, foundPair));
				if (foundKey) {
					continue;
				}
			}
			builder.append(token);
			if (i != count) {
				builder.append(UserSdkConstant.QUERY_PARAM_CONCAT);
			}
		}
		return builder.toString();
	}

	private static boolean findToken(String[] keys, String tokenKey) {
		for (String key : keys) {
			if (tokenKey.equals(key)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNumber(String str) {
		String reg = "^[0-9]+(.[0-9]+)?$";
		return str.matches(reg);
	}
}
