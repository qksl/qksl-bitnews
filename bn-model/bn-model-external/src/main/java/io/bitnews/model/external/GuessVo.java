package io.bitnews.model.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class GuessVo {
    /*
    讨论ID
    */
    @ApiModelProperty(value = "咨询id", name = "discussionId", example = "如果有相关则填")
    private Long discussionId;
    /*
    竞猜币种
    */
    @ApiModelProperty(value = "竞猜币种", name = "tokenType", example = "竞猜币种:BTC")
    private String tokenType;
    /*
    猜胜方：0-能; 1-不能
    */
    @ApiModelProperty(value = "高于or低于", name = "guessWinner", example = "0：高于，1：低于")
    private String guessWinner;
    /*
    预测金额大小
    */
    @ApiModelProperty(value = "金额", name = "guessGold", example = "预测金额：100")
    private String guessGold;
    /*
    投注截止时间
    */
    @ApiModelProperty(value = "封盘的时间", name = "permitTime", example = "提前封盘时间: 1")
    @DateTimeFormat
    private String permit;
    /*
    结算时间
    */
    @ApiModelProperty(value = "竞猜内容中的时间字段", name = "settlementTime", example = "预测时间：2019-10-11 09:12:00")
    @DateTimeFormat
    private Date settlementTime;

    /*
    竞猜类型: 1-自动收益, 2-水友开盘
    */
    @ApiModelProperty(value = "竞猜类型", name = "type", example = "1-自动收益, 2-水友开盘")
    private String type;
}