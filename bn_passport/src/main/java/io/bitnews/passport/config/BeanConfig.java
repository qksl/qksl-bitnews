package io.bitnews.passport.config;

import io.bitnews.framework.file.AliyunOosFileStorage;
import io.bitnews.framework.mail.MailSender;
import io.bitnews.framework.sms.SmsSender;
import io.bitnews.framework.sms.sender.AliyunSmsSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
public class BeanConfig {

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }

    @Bean
    public SmsSender smsSender() {
        return new AliyunSmsSender();
    }

    @Bean
    public MailSender mailSender() {
        return new MailSender();
    }

    @Bean
    public AliyunOosFileStorage aliyunOosFileStorage() {
        return new AliyunOosFileStorage();
    }
}