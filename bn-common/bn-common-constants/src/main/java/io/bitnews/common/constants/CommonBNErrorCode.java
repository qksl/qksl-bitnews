package io.bitnews.common.constants;

/**
 * Created by ywd on 2019/8/29.
 */
public class CommonBNErrorCode implements BNErrorCode {

    private String errorCode;
    private String message;
    private String messageZh;

    public CommonBNErrorCode(String errorCode, String message, String messageZh) {
        this.errorCode = errorCode;
        this.message = message;
        this.messageZh = messageZh;
    }

    @Override
    public String getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getMessageZh() {
        return this.messageZh;
    }
}
