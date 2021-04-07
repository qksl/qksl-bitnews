package io.bitnews.news.controller;


import io.bienews.common.helper.StringUtil;
import io.bienews.common.helper.exception.CommonException;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.GuessInfo;
import io.bitnews.model.internal.BetsVo;
import io.bitnews.model.internal.GuessTriggerVo;
import io.bitnews.model.internal.PromoterTopicVo;
import io.bitnews.model.internal.PromoterVo;
import io.bitnews.model.news.po.TBets;
import io.bitnews.model.news.po.TPromoter;
import io.bitnews.model.news.po.TPromoterTopic;
import io.bitnews.model.show.GuessRecord;
import io.bitnews.news.scheduled.CmcFairValueJob;
import io.bitnews.news.service.BetsService;
import io.bitnews.news.service.PromoterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Created by ywd on 2019/7/30.
 */
@Slf4j
@Api("竞猜发起接口")
@RestController
@RequestMapping("/v1/user/guess")
public class GuessController {

    @Autowired
    private PromoterService promoterService;

    @Autowired
    private BetsService betsService;

    @Autowired
    private CmcFairValueJob cmcFairValueJob;

    private final static String GUESS_CLOSE_STATUS = "1";//封盘
    private final static String GUESS_SETTLEMENT_STATUS = "2";//结束
    private final static String GUESS_NULL_STATUS = "3";//流猜


    @GetMapping("latest")
    @ApiOperation(value = "最新竞猜")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "tokenType", value = "代币类别", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "discussionId", value = "资讯id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "竞猜状态", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "大小", dataType = "String")
    })
    public BNPageResponse<GuessInfo> latest(@RequestParam(value = "tokenType", required = false) String tokenType,
                                            @RequestParam(value = "discussionId", required = false) String discussionId,
                                            @RequestParam(value = "status", required = false) String status,
                                            @RequestParam(value = "userId", required = false) String userId,
                                            @RequestParam(value = "type", required = false) String type,
                                            @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue =
                                                          CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                            @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue =
                                                          CommonConstant.PAGE_SIZE_DEFAULT) String pageSize) {
        PageQuery<TPromoterTopic> query = new PageQuery<>();
        query.setPageSize(Long.parseLong(pageSize));
        query.setPageNumber(Long.parseLong(pageNum));
        if (StringUtil.isNotEmpty(tokenType)){
            query.setPara("tokenType", tokenType);
        }
        if (StringUtil.isNotEmpty(discussionId)){
            query.setPara("discussionId", discussionId);
        }
        if (StringUtil.isNotEmpty(status)){
            query.setPara("status", status);
        }
        if (StringUtil.isNotEmpty(type)){
            query.setPara("type", type);
        }
        if (StringUtil.isNotEmpty(userId)){
            query.setPara("createUserId", userId);
        }
        query.setOrderBy("id desc");
        BNPageResponse<GuessInfo> rs = promoterService.queryPageByCondition(query);
        return rs;
    }

    @PostMapping("launch")
    @ApiOperation(value = "发起一个竞猜")
    public BaseResponse launch(@RequestBody PromoterTopicVo promoterTopicVo) throws Exception {
        promoterService.insertTopic(promoterTopicVo);
        return new BaseResponse();
    }

    @PostMapping("/contract/launch")
    @ApiOperation(value = "发起一个竞猜")
    public BaseResponse contractLaunch(@RequestBody PromoterTopicVo promoterTopicVo) throws Exception {
        Long promoterId = promoterTopicVo.getPromoterId();
        TPromoter tPromoter = promoterService.findByPromoterId(promoterId);
        BeanUtils.copyProperties(tPromoter, promoterTopicVo);
        promoterTopicVo.setType(CommonConstant.GUESS_TYPE_TWO);
        promoterService.insertTopic(promoterTopicVo);
        return new BaseResponse();
    }

    @PostMapping("contract")
    @ApiOperation(value = "管理员发起一个合约")
    public BaseResponse createContract(@RequestBody PromoterVo promoterVo) throws CommonException {
        promoterService.insertPromoter(promoterVo);
        return new BaseResponse();
    }

    @PostMapping("join")
    @ApiOperation(value = "参加竞猜")
    public BaseResponse join(@RequestBody BetsVo betsVo) {
        TPromoterTopic tPromoterTopic = promoterService.findByPromoterTopicId(betsVo.getPromoterTopicId());
        //检查是否为开盘状态
        if(null == tPromoterTopic) {
            return new BaseResponse(UserSdkErrorCode.GUESS_BAD_REQUEST);
        }
        if (GUESS_CLOSE_STATUS.equals(tPromoterTopic.getStatus())) {
            return new BaseResponse(UserSdkErrorCode.GUESS_BAD_REQUEST_ClOSE);
        }
        if (GUESS_SETTLEMENT_STATUS.equals(tPromoterTopic.getStatus()) || GUESS_NULL_STATUS.equals(tPromoterTopic.getStatus())){
            return new BaseResponse(UserSdkErrorCode.GUESS_BAD_REQUEST_SETTLEMENT);
        }
        //如果是第二种开猜方式，那么用户只能下对立面
        if (CommonConstant.GUESS_TYPE_TWO.equals(tPromoterTopic.getType()) && betsVo.getBets().equals(tPromoterTopic.getWinner())) {
            return new BaseResponse(UserSdkErrorCode.GUESS_BAD_REQUEST_ERROR);
        }
        promoterService.joinBets(betsVo, tPromoterTopic);
        return new BaseResponse();
    }

    @GetMapping("query/topic")
    @ApiOperation(value = "查询发起的竞猜")
    public BNPageResponse<GuessInfo> queryTopicByMyself(@RequestParam(value = "userId") String userId,
                                                                @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue =
                                                                        CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue =
                                                                        CommonConstant.PAGE_SIZE_DEFAULT) String pageSize) {
        PageQuery<TPromoterTopic> query = new PageQuery<>();
        query.setPageSize(Long.parseLong(pageSize));
        query.setPageNumber(Long.parseLong(pageNum));
        StringBuffer sb = new StringBuffer(pageNum+pageSize);
        if (StringUtil.isNotEmpty(userId)){
            query.setPara("createUserId", userId);
            sb.append(userId);
        }
        query.setOrderBy("id desc");
        BNPageResponse<GuessInfo> rs = promoterService.queryPageByCondition(query);
        return rs;
    }

    @GetMapping("query/bets")
    @ApiOperation(value = "查询参与的竞猜")
    public BNPageResponse<GuessRecord> queryBetsByMyself(@RequestParam(value = "userId") String userId,
                                                      @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue =
                                                              CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                      @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue =
                                                              CommonConstant.PAGE_SIZE_DEFAULT) String pageSize) {
        PageQuery<TBets> query = new PageQuery<>();
        query.setPageSize(Long.parseLong(pageSize));
        query.setPageNumber(Long.parseLong(pageNum));
        if (StringUtil.isNotEmpty(userId)){
            query.setPara("userId", userId);
        }
        query.setOrderBy("id desc");
        BNPageResponse<GuessRecord> rs = betsService.queryPage(query);
        return rs;
    }

    @GetMapping("topic/byId")
    @ApiOperation(value = "查询竞猜")
    public BNResponse<GuessInfo> queryTopicById(@RequestParam("id") String id) {
        GuessInfo rs = promoterService.findById(Long.parseLong(id));
        return new BNResponse(rs);
    }


    @PostMapping("close")
    public BaseResponse close(@RequestParam("id") String id) {
        log.info("竞猜封盘：" + id);
        promoterService.closePromoter(id);
        return new BaseResponse();
    }

    @PostMapping("start_job")
    public BaseResponse startJob(@RequestParam("id") String id) throws Exception {
        log.info("添加job：" + id);
        TPromoterTopic tPromoterTopic = promoterService.findByPromoterTopicId(Long.parseLong(id));
        GuessTriggerVo guessTriggerVo = new GuessTriggerVo();
        guessTriggerVo.setJobId(tPromoterTopic.getId().toString());
        guessTriggerVo.setCloseTriggerTime(tPromoterTopic.getPermitTime());
        guessTriggerVo.setSettlementTriggerTime(tPromoterTopic.getSettlementTime());
        promoterService.startJob(guessTriggerVo);
        return new BaseResponse();
    }

    @PostMapping("settlement")
    public BaseResponse settlement(@RequestParam("id") String id) throws Exception {
        log.info("竞猜结算：" + id);
        GuessInfo guessInfo = promoterService.findById(Long.parseLong(id));
        BigDecimal priceNow = cmcFairValueJob.getPrice(guessInfo.getTokenType());
        //当前大于猜测金额则表示能结果为0
        log.info(String.format("查询当前时间点%s的价格为%s", guessInfo.getTokenType(), priceNow.toString()));
        String compare = priceNow.compareTo(guessInfo.getGuessGold()) >= 0?"0":"1";//0是大于，1是小于
        String winner;
        if(guessInfo.getGuessWinner().equals(compare)) {
            //如果结果和判断相等则，支持者获胜
            winner = CommonConstant.GUESS_YES;
        } else {
            winner = CommonConstant.GUESS_NO;
        }
        promoterService.settlement(id,winner);
        return new BaseResponse();
    }
}
