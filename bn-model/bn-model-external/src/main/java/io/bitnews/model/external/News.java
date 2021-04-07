package io.bitnews.model.external;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by ywd on 2019/12/23.
 */
@Data
@ApiModel("资讯信息")
public class News {

    /*
	ID
	*/
    private Long id;
    /*
    利空
    */
    @ApiModelProperty("利空数")
    private Integer bearCount;
    /*
    利好
    */
    private Integer bullCount;
    /*
    内容
    */
    private String content;

    @ApiModelProperty(notes = "摘要")
    private String summary;

    @ApiModelProperty(notes = "关键字")
    private String keywords;
    /*
    图片路径
    */
    private String picturePath;
    /*
    来源
    */
    @ApiModelProperty(notes = "来源")
    private String source;
    /*
    置顶用户ID
    */
    @ApiModelProperty(notes = "置顶用户ID")
    private Long upCommontUserId;
    /*
    置顶用户名字
    */
    private String upCommontUsername;
    /*
    置顶用户ID
    */
    private String upCommontUserImage;
    /*
    置顶评论时间
    */
    private String upCommontTime;
    /*
    置顶评论
    */
    private String upCommont;
    /*
    置顶评论
    */
    private boolean launchFlag;
    /*
    标识：1-政策, 2-区块链,3-交易所
    */
    private String tag;
    /*
    标题
    */
    private String title;
    /*
    评论数
    */
    private Integer commontNums;
    /*
    更新时间
    */
    private Date updateTime;

}
