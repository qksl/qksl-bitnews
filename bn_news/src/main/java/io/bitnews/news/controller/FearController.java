package io.bitnews.news.controller;

import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.internal.FearGreedVo;
import io.bitnews.news.service.FearGreedService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ywd on 2019/11/27.
 */
@Slf4j
@Api("用户评分模块")
@RestController
@RequestMapping("/v1/user/fear")
public class FearController {

    @Autowired
    FearGreedService fearGreedService;


    @GetMapping("latest")
    @ApiOperation(value = "最新恐慌值")
    public BNResponse<List<FearGreedVo>> latestFear(@RequestParam(value = "limit", required = false) String limit) {
        List<FearGreedVo> latest = fearGreedService.latest(limit);
        return new BNResponse<>(latest);
    }

    @PostMapping("save")
    @ApiOperation(value = "查询恐慌值")
    public BaseResponse saveFear(@RequestBody FearGreedVo fearGreedVo) {
        fearGreedService.save(fearGreedVo);
        return new BaseResponse();
    }

}
