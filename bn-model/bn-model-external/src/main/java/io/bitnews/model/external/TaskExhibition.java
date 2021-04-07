package io.bitnews.model.external;

import io.bitnews.model.em.TaskStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by ywd on 2019/12/16.
 */
@Data
public class TaskExhibition {

    /*
    自增ID
    */
    private Long id;
    /*
    任务奖励
    */
    private Integer reward;
    /*
    描叙
    */
    private String desc;
    /*
    类型: 0-每日, 1-成长, 2-运营
    */
    @ApiModelProperty("类型: 0-每日, 1-成长, 2-运营")
    private String type;
    /*
    是否完成
    */
    private Integer status;

    private String statusStr;

    public String getStatusStr() {
        if (null != status) {
            return TaskStatusEnum.getName(status);
        }
        return statusStr;
    }
}
