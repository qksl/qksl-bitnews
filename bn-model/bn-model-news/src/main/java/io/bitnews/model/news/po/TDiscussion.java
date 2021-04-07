package io.bitnews.model.news.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.util.Date;


/*
 *
 * gen by beetlsql 2019-07-03
 */
@Table(name = "t_discussion")
@NoArgsConstructor
@Data
public class TDiscussion {

    /*
    ID
    */
    private Long id;
    /*
    利空
    */
    private Integer bearCount;
    /*
    利好
    */
    private Integer bullCount;
    /*
    管理员ID
    */
    private Long adminId;
    /*
    内容
    */
    private String content;

    private String summary;

    private String keywords;
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
    /*
    更新时间
    */
    private Date updateTime;
}
