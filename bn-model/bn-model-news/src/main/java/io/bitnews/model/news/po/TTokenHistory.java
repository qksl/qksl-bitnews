package io.bitnews.model.news.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.math.BigDecimal;
import java.util.Date;


/*
 *
 * gen by beetlsql 2019-07-30
 */
@Table(name = "t_token_history")
@NoArgsConstructor
@Data
public class TTokenHistory {

    /*
    自增ID
    */
    private Long id;
    /*
    原因
    */
    private String reason;
    /*
    类型：1,系统发放 2，参与竞猜
    */
    private String type;
    /*
    变动代币数量
    */
    private BigDecimal token;

    /*
    用户id
    */
    private Long userId;
    /*
    变动时间
    */
    private Date createTime;


}
