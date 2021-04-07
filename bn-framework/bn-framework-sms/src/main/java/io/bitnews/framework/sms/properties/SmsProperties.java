package io.bitnews.framework.sms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by ywd on 2019/11/15.
 */
@Data
@ConfigurationProperties(prefix = "scs.framework.sms")
public class SmsProperties {

    private String accessKeyId;
    private String accessKeySecret;
    private String templateCode;
    private String signName;
    private Long codeEffectiveTime;
}
