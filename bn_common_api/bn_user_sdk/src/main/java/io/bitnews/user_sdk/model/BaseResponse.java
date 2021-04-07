package io.bitnews.user_sdk.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.bitnews.user_sdk.constant.BNErrorCode;
import io.bitnews.user_sdk.constant.UserSdkErrorCode;
import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class BaseResponse implements Serializable {

	private static final long serialVersionUID = -1577558359423783595L;
	protected boolean result = true;
	protected String code;
	protected String message;
	protected String messageZh;
	//true: app提示框(成功、网络无法连接等), false: input提示框
	protected boolean toast = true;

	//成功操作
	public BaseResponse() {
		this(true, UserSdkErrorCode.SUCCESS, true);
	}

	//业务逻辑验证异常
	public BaseResponse(BNErrorCode errorCode) {
		this(false, errorCode, false);
	}

	//业务逻辑验证异常
	public BaseResponse(boolean result, BNErrorCode errorCode) {
		this(result, errorCode, false);
	}
	
	public BaseResponse(boolean result, BNErrorCode errorCode, boolean toast) {
		this.result = result;
		this.code = errorCode.getErrorCode();
		this.message = errorCode.getMessage();
		this.messageZh = errorCode.getMessageZh();
		this.toast = toast;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
