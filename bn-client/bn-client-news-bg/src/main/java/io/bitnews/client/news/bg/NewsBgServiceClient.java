package io.bitnews.client.news.bg;

import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.internal.GuessTriggerVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by ywd on 2019/11/21.
 */
@FeignClient(value = "bn-news-bg",fallbackFactory = NewsBgServiceClientFallbackFactory.class )
public interface NewsBgServiceClient {

    @PostMapping("/v1/bg/guess/lunch")
    BaseResponse launchGuessJob(@RequestBody GuessTriggerVo guessTriggerVo);
}
