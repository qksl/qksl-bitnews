package io.bitnews.framework.sms;

import lombok.Data;

@Data
public class SmsSendResult {

	private boolean success;
	
	private String code;
	
}
