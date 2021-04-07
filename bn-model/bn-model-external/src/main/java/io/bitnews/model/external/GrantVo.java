package io.bitnews.model.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GrantVo {
    String id;
    @ApiModelProperty(value = "token数量", name = "amount", example = "10000")
    String amount;
}