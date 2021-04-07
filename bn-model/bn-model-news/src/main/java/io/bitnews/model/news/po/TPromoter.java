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
@Table(name = "t_promoter")
@NoArgsConstructor
@Data
public class TPromoter {

    /*
	自增ID
	*/
    private Long id;
    private Long promoterId;
    /*
    资讯ID
    */
    private Long discussionId;
    /*
    预测价格
    */
    private BigDecimal guessGold;
    /*
    发起竞猜：0-高于; 1-低于
    */
    private String guessWinner;
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
    发起人ID
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
