package io.bitnews.user_sdk.util;

import java.util.Locale;

/**
 * @Author: wangyufei
 * @Date: 2019/2/14 10:55
 */
public class LangUtil {

	private static final String ZH = "zh";
	private static final String EN = "en";
	private static final String CN = "cn";

	public static Integer local2lang(Locale locale) {
		if (locale == null) {
			return 0;
		}
		if (ZH.equalsIgnoreCase(locale.getLanguage())) {
			if (CN.equalsIgnoreCase(locale.getCountry())) {
				return 0;
			} else {
				return 2;
			}
		} else if (EN.equalsIgnoreCase(locale.getLanguage())) {
			return 1;
		}
		return 0;
	}

}
