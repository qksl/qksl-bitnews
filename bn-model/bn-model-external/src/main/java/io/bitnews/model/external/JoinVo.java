package io.bitnews.model.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class JoinVo {
    /*
    下注：0-能; 1-不能
    */
    @ApiModelProperty(value = "下注：0-支持; 1-反对", name = "bets", example = "0-支持; 1-反对")
    private String bets;
    /*
    投入金额
    */
    @ApiModelProperty(value = "压入的token", name = "betsGold", example = "1000")
    private BigDecimal betsGold;
    /*
    竞猜盘主题id
    */
    @ApiModelProperty(value = "竞猜id", name = "promoterTopicId", example = "id")
    private Long promoterTopicId;

}