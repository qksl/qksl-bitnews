package io.bitnews.client.passport;

import feign.hystrix.FallbackFactory;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.internal.PromoterTopicVo;
import io.bitnews.model.internal.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by ywd on 2019/11/21.
 */
@Slf4j
@Component
public class PassportServiceClientFallbackFactory implements FallbackFactory<PassportServiceClient> {
    @Override
    public PassportServiceClient create(Throwable throwable) {
        return new PassportServiceClient() {
            BaseResponse baseResponse = new BaseResponse(UserSdkErrorCode.INTERNAL_SERVER_ERROR);
            BNResponse bnResponse = new BNResponse(UserSdkErrorCode.INTERNAL_SERVER_ERROR);
            BNPageResponse bnPageResponse = new BNPageResponse(UserSdkErrorCode.INTERNAL_SERVER_ERROR);

            @Override
            public BaseResponse close(String id) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BaseResponse settlement(String id) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BNResponse<PromoterTopicVo> queryTopicById(String id) {
                log.info("fallback; reason was: " , throwable);
                return bnResponse;
            }

            @Override
            public BNResponse<UserVo> findById(String userId) {
                log.info("fallback; reason was: " , throwable);
                return bnResponse;
            }

        };
    }
}
