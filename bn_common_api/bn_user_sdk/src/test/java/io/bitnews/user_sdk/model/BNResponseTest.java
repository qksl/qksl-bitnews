package io.bitnews.user_sdk.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.validation.ObjectError;

import io.bitnews.user_sdk.constant.UserSdkErrorCode;

public class BNResponseTest {

	@Test
	public void testError() {
		List<ObjectError> errors = new ArrayList<ObjectError>();
		errors.add(new ObjectError("O1", "Field1 value is required"));
		errors.add(
				new ObjectError("O2", new String[] { "Field2" }, new String[] { "a" }, "Field2 value requires number"));
		System.out.println(BNResponse.error(UserSdkErrorCode.BAD_REQUEST, errors));
	}

	@Test
	public void testResult() {
		System.out.println(new BNResponse<>("ok"));
		System.out.println(new BNResponse<>(UserSdkErrorCode.BAD_REQUEST));
		System.out.println(BNResponse.ok(UserSdkErrorCode.BAD_REQUEST));
		System.out.println(BNResponse.error(UserSdkErrorCode.BAD_REQUEST));
	}
}
