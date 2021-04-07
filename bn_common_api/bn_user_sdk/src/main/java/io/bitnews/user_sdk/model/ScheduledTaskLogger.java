package io.bitnews.user_sdk.model;

import java.util.Date;

import org.beetl.sql.core.annotatoin.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.bitnews.user_sdk.constant.UserSdkConstant;
import lombok.Data;

@Data
@Table(name = "t_scheduled_task_logger")
public class ScheduledTaskLogger {

	/**
	 *  自增主键
	 */
	private Integer id;
	/**
	 *  响应时间
	 */
	private Long duration = 0L;
	private String logKey;
	private String ClassName;
	private String method;
	
	private String exception="";
	private String cron;
	
	private String desc;
	/**
	 *  创建时间
	 */
	@JsonFormat(pattern = UserSdkConstant.DATE_TIME_PATTERN, timezone = UserSdkConstant.DATE_TIMEZONE)
	private Date createTime;
	
	@JsonFormat(pattern = UserSdkConstant.DATE_TIME_PATTERN, timezone = UserSdkConstant.DATE_TIMEZONE)
	private Date startTime;
	@JsonFormat(pattern = UserSdkConstant.DATE_TIME_PATTERN, timezone = UserSdkConstant.DATE_TIMEZONE)
	private Date endTime;
	

}
