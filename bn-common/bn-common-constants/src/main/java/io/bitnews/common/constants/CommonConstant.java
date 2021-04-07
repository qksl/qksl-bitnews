package io.bitnews.common.constants;

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
    public static final String PAGE_NUM_DEFAULT = "1";

    /**
     * 默认分页大小
     */
    public static final String PAGE_SIZE_DEFAULT = "10";

    /**
     * 默认排序
     */
    public static final String PAGE_SORT_DEFAULT = "create_time";

    /**
     * 最新优先排序
     */
    public static final String PAGE_SORT_UPDATE = "update_time";

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
     * utf-8
     */
    public static final String SLASH = "/";

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


    public static final String NOT_PASSWORD = "NOT_PASSWORD";

    /**
     * 邮件
     */
    public static final String CONTACT_EMAIL = "email";

    /**
     * 电话
     */
    public static final String CONTACT_MOBILE = "mobile";

    /**
     * 分割符
     */
    public static final String SF_FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String SF_LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String SF_PATH_SEPARATOR = System.getProperty("path.separator");


    /**
     *
     */
    public static final Long TIME_15_MINUTE = 15*60L;
    public static final Long TIME_1_DAY = 24*60*60L;

    public static final String APP_NAME = "区块森林";

    public static final String SMS_EMAIL_CODE = "【%s】欢迎使用%s，验证码%s, 有效时间15分钟。如非本人操作，请检查帐号安全 ";

    public static final String BTC_NAME = "BTC";
    public static final String BTC_BULL = "1";
    public static final String BTC_BEAR = "2";

    public static final String COMMENT_ISSUE = "2";
    public static final String COMMENT_DEL = "3";

    public static final String USER_COMMON = "0";
    public static final String USER_VIP = "1";

    public static final String USER_STATUS_BAN = "2";
    public static final String USER_STATUS = "1";
    public static final String USER_STATUS_NOTPASSWORD = "0";

    public static final String PROMOTER_CLOSE = "1";
    public static final String PROMOTER_SETTLEMENT = "2";

    //资讯的状态
    public static final String DISCUSSION_CREATE = "1";
    public static final String DISCUSSION_ISSUE = "2";
    public static final String DISCUSSION_DEL = "3";
    //滚动栏
    public static final String BANNER_TYPE_DEFAULT = "1";
    public static final String BANNER_TYPE_NEWS = "2";

    //重大事件
    public static final String NEWS_IS_EVENT = "1";
    public static final String NEWS_NOT_EVRNT = "2";

    //登录类型
    public static final String VC_AUTH_TYPE = "vc";
    public static final String SMS_AUTH_TYPE = "sms";
    public static final String ADMIN_AUTH_TYPE = "mg";

    //用户类型
    public static final String USER_TYPE_BIGV = "1";
    public static final String USER_TYPE_COMMON = "0";
    public static final String USER_TYPE_NO_LOGIN = "9";

    //token奖励
    public static final String FIRST_LOGIN_REWARD = "1000";

    public static final String REWARD_REASON_FISET_LOGIN = "首次登录奖励";

    //redis缓存时间设置
    public static final int SEVEN_DAY = 7*24*60*60;

    //任务类型
    public static final String TASK_TYPE_DAILY = "0";
    public static final String TASK_TYPE_GROW_UP = "1";
    public static final String TASK_TYPE_OPERATE= "2";

    public static final int TASK_STATUS_NO = 0;
    public static final int TASK_STATUS_YES = 1;
    public static final int TASK_STATUS_REWARD = 2;

    public static final String TASK_MARK_NAME_FIRST_LOGIN= "FIRST-LOGIN";
    public static final String TASK_MARK_NAME_DAILY_SIGNIN= "DAILY-SIGNIN";
    public static final String TASK_MARK_NAME_DAILY_GUESS= "DAILY-GUESS";


    //竞猜
    public static final String GUESS_TYPE_ONE = "1";//自动收益

    public static final String GUESS_TYPE_TWO = "2";//水友开盘

    public static final String GUESS_YES = "0";//支持

    public static final String GUESS_NO = "1";//反对

    public static final String GUESS_STATUS = "0";

    public static final String GUESS_STATUS_CLOSE = "1";

    public static final String GUESS_STATUS_FINISH = "2";

    public static final String GUESS_STATUS_ABANDON = "3";

    public static final String IS_FIRST_LOGIN = "0";
    public static final String NOT_FIRST_LOGIN = "1";

    //token流水类型
    //后台发放
    public static final String ADMIN_GRANT = "1";
    //参加竞猜
    public static final String JOIN_GUSS = "2";
    //竞猜收益
    public static final String JOIN_INCOME = "3";


    //市场分布的key
    public static final String MARKET_DISTRIBUTION = "MARKET:DISTRIBUTION";
}
