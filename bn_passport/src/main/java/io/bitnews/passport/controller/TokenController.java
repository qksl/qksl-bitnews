package io.bitnews.passport.controller;

import io.bitnews.common.model.BNResponse;
import io.bitnews.passport.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

/**
 * Created by ywd on 2019/8/28.
 */
@Slf4j
@Api(tags = "token操作接口")
@RestController
@RequestMapping("/v1/passport/token")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("amount")
    @ApiOperation(value = "查询积分")
    public BNResponse<BigDecimal> amount(Principal principal){
        String userId = principal.getName();
        return tokenService.queryTokenAmount(userId);
    }
}
