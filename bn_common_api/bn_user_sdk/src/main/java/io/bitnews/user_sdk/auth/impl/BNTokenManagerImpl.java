package io.bitnews.user_sdk.auth.impl;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import io.bitnews.user_sdk.auth.BNTokenManager;
import io.bitnews.user_sdk.cache.keys.BNRedisKeys;
import io.bitnews.user_sdk.constant.LoginSource;
import io.bitnews.user_sdk.constant.UserRole;
import io.bitnews.user_sdk.constant.UserStatus;

@Component
public class BNTokenManagerImpl implements BNTokenManager {

	/**
	 * 1 hours
	 */
	private static final int WEB_EXPIRATION = 60;
	/**
	 * 3 days
	 */
	private static final int APP_EXPIRATION = 24 * 60 * 3;
	/**
	 * 1 hours
	 */
	private static final int WEB_REFRESH_EXPIRATION = 60;
	/**
	 * 3 days
	 */
	private static final int APP_REFRESH_EXPIRATION = 24 * 60 * 3;

	/**
	 * 占位符: 1-LoginSource,2-token
	 */
	private static final String USER_TOKEN_KEY = "user:tokens:%s:%s";

	/**
	 * 占位符: userId
	 */
	private static final String USER_STATUS_KEY = "user:status:%s";

	/**
	 * 占位符: userId
	 */
	private static final String USER_ROLE_KEY = "user:role:%s";

	/**
	 * 占位符: userId
	 */
	private static final String USER_TOKENS_HASH = "user:tokens:%s";

	@Autowired
	@Qualifier("intRedisTemplate")
	private RedisTemplate<String, Integer> intRedis;

	@Autowired
	private StringRedisTemplate stringRedis;

	protected String toUserStatusKey(Integer userId) {
		return String.format(USER_STATUS_KEY, userId);
	}

	protected String toUserRoleKey(Integer userId) {
		return String.format(USER_ROLE_KEY, userId);
	}

	private String generateToken() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	@Override
	public String createToken(Integer userId, Integer status, Integer userRole, LoginSource loginSource) {
		// 踢出原Token
		deleteOriginToken(userId, loginSource);
		// 生成新Token
		String token = generateToken();
		setOriginToken(userId, token, loginSource);
		// 缓存其他数据
		cacheStatusAndRole(userId, status, userRole);
		// 返回现Token
		return token;
	}

	/**
	 * 设置新Token
	 * 
	 * @param userId
	 * @param token
	 * @param loginSource
	 */
	private void setOriginToken(Integer userId, String token, LoginSource loginSource) {
		String key = String.format(USER_TOKEN_KEY, loginSource, token);
		stringRedis.opsForValue().set(key, String.valueOf(userId),
				(loginSource == LoginSource.WEB ? WEB_EXPIRATION : APP_EXPIRATION), TimeUnit.MINUTES);

		String hashKey = String.format(USER_TOKENS_HASH, userId);
		stringRedis.opsForHash().put(hashKey, loginSource.name(), token);
	}

	/**
	 * 踢出原loginSource的Token
	 * 
	 * @param userId
	 * @param loginSource
	 */
	private void deleteOriginToken(Integer userId, LoginSource loginSource) {
		String key = String.format(USER_TOKENS_HASH, userId);
		Object value = stringRedis.boundHashOps(key).get(loginSource.name());
		if (value != null) {
			deleteToken(value.toString(), loginSource);
		}
	}

	private Integer getUserIdByTokenAndLoginSource(String token, LoginSource loginSource) {
		String key = String.format(USER_TOKEN_KEY, loginSource, token);
		return intRedis.boundValueOps(key).get();
	}

	/**
	 * 缓存用户状态和角色
	 * 
	 * @param userId
	 * @param status
	 * @param userRole
	 */
	private void cacheStatusAndRole(Integer userId, Integer status, Integer userRole) {
		intRedis.boundValueOps(toUserStatusKey(userId)).set(status);
		intRedis.boundValueOps(toUserRoleKey(userId)).set(userRole);
	}

	/**
	 * Token续时
	 * 
	 * @param userType
	 * @param expiration
	 * @param webTokenKey
	 * @param userId
	 */
	protected void refreshToken(Integer userId, String token, LoginSource loginSource) {
		String key = String.format(USER_TOKEN_KEY, loginSource, token);
		stringRedis.boundValueOps(key).expire(
				(loginSource == LoginSource.WEB ? WEB_REFRESH_EXPIRATION : APP_REFRESH_EXPIRATION), TimeUnit.MINUTES);
	}

	@Override
	public boolean deleteToken(String token, LoginSource loginSource) {
		if (StringUtils.isBlank(token) || loginSource == null) {
			return false;
		}
		String key = String.format(USER_TOKEN_KEY, loginSource, token);
		stringRedis.delete(key);

		return true;
	}

	@Override
	public void cleanTokenByUserId(Integer userId) {
		String key = String.format(USER_TOKENS_HASH, userId);
		for (LoginSource loginSource : LoginSource.values()) {
			Object value = stringRedis.boundHashOps(key).get(loginSource.name());
			if (value != null) {
				deleteToken(value.toString(), loginSource);
			}
		}
	}

	@Override
	public Integer getUserIdByToken(String token, LoginSource loginSource) {
		Integer userId = getUserIdByTokenAndLoginSource(token, loginSource);
		if (userId == null) {
			return null;
		}
		refreshToken(userId, token, LoginSource.WEB);
		return userId;
	}

	@Override
	public Integer getUserIdByToken(String token) {
		Integer userId = getUserIdByTokenAndLoginSource(token, null);
		if (userId == null) {
			return null;
		}
		refreshToken(userId, token, LoginSource.WEB);
		return userId;
	}

	@Override
	public UserStatus getUserStatus(Integer userId) {
		Integer status = intRedis.boundValueOps(toUserStatusKey(userId)).get();
		if (status != null) {
			return UserStatus.getStatus(status);
		}
		return null;
	}

	@Override
	public UserRole getUserRole(Integer userId) {
		Integer role = intRedis.boundValueOps(toUserRoleKey(userId)).get();
		if (role != null) {
			return UserRole.getRole(role);
		}
		return null;
	}

	@Override
	public void updateUserStatusIfPresent(Integer userId, UserStatus newStatus) {
		Integer status = intRedis.boundValueOps(toUserStatusKey(userId)).get();
		if (status == null) {
			return;
		}
		intRedis.boundValueOps(toUserStatusKey(userId)).set(newStatus.getValue());

	}

	@Override
	public String getSecretKey(Integer platformId, String deviceId) {
		String key = String.format(BNRedisKeys.DEVICE_SECRET_KEY, platformId, deviceId);
		String value = stringRedis.boundValueOps(key).get();
		if (value != null) {
			stringRedis.boundValueOps(key).expire(APP_EXPIRATION, TimeUnit.MINUTES);
		}
		return value;

	}

	@Override
	public void updateSecretKey(Integer platformId, String deviceId, String secretKey) {
		String key = String.format(BNRedisKeys.DEVICE_SECRET_KEY, platformId, deviceId);
		stringRedis.boundValueOps(key).set(secretKey, APP_EXPIRATION, TimeUnit.MINUTES);
	}

	@Override
	public boolean refreshToken(String token, LoginSource loginSource) {
		Integer userId = getUserIdByTokenAndLoginSource(token, loginSource);
		if (userId == null) {
			return false;
		}
		refreshToken(userId, token, loginSource);
		return true;
	}
}
