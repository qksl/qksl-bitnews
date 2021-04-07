package io.bitnews.model.news.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.util.Date;


/*
 *
 * gen by beetlsql 2019-07-03
 */
@Table(name = "t_liked")
@NoArgsConstructor
@Data
public class TLiked {

    /*
    ID
    */
    private Long id;
    /*
    发布ID
    */
    private Long postId;
    /*
    用户ID
    */
    private Long userId;
    /*
    更新时间
    */
    private Date updateTime;

}
