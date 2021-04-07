package io.bitnews.passport.service;

import io.bitnews.client.news.NewsServiceClient;
import io.bitnews.common.model.BNResponse;
import io.bitnews.model.internal.FearGreedVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ywd on 2019/11/27.
 */
@Service
public class ToolService {

    @Autowired
    private NewsServiceClient newsServiceClient;

    public BNResponse<List<FearGreedVo>> latest(String limit) {
        return newsServiceClient.latestFear(limit);
    }
}
