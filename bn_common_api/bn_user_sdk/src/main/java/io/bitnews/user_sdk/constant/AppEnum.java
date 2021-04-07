package io.bitnews.user_sdk.constant;

/**
 * 操作类型
 * 
 * @author apple
 *
 */
public enum AppEnum implements BaseStrEnum {
	/**
	 * 用户操作
	 */
	USER_PASSPORT("USER_PASSPORT", "用户操作"), 
	/**
	 * 后台管理
	 */
	ADMIN("ADMIN", "后台管理"), 
	/**
	 * 公用模块
	 */
	COMMON("COMMON", "公用模块"),
	/**
	 * 用户coin
	 */
	USER_COIN("USER_COIN", "用户coin"), 
	/**
	 * 充值返币活动
	 */
	RECHARGE_ACTIVITY("RECHARGE_ACTIVITY", "充值返币活动"), 
	/**
	 * 体验金活动
	 */
	CAPITAL_ACTIVITY("CAPITAL_ACTIVITY", "体验金活动"), 
	/**
	 * 扫码登录
	 */
	SCAN_LOGIN("SCAN_LOGIN", "扫码登录"),
	/**
	 * 提币
	 */
	WITHDRAW("WITHDRAW", "提币");

	private String code;
	private String message;

	private AppEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
