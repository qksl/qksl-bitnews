package io.bitnews.news.job;

import io.bitnews.news.service.JobService;
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
    private JobService jobService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //执行任务逻辑....
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        log.info("执行自定义封盘任务"+key.getName());
        jobService.closeJob(key.getName().substring(10));
    }
}
