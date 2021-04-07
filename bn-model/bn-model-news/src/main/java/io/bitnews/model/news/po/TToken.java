package io.bitnews.model.news.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.math.BigDecimal;
import java.util.Date;


/*
 *
 * gen by beetlsql 2019-07-31
 */
@Table(name = "t_token")
@NoArgsConstructor
@Data
public class TToken {

    /*
    自增ID
    */
    private Long id;
    /*
    代币数量
    */
    private BigDecimal token;
    /*
    用户id
    */
    private Long userId;
    /*
    变动时间
    */
    private Date updateTime;

}
