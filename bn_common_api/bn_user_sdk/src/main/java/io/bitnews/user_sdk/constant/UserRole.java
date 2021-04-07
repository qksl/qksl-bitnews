package io.bitnews.user_sdk.constant;

/**
 * 用户角色
 * 
 * @author apple
 *
 */
public enum UserRole {
	/**
	 * 普通用户
	 */
	USER(0, "USER"), 
	/**
	 * KOL
	 */
	STAR(1, "STAR");

	private int code;
	private String meaning;

	UserRole(int code, String meaning) {
		this.code = code;
		this.meaning = meaning;
	}

	public int getValue() {
		return this.code;
	}

	public int getCode() {
		return this.code;
	}

	public String getMeaning() {
		return this.meaning;
	}

	@Override
	public String toString() {
		return String.valueOf(this.code);
	}

	public static UserRole getRole(int code) {
		switch (code) {
		case 0:
			return USER;
		case 1:
			return STAR;
		default:
			return null;
		}
	}

}
