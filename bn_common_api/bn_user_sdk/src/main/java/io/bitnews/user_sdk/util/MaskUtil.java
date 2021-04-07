package io.bitnews.user_sdk.util;

import org.apache.commons.lang3.StringUtils;

import io.bitnews.user_sdk.constant.UserSdkConstant;

public class MaskUtil {

	private static final int MOBILE_MIN_LENGTH = 6;

	private static final int MASK_START = 2;

	private static final int MASK_END = 3;

	private static final String MASK = "%s****%s";

	private static final int ID_FACTORS = 31; 

	private static final int INVITATION_CODE_FACTORS = 67;

	private static final String USERNAME_PREFIX = "BN_";

	private static final String USERNAME_PREFIX_HW = "HW_";

	//TODO: need change by UIS
	public static String username(Integer id, Integer source) {
		return getPrefix(source) + Long.toHexString(ID_FACTORS * id);
	}

	private static String getPrefix(Integer source) {
		return (source == null || source == UserSdkConstant.SOURCE_BN) ? USERNAME_PREFIX : USERNAME_PREFIX_HW;
	}

	public static String invitationCode(Integer id) {
		return Long.toHexString(INVITATION_CODE_FACTORS * id).toUpperCase();
	}

	/**
	 * 不论几位,前2后3中4,如123456789 -> 12****789,123456 -> 12****456
	 * 
	 * @param mobile
	 * @return
	 */
	public static String maskMobile(String mobile) {
		if (StringUtils.isEmpty(mobile) || mobile.length() < MOBILE_MIN_LENGTH) {
			return mobile;
		}
		int length = mobile.length();
		String start = mobile.substring(0, MASK_START);
		String end = mobile.substring(length - MASK_END, length);
		return String.format(MASK, start, end);
	}

	/**
	 * 前3后域名,如abc123@456.io -> abc****@456.io
	 * 
	 * @param mail
	 * @return
	 */
	public static String maskMail(String mail) {
		String result = "";
		if (!StringUtils.isEmpty(mail)) {
			if (mail.lastIndexOf("@") != -1) {
				String pre, sufix = "";
				sufix = mail.substring(mail.lastIndexOf("@"), mail.length());
				if (mail.substring(0, mail.indexOf("@")).length() > 3) {
					pre = mail.substring(0, 3);
				} else {
					pre = mail.substring(0, mail.indexOf("@"));
				}
				result = String.format("%s****%s", pre, sufix);
			} else {
				result = mail;
			}
		}
		return result;
	}

	/**
	 * 
	 * @param idNo
	 * @return
	 */
	public static String maskIdNo(String idNo) {
		String result = "";
		if (!StringUtils.isEmpty(idNo)) {
			if (idNo.trim().length() == 18) {
				result = idNo.replaceAll("(\\d{3})\\d{12}(\\d{3})", "$1****$2");
			} else if (idNo.trim().length() > 4) {
				result = String.format("%s****%s", idNo.substring(0, 2),
						idNo.substring(idNo.length() - 2, idNo.length()));
			} else {
				result = idNo;
			}
		}
		return result;
	}

}
