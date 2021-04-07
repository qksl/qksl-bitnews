package io.bitnews.news.controller;

import io.bienews.common.helper.PageUtil;
import io.bienews.common.helper.StringUtil;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.model.external.News;
import io.bitnews.model.internal.BannerVo;
import io.bitnews.model.internal.DetailsDiscussionVo;
import io.bitnews.model.internal.DiscussionVo;
import io.bitnews.model.internal.PostVo;
import io.bitnews.model.news.po.TDiscussion;
import io.bitnews.news.service.BannerService;
import io.bitnews.news.service.CommentService;
import io.bitnews.news.service.DiscussionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ywd on 2019/7/11.
 */
@Slf4j
@Api("查询模块")
@RestController
@RequestMapping("/v1/user/news")
public class NewsController {

    @Autowired
    private DiscussionService discussionService;

    @Autowired
    private BannerService bannerService;

    @Autowired
    private CommentService commentService;

    @GetMapping("latest")
    @ApiOperation(value = "获取最新资讯")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "tag", value = "标签", defaultValue =
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
        PageQuery<TDiscussion> query = new PageQuery<>();
        query.setPageSize(Long.parseLong(pageSize));
        query.setPageNumber(Long.parseLong(pageNum));
        StringBuffer cacheKey = new StringBuffer(pageNum+pageSize);
        tag = "0".equals(tag)? "":tag;
        if (StringUtil.isNotEmpty(tag)){
            query.setPara("tag", tag);
            cacheKey.append(tag);
        }
        query.setOrderBy("id desc");
        //用户方需要添加状态
        query.setPara("status", CommonConstant.DISCUSSION_ISSUE);
        cacheKey.append(CommonConstant.DISCUSSION_ISSUE);
        return discussionService.queryNewsByPage(query, cacheKey.toString());
    }

    @GetMapping("details")
    @ApiOperation(value = "获取资讯详情页")
    public BNResponse<DetailsDiscussionVo> details(@RequestParam(value = "id") String id) {
        DiscussionVo discussionVo = discussionService.get(Long.valueOf(id));
        List<DiscussionVo> list = discussionService.queryRelated(Long.valueOf(id),discussionVo.getTag());
        List<PostVo> posts = commentService.queryHots(Long.valueOf(id));
        DetailsDiscussionVo vo = new DetailsDiscussionVo();
        vo.setDiscussionVo(discussionVo);
        vo.setLists(list);
        vo.setPosts(posts);
        return new BNResponse<>(vo);
    }

    @GetMapping("event")
    @ApiOperation(value = "重大事件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "start", value= "起始时间", defaultValue =
                    "2019-07-01", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "end", value = "结束时间", defaultValue =
                    "2019-08-01", dataType = "String")
    })
    public BNPageResponse<DiscussionVo> event(@RequestParam("start")String start, @RequestParam("end")String end) {
        List<DiscussionVo> discussionVos = discussionService.queryEventWithStatus(CommonConstant.DISCUSSION_ISSUE, start, end);
        return PageUtil.genPage(discussionVos);
    }


    @GetMapping("banner/list")
    @ApiOperation(value = "获取横幅数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "type", value= "起始时间", defaultValue =
                    "1", dataType = "String")
    })
    public BNPageResponse<BannerVo> list(@RequestParam(value = "type", defaultValue = "1")String type) {
        List<BannerVo> bannerVos = bannerService.queryByType(type);
        return PageUtil.genPage(bannerVos);
    }



}
