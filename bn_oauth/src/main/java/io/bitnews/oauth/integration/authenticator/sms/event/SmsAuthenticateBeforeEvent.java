package io.bitnews.oauth.integration.authenticator.sms.event;

import io.bitnews.oauth.integration.IntegrationAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;

/**
 * 短信认证之前的事件，可以监听事件进行用户手机号自动注册
 **/
@Slf4j
public class SmsAuthenticateBeforeEvent extends ApplicationEvent {

    public SmsAuthenticateBeforeEvent(Object source) {
        super(source);
    }
}