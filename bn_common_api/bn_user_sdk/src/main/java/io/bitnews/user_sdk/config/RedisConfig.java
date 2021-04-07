package io.bitnews.user_sdk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort;

	@Value("${spring.redis.password}")
	private String redisPassword;

	@Value("${spring.redis.database}")
	private int redisDatabase;

	@Value("${spring.redis.timeout}")
	private int redisTimeout;

	@Value("${spring.redis.pool.max-total}")
	private int redisMaxTotal;

	@Value("${spring.redis.pool.max-idle}")
	private int redisMaxIdle;

	@Value("${spring.redis.pool.min-idle}")
	private int redisMinIdle;

	@Value("${spring.redis.pool.max-wait}")
	private int redisMaxWait;

	@Value("${spring.redis.pool.test-on-borrow}")
	private String redisTestOnBorrow;

	@Value("${spring.datasource.host}")
	private String dbHost;

	@Value("${spring.datasource.password}")
	private String password;

	@Bean
	@Primary
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig pool = new JedisPoolConfig();
		pool.setMaxTotal(redisMaxTotal);
		pool.setMaxIdle(redisMaxIdle);
		pool.setMinIdle(redisMinIdle);
		pool.setMaxWaitMillis(redisMaxWait * 1000);
		pool.setTestOnBorrow(Boolean.valueOf(redisTestOnBorrow));
		return pool;
	}

	@Bean
	@Primary
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory cf = new JedisConnectionFactory();
		cf.setHostName(redisHost);
		cf.setPort(redisPort);
		cf.setPassword(redisPassword);
		cf.setDatabase(redisDatabase);
		cf.setTimeout(redisTimeout);
//		cf.setPoolConfig(jedisPoolConfig());
		cf.afterPropertiesSet();
		return cf;
	}

	@Primary
	@Bean
	public JedisPool jedisPool() {
		JedisPoolConfig config = jedisPoolConfig();
		JedisPool pool = new JedisPool(config, redisHost, redisPort, redisTimeout, redisPassword, redisDatabase);
		return pool;
	}

	@Primary
	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		StringRedisTemplate rt = new StringRedisTemplate(jedisConnectionFactory());
		return rt;
	}

	@Bean("intRedisTemplate")
	public RedisTemplate<String, Integer> intRedisTemplate() {
		RedisTemplate<String, Integer> rt = new RedisTemplate<String, Integer>();
		rt.setKeySerializer(new StringRedisSerializer());
		rt.setValueSerializer(new GenericToStringSerializer<Integer>(Integer.class));
		rt.setConnectionFactory(jedisConnectionFactory());
		return rt;
	}

}
