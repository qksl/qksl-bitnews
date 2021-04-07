package io.bitnews.framework.mail;

import lombok.Data;

import java.util.Properties;

/**
 * Created by ywd on 2019/11/21.
 */
@Data
public class MailSenderParams {

    private String[] email;//接收方邮件
    private String subject;//主题
    private String content;//邮件内容
}
