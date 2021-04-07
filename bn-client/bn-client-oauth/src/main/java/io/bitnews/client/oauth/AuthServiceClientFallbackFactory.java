package io.bitnews.client.oauth;

import feign.hystrix.FallbackFactory;
import io.bitnews.common.model.JWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by ywd on 2019/11/21.
 */
@Component
@Slf4j
public class AuthServiceClientFallbackFactory implements FallbackFactory<AuthServiceClient> {
    @Override
    public AuthServiceClient create(Throwable throwable) {
        return new AuthServiceClient() {
            @Override
            public JWT getToken(String authorization, String type, String username, String password, String auth_type) {
                log.info("fallback; reason was: " , throwable);
                return null;
            }
        };
    }
}
