package io.bitnews.client.news;

import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.*;
import io.bitnews.model.internal.*;
import io.bitnews.model.show.GuessRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ywd on 2019/11/21.
 */
@FeignClient(value = "bn-news",fallbackFactory = NewsServiceClientFallbackFactory.class )
public interface NewsServiceClient {

    /**
     * 下方为管理端调用
     * @param bannerVo
     * @return
     */

    @PostMapping("/v1/admin/guess/close")
    BaseResponse closeGuess(@RequestParam("id") String id);

    @PostMapping("/v1/admin/guess/settlement")
    BaseResponse settlementGuess(@RequestParam("id") String id, @RequestParam("winner") String winner);


    /**
     * 下方为客户端调用
     *
     */
    @GetMapping("/v1/user/banner/list")
    BNPageResponse<BannerVo> list(
            @RequestParam(value = CommonConstant.PAGE_NUM) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE) String pageSize);

    @GetMapping("/v1/user/comment/list")
    BNPageResponse<PostVo> list(
            @RequestParam(value = "discussionId") String discussionId,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "status") String status,
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = CommonConstant.PAGE_NUM) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE) String pageSize);

    @GetMapping("/v1/user/comment/max")
    BNResponse<PostVo> maxLike(@RequestParam(value = "discussionId") String discussionId);

    @GetMapping("/v1/user/news/latest")
    BNPageResponse<News> latestNews(
            @RequestParam(value = "tag") String tag,
            @RequestParam(value = CommonConstant.PAGE_NUM) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE) String pageSize);

    @GetMapping("/v1/user/news/details")
    BNResponse<DetailsDiscussionVo> details(@RequestParam("id") String id);

    @GetMapping("/v1/user/news/event")
    BNPageResponse<DiscussionVo> event(@RequestParam("start")String start, @RequestParam("end")String end);

    @GetMapping("/v1/user/news/banner/list")
    BNPageResponse<BannerVo> list(@RequestParam(value = "type")String type);

    /**
     * 市场行情
     * @param symbol
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/v1/user/quotes/latest")
    BNPageResponse<CoinMarketVo> latestQuotes(@RequestParam("symbol")String symbol, @RequestParam("start")String start, @RequestParam("end")String end);

    @GetMapping("/v1/user/quotes/market/info")
    BNResponse<MarketInfo> marketInfo();

    @GetMapping("/v1/user/quotes/market/distribution")
    BNResponse<DistributionInfo> marketDistribution();

    /**
     *
     * @param postVo
     * @return
     */

    @PostMapping("/v1/user/comment/issue")
    BaseResponse issue(@RequestBody PostVo postVo);

    @PostMapping("/v1/user/comment/liked")
    BaseResponse liked(@RequestParam("postId")String postId, @RequestParam("userId")String userId);

    @PostMapping("/v1/user/comment/liked/cancel")
    BaseResponse cancelLike(@RequestParam("postId")String postId, @RequestParam("userId")String userId);
    @GetMapping("/v1/user/guess/latest")
    BNPageResponse<GuessInfo> latest(@RequestParam(value = "tokenType", required = false) String tokenType,
                                     @RequestParam(value = "discussionId", required = false) String discussionId,
                                     @RequestParam(value = "status", required = false) String status,
                                     @RequestParam(value = "userId", required = false) String userId,
                                     @RequestParam(value = "type", required = false) String type,
                                     @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue =
                                                   CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                     @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue =
                                                   CommonConstant.PAGE_SIZE_DEFAULT) String pageSize);


    @GetMapping("/v1/user/guess/query/topic")
    BNPageResponse<GuessInfo> queryTopicByMyself(@RequestParam(value = "userId") String userId,
                                                       @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue =
                                                               CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                       @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue =
                                                               CommonConstant.PAGE_SIZE_DEFAULT) String pageSize);

    @GetMapping("/v1/user/guess/query/bets")
    BNPageResponse<GuessRecord> queryBetsByMyself(@RequestParam(value = "userId") String userId,
                                                  @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue =
                                                     CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                  @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue =
                                                     CommonConstant.PAGE_SIZE_DEFAULT) String pageSize);

    @PostMapping("/v1/user/guess/launch")
    BaseResponse launch(@RequestBody PromoterTopicVo promoterTopicVo);

    @PostMapping("/v1/user/guess/contract/launch")
    BaseResponse contractLaunch(@RequestBody PromoterTopicVo promoterTopicVo);

    @PostMapping("/v1/user/guess/join")
    BaseResponse join(@RequestBody BetsVo betsVo);

    @PostMapping("/v1/user/guess/contract")
    BaseResponse createContract(@RequestBody PromoterVo promoterVo);

    @GetMapping("/v1/user/token/amount")
    BNResponse<BigDecimal> amount(@RequestParam(value = "userId")String userId);

    @PostMapping("/v1/user/token/grant")
    BaseResponse grant(@RequestParam(value = "userId")String userId, @RequestParam("amount")String amount);

    @PostMapping("/v1/user/token/reward")
    BaseResponse reward(@RequestParam(value = "userId")String userId, @RequestParam("amount")String amount, @RequestParam("reason")String reason);

    @PostMapping("/v1/user/token/create")
    BNResponse createTokenAccount(@RequestParam(value = "userId")String userId);

    @GetMapping("/v1/user/token/amount/history")
    BNPageResponse<UserTokenHistory> amountHistory(@RequestParam(value = "userId")String userId, @RequestParam(value = CommonConstant.PAGE_NUM) String pageNum,
                                                   @RequestParam(value = CommonConstant.PAGE_SIZE) String pageSize);
    /**
     * 下方为定时任务模块调用
     */
    @PostMapping("/v1/user/guess/close")
    BaseResponse close(@RequestParam("id") String id);

    @PostMapping("/v1/user/guess/settlement")
    BaseResponse settlement(@RequestParam("id") String id);

    @GetMapping("/v1/user/guess/topic/byId")
    BNResponse<GuessInfo> queryTopicById(@RequestParam("id") String id);

    /**
     * 恐慌值
     */
    @GetMapping("/v1/user/fear/latest")
    BNResponse<List<FearGreedVo>> latestFear(@RequestParam(value = "limit", required = false) String limit);

    @PostMapping("/v1/user/fear/save")
    BaseResponse saveFear(@RequestBody FearGreedVo fearGreedVo);

    /**
     * 任务模块
     */
    @PostMapping("/v1/user/task/signin")
    BaseResponse signin(@RequestParam("userId")String userId);

    @GetMapping("/v1/user/task/list")
    BNResponse<TaskCenterVo> listTask(@RequestParam("userId") String userId);

    @PostMapping("/v1/user/task/awards")
    BaseResponse receiveAwardsTask(@RequestParam("taskId")String taskId, @RequestParam("userId")String userId);

    @PostMapping("/v1/user/task/first")
    BaseResponse firstLogin(@RequestParam("userId")String userId);

    @PostMapping("/v1/user/task/insert")
    BaseResponse insertTask(@RequestBody TaskVo taskVo);

    @PostMapping("/v1/user/task/delete")
    BaseResponse deleteTask(@RequestParam("taskId")String taskId);

    @GetMapping("/v1/user/task/find")
    BNResponse<TaskVo> findTaskById(@RequestParam("id") String id);
}
