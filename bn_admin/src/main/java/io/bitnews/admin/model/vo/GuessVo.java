package io.bitnews.admin.model.vo;

import io.bitnews.model.em.ChoiceEnum;
import io.bitnews.model.em.GuessStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ywd on 2020/4/28.
 */
@ApiModel
@Data
public class GuessVo {
    /*
   自增ID
   */
    private Long id;
    /*
    竞猜主题
    */
    private String topic;
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
    @ApiModelProperty(notes = "咨询的id")
    private Long discussionId;
    private String title;
    /*
	发布状态：0-开盘, 1-封盘, 2-结算
	*/
    @ApiModelProperty(notes = "发布状态：0-开盘, 1-封盘, 2-结算")
    private String status;
    private String statusStr;
    /*
    竞猜币种
    */
    @ApiModelProperty(notes = "竞猜币种")
    private String tokenType;
    /*
    预测价格
    */
    @ApiModelProperty(notes = "预测价格")
    private BigDecimal guessGold;
    /*
    竞猜类型: 1-自动收益, 2-水友开盘
    */
    @ApiModelProperty(notes = "竞猜类型: 1-自动收益, 2-水友开盘")
    private String type;
    /*
	猜胜方：0-支持; 1-反对
	*/
    @ApiModelProperty(notes = "猜胜方：0-支持; 1-反对")
    private String guessWinner;
    /*
    投注截止时间
    */
    @ApiModelProperty(notes = "投注截止时间")
    private Date permitTime;
    /*
	结算时间
	*/
    @ApiModelProperty(notes = "结算时间")
    private Date settlementTime;

}
