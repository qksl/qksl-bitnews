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
@Table(name = "t_bets")
@NoArgsConstructor
@Data
public class TBets {

    /*
    自增ID
    */
    private Long id;
    /*
    下注：0-能; 1-不能
    */
    private String bets;
    /*
    投入金额
    */
    private BigDecimal betsGold;
    /*
    收益
    */
    private BigDecimal income;
    /*
    赔率
    */
    private BigDecimal odds;
    /*
    竞猜盘主题id
    */
    private Long promoterTopicId;
    /*
    发布状态：0-未结束, 1-赢, 2-输
    */
    private String status;
    /*
    参与用户ID
    */
    private Long userId;
    /*
    胜方：0-能; 1-不能
    */
    private String winner;
    /*
    创建时间
    */
    private Date createTime;

}
