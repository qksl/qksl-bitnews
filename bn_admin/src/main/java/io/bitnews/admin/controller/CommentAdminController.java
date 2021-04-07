package io.bitnews.admin.controller;

import io.bitnews.admin.service.CommentService;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.IdVo;
import io.bitnews.model.internal.PostVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ywd on 2019/7/23.
 */
@Slf4j
@Api(tags = "管理员评论管理")
@RestController
@RequestMapping("/v1/admin/comment")
@PreAuthorize("hasAuthority('ADMIN')")
public class CommentAdminController {

    @Autowired
    private CommentService commentService;

    @GetMapping("list")
    @ApiOperation(value = "查询评论")
    public BaseResponse list(
            @RequestParam("discussionId") String discussionId,
            @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String
                    pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String
                    pageSize) {
        return commentService.queryComment(discussionId, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
    }

    @PostMapping("del")
    @ApiOperation(value = "删除评论")
    public BaseResponse delete(@RequestBody IdVo idVo) {
        commentService.deleteComment(Long.parseLong(idVo.getId()));
        return new BaseResponse();
    }

}
