package io.bitnews.user_sdk.util;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import io.bitnews.user_sdk.constant.UserSdkErrorCode;
import io.bitnews.user_sdk.model.BNResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BNResponseUtil {
	public static String convertValidateErrorsToJson(List<FieldError> validateErrors) {
		if (validateErrors == null || validateErrors.isEmpty()) {
			return "";
		}
		StringBuilder sbd = new StringBuilder();
		sbd.append("{");
		for (FieldError error : validateErrors) {
			if (error == null) {
				continue;
			}
			sbd.append("\"").append(error.getField()).append("\":").append("\"").append(error.getDefaultMessage())
					.append("\",");
		}
		return sbd.substring(0, sbd.length() - 1) + "}";
	}

	public static BNResponse<?> validateParam(BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			if (log.isInfoEnabled()) {
				bindingResult.getAllErrors().forEach(error -> {
					log.info("validate error: {}", error);
				});
			}
			String message = convertValidateErrorsToJson(bindingResult.getFieldErrors());
			return BNResponse.error(UserSdkErrorCode.BAD_REQUEST, message);
		}
		return null;
	}
}
