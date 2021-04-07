package io.bitnews.news.controller;

import io.bitnews.common.constants.CommonBNErrorCode;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.UserTokenHistory;
import io.bitnews.model.news.po.TDiscussion;
import io.bitnews.model.news.po.TToken;
import io.bitnews.model.news.po.TTokenHistory;
import io.bitnews.news.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Created by ywd on 2019/8/26.
 */
@Slf4j
@Api("token操作接口")
@RestController
@RequestMapping("/v1/user/token")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PostMapping("grant")
    @ApiOperation(value = "发放积分")
    public BaseResponse grant(@RequestParam(value = "userId")String userId, @RequestParam("amount")String amount){
        TToken tToken = tokenService.findByUserId(Long.parseLong(userId));
        if (null == tToken) {
            return new BaseResponse(new CommonBNErrorCode("10001", "用户ID无效", "用户ID无效"));
        }
        tokenService.newAdd(Long.parseLong(userId), new BigDecimal(amount), "发放积分");
        return new BaseResponse();
    }

    @PostMapping("reward")
    @ApiOperation(value = "奖励积分")
    public BaseResponse reward(@RequestParam(value = "userId")String userId, @RequestParam("amount")String amount, @RequestParam("reason")String reason){
        tokenService.newAdd(Long.parseLong(userId), new BigDecimal(amount), reason);
        return new BaseResponse();
    }

    @GetMapping("amount")
    @ApiOperation(value = "查询积分")
    public BNResponse<BigDecimal> amount(@RequestParam(value = "userId")String userId){
        BigDecimal aLong = tokenService.queryTokenAmount(Long.parseLong(userId));
        return new BNResponse(aLong);
    }

    @GetMapping("amount/history")
    @ApiOperation(value = "查询积分历史")
    public BNPageResponse<UserTokenHistory> amountHistory(@RequestParam(value = "userId")String userId, @RequestParam(value = CommonConstant.PAGE_NUM) String pageNum,
                                                          @RequestParam(value = CommonConstant.PAGE_SIZE) String pageSize){
        PageQuery<TTokenHistory> query = new PageQuery<>();
        query.setPageSize(Long.parseLong(pageSize));
        query.setPageNumber(Long.parseLong(pageNum));
        query.setPara("userId", userId);
        String cacheKey = userId;
        return tokenService.queryTokenAmountHistory(query, cacheKey);
    }

    @PostMapping("create")
    @ApiOperation(value = "创建账户")
    public BNResponse createTokenAccount(@RequestParam(value = "userId")String userId) {
        tokenService.createTokenAccount(userId);
        return new BNResponse();
    }
}
