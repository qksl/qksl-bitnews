package io.bitnews.model.internal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by ywd on 2019/7/12.
 */
@NoArgsConstructor
@Data
@ApiModel("评论")
public class PostVo {

    /*
    ID
    */
    private Long id ;
    /*
    用户名
    */
    private String userName ;
    /*
    头像
    */
    private String txPicture ;
    /*
    点赞数
    */
    @ApiModelProperty("点赞数")
    private Integer likedSum ;
    /*
    内容
    */
    @ApiModelProperty("内容")
    private String content ;
    /*
    讨论ID
    */
    @ApiModelProperty("咨询id")
    private Long discussionId ;
    /*
    消息面: 1-利好, 2-利空
    */
    @ApiModelProperty("消息面: 1-利好, 2-利空")
    private String type ;
    /*
    发布的用户ID
    */
    private Long userId ;
    /*
    更新时间
    */
    private Date updateTime ;
    /*
    更新时间
    */
    private Date createTime;

    private boolean isLiked;
}
