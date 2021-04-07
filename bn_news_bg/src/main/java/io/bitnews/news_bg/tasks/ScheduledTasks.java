package io.bitnews.news_bg.tasks;

import com.alibaba.fastjson.JSONObject;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.internal.FearGreedVo;
import io.bitnews.news_bg.service.ToolService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * Created by ywd on 2019/11/27.
 */
@Component
@Slf4j
public class ScheduledTasks {

    @Autowired
    private ToolService toolService;

    OkHttpClient client = new OkHttpClient();

    @Scheduled(cron = "0 30 13 1/1 * ?")
    public void reportCurrentTime() {
//        System.out.println("现在时间：" + dateFormat.format(new Date()));
        Request request = new Request.Builder()
                .url("https://api.alternative.me/fng/")
                .build();
        String rs;
        for (int i = 0; i < 5; i++) {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    rs = response.body().string();
                    log.info("获取恐慌值成功", rs);
                    FearGreedVo fearGreedVo = toolService.create(rs);
                    BaseResponse save = toolService.save(fearGreedVo);
                    if (save.isToast()) {
                        log.info("恐慌值保存成功");
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("获取恐慌值失败", e);
            }
            try {
                Thread.sleep(10* 60*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
