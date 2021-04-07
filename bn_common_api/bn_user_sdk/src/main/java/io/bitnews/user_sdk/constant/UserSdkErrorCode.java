package io.bitnews.user_sdk.constant;

/**
 * 错误码基类
 * 
 * @author apple
 *
 */
public enum UserSdkErrorCode implements BNErrorCode {

	SUCCESS("000000", "SUCCESS", "返回成功"),

	// System error
	INTERNAL_SERVER_ERROR("001001", "The service is temporarily unavailable", "服务暂时不可用"), //
	NOT_FOUND("001002", "Page not found", "找不到网页"), //
	UNAUTHORIZED("001003", "Unauthorized", "登录超时"),
	DB_SQL_FAILURE("001004", "Fail to execute SQL", "数据库操作失败"), //
	FORBIDDEN("001005", "Forbidden", "限制访问"),
	
	KYC_REQUIRED("001006", "It requires real name authentication", "需要实名认证"), //
	REDIS_CONNECTION_FAILURE("001007", "Fail to connect token server", "无法连接令牌服务器"), //

	USER_SERVER_LOGIN_TOKEN_EMPTY("001008", "Login token is required", "登录令牌为空"), //
	USER_SERVER_LOGIN_TOKEN_INVALID("001009", "Login token is invalid", "登录令牌无效"), //
	USER_SERVER_CONNECTION_FAILURE("001010", "Fail to connect user server", "无法连接用户服务器"), //
	USER_SERVER_CALL_FAILURE("001011", "Fail to call user server", "无法呼叫用户服务器"),

	// SMS
	SMS_CONNECTION_FAILURE("001012", "Fail to receive phone verification code", "获取短信验证码失败"), //
	USER_SMS_CHECK_RESULT_FAILURE("001013", "Fail to send phone verification code", "发送短信验证码失败"), //
	USER_SMS_CHECK_CHECK_FAILURE("001014", "Incorrect phone verification code", "短信验证码输入有误"), //
	USER_SMS_CHECK_EMPTY("001015", "Incorrect phone verification code", "短信验证码错误"), //
	USER_SMS_PATH_NULL("001016", "Phone verification code is required", "请先获取短信验证码"), //
	USER_SMS_CODE_NULL("001017", "Incorrect phone verification code", "短信验证码错误"),
	USER_SMS_CHECK_RESULT_AGAIN("001020", "Incorrect phone verification code", "短信验证码错误，请重新输入"), //

	// Mail
	MAIL_CONNECTION_FAILURE("001025", "Fail to receive email verification code", "获取邮箱验证码失败"), //
	USER_MAIL_NULL("001028", "Email address is required", "邮箱地址为空"), //
	USER_MAIL_PATH_NULL("001030", "Email verification code is required", "请先获取邮箱验证码"), //
	USER_MAIL_CODE_NULL("001031", "Email verification code is required", "邮箱验证码为空"), //
	USER_MAIL_SEND_RESULT_FALSE("001032", "Incorrect email verification code", "邮箱验证码错误"), //
	USER_MAIL_SEND_EMPTY("001033", "Email verification code expired", "邮箱验证码已失效，请重新发送"), //
	USER_MAIL_CHECK_RESULT_FAILURE("001044", "Fail to send email verification code", "发送邮箱验证码失败"), //
	USER_MAIL_CHECK_CHECK_FAILURE("001045", "Incorrect email verification code", "邮箱验证码输入有误"), //
	USER_MAIL_CHECK_RESULT_AGAIN("001024", "Incorrect email verification code", "邮箱验证码错误，请重新输入"), //

	// CAPTCHA
	CAPTCHA_CONNECTION_FAILURE("001018", "Fail to connect CAPTCHA server", "获取图形验证码失败"), //
	USER_CAPTCHA_CHECK_RESULT_FAILURE("001019", "CAPTCHA code is invalid", "图形验证码校验失败"), //
	USER_CAPTCHA_CHECK_EMPTY("001021", "Incorrect CAPTCHA code, please try again", "图形验证码错误，请重新输入"), //
	USER_CAPTCHA_PATH_NULL("001022", "Fail to send CAPTCHA code", "请求图片验证码失败"), //
	USER_CAPTCHA_CODE_NULL("001023", "CAPTCHA code is required", "图形验证码不能为空"),
	
	USER_MOBILE_NULL("001046", "User mobile is required", "手机号码为空"), //
	USER_AREACODE_NULL("001047", "User areacode is required", "用户区号为空"),
	// settings
	SYSTEM_SETTING_KEY_NOT_FOUND("001039", "System setting key is not found", "找不到系统配置项"), //
	APP_ACCESS_SIGN_CHECK("001040", "App sign check error", "app签名检查错误"), //

	// notify
	NOTIFY_PARAMS_EMPTY("001048", "Invalid notification parameters", "非法参数"), //
	SMS_NOTIFY_FAIL("001049", "Sms notification failed", "短信通知失败"), //
	MAIL_NOTIFY_FAIL("001050", "Mail notification failed", "邮件通知失败"), //
	USER_IS_DELETED("010051", "User is not available", "账号不可用，如有疑问请联系客服"),

	USERID_NULL("010053", "User id is null", "账号不可用，如有疑问请联系客服"), //
	
	POSTER_GET_ERROR("010058", "No poster", "未生成海报"),
	TO_FASTER("010059", "The operation is too fast.", "操作太快了"),

	BAD_REQUEST("010101", "Invalid parameter", "非法参数"), //
	PARAMETER_ERROR("010102", "Invalid parameter", "非法参数"), //
	API_INVOKE_ERROR("010102", "api invoke error", "api调用错误"), //
	API_UNAUTHORIZED("010103", "Unauthorized", "未授权调用"), //
	OPERATION_TIMEOUT("010104", "Operation timeout", "操作超时"), //

	VALIDATOR_ERROR("001005", "Validator failed", "校验失败"), //

	JSON_PARSE_ERROR("001006", "JSON parse failed", "JSON解析失败"), //

	REQUEST_METHOD_NOT_SUPPORTED("001007", "Request method not supported", "不支持的请求方法"), //

	;
	private String errorCode;
	private String message;
	private String messageZh;

	UserSdkErrorCode(String errorCode, String message, String messageZh) {
		this.errorCode = errorCode;
		this.message = message;
		this.messageZh = messageZh;
	}

	@Override
	public String getMessageZh() {
		return messageZh;
	}
	
	@Override
	public String getErrorCode() {
		return this.errorCode;
	}
	
	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public String toString() {
		return this.errorCode;
	}

}
