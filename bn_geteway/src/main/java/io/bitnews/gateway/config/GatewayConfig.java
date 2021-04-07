package io.bitnews.gateway.config;

import io.bitnews.gateway.filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ywd on 2020/2/21.
 */
@Configuration
public class GatewayConfig {

    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter();
    }
}
