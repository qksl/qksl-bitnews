package io.bitnews.model.internal;

import io.bitnews.model.external.LaunchVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ywd on 2019/7/30.
 */
@NoArgsConstructor
@Data
public class PromoterTopicVo extends LaunchVo {
    /*
    内容
    */
    private String context;
    /*
    发起人
    */
    private Long createUserId;

    private Long promoterId;
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
    消息面: 1-自动收益, 2-水友开盘
    */
    private String type;
    /*
    胜方：0-支持; 1-反对
    */
    private String winner;
    /*
    投注截止时间
    */
    private Date permitTime;

}
