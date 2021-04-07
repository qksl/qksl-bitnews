package io.bitnews.news_bg.controller;

import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.internal.GuessTriggerVo;
import io.bitnews.news_bg.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by ywd on 2019/9/26.
 */
@RestController
@Slf4j
@RequestMapping("/v1/bg/guess")
public class JobController {

    @Autowired
    private JobService jobService;


    @PostMapping("lunch")
    public BaseResponse launchGuessJob(@RequestBody GuessTriggerVo guessTriggerVo) throws SchedulerException {
        jobService.triggerCloseAndSettlementTask(guessTriggerVo);
        return new BaseResponse();
    }
}
