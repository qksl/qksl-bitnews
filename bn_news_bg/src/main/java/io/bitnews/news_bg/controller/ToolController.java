package io.bitnews.news_bg.controller;

import com.alibaba.fastjson.JSONObject;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.internal.FearGreedVo;
import io.bitnews.model.internal.GuessTriggerVo;
import io.bitnews.news_bg.service.ToolService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;

/**
 * Created by ywd on 2019/11/28.
 */
@RestController
@Slf4j
@RequestMapping("/v1/bg/tool")
public class ToolController {

    @Autowired
    private ToolService toolService;

    @PostMapping("invoke")
    public BaseResponse launchGuessJob() throws IOException {
        Request request = new Request.Builder()
                .url("https://api.alternative.me/fng/")
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            String rs = response.body().string();
            log.info("获取恐慌值成功", rs);
            FearGreedVo fearGreedVo = toolService.create(rs);
            BaseResponse save = toolService.save(fearGreedVo);
            if (save.isToast()) {
                log.info("恐慌值保存成功");
            }
        }else {
            return new BaseResponse(UserSdkErrorCode.API_INVOKE_ERROR);
        }
        return new BaseResponse();
    }
}
