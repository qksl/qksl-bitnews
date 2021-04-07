package io.bitnews.client.passport;

import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.internal.PromoterTopicVo;
import io.bitnews.model.internal.UserDataVo;
import io.bitnews.model.internal.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by ywd on 2019/11/21.
 */
@FeignClient(value = "bn-passport", fallbackFactory = PassportServiceClientFallbackFactory.class)
public interface PassportServiceClient {


    @PostMapping("/v1/user/guess/close")
    BaseResponse close(@RequestParam("id") String id);

    @PostMapping("/v1/user/guess/settlement")
    BaseResponse settlement(@RequestParam("id") String id);

    @GetMapping("/v1/user/guess/topic/byId")
    BNResponse<PromoterTopicVo> queryTopicById(@RequestParam("id") String id);

    @GetMapping("/v1/passport/user/find/id")
    BNResponse<UserVo> findById(@RequestParam("userId") String userId);

}
