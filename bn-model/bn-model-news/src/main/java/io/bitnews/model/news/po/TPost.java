package io.bitnews.model.news.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.util.Date;


/*
 *
 * gen by beetlsql 2019-07-03
 */
@Table(name = "t_post")
@NoArgsConstructor
@Data
public class TPost {
    /*
    ID
    */
    private Long id;
    /*
    点赞数
    */
    private Integer likedSum;
    /*
    内容
    */
    private String content;
    /*
    讨论ID
    */
    private Long discussionId;
    /*
    发布状态：2-发布, 3-删除
    */
    private String status;
    /*
    消息面: 1-利好, 2-利空
    */
    private String type;
    /*
    发布的用户ID
    */
    private Long userId;
    /*
    更新时间
    */
    private Date createTime;
    /*
    更新时间
    */
    private Date updateTime;

}
