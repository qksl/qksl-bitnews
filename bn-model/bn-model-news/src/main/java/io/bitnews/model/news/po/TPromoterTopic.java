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
@Table(name = "t_promoter_topic")
@NoArgsConstructor
@Data
public class TPromoterTopic {

    /*
	自增ID
	*/
    private Long id;
    /*
    内容
    */
    private String context;
    /*
    发起人
    */
    private Long createUserId;
    /*
    讨论ID
    */
    private Long discussionId;
    /*
    预测价格
    */
    private BigDecimal guessGold;
    /*
    猜大小: 0:高于，1：低于
    */
    private String guessWinner;
    /*
    投入金额
    */
    private BigDecimal betsGold;
    private BigDecimal odds;
    /*
    发布状态：0-开盘, 1-封盘, 2-结算
    */
    private String status;
    /*
    竞猜币种
    */
    private String tokenType;
    /*
    竞猜主题
    */
    private String topic;
    /*
    消息面: 1-自动收益, 2-水友开盘
    */
    private String type;
    /*
    胜方：0-支持; 1-反对
    */
    private String winner;
    /*
    创建时间
    */
    private Date createTime;
    /*
    投注截止时间
    */
    private Date permitTime;
    /*
    结算时间
    */
    private Date settlementTime;

}
