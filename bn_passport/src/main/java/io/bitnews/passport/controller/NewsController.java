package io.bitnews.passport.controller;

import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.model.external.News;
import io.bitnews.model.internal.BannerVo;
import io.bitnews.model.internal.DetailsDiscussionVo;
import io.bitnews.model.internal.DiscussionVo;
import io.bitnews.model.internal.PostVo;
import io.bitnews.passport.service.NewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by ywd on 2019/7/23.
 */
@Slf4j
@Api(tags = "资讯获取接口,以下方法都不需要权限即可调用")
@RestController
@RequestMapping("/v1/passport/news")
public class NewsController {

    @Autowired
    private NewService newService;

    @GetMapping("latest")
    @ApiOperation(value = "获取最新资讯")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "tag", value = "0表示不区分类别,标识：1-政策, 2-区块链,3-交易所 ", defaultValue =
                    "0", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue =
                    CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue =
                    CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String")}
    )
    public BNPageResponse<News> latestNews(
            @RequestParam(value = "tag", defaultValue = "0") String tag,
            @RequestParam(value = CommonConstant.PAGE_NUM) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE) String pageSize){
        return newService.queryLatestNews(tag, pageNum, pageSize);
    }

    @GetMapping("details")
    @ApiOperation(value = "获取资讯详情页")
    public BNResponse<DetailsDiscussionVo> details(@RequestParam(value = "id") String id) {
        return newService.queryDetails(id);
    }

    @GetMapping("event")
    @ApiOperation(value = "重大事件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "start", value= "起始时间,格式：2019-07-01", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "end", value = "结束时间,格式：2019-07-01", dataType = "String")
    })
    public BNPageResponse<DiscussionVo> event(@RequestParam("start")String start, @RequestParam("end")String end) {
        return newService.queryEvent(start, end);
    }


    @GetMapping("banner/list")
    @ApiOperation(value = "获取横幅数据")
    public BNPageResponse<BannerVo> list() {
        return newService.queryBanner("1");
    }


    @GetMapping("comment/list")
    @ApiOperation(value = "查询评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value= "评论的id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value= "消息面: 1-利好, 2-利空", dataType = "String")
    })
    public BNPageResponse<PostVo> list(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "type", defaultValue = CommonConstant.BTC_BULL) String type,
            @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize){
        return newService.queryCommentPage(id, type, CommonConstant.COMMENT_ISSUE,"TOURIST", pageNum, pageSize);
    }

    @GetMapping("comment/max")
    @ApiOperation(value = "查询点赞最多评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "id", value= "资讯的id", dataType = "String")
    })
    public BNResponse<PostVo> maxLike(@RequestParam(value = "id") String id) {
        return newService.queryCommentMaxLike(id);
    }

}
