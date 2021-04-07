package io.bitnews.news_bg.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

@Slf4j
public class SchedulerListener implements JobListener {
    public static final String LISTENER_NAME = "QuartSchedulerListener";

    @Override
    public String getName() {
        return LISTENER_NAME; //must return a name
    }

    //任务被调度前
    @Override
    public void jobToBeExecuted(JobExecutionContext context) {

        String jobName = context.getJobDetail().getKey().toString();
        log.info("jobToBeExecuted");
        log.info("Job : " + jobName + " is going to start...");

    }

    //任务调度被拒了
    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.info("jobExecutionVetoed");
        //可以做一些日志记录原因

    }

    //任务被调度后
    @Override
    public void jobWasExecuted(JobExecutionContext context,
                               JobExecutionException jobException) {
        log.info("jobWasExecuted");

        String jobName = context.getJobDetail().getKey().toString();
        log.info("Job : " + jobName + " is finished...");

        if (jobException!=null&&!jobException.getMessage().equals("")) {
            log.info("Exception thrown by: " + jobName
                    + " Exception: " + jobException.getMessage());
        }

    }
}
