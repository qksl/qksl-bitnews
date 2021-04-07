package io.bitnews.news.controller;


import io.bienews.common.helper.StringUtil;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.framework.redis.RedisManager;
import io.bitnews.model.internal.PostVo;
import io.bitnews.model.news.po.TPost;
import io.bitnews.model.param.PostParam;
import io.bitnews.news.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ywd on 2019/7/12.
 */
@Slf4j
@Api("用户评分模块")
@RestController
@RequestMapping("/v1/user/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private RedisManager redisManager;

    @PostMapping("issue")
    @ApiOperation(value = "发布评论")
    public BaseResponse issue(@RequestBody PostVo postVo){
        commentService.issue(postVo);
        return new BaseResponse();
    }

    @PostMapping("liked")
    @ApiOperation(value = "点赞评论")
    public BaseResponse liked(@RequestParam("postId")String postId, @RequestParam("userId")String userId){
        TPost tPost = commentService.findById(Long.parseLong(postId));
        PostParam postParam = new PostParam();
        postParam.setId(tPost.getId());
        postParam.setDiscussionId(tPost.getDiscussionId());
        postParam.setUserId(Long.parseLong(userId));
        boolean liked = commentService.liked(postParam);
        redisManager.sSet(postId, userId);
        if (liked){
            return new BaseResponse();
        }
        return new BaseResponse(UserSdkErrorCode.DOUBLE_CLICK);
    }

    @PostMapping("liked/cancel")
    @ApiOperation(value = "取消点赞")
    public BaseResponse cancelLike(@RequestParam("postId")String postId, @RequestParam("userId")String userId){
        TPost tPost = commentService.findById(Long.parseLong(postId));
        PostParam postParam = new PostParam();
        postParam.setId(tPost.getId());
        postParam.setDiscussionId(tPost.getDiscussionId());
        postParam.setUserId(Long.parseLong(userId));
        commentService.cancelLike(postParam);
        redisManager.setRemove(postId, userId);
        return new BaseResponse();
    }

    @GetMapping("list")
    @ApiOperation(value = "查询评论")
    public BNPageResponse<PostVo> list(
            @RequestParam(value = "discussionId") String discussionId,
            @RequestParam(value = "type", defaultValue = CommonConstant.BTC_BULL) String type,
            @RequestParam(value = "status", defaultValue = CommonConstant.COMMENT_ISSUE) String status,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize){
        PageQuery<TPost> query = new PageQuery<>();
        query.setPageSize(Long.parseLong(pageSize));
        query.setPageNumber(Long.parseLong(pageNum));
        StringBuffer sb = new StringBuffer(pageNum+pageSize);
        if (StringUtil.isNotEmpty(discussionId)){
            query.setPara("discussionId", discussionId);
            sb.append(discussionId);
        }
        if (StringUtil.isNotEmpty(type)){
            query.setPara("type", type);
            sb.append(type);
        }
        query.setPara("status", CommonConstant.COMMENT_ISSUE);
        query.setOrderBy("id desc");
        BNPageResponse<PostVo> rs = commentService.queryPageByCondition(query, sb.toString());
        List<PostVo> list = rs.getList();
        if (null != userId) {
            log.info("从redis中获取是否点赞: " + userId);
            for (PostVo vo : list){
                boolean flag = redisManager.sHasKey(vo.getId().toString(), userId);
                vo.setLiked(flag);
            }
        }
        rs.setList(list);
        return rs;
    }

    @GetMapping("max")
    @ApiOperation(value = "查询点赞最多评论")
    public BNResponse<PostVo> maxLike(@RequestParam(value = "discussionId") String discussionId) {
        PostVo postVo = commentService.queryMaxLiked(discussionId);
        return new BNResponse<>(postVo);
    }

    @GetMapping("maxtest")
    @ApiOperation(value = "查询点赞最多评论")
    public BNResponse<TPost> maxLikeTest(@RequestParam(value = "discussionId") String discussionId) {
        TPost postVo = commentService.maxLiked(Long.parseLong(discussionId));
        return new BNResponse<>(postVo);
    }

}
