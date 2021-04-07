package io.bitnews.news.service;

import io.bienews.common.helper.PageUtil;
import io.bitnews.common.model.BNPageResponse;
import io.bitnews.model.news.po.TBets;
import io.bitnews.model.news.po.TPromoterTopic;
import io.bitnews.model.show.GuessRecord;
import io.bitnews.news.dao.TBetsDao;
import io.bitnews.news.dao.TPromoterTopicDao;
import org.beetl.sql.core.engine.PageQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/8/27.
 */
@Service
@CacheConfig(cacheNames = "bets")
public class BetsService {

    @Autowired
    private TBetsDao betsDao;

    @Autowired
    private PromoterService promoterService;


    /**
     * 查询当前用户的竞猜历史
     * @return
     */
    public BNPageResponse<GuessRecord> queryPage(PageQuery<TBets> query) {
        betsDao.queryPage(query);
        List<TBets> tList = query.getList();
        List<GuessRecord> vList = transform(tList);
        return PageUtil.createPage(vList,
                query.getPageNumber(), query.getPageSize(), query.getTotalPage(),query.getTotalRow());
    }

    private List<GuessRecord> transform(List<TBets> tBetsList) {
        return tBetsList.stream().map(tb -> {
            GuessRecord gr = new GuessRecord();
            BeanUtils.copyProperties(tb, gr);
            gr.setGuessWinner(tb.getBets());
            TPromoterTopic tPromoterTopic = promoterService.findByPromoterTopicId(tb.getPromoterTopicId());
            gr.setStatus(tPromoterTopic.getStatus());
            gr.setTopic(tPromoterTopic.getTopic());
            return gr;
        }).collect(Collectors.toList());
    }

    public BigDecimal sumByYes(Long promoterTopicId) {
        return betsDao.sumByYes(promoterTopicId);
    }

    public BigDecimal sumByNo(Long promoterTopicId) {
        return betsDao.sumByNo(promoterTopicId);
    }

    public BigDecimal queryBetsGold(Long createUserId, Long promoterTopicId) {
        return betsDao.queryBetsGold(createUserId, promoterTopicId);
    }

    public List<TBets> findNoByPromoterTopicId(Long promoterTopicId) {
        return betsDao.findNoByPromoterTopicId(promoterTopicId);
    }

    public List<TBets> findYesByPromoterTopicId(Long promoterTopicId) {
        return betsDao.findYesByPromoterTopicId(promoterTopicId);
    }

    public Integer countBetsNoNumber(Long promoterTopicId) {
        return betsDao.countBetsNoNumber(promoterTopicId);
    }

    public Integer countBetsYesNumber(Long promoterTopicId) {
        return betsDao.countBetsYesNumber(promoterTopicId);
    }

    public List<TBets> findByPromoterTopicId(Long promoterTopicId) {
        return betsDao.findByPromoterTopicId(promoterTopicId);
    }

    public List<TBets> findByUserIdToday(String userId) {
        return betsDao.findByUserIdToday(userId);
    }

    public void updateById(TBets tBets) {
        betsDao.updateById(tBets);
    }

    public void insert(TBets tBets) {
        betsDao.insert(tBets);
    }

    public void deleteById(Long promoterTopicId) {
        betsDao.deleteById(promoterTopicId);
    }

    public BigDecimal findByPromoterTopicIdAndBets(Long promoterTopicId, String oppose) {
        return betsDao.findByPromoterTopicIdAndBets(promoterTopicId, oppose);
    }

    public List<TBets> findListByPromoterTopicIdAndBets(Long promoterTopicId, String winner) {
        return betsDao.findListByPromoterTopicIdAndBets(promoterTopicId, winner);
    }


}
