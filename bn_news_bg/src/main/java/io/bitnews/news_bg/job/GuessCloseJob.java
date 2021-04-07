package io.bitnews.news_bg.job;

import io.bitnews.news_bg.service.GuessService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ywd on 2019/9/26.
 */
@Slf4j
public class GuessCloseJob implements Job {

    @Autowired
    private GuessService guessService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //执行任务逻辑....
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        log.info("执行自定义封盘任务"+key.getName());
        guessService.closeJob(key.getName().substring(10));
    }
}
