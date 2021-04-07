package io.bitnews.news_bg.service;

import com.alibaba.fastjson.JSONObject;
import io.bitnews.client.news.NewsServiceClient;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.internal.FearGreedVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by ywd on 2019/11/27.
 */
@Service
public class ToolService {

    @Autowired
    private NewsServiceClient newsServiceClient;

    public BaseResponse save(FearGreedVo fearGreedVo) {
        return newsServiceClient.saveFear(fearGreedVo);
    }

    public FearGreedVo create(String json) {
        JSONObject object = JSONObject.parseObject(json);
        JSONObject data = object.getJSONArray("data").getJSONObject(0);
        FearGreedVo fearGreedVo = new FearGreedVo();
        fearGreedVo.setFearValue(data.getIntValue("value"));
        fearGreedVo.setValueClassification(data.getString("value_classification"));
        String timestamp = data.getString("timestamp");
        long tp = Long.parseLong(timestamp);
        fearGreedVo.setUpdateTime(new Date(tp * 1000));
        return fearGreedVo;
    }
}
