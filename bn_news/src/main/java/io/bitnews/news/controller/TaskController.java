package io.bitnews.news.controller;

import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.TaskCenterVo;
import io.bitnews.model.external.TaskVo;
import io.bitnews.news.service.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by ywd on 2019/12/16.
 */
@Slf4j
@Api("token操作接口")
@RestController
@RequestMapping("/v1/user/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("signin")
    @ApiOperation(value = "每日签到")
    public BaseResponse signin(@RequestParam("userId")String userId) {
        return taskService.signIn(userId);
    }

    @PostMapping("awards")
    @ApiOperation(value = "领取奖励")
    public BaseResponse receiveAwardsTask(@RequestParam("taskId")String taskId, @RequestParam("userId")String userId) {
        return taskService.receiveAwards(taskId,userId);
    }

    @PostMapping("first")
    @ApiOperation(value = "第一次登录")
    public BaseResponse firstLogin(@RequestParam("userId")String userId) {
        taskService.firstLogin(userId);
        return new BaseResponse();
    }

    @GetMapping("list")
    @ApiOperation(value = "任务展示")
    public BNResponse<TaskCenterVo> list(@RequestParam("userId") String userId) {
        TaskCenterVo taskCenterVo = taskService.taskCenter(userId);
        return new BNResponse(taskCenterVo);
    }

    @GetMapping("find")
    @ApiOperation(value = "查找任务")
    public BNResponse<TaskVo> findTaskById(@RequestParam("id") String id) {
        TaskVo taskVo = taskService.queryById(id);
        return new BNResponse(taskVo);
    }

    @PostMapping("insert")
    @ApiOperation(value = "添加任务")
    public BaseResponse insertTask(@RequestBody TaskVo taskVo) {
        taskService.saveTask(taskVo);
        return new BaseResponse();
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除任务")
    public BaseResponse deleteTask(@RequestParam("taskId")String taskId) {
        taskService.deleteTask(taskId);
        return new BaseResponse();
    }
}
