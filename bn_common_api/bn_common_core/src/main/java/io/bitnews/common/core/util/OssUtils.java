package io.bitnews.common.core.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;

import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;

public class OssUtils {

	public static String uploadPublicFile(String endpoint, String accessKey, String accessKeySecret, String bucketName,
			String key, InputStream file) {
		OSS ossClient = null;
		try {
			ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessKeySecret);
			ossClient.putObject(bucketName, key, file);
			return generatePublicUri(key);
		} finally {
			if (ossClient != null) {
				ossClient.shutdown();
			}
		}
	}
	
	public static String uploadPrivateFile(String endpoint, String accessKey, String accessKeySecret, String bucketName,
			String key, InputStream file) {
		OSS ossClient = null;
		try {
			ossClient = new OSSClientBuilder().build(endpoint, accessKey, accessKeySecret);
			ossClient.putObject(bucketName, key, file);
			return generatePrivateUri(ossClient, bucketName, key);
		} finally {
			if (ossClient != null) {
				ossClient.shutdown();
			}
		}
	}
	
	public static String generatePublicUri( String key) {
		return "/"+ key;
	}

	public static String generatePrivateUri(OSS ossClient, String bucketName, String key) {
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key);
		Calendar calandar = Calendar.getInstance();
		calandar.add(Calendar.DAY_OF_MONTH, 30);
		request.setExpiration(calandar.getTime());
		URL url = ossClient.generatePresignedUrl(request);
		String signedUri = url.getPath() + "?" + url.getQuery();
		return signedUri;
	}
}
