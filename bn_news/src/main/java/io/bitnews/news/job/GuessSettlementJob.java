package io.bitnews.news.job;

import io.bitnews.model.external.GuessInfo;
import io.bitnews.news.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;

/**
 * Created by ywd on 2019/9/26.
 */
@Slf4j
public class GuessSettlementJob implements Job {

    private static final String CLOSE_STATUS = "1";

    @Autowired
    private JobService jobService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        //执行任务逻辑....
        JobKey key = jobExecutionContext.getJobDetail().getKey();
        log.info("执行自定义结算任务"+key.getName());
        String topicId = key.getName().substring(15);
        //判断该竞猜是否已经封盘
        GuessInfo guessInfo = jobService.verification(topicId);
        if (guessInfo == null || !CLOSE_STATUS.equals(guessInfo.getStatus())){
            log.error(String.format("结算任务%s执行失败", key.getName()));
            return;
        }
        try {
            jobService.settlementJob(guessInfo.getId().toString());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("结算任务执行失败", e);
        }
    }
}
