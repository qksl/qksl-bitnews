package io.bitnews.news.controller;

import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.model.internal.BannerVo;
import io.bitnews.model.news.po.TBanner;
import io.bitnews.news.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ywd on 2019/7/18.
 */
@Slf4j
@Api("滚动横幅前台查询")
@RestController
@RequestMapping("/v1/user/banner")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @PostMapping("list")
    @ApiOperation(value = "查询banner")
    public BNPageResponse<BannerVo> list(
            @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize){

        PageQuery<TBanner> query = new PageQuery<>();
        query.setPageSize(Long.parseLong(pageSize));
        query.setPageNumber(Long.parseLong(pageNum));
        query.setPara("type", 1);//类型为横幅
        query.setOrderBy("picture_order");
        String cacheKey = "type1";
        return bannerService.queryByPage(query,cacheKey);
    }
}
