package io.bitnews.user_sdk.constant;

import java.math.RoundingMode;

public abstract class UserSdkConstant {
	/**
	 *  http request header:
	 */
	public static final String HEADER_USER_AGENT = "user-agent";
	public static final String HEADER_REQUEST_FROM = "request-from";
	public static final String HEADER_TOKEN = "token";
	public static final String HEADER_PLATFORM_ID = "platFormId";
	public static final String HEADER_DEVICE_ID = "deviceId";
	public static final String HEADER_SIGN = "sign";
	public static final String HEADER_AUTHORIZATION = "authorization";

	public static final String HEADER_USER_AGENT_bitnews = "bitnews";

	public static final String HEADER_USER_AGENT_HASHKEYWALLET = "HashKeyWallet";

	public static final String HEADER_USER_AGENT_OKHTTP = "okhttp";
	
	public static final String HEADER_REQUEST_FROM_HASHKEY_WALLET = "HashKeyWallet";

	/**
	 *  http request attribute:
	 */
	public static final String REQ_START_TIME = "REQ_START_TIME";
	public static final String REQ_NORMAL_EXIT = "REQ_RUN_CONTROLLER";	
	public static final String REQ_AUTH_TOKEN = "REQ_AUTH_TOKEN";
	public static final String REQ_AUTH_ID = "REQ_AUTH_ID";
	public static final String REQ_AUTH_USERNAME = "REQ_AUTH_USERNAME";
	public static final String REQ_USER_TYPE = "REQ_USER_TYPE";
	public static final String REQ_USER_STATUS = "REQ_USER_STATUS";
	public static final String REQ_PLATFORM_ID = "REQ_PLATFORM_ID";
	public static final String REQ_DEVICE_ID = "REQ_DEVICE_ID";

	public static final String SESSION_STATUS = "sessionstatus";
	public static final String TIMEOUT = "timeout";

	public static final String LOGIN_TOKEN = "login_token";
	public static final String RETURN_URI = "return_uri";
	public static final String QUERY_PARAM_ASSIGN_SIGN = "=";
	public static final String QUERY_PARAM_CONCAT = "&";
	public static final String URI_ANCHOR = "#";
	public static final String URI_TOKEN = ":";
	public static final String USER_MESSAGE = "user_message";
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String DATE_TIMEZONE = "GMT+8";
	/**
	 *  User Length validation
	 */
	public static final int MIN_LENGTH_SIGNATURE_NAME = 1;
	public static final int MAX_LENGTH_SIGNATURE_NAME = 50;
	public static final int MIN_LENGTH_SATR_USERNAME = 1;
	public static final int MIN_LENGTH_USERNAME = 4;
	public static final int MAX_LENGTH_USERNAME = 20;
	public static final int MIN_LENGTH_PASSWORD = 8;
	public static final int MAX_LENGTH_PASSWORD = 20;
	public static final int LENGTH_MOBILE = 11;

	/**
	 *  Send activation request mail
	 */
	public static final String ACTIVATE_ACCOUNT = "activate";
	public static final String RESET_PASSWORD = "resetpwd";
	public static final String BIND_MAIL = "bind";

	public static final String SELECT_LAST_INSERT_ID = "select last_insert_id()";

	public static final String LANG_ZH = "zh";
	public static final String LANG_EN = "en";
	public static final String LANG_TW = "tw";
	/**
	 *  user coin
	 */
	public static final int REWARD_SCALE = 8;

	public static final RoundingMode ROUNDING_MODE = RoundingMode.FLOOR;

	/**
	 *  weixin
	 */
	public static final String WIN_XIN_QR_PATH1 = "WIN_XIN_QR_PATH1";

	public static final String WIN_XIN_QR_PATH2 = "WIN_XIN_QR_PATH2";

	public static final String WHITE_PAPER_PATH = "WHITE_PAPER_PATH";

	public static final int MAX_TRIES = 3;
	public static final int MAX_TRIES_LOCK = 10;

	/**
	 *  App token
	 */
	public static final String APP_TOKEN_USER_ID = "userId";
	public static final String APP_TOKEN_DEVICE_ID = "deviceId";
	public static final String APP_TOKEN_USER_TYPE = "userType";
	public static final String APP_TOKEN_STATUS = "status";
	public static final String APP_TOKEN_USER_NAME = "userName";
	// source constant
	public static final int SOURCE_BN = 0;
	public static final int SOURCE_HKW = 1;
	
	/**
	 * security summary
	 */
	public static final String MAIL_BOUND = "mailBound";
	public static final String MOBILE_BOUND = "mobileBound";
	public static final String GA_BOUND = "GABound";
	
	public static final String TRANSFER_PASSWORD = "transferPassword";

	
	public static final String SECURITY_KEY_PREFIX = "security:ops:%s:%s";
	public static final String FORGET_TRANSFER_PASSOWRD_OPS = "ops-forget-transfer-passowrd";
	
	public static final String OFFLINE_WITHDRAW_OPS = "ops-withdraw";
	
}
