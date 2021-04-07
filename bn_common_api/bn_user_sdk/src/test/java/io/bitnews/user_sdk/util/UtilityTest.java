package io.bitnews.user_sdk.util;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

public class UtilityTest {
	
	@Test
	public void testFrontendPassword(){
		//sha256(base64(password)+"hash")
		System.out.println(DigestUtils.sha256Hex(Utility.encodeBase64("1234qwer.")+"hash"));
	}

	@Test
	public void testID() {
		System.out.println(Utility.checkID18("142302197001011011"));
		System.out.println(Utility.checkID18("432522199211247430"));
	}

	@Test
	public void testTimeCost() {
		for (int i = 0; i < 100; i++) {
			System.out.println("=========" + i + "==========");
			long s = System.currentTimeMillis();
			System.out.println(DigestUtils.md5Hex("1111abcefe222233333339999999933eee"));
			System.out.println(System.currentTimeMillis() - s);
			String str = UUID.randomUUID().toString();
			long s2 = System.currentTimeMillis();
			System.out.println(str.hashCode());
			System.out.println(System.currentTimeMillis() - s2);
			long s3 = System.currentTimeMillis();
			System.out.println(Utility.randomPassword());
			System.out.println(System.currentTimeMillis() - s3);
			long s4 = System.currentTimeMillis();
			System.out.println(Utility.encodeBase64("dwdfdfdfd"));
			System.out.println(System.currentTimeMillis() - s4);
			long s5 = System.currentTimeMillis();
			System.out.println(Utility.containsAlphaNumberSpecialChar("dwdfa5.33dfdfd"));
			System.out.println(System.currentTimeMillis() - s5);
			long s6 = System.currentTimeMillis();
			System.out.println(Utility.randomUniqueName());
			System.out.println(System.currentTimeMillis() - s6);
		}
	}

	@Test
	public void testPasswordRule() {
		Assert.assertTrue(Utility.containsAlphaNumberSpecialChar("a1#"));
		Assert.assertTrue(Utility.containsAlphaNumberSpecialChar("a1."));
		Assert.assertFalse(Utility.containsAlphaNumberSpecialChar("a1b"));
		Assert.assertFalse(Utility.containsAlphaNumberSpecialChar("233"));
		Assert.assertFalse(Utility.containsAlphaNumberSpecialChar("233."));
		Assert.assertFalse(Utility.containsAlphaNumberSpecialChar("s."));
		Assert.assertFalse(Utility.containsAlphaNumberSpecialChar("<3."));
		Assert.assertTrue(Utility.containsAlphaNumberSpecialChar("a<3."));
		Assert.assertFalse(Utility.containsAlphaNumberSpecialChar("我<3."));
		Assert.assertTrue("abb@dd".contains("@"));
	}

	@Test
	public void testUUID() {
		System.out.println(UUID.randomUUID().toString());
	}

	@Test
	public void testIUUID() {
		System.out.println(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
	}

}
