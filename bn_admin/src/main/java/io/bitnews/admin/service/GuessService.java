package io.bitnews.admin.service;

import io.bienews.common.helper.PageUtil;
import io.bienews.common.helper.StringUtil;
import io.bienews.common.helper.exception.CommonException;
import io.bitnews.admin.beanmapper.GuessBeanMapper;
import io.bitnews.admin.beanmapper.NewsBeanMapper;
import io.bitnews.admin.dao.news.TBetsDao;
import io.bitnews.admin.dao.news.TPromoterTopicDao;
import io.bitnews.client.news.NewsServiceClient;
import io.bitnews.common.constants.CommonConstant;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.common.model.BaseResponse;
import io.bitnews.model.external.GuessInfo;
import io.bitnews.model.internal.PromoterTopicVo;
import io.bitnews.model.internal.PromoterVo;
import io.bitnews.model.news.po.TBets;
import io.bitnews.model.news.po.TDiscussion;
import io.bitnews.model.news.po.TPromoterTopic;
import lombok.extern.slf4j.Slf4j;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by ywd on 2019/11/4.
 */
@Service
@Slf4j
public class GuessService {

    @Autowired
    private TPromoterTopicDao tPromoterTopicDao;

    @Autowired
    private TBetsDao betsDao;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private NewsServiceClient newsServiceClient;

    private static final String GUESS_CLOSE= "2";
    private static final String GUESS_FAIL = "3";

    public BNPageResponse<GuessInfo> query(String tokenType, String discussionId, String status, String userId,
                                           String type, Integer pageNum, Integer pageSize) {

        Query<TPromoterTopic> query = tPromoterTopicDao.createLambdaQuery();
        if (StringUtil.isNotEmpty(tokenType)){
            query.andEq("tokenType", tokenType);
        }
        if (StringUtil.isNotEmpty(discussionId)){
            query.andEq("discussionId", discussionId);
        }
        if (StringUtil.isNotEmpty(status)){
            query.andEq("status", status);
        }
        if (StringUtil.isNotEmpty(type)){
            query.andEq("type", type);
        }
        if (StringUtil.isNotEmpty(userId)){
            query.andEq("createUserId", userId);
        }
        PageQuery<TPromoterTopic> queryPage = query.orderBy("create_time desc")
                .page(pageNum, pageSize);
        return PageUtil.createPage(queryPage).map(GuessBeanMapper.INSTANCE::toGuessInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deletePromoterTopic(String id) {
        TPromoterTopic topic = tPromoterTopicDao.single(Long.parseLong(id));
        if (GUESS_CLOSE.equals(topic.getStatus()) || GUESS_FAIL.equals(topic.getStatus())) {
            tPromoterTopicDao.deleteById(topic.getId());
        }else {
            //如果还未结算，则返回token到原始账户
            abandonAndReturnToken(topic.getId());
            tPromoterTopicDao.deleteById(topic.getId());
        }
        //删除参与竞猜的历史信息
        deleteTopicRelated(topic.getId());
    }

    private void deleteTopicRelated(Long id) {
        List<TBets> lists = betsDao.findByPromoterTopicId(id);
        for (TBets tBet : lists) {
            betsDao.deleteById(tBet.getId());
        }
    }

    private void abandonAndReturnToken(Long id) {
        TPromoterTopic topic = tPromoterTopicDao.single(id);
        if (null == topic) {
            throw new CommonException("找不到该竞猜");
        }
        if (CommonConstant.GUESS_STATUS.equals(topic.getStatus()) || CommonConstant.GUESS_STATUS_CLOSE.equals(topic.getStatus())) {
            List<TBets> promoterTopicIds = betsDao.findByPromoterTopicId(id);
            for (TBets tBets : promoterTopicIds) {
                log.info(String.format("删除竞猜%s,返回%s代币: %s", tBets.getPromoterTopicId().toString(),
                        tBets.getUserId().toString(), tBets.getBetsGold().toString()));
                tokenService.newAdd(tBets.getUserId(), tBets.getBetsGold(),CommonConstant.JOIN_INCOME, "竞猜抵押底金返还");
                betsDao.deleteById(tBets.getId());
            }
            topic.setStatus(CommonConstant.GUESS_STATUS_ABANDON);
            tPromoterTopicDao.updateTemplateById(topic);
        }
    }

    public BaseResponse close(String id) {
        return newsServiceClient.closeGuess(id);
    }

    public BaseResponse settlement(String id, String winner) {
        return newsServiceClient.settlementGuess(id, winner);
    }
}
