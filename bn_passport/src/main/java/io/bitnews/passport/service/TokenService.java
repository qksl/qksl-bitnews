package io.bitnews.passport.service;

import io.bitnews.client.news.NewsServiceClient;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by ywd on 2019/8/28.
 */
@Service
public class TokenService {

    @Autowired
    private NewsServiceClient newsServiceClient;

    public BNResponse<BigDecimal> queryTokenAmount(String userId) {
       return newsServiceClient.amount(userId);
    }

    public BNResponse createTokenAccount(String userId) {
        return newsServiceClient.createTokenAccount(userId);
    }

    public BaseResponse reward(String userId, String aount, String reason) {
        return newsServiceClient.reward(userId, aount, reason);
    }

}
