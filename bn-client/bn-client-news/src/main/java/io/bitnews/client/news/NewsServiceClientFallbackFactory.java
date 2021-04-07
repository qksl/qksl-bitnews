package io.bitnews.client.news;

import feign.hystrix.FallbackFactory;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.*;
import io.bitnews.model.internal.*;
import io.bitnews.model.show.GuessRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ywd on 2019/11/21.
 */
@Slf4j
@Component
public class NewsServiceClientFallbackFactory implements FallbackFactory<NewsServiceClient> {

    @Override
    public NewsServiceClient create(Throwable throwable) {
        return new NewsServiceClient() {

            BaseResponse baseResponse = new BaseResponse(UserSdkErrorCode.INTERNAL_SERVER_ERROR);
            BNResponse bnResponse = new BNResponse(UserSdkErrorCode.INTERNAL_SERVER_ERROR);
            BNPageResponse bnPageResponse = new BNPageResponse(UserSdkErrorCode.INTERNAL_SERVER_ERROR);

            @Override
            public BaseResponse closeGuess(String id) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BaseResponse settlementGuess(String id, String winner) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BNPageResponse<BannerVo> list(String pageNum, String pageSize) {
                log.info("fallback; reason was: " , throwable);
                return bnPageResponse;
            }

            @Override
            public BNPageResponse<PostVo> list(String discussionId, String type, String status, String userId,
                                               String pageNum, String pageSize) {
                log.info("fallback; reason was: " , throwable);
                return bnPageResponse;
            }

            @Override
            public BNResponse<PostVo> maxLike(String discussionId) {
                log.info("fallback; reason was: " , throwable);
                return bnResponse;
            }

            @Override
            public BNPageResponse<News> latestNews(String tag, String pageNum, String pageSize) {
                log.info("fallback; reason was: " , throwable);
                return bnPageResponse;
            }

            @Override
            public BNResponse<DetailsDiscussionVo> details(String id) {
                log.info("fallback; reason was: " , throwable);
                return bnResponse;
            }

            @Override
            public BNPageResponse<DiscussionVo> event(String start, String end) {
                log.info("fallback; reason was: " , throwable);
                return bnPageResponse;
            }

            @Override
            public BNPageResponse<BannerVo> list(String type) {
                log.info("fallback; reason was: " , throwable);
                return bnPageResponse;
            }

            @Override
            public BNPageResponse<CoinMarketVo> latestQuotes(String symbol, String start, String end) {
                log.info("fallback; reason was: " , throwable);
                return bnPageResponse;
            }

            @Override
            public BNResponse<MarketInfo> marketInfo() {
                log.info("fallback; reason was: " , throwable);
                return bnResponse;
            }

            @Override
            public BNResponse<DistributionInfo> marketDistribution() {
                log.info("fallback; reason was: " , throwable);
                return bnResponse;
            }

            @Override
            public BaseResponse issue(PostVo postVo) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BaseResponse liked(String postId, String userId) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BaseResponse cancelLike(String postId, String userId) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BNPageResponse<GuessInfo> latest(String tokenType, String discussionId, String status, String userId, String type
                    , String pageNum, String pageSize) {
                return bnPageResponse;
            }

            @Override
            public BNPageResponse<GuessInfo> queryTopicByMyself(String userId, String pageNum, String pageSize) {
                log.info("fallback; reason was: " , throwable);
                return bnPageResponse;
            }

            @Override
            public BNPageResponse<GuessRecord> queryBetsByMyself(String userId, String pageNum, String pageSize) {
                log.info("fallback; reason was: " , throwable);
                return bnPageResponse;
            }

            @Override
            public BaseResponse launch(PromoterTopicVo promoterTopicVo) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BaseResponse contractLaunch(PromoterTopicVo promoterTopicVo) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BaseResponse join(BetsVo betsVo) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BaseResponse createContract(PromoterVo promoterVo) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BNResponse<BigDecimal> amount(String userId) {
                log.info("fallback; reason was: " , throwable);
                return bnResponse;
            }

            @Override
            public BaseResponse grant(String userId, String amount) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BaseResponse reward(String userId, String amount, String reason) {
                log.info("fallback; reason was: " , throwable);
                return bnResponse;
            }

            @Override
            public BNResponse createTokenAccount(String userId) {
                log.info("fallback; reason was: " , throwable);
                return bnResponse;
            }

            @Override
            public BNPageResponse<UserTokenHistory> amountHistory(String userId, String pageNum, String pageSize) {
                log.info("fallback; reason was: " , throwable);
                return bnPageResponse;
            }

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
            public BNResponse<GuessInfo> queryTopicById(String id) {
                log.info("fallback; reason was: " , throwable);
                return bnResponse;
            }

            @Override
            public BNResponse<List<FearGreedVo>> latestFear(String limit) {
                return bnResponse;
            }

            @Override
            public BaseResponse saveFear(FearGreedVo fearGreedVo) {
                return baseResponse;
            }

            @Override
            public BaseResponse signin(String userId) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BNResponse<TaskCenterVo> listTask(String userId) {
                log.info("fallback; reason was: " , throwable);
                return bnResponse;
            }

            @Override
            public BaseResponse receiveAwardsTask(String taskId, String userId) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BaseResponse firstLogin(String userId) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BaseResponse insertTask(TaskVo taskVo) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BaseResponse deleteTask(String taskId) {
                log.info("fallback; reason was: " , throwable);
                return baseResponse;
            }

            @Override
            public BNResponse<TaskVo> findTaskById(String id) {
                log.info("fallback; reason was: " , throwable);
                return bnResponse;
            }
        };
    }
}
