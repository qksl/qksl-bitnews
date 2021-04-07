package io.bitnews.framework.sms;

import io.bitnews.framework.sms.properties.SmsProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ywd on 2019/11/15.
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
public class SmsConfig {
}
