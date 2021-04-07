package io.bitnews.user_sdk.config;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @Author: wangyufei
 * @Date: 2018/8/13 11:48
 */
@Component
public class BigDecimaSerializer extends JsonSerializer<BigDecimal> {

	@Override
	public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeNumber(value.toPlainString());

	}

	@Override
	public Class<BigDecimal> handledType() {
		return BigDecimal.class;
	}
}
