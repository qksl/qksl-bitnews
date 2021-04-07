package io.bitnews.passport.controller;

import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.model.external.DistributionInfo;
import io.bitnews.model.external.MarketInfo;
import io.bitnews.model.internal.CoinMarketVo;
import io.bitnews.passport.service.NewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ywd on 2019/8/12.
 */
@Slf4j
@Api(tags = "市场行情接口")
@RestController
@RequestMapping("/v1/passport/quotes")
public class MarketAdminController {

    @Autowired
    private NewService newService;

    @GetMapping("latest")
    @ApiOperation(value = "最新价格")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "symbol", value= "代币类别", defaultValue =
                    "BTC", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "start", value= "起始时间,格式: 2019-08-02", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "end", value = "结束时间，格式: 2019-08-02", dataType = "String")
    })
    public BNPageResponse<CoinMarketVo> latest(@RequestParam(value = "symbol", defaultValue = "BTC")String symbol, @RequestParam("start")String start, @RequestParam("end")String end) {
        return newService.queryLatestQuotes(symbol, start, end);
    }

    @GetMapping("market/info")
    @ApiOperation(value = "市场情况")
    public BNResponse<MarketInfo> marketInfo() {
        return newService.getMarketInfo();
    }

    @GetMapping("market/disb")
    @ApiOperation(value = "涨跌分布")
    public BNResponse<DistributionInfo> marketDistribution() {
        return newService.getMarketDistribution();
    }
}
