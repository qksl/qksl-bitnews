package io.bitnews.framework.mail.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by ywd on 2019/11/21.
 */
@Data
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {

    String host;
    String username;
    String password;
    @Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
    String connectiontimeout;
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    String timeout;
    @Value("${spring.mail.properties.mail.smtp.writetimeout}")
    String writetimeout;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    String auth;
    @Value("${spring.mail.properties.mail.smtp.socketFactory.class}")
    String socketFactoryClass;
    @Value("${spring.mail.properties.mail.smtp.socketFactory.port}")
    String socketFactoryPort;
}
