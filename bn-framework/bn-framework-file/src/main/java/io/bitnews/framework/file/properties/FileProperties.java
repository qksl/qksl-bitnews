package io.bitnews.framework.file.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by ywd on 2019/11/21.
 */
@Data
@ConfigurationProperties(prefix = "file.aliyun.oss")
public class FileProperties {
    String endpoint;
    String accessKeyId;
    String accessKeySecret;
    String bucketName;
    String getUrl;
}
