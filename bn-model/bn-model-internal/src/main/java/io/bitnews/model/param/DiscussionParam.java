package io.bitnews.model.param;

import lombok.Data;

import java.util.Date;

/**
 * Created by ywd on 2019/12/30.
 */
@Data
public class DiscussionParam {

    /*
   ID
   */
    private Long id;
    /*
    利空
    */
    private Integer bearCountAdd;
    /*
    利好
    */
    private Integer bullCountAdd;
    /*
    内容
    */
    private String content;
    /*
    重大事件：1:是, 2:否
    */
    private String eventFlag;
    /*
    事件日期：格式如2019-01-01
    */
    private Date eventTime;
    /*
    图片路径
    */
    private String picturePath;
    /*
    来源
    */
    private String source;
    /*
    发布状态：1:创建, 2:发布, 3:删除
    */
    private String status;
    /*
    置顶用户ID
    */
    private Long stickyPostId;
    /*
    标识：1-政策, 2-区块链,3-交易所
    */
    private String tag;
    /*
    标题
    */
    private String title;

}
