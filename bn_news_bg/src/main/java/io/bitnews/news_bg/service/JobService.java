package io.bitnews.news_bg.service;

import io.bitnews.model.internal.GuessTriggerVo;
import io.bitnews.news_bg.job.GuessCloseJob;
import io.bitnews.news_bg.job.GuessSettlementJob;
import io.bitnews.news_bg.job.SchedulerManager;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ywd on 2019/9/26.
 */
@Service
public class JobService {

    private static final String GROUP_CLOSE = "GUESS_CLOSE";
    private static final String GROUP_SETTLEMENT = "GUESS_SETTLEMENT";
    private static final String JOB_CLOSE = "JOB_CLOSE_";
    private static final String JOB_SETTLEMENT = "JOB_SETTLEMENT_";

    @Autowired
    SchedulerManager schedulerManager;

    public void triggerCloseAndSettlementTask(GuessTriggerVo guessTriggerVo) throws SchedulerException {
        String jobId = guessTriggerVo.getJobId();
        //设置封盘job
        schedulerManager.startJobWithDate(guessTriggerVo.getCloseTriggerTime(), JOB_CLOSE+jobId, GROUP_CLOSE+jobId, GuessCloseJob.class);
        //设置结算job
        schedulerManager.startJobWithDate(guessTriggerVo.getSettlementTriggerTime(), JOB_SETTLEMENT+jobId, GROUP_SETTLEMENT+jobId, GuessSettlementJob.class);
    }
}
