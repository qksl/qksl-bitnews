package io.bitnews.admin.controller;

import io.bienews.common.helper.StringUtil;
import io.bitnews.admin.service.AdminService;
import io.bitnews.admin.service.NewsService;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.framework.file.AliyunOosFileStorage;
import io.bitnews.model.admin.po.TAdmin;
import io.bitnews.model.external.IdVo;
import io.bitnews.model.internal.DiscussionVo;
import io.bitnews.model.news.po.TDiscussion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by ywd on 2019/7/23.
 */
@Slf4j
@Api(tags = "管理员资讯操作接口")
@RestController
@RequestMapping("/v1/admin/news")
@PreAuthorize("hasAuthority('ADMIN')")
public class NewsAdminController {

    @Autowired
    private NewsService newsService;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private AliyunOosFileStorage aliyunOosFileStorage;

    @Autowired
    private AdminService adminService;

    @PostMapping("create")
    @ApiOperation(value = "创建或者编辑资讯")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "title", value = "标题", dataType = "String"),
            @ApiImplicitParam(paramType = "form", name = "content", value = "内容", dataType = "String"),
            @ApiImplicitParam(paramType = "form", name = "summary", value = "摘要", dataType = "String"),
            @ApiImplicitParam(paramType = "form", name = "eventFlag", value = "重大事件：1:是, 2:否", dataType = "String"),
            @ApiImplicitParam(paramType = "form", name = "eventTime", value = "事件日期：格式如2019-01-01", dataType =
                    "String"),
            @ApiImplicitParam(paramType = "form", name = "source", value = "来源", dataType = "String"),
            @ApiImplicitParam(paramType = "form", name = "tag", value = "标识：1-政策, 2-区块链,3-交易所", dataType = "String")}
    )
    public BaseResponse create(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("summary") String summary,
            @RequestParam("keywords") String keywords,
            @RequestParam("eventFlag") String eventFlag,
            @RequestParam(value = "eventTime", required = false) String eventTime,
            @RequestParam("source") String source,
            @RequestParam("tag") String tag,
            @RequestParam("image") MultipartFile image, Principal principal) {
        String name = principal.getName();
        TAdmin tAdmin = adminService.findByName(name);
        if (null == tAdmin) {
            return new BaseResponse(UserSdkErrorCode.USER_SERVER_LOGIN_TOKEN_INVALID);
        }
        TDiscussion tDiscussion = new TDiscussion();
        if (image != null) {
            //上传图片
            String originalFilename = image.getOriginalFilename();
            InputStream inputStream = null;
            try {
                inputStream = image.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (inputStream != null) {
                aliyunOosFileStorage.store(inputStream, originalFilename);
            } else {
                log.warn("上传图片失败");
            }
            tDiscussion.setPicturePath(aliyunOosFileStorage.getFileProperties().getGetUrl() + CommonConstant.SLASH + originalFilename);
        }

        tDiscussion.setAdminId(tAdmin.getId());
        tDiscussion.setTitle(title);
        tDiscussion.setContent(content);
        tDiscussion.setEventFlag(eventFlag);
        if (CommonConstant.NEWS_IS_EVENT.equals(eventFlag)) {
            LocalDate parse = LocalDate.parse(eventTime, dateTimeFormatter);
            tDiscussion.setEventTime(Date.from(parse.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        tDiscussion.setSource(source);
        tDiscussion.setTag(tag);
        tDiscussion.setStatus(CommonConstant.DISCUSSION_CREATE);

        tDiscussion.setBullCount(0);
        tDiscussion.setBearCount(0);
        tDiscussion.setSummary(summary);
        tDiscussion.setKeywords(keywords);
        if (StringUtil.isEmpty(id)) {
            newsService.createDiscussion(tDiscussion);
        }else {
            newsService.update(Long.parseLong(id), tDiscussion);
        }

        return new BaseResponse();
    }

    @PostMapping("issue")
    @ApiOperation(value = "发布资讯")
    public BaseResponse issue(@RequestBody IdVo vo) {
        newsService.issueDiscussion(Long.parseLong(vo.getId()));
        return new BaseResponse();
    }

    @PostMapping("del")
    @ApiOperation(value = "删除资讯")
    public BaseResponse delete(@RequestBody IdVo vo) {
        newsService.delDiscussion(Long.parseLong(vo.getId()));
        return new BaseResponse();
    }

    @PostMapping("cancel")
    @ApiOperation(value = "删除资讯")
    public BaseResponse cancel(@RequestBody IdVo vo) {
        newsService.cancelDiscussion(Long.parseLong(vo.getId()));
        return new BaseResponse();
    }

    @GetMapping("latest")
    @ApiOperation(value = "获取最新资讯")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "tag", value = "标签 标识：0-最新，1-政策, 2-区块链,3-交易所", defaultValue =
                    "0", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue =
                    CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue =
                    CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String")}
    )
    public BNPageResponse<DiscussionVo> latestNews(
            @RequestParam(value = "tag", defaultValue = "0") String tag,
            @RequestParam(value = CommonConstant.PAGE_NUM) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE) String pageSize) {
        return newsService.listDiscussion(tag, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
    }

    @GetMapping("event")
    @ApiOperation(value = "重大事件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "start", value = "起始时间", defaultValue =
                    "2019-07-01", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "end", value = "结束时间", defaultValue =
                    "2019-08-01", dataType = "String")
    })
    public BNResponse<List<DiscussionVo>> event(@RequestParam("start") String start, @RequestParam("end") String end) {
        List<DiscussionVo> discussions = newsService.queryEvent(start, end);
        return new BNResponse<>(discussions);
    }

}
