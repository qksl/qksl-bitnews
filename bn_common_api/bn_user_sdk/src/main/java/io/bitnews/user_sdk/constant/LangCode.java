package io.bitnews.user_sdk.constant;

/**
 * 用户语言
 * 
 * @author apple
 *
 */
public enum LangCode {
	/**
	 * 中文
	 */
	ZH(0, UserSdkConstant.LANG_ZH), 
	/**
	 * 英文
	 */
	EN(1, UserSdkConstant.LANG_EN),
	/**
	 *繁体中文
	 */
	TW(2, UserSdkConstant.LANG_TW);
	
	private int code;
	private String meaning;

	LangCode(int code, String meaning) {
		this.code = code;
		this.meaning = meaning;
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

	public static LangCode getLang(String meaning) {
		switch (meaning) {
		case UserSdkConstant.LANG_ZH:
			return ZH;
		case UserSdkConstant.LANG_EN:
			return EN;
		case UserSdkConstant.LANG_TW:
			return TW;
		default:
			return null;
		}
	}

	public int getValue() {
		return this.code;
	}

	public static LangCode getLang(int code) {
		LangCode[] allLang = LangCode.values();
		for (LangCode lang : allLang) {
			if (lang.getValue() == code) {
				return lang;
			}
		}
		return null;
	}

}
