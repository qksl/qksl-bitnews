package io.bitnews.user_sdk.constant;

/**
 * 用户状态
 * 
 * @author apple
 *
 */
public enum UserStatus {
	/**
	 * 已删除
	 */
	DELETE(0), 
	/**
	 * 未激活
	 */
	INACTIVE(1), 
	/**
	 * 正常
	 */
	ACTIVE(2), 
	/**
	 * KYC已提交
	 */
	KYC_SUBMIT(3), 
	/**
	 * KYC审核通过
	 */
	KYC_PASS(4);
	
	private int code;

	UserStatus(int code) {
		this.code = code;
	}

	public int getValue() {
		return this.code;
	}

	public int getCode() {
		return this.code;
	}

	@Override
	public String toString() {
		return String.valueOf(this.code);
	}

	public static UserStatus getStatus(int code) {
		UserStatus[] allStatus = UserStatus.values();
		for (UserStatus status : allStatus) {
			if (status.getValue() == code) {
				return status;
			}
		}
		return null;
	}

}
