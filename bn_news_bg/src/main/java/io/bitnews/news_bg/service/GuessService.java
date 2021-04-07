package io.bitnews.news_bg.service;

import io.bitnews.client.news.NewsServiceClient;
import io.bitnews.common.model.BNResponse;
import io.bitnews.model.external.GuessInfo;
import io.bitnews.model.internal.PromoterTopicVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by ywd on 2019/9/26.
 */
@Service
public class GuessService {

    @Autowired
    private NewsServiceClient newsServiceClient;

    public GuessInfo verification(String topicId) {
        BNResponse<GuessInfo> rs = newsServiceClient.queryTopicById(topicId);
        return rs.getBody();
    }

    public void closeJob(String topicId) {
        newsServiceClient.close(topicId);
    }

    public void settlementJob(String topicId) {
        newsServiceClient.settlement(topicId);
    }

}
