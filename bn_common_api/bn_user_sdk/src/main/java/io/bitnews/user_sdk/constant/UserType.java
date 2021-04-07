package io.bitnews.user_sdk.constant;

/**
 * 用户类型 0-普通用户
 * 
 * @author apple
 *
 */
public enum UserType {
	/**
	 * 普通用户
	 */
	USER(0, "USER"), 
	/**
	 * 未定义
	 */
	NONE(3, "NONE"),;

	private int code;
	private String meaning;

	UserType(int code, String meaning) {
		this.code = code;
		this.meaning = meaning;
	}

	public int getCode() {
		return this.code;
	}

	public String getMeaning() {
		return this.meaning;
	}

	public Integer getValue() {
		return this.code;
	}

	public static UserType[] getAllUserTypes() {
		return new UserType[] { USER };
	}

	@Override
	public String toString() {
		return String.valueOf(this.meaning);
	}

	public static UserType getType(Integer code) {
		if (code == null) {
			return NONE;
		}
		UserType[] allTypes = getAllUserTypes();
		for (UserType userType : allTypes) {
			if (userType.getValue() == code.intValue()) {
				return userType;
			}
		}
		return NONE;
	}

}
