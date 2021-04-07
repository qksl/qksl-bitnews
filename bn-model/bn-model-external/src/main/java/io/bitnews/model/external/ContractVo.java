package io.bitnews.model.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class ContractVo {

    /*
   资讯ID
   */
    @ApiModelProperty(value = "咨询id", name = "discussionId", example = "1000")
    protected Long discussionId;
    /*
     发起竞猜：0-能; 1-不能
     */
    @ApiModelProperty(value = "0-高于; 1-低于", name = "guessWinner", example = "0-高于; 1-低于")
    protected String guessWinner;
    /*
    预测价格
    */
    @ApiModelProperty(value = "预测价格", name = "guessGold", example = "10000")
    protected BigDecimal guessGold;
    /*
    竞猜币种
    */
    @ApiModelProperty(value = "竞猜币种", name = "tokenType", example = "10000")
    protected String tokenType;
    /*
    竞猜主题
    */
    @ApiModelProperty(value = "竞猜主题", name = "topic", example = "行情启动，中线看好")
    protected String topic;


}