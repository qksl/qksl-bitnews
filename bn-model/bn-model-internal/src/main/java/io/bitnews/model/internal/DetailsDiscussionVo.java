package io.bitnews.model.internal;

import lombok.Data;

import java.util.List;

/**
 * Created by ywd on 2019/9/5.
 */
@Data
public class DetailsDiscussionVo {

    //详细咨询
    private DiscussionVo discussionVo;

    //相关推荐
    private List<DiscussionVo> lists;

    //热点评论
    private List<PostVo> posts;

    private String desc = "{\"lists\":\"相关推荐\",\"posts\":\"热点评论\"}";
}
