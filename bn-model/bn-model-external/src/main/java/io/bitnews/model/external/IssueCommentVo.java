package io.bitnews.model.external;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IssueCommentVo {

    String id;
    String content;
    @ApiModelProperty(value = "类别", name = "type", example = "消息面: 1-利好, 2-利空")
    String type;
}