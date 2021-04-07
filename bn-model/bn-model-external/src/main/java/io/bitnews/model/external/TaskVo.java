package io.bitnews.model.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by ywd on 2019/12/16.
 */
@Data
public class TaskVo {

    /*
    任务奖励
    */
    @ApiModelProperty(value = "任务奖励", name = "reward", example = "1000")
    private Integer reward;
    /*
    描叙
    */
    @ApiModelProperty(value = "描叙", name = "desc", example = "每日签到")
    private String desc;
    /*
	唯一标识
	*/
    @ApiModelProperty(value = "唯一标识", name = "uniqueMark", example = "FIRST-LOGIN")
    private String uniqueMark;
    /*
    类型: 0-每日, 1-成长, 2-运营
    */
    @ApiModelProperty(value = "类型", name = "type", example = "类型: 0-每日, 1-成长, 2-运营")
    private String type;
}
