package io.bitnews.user_sdk.constant;

abstract public class ErrorCodeBuilder {

	private static final String DEFAULT_CODE = "999999";

	public static ErrorCode build(String errorCode, String message, String messageZh) {
		return new ErrorCode(errorCode, message, messageZh);
	}

	public static ErrorCode build(String message, String messageZh) {
		return build(DEFAULT_CODE, message, messageZh);
	}

	static class ErrorCode implements BNErrorCode {
		private String errorCode;
		private String message;
		private String messageZh;

		public ErrorCode(String errorCode, String message, String messageZh) {
			this.errorCode = (errorCode != null ? errorCode : DEFAULT_CODE);
			this.message = message;
			this.messageZh = messageZh;
		}

		@Override
		public String getErrorCode() {
			return errorCode;
		}

		@Override
		public String getMessage() {
			return message;
		}

		@Override
		public String getMessageZh() {
			return messageZh;
		}
	}
}
