package io.bitnews.user_sdk.constant;

/**
 * Created by ywd on 2019/7/1.
 */
public class CommonConstant {

    /**
     * 正常
     */
    public static final String STATUS_NORMAL = "0";

    /**
     * 异常
     */
    public static final String STATUS_EXCEPTION = "1";

    /**
     * 锁定
     */
    public static final String STATUS_LOCK = "9";

    /**
     * jwt签名
     */
    public static final String SIGN_KEY = "EXAM";

    /**
     * 页码
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 分页大小
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序
     */
    public static final String SORT = "sort";

    /**
     * 排序方向
     */
    public static final String ORDER = "order";

    /**
     * 默认页数
     */
    public static final String PAGE_NUM_DEFAULT = "0";

    /**
     * 默认分页大小
     */
    public static final String PAGE_SIZE_DEFAULT = "10";

    /**
     * 默认排序
     */
    public static final String PAGE_SORT_DEFAULT = "create_date";

    /**
     * 默认排序方向
     */
    public static final String PAGE_ORDER_DEFAULT = " desc";

    /**
     * 正常状态
     */
    public static final Integer DEL_FLAG_NORMAL = 0;

    /**
     * 删除状态
     */
    public static final Integer DEL_FLAG_DEL = 1;

    /**
     * 路由配置在Redis中的key
     */
    public static final String ROUTE_KEY = "_ROUTE_KEY";

    /**
     * 菜单标识
     */
    public static final String MENU = "0";

    /**
     * token请求头名称
     */
    public static final String REQ_HEADER = "Authorization";

    /**
     * token分割符
     */
    public static final String TOKEN_SPLIT = "Bearer ";

    /**
     * 默认密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 默认排序
     */
    public static final String DEFAULT_SORT = "30";

    /**
     * utf-8
     */
    public static final String UTF8 = "UTF-8";

    /**
     * 阿里
     */
    public static final String ALIYUN_SMS = "aliyun_sms";

    /**
     * 参数校验失败
     */
    public static final String IllEGAL_ARGUMENT = "参数校验失败！";

    /**
     * 保存code的前缀
     */
    public static final String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY";

    /**
     * Bearer
     */
    public static final String AUTHORIZATION_BEARER = "Bearer ";

    /**
     * 密码类型
     */
    public static final String GRANT_TYPE_PASSWORD = "password";
}
