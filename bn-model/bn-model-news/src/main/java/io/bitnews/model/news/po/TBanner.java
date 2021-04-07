package io.bitnews.model.news.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.util.Date;


/*
 *
 * gen by beetlsql 2019-07-03
 */
@Table(name = "t_banner")
@NoArgsConstructor
@Data
public class TBanner {

    /*
    ID
    */
    private Long id;
    /*
    管理员ID
    */
    private Long adminId;
    /*
    图片顺序
    */
    private Integer pictureOrder;
    /*
    是否跳转 1-是, 2-否
    */
    private String jump;
    /*
    跳转链接
    */
    private String jumpUrl;
    /*
    图片路径
    */
    private String picturePath;
    /*
    横幅类型 1-首页, 2-资讯
    */
    private String type;
    /*
    更新时间
    */
    private Date updateTime;

}
