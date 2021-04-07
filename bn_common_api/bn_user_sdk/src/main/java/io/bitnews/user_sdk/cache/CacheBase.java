package io.bitnews.user_sdk.cache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ydd
 *
 */
@Component
public class CacheBase {

	@Autowired
	@Qualifier("intRedisTemplate")
	private RedisTemplate<String, Integer> redisInt;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * decrease --
	 */
	public long decr(String key, int count) {
		return redisInt.opsForValue().increment(key, -count);
	}

	/**
	 * increase ++
	 */
	public long incr(String key, int count) {
		return redisInt.opsForValue().increment(key, count);
	}

	public String  getString(String key) {
		return stringRedisTemplate.boundValueOps(key).get();
	}

	public void setString(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}

	public Integer getInt(String key) {
		return redisInt.boundValueOps(key).get();
	}

	public void setInt(String key, int value) {
		redisInt.boundValueOps(key).set(value);
	}

	public void setInt(String key, int value, long time) {
		redisInt.boundValueOps(key).set(value);
		if (time > 0) {
			expire(key, time, TimeUnit.SECONDS);
		}
	}

	public boolean expire(String key, long time, TimeUnit unit) {
		return redisInt.expire(key, time, unit);
	}

	public Boolean hasKey(String key) {
		return stringRedisTemplate.hasKey(key);
	}

	public void hashPut(String key, String hashKey, String value) {
		stringRedisTemplate.opsForHash().put(key, hashKey, value);
	}

	public void hashPutAll(String key, Map<String, String> m) {
		stringRedisTemplate.opsForHash().putAll(key, m);
	}

	public void hashDel(String key, String hashKey) {
		stringRedisTemplate.opsForHash().delete(key, hashKey);
	}

	public Object hashGet(String key, String hashKey) {
		return stringRedisTemplate.opsForHash().get(key, hashKey);
	}

	public Set<Object> hashKeys(String key) {
		return stringRedisTemplate.opsForHash().keys(key);
	}

	public long getExpire(String key) {
		return stringRedisTemplate.getExpire(key);
	}

	public void del(String key) {
		stringRedisTemplate.delete(key);
	}
	
	public long listLeftPush(String key,String result) {
		return stringRedisTemplate.opsForList().leftPush(key,result);
	}
	public long listLeftPushAll(String key,String result[]) {
		return stringRedisTemplate.opsForList().leftPushAll(key,result);
	}
	public long listRightPush(String key,String result) {
		return stringRedisTemplate.opsForList().rightPush(key,result);
	}
	public long listRightPushAll(String key,String result[]) {
		return stringRedisTemplate.opsForList().rightPushAll(key,result);
	}
	
	public String listRightPop(String key) {
		return stringRedisTemplate.opsForList().rightPop(key);
	}
	public String listRightPop(String key,int timeOut) {
		return stringRedisTemplate.opsForList().rightPop(key, timeOut,TimeUnit.SECONDS);
	}
		
	public Long setAddArray(String key,String[] values) {
		return stringRedisTemplate.opsForSet().add(key,values);
	}
	public Long setAdd(String key,String value) {
		return stringRedisTemplate.opsForSet().add(key,value);
	}
	public Long setRemove(String key,String value) {
		return stringRedisTemplate.opsForSet().remove(key,value);
	}
	public Boolean setIsMember(String key,String value) {
		return stringRedisTemplate.opsForSet().isMember(key,value);
	}

	public Set<String> member(String key) {
		return stringRedisTemplate.opsForSet().members(key);
	}
}
