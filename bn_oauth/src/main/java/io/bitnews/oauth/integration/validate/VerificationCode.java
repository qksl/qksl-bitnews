package io.bitnews.oauth.integration.validate;

/**
 * Created by ywd on 2019/11/18.
 */
public interface VerificationCode {

    boolean validate(String code, String param);
}
