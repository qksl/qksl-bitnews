package io.bitnews.admin.controller;

import io.bitnews.admin.service.AdminService;
import io.bitnews.admin.service.BannerService;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.framework.file.AliyunOosFileStorage;
import io.bitnews.framework.file.FileStorage;
import io.bitnews.model.admin.po.TAdmin;
import io.bitnews.model.external.IdVo;
import io.bitnews.model.internal.BannerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;


/**
 * Created by ywd on 2019/7/12.
 */
@Slf4j
@Api(tags = "滚动横幅后台管理")
@RestController
@RequestMapping("/v1/admin/banner")
@PreAuthorize("hasAuthority('ADMIN')")
public class BannerAdminController {

    @Autowired
    private AliyunOosFileStorage aliyunOosFileStorage;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private AdminService adminService;

    @PostMapping("issue")
    @ApiOperation(value = "发布or编辑")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "pictureOrder", value = "图片顺序",  dataType = "String"),
            @ApiImplicitParam(paramType = "form", name = "jump", value = "是否跳转 1-是, 2-否", dataType = "String"),
            @ApiImplicitParam(paramType = "form", name = "jumpUrl", value = "跳转链接",  dataType = "String")}
    )
    public BaseResponse issue(@RequestParam("pictureOrder")String pictureOrder,
                              @RequestParam("jump")String jump,
                              @RequestParam(value = "jumpUrl", required = false)String jumpUrl,
                              @RequestParam("image") MultipartFile image,
                              Principal principal){
        String name = principal.getName();
        TAdmin tAdmin = adminService.findByName(name);
        if (null == tAdmin) {
            return new BaseResponse(UserSdkErrorCode.USER_SERVER_LOGIN_TOKEN_INVALID);
        }
        //上传图片
        String originalFilename = image.getOriginalFilename();
        InputStream inputStream = null;
        try {
            inputStream = image.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inputStream != null){
            aliyunOosFileStorage.store(inputStream, originalFilename);
        } else {
            log.warn("上传图片失败");
        }

        BannerVo bannerVo = new BannerVo();
        bannerVo.setAdminId(tAdmin.getId());
        bannerVo.setPictureOrder(Integer.parseInt(pictureOrder));
        bannerVo.setJump(jump);
        bannerVo.setJumpUrl(jumpUrl);
        bannerVo.setPicturePath(aliyunOosFileStorage.getFileProperties().getGetUrl()+ CommonConstant.SLASH+originalFilename);
        bannerVo.setType(CommonConstant.BANNER_TYPE_DEFAULT);
        bannerService.issue(bannerVo);
        return new BaseResponse();
    }

    @PostMapping("del")
    @ApiOperation(value = "删除横幅")
    public BaseResponse delete(@RequestBody IdVo idVo){
        bannerService.delete(Long.parseLong(idVo.getId()));
        return new BaseResponse();
    }

    @GetMapping("list")
    @ApiOperation(value = "查询banner")
    public BNPageResponse<BannerVo> list(
            @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize){
        return bannerService.queryBanner(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
    }

}
