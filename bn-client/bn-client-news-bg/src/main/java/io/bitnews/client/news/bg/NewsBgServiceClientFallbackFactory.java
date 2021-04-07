package io.bitnews.client.news.bg;

import feign.hystrix.FallbackFactory;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.internal.GuessTriggerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Created by ywd on 2019/11/21.
 */
@Slf4j
@Component
public class NewsBgServiceClientFallbackFactory implements FallbackFactory<NewsBgServiceClient> {
    @Override
    public NewsBgServiceClient create(Throwable throwable) {
        return new NewsBgServiceClient() {
            BaseResponse baseResponse = new BaseResponse(UserSdkErrorCode.INTERNAL_SERVER_ERROR);
            @Override
            public BaseResponse launchGuessJob(GuessTriggerVo guessTriggerVo) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }
        };
    }
}
