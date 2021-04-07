package io.bitnews.passport.controller;

import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.CodeVo;
import io.bitnews.model.external.IdVo;
import io.bitnews.model.external.TaskCenterVo;
import io.bitnews.model.passport.po.TUser;
import io.bitnews.passport.service.TaskService;
import io.bitnews.passport.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by ywd on 2019/12/12.
 */
@Slf4j
@Api(tags = "用户任务接口")
@RestController
@RequestMapping("/v1/passport/task")
public class UserTaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("signin")
    @ApiOperation(value = "每日签到")
    public BaseResponse signin(Principal principal) {
        String userId = principal.getName();
        return taskService.signIn(userId);
    }

    @GetMapping("list")
    @ApiOperation(value = "任务展示")
    public BNResponse<TaskCenterVo> list(Principal principal) {
        String userId = principal.getName();
        return taskService.taskCenter(userId);
    }

    @PostMapping("awards")
    @ApiOperation(value = "领取奖励")
    public BaseResponse receiveAwardsTask(@RequestBody IdVo idVo, Principal principal) {
        String userId = principal.getName();
        return taskService.receiveAwards(idVo.getId(), userId);
    }
}
