package io.bitnews.user_sdk.exception;

import io.bitnews.user_sdk.constant.BNErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BNException extends RuntimeException {

	private static final long serialVersionUID = -743309525085095430L;
	protected BNErrorCode errorCode;
	protected String code;
	protected String message;

	public BNException(BNErrorCode errorCode) {
		this.code = errorCode.getErrorCode();
		this.message = errorCode.getMessage();
		this.errorCode = errorCode;
	}

}
