package io.bitnews.oauth.integration.authenticator.sms.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

/**
 * 短信认证成功事件
 **/
@Slf4j
public class SmsAuthenticateSuccessEvent extends ApplicationEvent {

    public SmsAuthenticateSuccessEvent(Object source) {
        super(source);
    }
}