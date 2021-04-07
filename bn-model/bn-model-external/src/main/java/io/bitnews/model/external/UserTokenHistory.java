package io.bitnews.model.external;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ywd on 2019/12/19.
 */
@Data
@NoArgsConstructor
@ApiModel
public class UserTokenHistory {

    /*
   自增ID
   */
    private Long id;
    /*
    原因
    */
    private String reason;
    /*
    1-新增, 2-消费
    */
    @ApiModelProperty(notes = "1-后台发放, 2-参加竞猜， 3-竞猜收益")
    private String type;

    /*
    变动代币数量
    */
    @ApiModelProperty(notes = "变动代币数量")
    private BigDecimal token;

    /*
    用户id
    */
    private Long userId;
    /*
    变动时间
    */
    private Date createTime;
}
