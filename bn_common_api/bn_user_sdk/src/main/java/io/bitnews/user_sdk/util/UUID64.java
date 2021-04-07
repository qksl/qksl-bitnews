package io.bitnews.user_sdk.util;

import java.util.UUID;
import org.apache.commons.net.util.Base64;

/**
 * @Author: wangyufei
 * @Date: 2019/4/2 13:54
 */
public class UUID64 {

	public static String uuid64() {
		UUID uuid = UUID.randomUUID();
		return compressedUUID(uuid);
	}


	protected static String compressedUUID(UUID uuid) {
		byte[] byUuid = new byte[16];
		long least = uuid.getLeastSignificantBits();
		long most = uuid.getMostSignificantBits();
		long2bytes(most, byUuid, 0);
		long2bytes(least, byUuid, 8);
		String compressUUID = Base64.encodeBase64String(byUuid);
		return compressUUID;
	}

	protected static void long2bytes(long value, byte[] bytes, int offset) {
		for (int i = 7; i > -1; i--) {
			bytes[offset++] = (byte) ((value >> 8 * i) & 0xFF);
		}
	}


}
