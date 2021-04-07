package io.bitnews.admin.controller;

import io.bitnews.admin.service.TokenService;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.GrantVo;
import io.bitnews.model.external.UserTokenHistory;
import io.bitnews.model.news.po.TToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

/**
 * Created by ywd on 2019/8/28.
 */
@Slf4j
@Api(tags = "token操作接口")
@RestController
@RequestMapping("/v1/admin/token")
@PreAuthorize("hasAuthority('ADMIN')")
public class TokenAdminController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("amount")
    @ApiOperation(value = "查询积分")
    public BNResponse<BigDecimal> amount(@RequestParam(value = "userId") String userId){
        TToken tToken = tokenService.queryAmount(Long.parseLong(userId));
        return new BNResponse<>(tToken.getToken());
    }

    @PostMapping("grant")
    @ApiOperation(value = "发放积分")
    public BaseResponse grant(@RequestBody @ApiParam(name = "发放积分对象", value = "传入json格式", required = true) GrantVo grantVo, Principal principal){
        log.info(String.format("%s发放%s积分数量：%s", principal.getName(), grantVo.getId(), grantVo.getAmount()));
        tokenService.grant(Long.parseLong(grantVo.getId()), BigDecimal.valueOf(Double.parseDouble(grantVo.getAmount())));
        return new BaseResponse();
    }

    @GetMapping("amount/history")
    @ApiOperation(value = "查询积分历史")
    public BNPageResponse<UserTokenHistory> amountHistory(@RequestParam(value = "userId")String userId, @RequestParam(value = CommonConstant.PAGE_NUM) String pageNum,
                                                          @RequestParam(value = CommonConstant.PAGE_SIZE) String pageSize) {
        return tokenService.amountHistory(Long.parseLong(userId), Integer.parseInt(pageNum),Integer.parseInt(pageSize));
    }

}
