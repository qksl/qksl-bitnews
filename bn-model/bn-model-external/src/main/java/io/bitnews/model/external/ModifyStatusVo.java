package io.bitnews.model.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ModifyStatusVo {
    String id;
    @ApiModelProperty(value = "状态", name = "status", example = "1:正常, 2:禁封")
    String status;
}