package io.bitnews.framework.sms;

/**
 * 短信发送器
 */
public interface SmsSender {

	/**
	 * 发送短信
	 * @param parameter
	 * @return
	 */
	SmsSendResult send(SmsParameter parameter);
}