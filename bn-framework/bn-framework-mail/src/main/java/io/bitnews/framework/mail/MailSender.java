package io.bitnews.framework.mail;

import io.bitnews.framework.mail.properties.MailProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Created by ywd on 2019/11/21.
 */
@Slf4j
public class MailSender {

    private JavaMailSenderImpl mailSender;

    @Autowired
    private MailProperties mailProperties;

    public MailSender(){
        log.info("初始化邮件组件");
    }

    public void send(MailSenderParams mailSenderParams) throws Exception {
        log.info("发送邮件：{}",mailSenderParams.getContent());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailProperties.getUsername());
        message.setTo(mailSenderParams.getEmail());
        message.setSubject(mailSenderParams.getSubject());
        message.setText(mailSenderParams.getContent());

        if (null == mailSender) {
            mailSender = new JavaMailSenderImpl();
            mailSender.setHost(mailProperties.getHost());
            mailSender.setUsername(mailProperties.getUsername());
            mailSender.setPassword(mailProperties.getPassword());
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.connectiontimeout", mailProperties.getConnectiontimeout());
            properties.setProperty("mail.smtp.timeout", mailProperties.getTimeout());
            properties.setProperty("mail.smtp.writetimeout", mailProperties.getWritetimeout());
            properties.setProperty("mail.smtp.auth", mailProperties.getAuth());
            properties.setProperty("mail.smtp.socketFactory.class", mailProperties.getSocketFactoryClass());
            properties.setProperty("mail.smtp.socketFactory.port", mailProperties.getSocketFactoryPort());
            mailSender.setJavaMailProperties(properties);
        }
        mailSender.send(message);
    }
}
