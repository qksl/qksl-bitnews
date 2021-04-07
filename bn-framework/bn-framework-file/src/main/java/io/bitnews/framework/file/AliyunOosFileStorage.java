package io.bitnews.framework.file;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import io.bitnews.framework.file.properties.FileProperties;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;

/**
 * Created by ywd on 2019/11/21.
 */
@Data
public class AliyunOosFileStorage implements FileStorage {

    @Autowired
    FileProperties fileProperties;

    @Override
    public void store(InputStream input, String key) {
        OSS ossClient = null;
        try {
            ossClient = new OSSClientBuilder().build(fileProperties.getEndpoint(), fileProperties.getAccessKeyId(), fileProperties.getAccessKeySecret());
            ossClient.putObject(fileProperties.getBucketName(), key, input);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
