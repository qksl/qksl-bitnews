package io.bitnews.user_sdk.exception;

public class BNWarningException extends RuntimeException {
	private static final long serialVersionUID = 3888014581487162338L;

	public BNWarningException(String message) {
		super(message);
	}
	
	public BNWarningException(Throwable cause) {
		super(cause);
	}

	public BNWarningException(String message, Throwable cause) {
		super(message, cause);
	}
}