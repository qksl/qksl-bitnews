package io.bitnews.user_sdk.util;

import org.junit.Assert;
import org.junit.Test;

public class MaskUtilTest {

	@Test
	public void testMobile(){

		Assert.assertTrue("12****456".equals(MaskUtil.maskMobile("123456")));

		Assert.assertTrue("12****567".equals(MaskUtil.maskMobile("1234567")));

		Assert.assertTrue("12****678".equals(MaskUtil.maskMobile("12345678")));

		Assert.assertTrue("12****789".equals(MaskUtil.maskMobile("123456789")));
	}
	
	@Test
	public void testMail(){
		Assert.assertTrue("1bc****@89.cm".equals(MaskUtil.maskMail("1bcd7832@89.cm")));
	}
	
	@Test
	public void testIdNo(){
		Assert.assertTrue("12****89".equals(MaskUtil.maskIdNo("123456789")));
	}
}
