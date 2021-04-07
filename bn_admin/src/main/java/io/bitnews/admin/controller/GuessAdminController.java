package io.bitnews.admin.controller;

import io.bienews.common.helper.StringUtil;
import io.bitnews.admin.service.AdminService;
import io.bitnews.admin.service.GuessService;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.constants.UserSdkErrorCode;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.admin.po.TAdmin;
import io.bitnews.model.external.*;
import io.bitnews.model.internal.PromoterTopicVo;
import io.bitnews.model.internal.PromoterVo;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by ywd on 2019/11/1.
 */
@Slf4j
@Api(tags = "竞猜操作接口")
@RestController
@RequestMapping("/v1/admin/guess")
@PreAuthorize("hasAuthority('ADMIN')")
public class GuessAdminController {

    @Autowired
    private GuessService guessService;

    private static final String GUESS_QUERY_ALL = "2";
    private static final String GUESS_QUERY_NO_SETTLEMENT  = "1";
    private static final String GUESS_QUERY_NO_CLOSE = "0";

    @GetMapping("query")
    @ApiOperation(value = "所有竞猜")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "tokenType", value = "代币类别", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "discussionId", value = "资讯id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "竞猜状态0-开盘, 1-封盘, 2-结算", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userId", value = "创建用户", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "大小", dataType = "String")
    })
    public BNPageResponse<GuessInfo> query(@RequestParam(value = "tokenType", required = false) String tokenType,
                                           @RequestParam(value = "discussionId", required = false) String discussionId,
                                           @RequestParam(value = "status", required = false) String status,
                                           @RequestParam(value = "userId", required = false) String userId,
                                           @RequestParam(value = "type", required = false) String type,
                                           @RequestParam(value = CommonConstant.PAGE_NUM, defaultValue =
                                                   CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                           @RequestParam(value = CommonConstant.PAGE_SIZE, defaultValue =
                                                   CommonConstant.PAGE_SIZE_DEFAULT) String pageSize) {
        //查询所有的
        return guessService.query(tokenType, discussionId, status, userId, type, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
    }

    @PostMapping("close")
    @ApiOperation(value = "封盘")
    public BaseResponse close(@RequestBody IdVo idVo) {
        log.info("竞猜封盘：" + idVo.getId());
        return guessService.close(idVo.getId());
    }

    @PostMapping("settlement")
    @ApiOperation(value = "结算处理")
    public BaseResponse settlement(@RequestBody SettlementVo settlementVo) {
        log.info("竞猜结算：" + settlementVo.getId());
        return guessService.settlement(settlementVo.getId(), settlementVo.getWinner());
    }

    @PostMapping("delete")
    @ApiOperation(value = "删除竞猜")
    public BaseResponse delete(@RequestBody IdVo idVo) {
        //「待封盘」、「待开奖」竞猜删除后，相关押注即时反还至用户
        //「已结束」竞猜删除后，已结算押注不变
        log.info("竞猜封盘：" + idVo.getId());
        guessService.deletePromoterTopic(idVo.getId());
        return new BaseResponse();
    }

//    @PostMapping("create")
//    @ApiOperation(value = "创建竞猜第二种类型")
//    public BaseResponse createContract(@RequestBody @ApiParam(name = "竞猜对象", value = "传入json格式", required = true) ContractVo contractVo, Principal principal) {
//        if (null == principal) {
//            return new BNResponse(UserSdkErrorCode.USERID_NULL);
//        }
//        TAdmin tAdmin = adminService.findByName(principal.getName());
//        PromoterVo promoterVo = new PromoterVo();
//        BeanUtils.copyProperties(contractVo, promoterVo);
//        promoterVo.setUserId(tAdmin.getId());
//        return guessService.issue(promoterVo);
//    }

}
