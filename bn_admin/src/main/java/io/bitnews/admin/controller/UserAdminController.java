package io.bitnews.admin.controller;

import io.bitnews.admin.service.UserService;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.IdVo;
import io.bitnews.model.external.ModifyStatusVo;
import io.bitnews.model.internal.UserDataVo;
import io.bitnews.model.internal.UserVo;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ywd on 2019/7/23.
 */
@Slf4j
@Api(tags = "管理员用户管理")
@RestController
@RequestMapping("/v1/admin/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserAdminController {

    @Autowired
    private UserService userService;

    @PostMapping("modify/status")
    @ApiOperation(value = "禁止")
    public BaseResponse status(@RequestBody ModifyStatusVo modifyStatusVo) {
        String userId = modifyStatusVo.getId();
        String status = modifyStatusVo.getStatus();
        if (null == userId || null == status) {
            return new BaseResponse(UserSdkErrorCode.INTERNAL_SERVER_ERROR);
        }
        userService.modifyUserStatus(userId, status);
        return new BaseResponse();
    }

    @GetMapping("list")
    @ApiOperation(value = "获取用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue =
                    CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue =
                    CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "condition", value = "查询条件,目前支持id", dataType = "String")}
    )
    public BNPageResponse<UserVo> adminList(@RequestParam(value = CommonConstant.PAGE_NUM) String pageNum,
                                            @RequestParam(value = CommonConstant.PAGE_SIZE) String pageSize,
                                            @RequestParam(value = "condition", required = false) String condition) {
        return userService.userList(Integer.parseInt(pageNum), Integer.parseInt(pageSize), condition);
    }

    @PostMapping("auth/influencer")
    @ApiOperation(value = "大V认证,需要管理员权限")
    public BaseResponse influencer(@RequestBody IdVo idVo) {
        userService.authInfluencer(idVo.getId());
       return new BaseResponse();
    }

    @GetMapping("show/user")
    @ApiOperation(value = "用户数据展示")
    public BNResponse<UserDataVo> show() {
        UserDataVo vo = userService.show();
        return new BNResponse<>(vo);
    }

}
