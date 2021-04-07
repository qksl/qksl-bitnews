package io.bitnews.framework.mail;

import io.bitnews.framework.mail.properties.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by ywd on 2019/11/21.
 */
@Configuration
@EnableConfigurationProperties(MailProperties.class)
public class MailConfig {
}
