package io.bitnews.user_sdk.config;

import java.math.BigDecimal;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wangyufei
 * @Date: 2018/8/13 11:33
 */
@Configuration
public class DeserializationConfig {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer addCustomBigDecimalSerializer(
			BigDecimaSerializer bigDecimalDeserializer) {
		return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.serializerByType(BigDecimal.class,
				bigDecimalDeserializer);
	}
}
