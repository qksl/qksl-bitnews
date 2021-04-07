package io.bitnews.framework.sms;

import lombok.Data;

/**
 * 短信
 */
@Data
public class SmsParameter {


	private String phoneNumber;
	private String params;

}
