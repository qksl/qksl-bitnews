package io.bitnews.model.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ContractLaunch {

    @ApiModelProperty(value = "合约id", name = "promoterId", example = "1000")
    protected String promoterId;

    @ApiModelProperty(value = "押金", name = "betsGold", example = "1000")
    protected String betsGold;

    @ApiModelProperty(value = "赔率", name = "odds", example = "2")
    private BigDecimal odds;

    @ApiModelProperty(value = "支持or反对", name = "winner", example = "0：支持；1：反对")
    protected String winner;
}