package io.bitnews.passport.controller;

import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.IdVo;
import io.bitnews.model.external.IssueCommentVo;
import io.bitnews.model.internal.PostVo;
import io.bitnews.model.passport.po.TUser;
import io.bitnews.passport.service.CommentService;
import io.bitnews.passport.service.NewService;
import io.bitnews.passport.service.UserService;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by ywd on 2019/7/23.
 */
@Slf4j
@Api(tags = "用户评论接口，需要登录")
@RestController
@RequestMapping("/v1/passport/user/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private NewService newService;

    @PostMapping("issue")
    @ApiOperation(value = "发布评论")
    public BaseResponse issue(@RequestBody @ApiParam(name = "评论对象", value = "传入json格式", required = true) IssueCommentVo issueCommentVo, Principal principal) {
        String content = issueCommentVo.getContent();
        String discussionId = issueCommentVo.getId();
        String type = issueCommentVo.getType();
        if (null == principal || null == discussionId || null == type) {
            return new BaseResponse(UserSdkErrorCode.USER_SERVER_LOGIN_TOKEN_INVALID);
        }
        String userId = principal.getName();
        TUser tUser = userService.findById(userId);
        PostVo postVo = new PostVo();
        postVo.setContent(content);
        postVo.setDiscussionId(Long.parseLong(discussionId));
        postVo.setLikedSum(0);
        postVo.setType(type);
        postVo.setUserId(tUser.getId());
        return commentService.issueComment(postVo);
    }

    @PostMapping("liked")
    @ApiOperation(value = "点赞评论")
    public BaseResponse liked(@RequestBody @ApiParam(name = "点赞对象", value = "传入json格式", required = true) IdVo clickLikedVo, Principal principal) {
        if (null == principal || null == clickLikedVo.getId()) {
            return new BaseResponse(UserSdkErrorCode.USER_SERVER_LOGIN_TOKEN_INVALID);
        }
        String userId = principal.getName();
        return commentService.liked(clickLikedVo.getId(), userId);
    }

    @PostMapping("liked/cancel")
    @ApiOperation(value = "取消点赞")
    public BaseResponse cancelLike(@RequestBody @ApiParam(name = "取消点赞", value = "传入json格式", required = true) IdVo idVo, Principal principal) {
        if (null == principal || null == idVo.getId()) {
            return new BaseResponse(UserSdkErrorCode.USER_SERVER_LOGIN_TOKEN_INVALID);
        }
        String userId = principal.getName();
        return commentService.cancelLike(idVo.getId(), userId);
    }

    @GetMapping("list")
    @ApiOperation(value = "查询评论,需要用户登录，该接口包含用户是否点赞的判断")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "discussionId", value = "评论的id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "消息面: 1-利好, 2-利空", dataType = "String")
    })
    public BNPageResponse<PostVo> list(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "type", defaultValue = CommonConstant.BTC_BULL) String type,
            @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
            Principal principal) {
        if (null == principal) {
            return new BNPageResponse(UserSdkErrorCode.USER_SERVER_LOGIN_TOKEN_INVALID);
        }
        String userId = principal.getName();
        return newService.queryCommentPage(id, type, CommonConstant.COMMENT_ISSUE, userId, pageNum, pageSize);
    }

}
