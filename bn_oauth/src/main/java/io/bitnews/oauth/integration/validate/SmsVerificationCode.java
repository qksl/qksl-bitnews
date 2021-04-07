package io.bitnews.oauth.integration.validate;

import io.bitnews.common.constants.RedisConstant;
import io.bitnews.framework.redis.RedisManager;
import io.bitnews.model.external.VerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ywd on 2019/11/18.
 */
@Service("smsVerificationCode")
public class SmsVerificationCode implements VerificationCode {

    @Autowired
    RedisManager redisManager;

    @Override
    public boolean validate(String smsCode, String mobile) {
//        String cacheKey = RedisConstant.LOGIN_KEY + mobile;
        if (mobile != null && smsCode != null) {
            return true;
        }
        return false;
    }
}
