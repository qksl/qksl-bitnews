package io.bitnews.user_sdk.cache;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import io.bitnews.user_sdk.constant.AppEnum;
import io.bitnews.user_sdk.constant.UserSdkErrorCode;
import io.bitnews.user_sdk.exception.BNException;

@Service
public class DistributedLockImpl implements DistributedLock {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	private final ThreadLocal<String> registry = new ThreadLocal<String>();

	@Override
	public boolean tryLock(AppEnum module, String method, int seconds) throws BNException {
		String uniqueKey = module.getCode() + "_" + method;
		if (stringRedisTemplate.opsForValue().setIfAbsent(uniqueKey, String.valueOf(Thread.currentThread().getId()))) {
			stringRedisTemplate.expire(uniqueKey, seconds, TimeUnit.SECONDS);
			registry.set(uniqueKey);
			return true;
		}
		return false;
	}

	@Override
	public void lock(AppEnum module, String method, int seconds) throws BNException {
		boolean locked = tryLock(module, method, seconds);
		if (!locked) {
			throw new BNException(UserSdkErrorCode.TO_FASTER);
		}
	}

	@Override
	public void unlock() throws BNException {
		final String uniqueKey = registry.get();
		if (!StringUtils.isEmpty(uniqueKey)) {
			stringRedisTemplate.opsForValue().getOperations().delete(uniqueKey);
			registry.remove();
		}
	}

}
