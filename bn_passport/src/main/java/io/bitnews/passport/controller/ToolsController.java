package io.bitnews.passport.controller;

import io.bitnews.common.model.BNResponse;
import io.bitnews.model.internal.FearGreedVo;
import io.bitnews.passport.service.ToolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ywd on 2019/11/27.
 */
@RestController
@Api(tags = "功能模块操作接口")
@RequestMapping("/v1/passport/fear")
public class ToolsController {


    @Autowired
    private ToolService toolService;

    @GetMapping("latest")
    @ApiOperation(value = "最新恐慌值")
    public BNResponse<List<FearGreedVo>> latestFear(@RequestParam(value = "limit", required = false) String limit) {
        return toolService.latest(limit);
    }
}
