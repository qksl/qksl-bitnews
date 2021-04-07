package io.bitnews.user_sdk.aop;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;

import io.bitnews.user_sdk.cache.CacheBase;
import io.bitnews.user_sdk.cache.keys.BNRedisKeys;
import io.bitnews.user_sdk.model.ScheduledTaskLogger;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Order(1)
@Component
public class ScheduleHandler implements EnvironmentAware {
	
	@Autowired
	private CacheBase cacheBase;
	
	private Environment env;

	private ThreadLocal<String> threadLogKey = new ThreadLocal<>();
	

	@Value("${schedule.log.key:"+BNRedisKeys.SCHEDULE_LOG_LIST+"}")
	private String scheduleLogKey;
	

	@Before("@annotation(org.springframework.scheduling.annotation.Scheduled)")
	public void before(JoinPoint joinPoint) {
		saveScheduleLog(joinPoint,Calendar.getInstance().getTimeInMillis());
	}

	@AfterReturning(value = "@annotation(org.springframework.scheduling.annotation.Scheduled)")
	public void afterReturning(JoinPoint joinPoint) {
		updateScheduleLog("");
	}

	private void saveScheduleLog(JoinPoint joinPoint,long startTime) {
		MethodSignature methodSig = (MethodSignature) joinPoint.getSignature();
		Scheduled scheduled = methodSig.getMethod().getAnnotation(Scheduled.class);
		
		ApiOperation apiOperation = methodSig.getMethod().getAnnotation(ApiOperation.class);
		ScheduledTaskLogger logger = new ScheduledTaskLogger();
		
		String cronKey = scheduled.cron().replace("${", "").replace("}", "").trim();
		if(cronKey.indexOf(":")!=-1) {
			cronKey = cronKey.substring(0, cronKey.indexOf(":"));
		}
		logger.setCron(env.getProperty(cronKey));
		Object target = joinPoint.getTarget();
	    logger.setClassName(target.getClass().getName());
	    logger.setCreateTime(new Date());
	    logger.setMethod(joinPoint.getSignature().getName());
	    logger.setStartTime(new Date(startTime));
	    if(apiOperation!=null) {
			logger.setDesc(apiOperation.value());
		}		
	    String logKeyStr = UUID.randomUUID().toString();
	    logger.setLogKey(logKeyStr);
	    cacheBase.hashPut(scheduleLogKey, logKeyStr, JSONObject.toJSONString(logger));
	    cacheBase.expire(scheduleLogKey, 2,TimeUnit.DAYS);
	    threadLogKey.set(logKeyStr);
	}
	
	private final int EXCEPTION_MAX_LENGTH=20000;
	private void updateScheduleLog(String ex) {
		String logKeyStr = threadLogKey.get();
		if(StringUtils.isEmpty(logKeyStr)) {
			log.info("###updateScheduleLog = logKeyStr={} , ex={}",logKeyStr,ex);
			return;
		}
		if(!StringUtils.isEmpty(ex)&&ex.length()>EXCEPTION_MAX_LENGTH) {
			ex = ex.substring(0, EXCEPTION_MAX_LENGTH);
		}
		threadLogKey.remove();
		ScheduledTaskLogger logger = JSONObject.parseObject(cacheBase.hashGet(scheduleLogKey, logKeyStr).toString(), ScheduledTaskLogger.class);
		Calendar endTime = Calendar.getInstance();
		long startTime = logger.getStartTime().getTime();
		long duration = endTime.getTimeInMillis() - startTime;
		logger.setEndTime(endTime.getTime());
		logger.setDuration(duration);
		logger.setException(ex);
		cacheBase.hashPut(scheduleLogKey, logKeyStr, JSONObject.toJSONString(logger));
		cacheBase.expire(scheduleLogKey, 2,TimeUnit.DAYS);
	}
	
	@AfterThrowing(pointcut="@annotation(org.springframework.scheduling.annotation.Scheduled)",throwing="ex")
    public void doException(JoinPoint joinPoint,Exception ex){
		log.error("###doException",ex);
		updateScheduleLog(ExceptionUtils.getFullStackTrace(ex));
    }
	
	@Override
	public void setEnvironment(Environment env) {
		this.env = env;
	}
}
