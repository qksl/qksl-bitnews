package io.bitnews.user_sdk.cache.keys;

abstract public class BNRedisKeys {

	public static String appendKeys(String key, Object... ids) {
		return String.format(key, ids);
	}
	
	public static final String SCHEDULE_LOG_LIST = "logger:schedule";
	
	public static final String DEVICE_SECRET_KEY = "device:%s:%s";
}
