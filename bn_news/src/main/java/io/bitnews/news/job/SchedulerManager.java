package io.bitnews.news.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Date;

/*
 * 此处可以注入数据库操作，查询出所有的任务配置
 */
@Component
@Slf4j
public class SchedulerManager {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    private JobListener scheduleListener;

    /**
     * 开始定时任务
     * @param jobName
     * @param jobGroup
     */
    public void startJobWithCron(String cron,String jobName,String jobGroup,Class<? extends Job> jobClass) throws SchedulerException
    {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if(scheduleListener==null){
            scheduleListener=new SchedulerListener();
            scheduler.getListenerManager().addJobListener(scheduleListener);
        }
        JobKey jobKey=new JobKey(jobName,jobGroup);
        if(!scheduler.checkExists(jobKey))
        {
            scheduleJobWithCron(cron,scheduler,jobName,jobGroup,jobClass);
        }
    }

    /**
     * 开始定时任务
     * @param jobName
     * @param jobGroup
     */
    public void startJobWithDate(Date schedulerTime,String jobName,String jobGroup,Class<? extends Job> jobClass) throws SchedulerException
    {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        if(scheduleListener==null){
            scheduleListener=new SchedulerListener();
            scheduler.getListenerManager().addJobListener(scheduleListener);
        }
        JobKey jobKey=new JobKey(jobName,jobGroup);
        if(!scheduler.checkExists(jobKey))
        {
            scheduleJobWithDate(schedulerTime,scheduler,jobName,jobGroup,jobClass);
        }
        log.info("jobName:{},jobGroup:{},time:{}", jobName, jobGroup, schedulerTime.getTime());
    }

    /**
     * 移除定时任务
     * @param jobName
     * @param jobGroup
     */
    public void deleteJob(String jobName,String jobGroup) throws SchedulerException
    {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey=new JobKey(jobName,jobGroup);
        scheduler.deleteJob(jobKey);
    }

    /**
     * 暂停定时任务
     * @param jobName
     * @param jobGroup
     */
    public void pauseJob(String jobName,String jobGroup) throws SchedulerException
    {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey=new JobKey(jobName,jobGroup);
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复定时任务
     * @param jobName
     * @param jobGroup
     * @throws SchedulerException
     */
    public void resumeJob(String jobName,String jobGroup) throws SchedulerException
    {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey triggerKey=new JobKey(jobName,jobGroup);
        scheduler.resumeJob(triggerKey);
    }
    /**
     * 清空所有当前scheduler对象下的定时任务【目前只有全局一个scheduler对象】
     * @throws SchedulerException
     */
    public void clearAll() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.clear();
    }

    /**
     * 动态创建Job
     * 此处的任务可以配置可以放到properties或者是放到数据库中
     * Trigger:name和group 目前和job的name、group一致，之后可以扩展归类
     * @param scheduler
     * @throws SchedulerException
     */
    private void scheduleJobWithCron(String cron,Scheduler scheduler,String jobName,String jobGroup,Class<? extends Job> jobClass) throws SchedulerException{
        /*
         *  此处可以先通过任务名查询数据库，如果数据库中存在该任务，更新任务的配置以及触发器
         *  如果此时数据库中没有查询到该任务，则按照下面的步骤新建一个任务，并配置初始化的参数，并将配置存到数据库中
         */
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();
        // 每5s执行一次
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroup).withSchedule(scheduleBuilder).build();
        scheduler.scheduleJob(jobDetail,cronTrigger);
    }

    private void scheduleJobWithDate(Date schedulerTime, Scheduler scheduler, String jobName, String jobGroup, Class<? extends Job> jobClass) throws SchedulerException{
        /*
         *  此处可以先通过任务名查询数据库，如果数据库中存在该任务，更新任务的配置以及触发器
         *  如果此时数据库中没有查询到该任务，则按照下面的步骤新建一个任务，并配置初始化的参数，并将配置存到数据库中
         */
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroup).build();

        SimpleTrigger trigger = (SimpleTrigger)TriggerBuilder.newTrigger()
                .withIdentity(jobGroup)
                .startAt(schedulerTime)
                .forJob(jobName, jobGroup)
                .build();
        scheduler.scheduleJob(jobDetail,trigger);
    }

}
