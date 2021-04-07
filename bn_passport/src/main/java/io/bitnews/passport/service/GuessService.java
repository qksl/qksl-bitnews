package io.bitnews.passport.service;

import io.bitnews.client.news.NewsServiceClient;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BNResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.GuessInfo;
import io.bitnews.model.internal.BetsVo;
import io.bitnews.model.internal.PromoterTopicVo;
import io.bitnews.model.internal.PromoterVo;
import io.bitnews.model.passport.po.TUser;
import io.bitnews.model.show.GuessRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/8/27.
 */
@Service
public class GuessService {

    @Autowired
    private NewsServiceClient newsServiceClient;

    @Autowired
    private UserService userService;

    public BNPageResponse<GuessInfo> queryLatest(String tokenType, String discussionId, String status, String userId,
                                                 String type, String pageNum, String pageSize) {
        BNPageResponse<GuessInfo> response = newsServiceClient.latest(tokenType, discussionId, status, userId, type,
                pageNum, pageSize);

        return addUserProperties(response);
    }


    public BNPageResponse<GuessInfo> queryTopic(String userId, String pageNum, String pageSize) {
        BNPageResponse<GuessInfo> response = newsServiceClient.queryTopicByMyself(userId, pageNum,
                pageSize);
        return addUserProperties(response);
    }

    private BNPageResponse<GuessInfo> addUserProperties(BNPageResponse<GuessInfo> response) {
        if (response.isToast()) {
            List<GuessInfo> list = response.getList();
            List<GuessInfo> collect = list.stream().map(guessInfo -> {
                Long createUserId = guessInfo.getCreateUserId();
                TUser tUser = userService.findById(createUserId.toString());
                guessInfo.setCreateUserName(tUser.getNickName());
                guessInfo.setCreateUserImage(tUser.getTxPicture());
                return guessInfo;
            }).collect(Collectors.toList());
            response.setList(collect);
        }
        return response;
    }

    public BNPageResponse<GuessRecord> queryBets(String userId, String pageNum, String pageSize) {
        return newsServiceClient.queryBetsByMyself(userId, pageNum, pageSize);
    }

    public BaseResponse launch(PromoterTopicVo promoterTopicVo) {
        return newsServiceClient.launch(promoterTopicVo);
    }

    public BaseResponse contractLaunch(PromoterTopicVo promoterTopicVo) {
        return newsServiceClient.contractLaunch(promoterTopicVo);
    }

    public BaseResponse join(BetsVo betsVo) {
        return newsServiceClient.join(betsVo);
    }

    public BaseResponse createContract(PromoterVo promoterVo) {
        return newsServiceClient.createContract(promoterVo);
    }
}
