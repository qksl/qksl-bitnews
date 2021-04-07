package io.bitnews.common.constants;

/**
 * Created by ywd on 2019/11/18.
 */
public class RedisConstant {

    /**
     * 用于短信验证
     */
    public static final String MOBILE_KEY_LOGIN = "MOBILE_KEY_LOGIN_";
    public static final String MOBILE_KEY_LOGIN_VALIDATE = "MOBILE_KEY_LOGIN_VALIDATE_";

    /**
     * 用于邮件登录验证
     */
    public static final String EMAIL_KEY_LOGIN = "EMAIL_KEY_LOGIN_";

    /**
     * 用于登录token管理
     */
    public static final String LOGIN_KEY = "LOGIN_KEY_";
    public static final String COLON_SEPARATE = ":";
    public static final String COLON_SEPARATE_MATCH = ":*";

    /**
     * 用于判断今日是否签到
     */
    public static final String DAILY_TASKS_KEY = "DAILY_TASKS_KEY_";
    public static final String GROW_UP_TASKS_KEY = "GROW_UP_TASKS_KEY_";
    public static final String OPERATE_TASKS_KEY = "OPERATE_TASKS_KEY_";

    /**
     * 存放market
     */
    public static final String MARKET_DISTRIBUTION_INFO = "MARKET_DISTRIBUTION_INFO";
    public static final String MARKET_CAP_INFO = "MARKET_CAP_INFO";
}
