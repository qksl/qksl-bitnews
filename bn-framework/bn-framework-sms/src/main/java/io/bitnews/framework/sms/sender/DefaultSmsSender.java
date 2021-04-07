package io.bitnews.framework.sms.sender;

import com.alibaba.fastjson.JSONObject;
import io.bitnews.framework.sms.SmsParameter;
import io.bitnews.framework.sms.SmsSendResult;
import io.bitnews.framework.sms.SmsSender;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ywd on 2019/11/15.
 */
@Slf4j
public class DefaultSmsSender implements SmsSender {

    @Override
    public SmsSendResult send(SmsParameter parameter) {
        JSONObject jsonObject = JSONObject.parseObject(parameter.getParams());
        log.info("发送短信验证码：" + parameter.getPhoneNumber()+"--"+jsonObject.getString("code"));
        SmsSendResult result = new SmsSendResult();
        result.setCode(jsonObject.getString("code"));
        result.setSuccess(true);
        return result;
    }
}
