package io.bitnews.passport.controller;

import io.bienews.common.helper.StringUtil;
import io.bienews.common.helper.exception.CommonException;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.*;
import io.bitnews.model.internal.BetsVo;
import io.bitnews.model.internal.PromoterTopicVo;
import io.bitnews.model.internal.PromoterVo;
import io.bitnews.model.passport.po.TUser;
import io.bitnews.model.show.GuessRecord;
import io.bitnews.passport.service.GuessService;
import io.bitnews.passport.service.TokenService;
import io.bitnews.passport.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by ywd on 2019/8/27.
 */
@Slf4j
@Api(tags = "竞猜查询调用接口")
@RestController
@RequestMapping("/v1/passport/guess")
public class GuessController {

    @Autowired
    private GuessService guessService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    private static final String GUESS_TYPE_ONE = "1";

    @GetMapping("bets")
    @ApiOperation(value = "查询发起参与的竞猜")
    public BNPageResponse<GuessRecord> queryBetsByMyself(@RequestParam(value = CommonConstant.PAGE_NUM, defaultValue =
            CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                         @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue =
                                                            CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                         Principal principal) {

        String userId = principal.getName();
        return guessService.queryBets(userId, pageNum, pageSize);
    }

    @GetMapping("topic")
    @ApiOperation(value = "查询发起的竞猜")
    public BNPageResponse<GuessInfo> queryTopicByMyself(
            @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize, Principal principal) {
        TUser tUser = userService.findById(principal.getName());
        BNPageResponse<GuessInfo> rs = guessService.queryTopic(tUser.getId().toString(), pageNum, pageSize);
        return rs;
    }

    @PostMapping("launch")
    @ApiOperation(value = "发起一个竞猜,自动收益竞猜")
    public BaseResponse launch(@RequestBody @ApiParam(name = "竞猜对象", value = "传入json格式", required = true) LaunchVo launchVo, Principal principal) {
        if (launchVo.getDiscussionId() == null) {
            return new BNResponse(UserSdkErrorCode.GUESS_BAD_REQUEST_DISCUSSION);
        }
        TUser tUser = userService.findById(principal.getName());
        if (!tUser.getType().equals(CommonConstant.USER_TYPE_BIGV)) {
            return new BNResponse(UserSdkErrorCode.GUESS_BAD_REQUEST_USER);
        }
        //判断用户是否有足够资金
        BNResponse<BigDecimal> amountBNResponse = tokenService.queryTokenAmount(tUser.getId().toString());
        if (!amountBNResponse.isToast()) {
            return new BNResponse(UserSdkErrorCode.TOKEN_BAD_REQUEST);
        }
        BigDecimal amount = amountBNResponse.getBody();
        if (amount.compareTo(launchVo.getBetsGold()) < 0) {
            return new BNResponse(UserSdkErrorCode.TOKEN_BAD_LACK);
        }
        PromoterTopicVo promoterTopicVo = new PromoterTopicVo();
        BeanUtils.copyProperties(launchVo, promoterTopicVo);
        promoterTopicVo.setCreateUserId(tUser.getId());
        //第一种竞猜，永远都是支持自己的观点
        promoterTopicVo.setType(CommonConstant.GUESS_TYPE_ONE);
        promoterTopicVo.setWinner(CommonConstant.GUESS_YES);
        try {
            long lp = 1;
            if (!"0".equals(launchVo.getPermit())) {
                //获取提前封盘的时间精确到分
                Double v = Double.parseDouble(launchVo.getPermit()) * 24 * 60;
                lp = v.longValue();
            }
            Instant instant = launchVo.getSettlementTime().toInstant();
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime preTimeLocal = instant.atZone(zoneId).toLocalDateTime();
            Instant postInstant = preTimeLocal.plusMinutes(-lp).atZone(zoneId).toInstant();
            final Date permitTime = Date.from(postInstant);
            if (permitTime.getTime() < new Date().getTime()) {
                return new BNResponse(UserSdkErrorCode.BAD_REQUEST);
            }
            promoterTopicVo.setPermitTime(permitTime);
            //竞猜内容
            String formatSettleTime = preTimeLocal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss "));
            String sr = "0".equals(launchVo.getGuessWinner()) ? "高于" : "低于";
            promoterTopicVo.setContext("预测"+formatSettleTime + launchVo.getTokenType() + sr + launchVo.getGuessGold());
        } catch (Exception e) {
            return new BNResponse(UserSdkErrorCode.BAD_REQUEST);
        }
        return guessService.launch(promoterTopicVo);
    }

    /**
     * 第二种方式
     * @param principal
     * @return
     */
    @PostMapping("contract/launch")
    @ApiOperation(value = "水友开猜")
    public BaseResponse contractLaunch(@RequestBody @ApiParam(name = "竞猜对象", value = "传入json格式", required = true) ContractLaunch contractLaunch, Principal principal) {
        if (null == principal) {
            return new BNResponse(UserSdkErrorCode.USERID_NULL);
        }
        String userId = principal.getName();
        PromoterTopicVo promoterTopicVo = new PromoterTopicVo();
        BeanUtils.copyProperties(contractLaunch, promoterTopicVo);
        promoterTopicVo.setCreateUserId(Long.parseLong(userId));
        return guessService.contractLaunch(promoterTopicVo);
    }

    @PostMapping("join")
    @ApiOperation(value = "参加竞猜")
    public BaseResponse join(@RequestBody @ApiParam(name = "竞猜对象", value = "传入json格式", required = true) JoinVo joinVo
            , Principal principal) {
        String userId = principal.getName();
        TUser tUser = userService.findById(userId);
        //判断用户是否有足够资金
        BNResponse<BigDecimal> amountBNResponse = tokenService.queryTokenAmount(tUser.getId().toString());
        if (!amountBNResponse.isToast()) {
            return new BNResponse(UserSdkErrorCode.TOKEN_BAD_REQUEST);
        }
        BigDecimal amount = amountBNResponse.getBody();
        if (amount.compareTo(joinVo.getBetsGold()) < 0) {
            return new BNResponse(UserSdkErrorCode.TOKEN_BAD_LACK);
        }
        BetsVo betsVo = new BetsVo();
        BeanUtils.copyProperties(joinVo, betsVo);
        betsVo.setUserId(tUser.getId());
        return guessService.join(betsVo);
    }

}
