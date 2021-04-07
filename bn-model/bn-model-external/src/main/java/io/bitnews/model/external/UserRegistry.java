package io.bitnews.model.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * Created by ywd on 2020/3/2.
 */
@Data
public class UserRegistry {

    @ApiModelProperty(value = "类型", name = "type", example = "类型:mobile,email")
    String type;
    String username;
    String code;
    String password;
    boolean isSetPassword;
}
