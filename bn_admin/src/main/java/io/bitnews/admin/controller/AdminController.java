package io.bitnews.admin.controller;

import io.bitnews.admin.service.AdminService;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.common.model.LoginDTO;
import io.bitnews.model.admin.po.TAdmin;
import io.bitnews.model.external.AdminLoginVo;
import io.bitnews.model.internal.AdminVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ywd on 2019/7/3.
 */
@Slf4j
@AllArgsConstructor
@Api(tags = "管理员个人中心接口")
@RestController
@RequestMapping("/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("list")
    @ApiOperation(value = "获取管理员列表")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue =
                    CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue =
                    CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String")}
    )
    public BNPageResponse<AdminVo> adminList(@RequestParam(value = CommonConstant.PAGE_NUM) String pageNum,
                                             @RequestParam(value = CommonConstant.PAGE_SIZE) String pageSize) {
        PageQuery<TAdmin> query = new PageQuery<>();
        query.setPageSize(Long.parseLong(pageSize));
        query.setPageNumber(Long.parseLong(pageNum));
        BNPageResponse<AdminVo> rs = adminService.queryByPage(query);
        return rs;
    }

    @PostMapping("login")
    @ApiOperation(value = "登录")
    public BNResponse<LoginDTO> login(@RequestBody AdminLoginVo adminLoginVo){
        String password = adminLoginVo.getPassword();
        String username = adminLoginVo.getUsername();
        if (null == username || null == password) {
            return new BNResponse<>(UserSdkErrorCode.NOTIFY_PARAMS_EMPTY);
        }
        //参数判断，省略
        LoginDTO loginDTO = adminService.login(username, password);
        return new BNResponse(loginDTO);
    }

}
