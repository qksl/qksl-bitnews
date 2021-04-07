package io.bitnews.user_sdk.constant;

/**
 * 平台代码
 * 
 * @author humorpie
 *
 */
public enum Platform {
	/**
	 * 网页
	 */
	WEB(0, "web"), 
	/**
	 * iOS
	 */
	IOS(1, "ios"), 
	/**
	 * Android
	 */
	ANDROID(2, "android"),;
	private int code;

	private String value;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private Platform(int code, String value) {
		this.code = code;
		this.value = value;
	}

	public static final boolean contains(String value) {
		for (Platform platform : Platform.values()) {
			if (platform.getValue().equals(value)) {
				return true;
			}
		}
		return false;
	}

	public static Integer parse(String code) {
		Platform[] values = Platform.values();
		for (Platform value : values) {
			if (value.getCode() == Integer.parseInt(code)) {
				return value.getCode();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return value;
	}
}
