package io.bitnews.passport.controller;

import io.bienews.common.helper.exception.CommonException;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.model.external.GuessInfo;
import io.bitnews.model.internal.PromoterTopicVo;
import io.bitnews.model.passport.po.TUser;
import io.bitnews.passport.service.GuessService;
import io.bitnews.passport.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * Created by ywd on 2019/8/27.
 */
@Slf4j
@Api(tags = "竞猜获取接口【以下方法都不需要权限即可调用】")
@RestController
@RequestMapping("/v1/passport/guess/query")
public class GuessQueryController {

    @Autowired
    private GuessService guessService;

    @Autowired
    private UserService userService;

    @GetMapping("latest")
    @ApiOperation(value = "最新竞猜")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "tokenType", value = "代币类别", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "discussionId", value = "资讯id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "竞猜状态", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "竞猜状态", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "大小", dataType = "String")
    })
    public BNPageResponse<GuessInfo> latest(@RequestParam(value = "tokenType", required = false) String tokenType,
                                            @RequestParam(value = "discussionId", required = false) String discussionId,
                                            @RequestParam(value = "status", required = false) String status,
                                            @RequestParam(value = "userId", required = false) String userId,
                                            @RequestParam(value = "type", required = false) String type,
                                            @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue =
                                                    CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                            @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue =
                                                    CommonConstant.PAGE_SIZE_DEFAULT) String pageSize) {
        return guessService.queryLatest(tokenType, discussionId, status, userId, type, pageNum, pageSize);
    }


    @GetMapping("topic")
    @ApiOperation(value = "查询发起的竞猜")
    public BNPageResponse<GuessInfo> queryTopicByMyself(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
            @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize) {
        TUser tUser = userService.findById(userId);
        if (null == tUser){
            throw new CommonException("找不到id");
        }
        BNPageResponse<GuessInfo> rs = guessService.queryTopic(tUser.getId().toString(), pageNum, pageSize);
        return rs;
    }


}
