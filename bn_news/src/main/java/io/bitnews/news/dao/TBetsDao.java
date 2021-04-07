package io.bitnews.news.dao;

import io.bitnews.model.news.po.TBets;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TBetsDao extends BaseMapper<TBets> {

    @Sql(value=" select sum(bets_gold) FROM t_bets where promoter_topic_id=? and bets = 0")
    BigDecimal sumByYes(Long promoterTopicId);

    @Sql(value=" select sum(bets_gold) FROM t_bets where promoter_topic_id=? and bets = 1")
    BigDecimal sumByNo(Long promoterTopicId);

    @Sql(value=" select bets_gold FROM t_bets where user_id=? and promoter_topic_id=?")
    BigDecimal queryBetsGold(Long userId, Long promoterTopicId);

    @Sql(value=" select * FROM t_bets where promoter_topic_id=? and bets= 0")
    List<TBets> findYesByPromoterTopicId(Long promoterTopicId);

    @Sql(value=" select * FROM t_bets where promoter_topic_id=? and bets= 1")
    List<TBets> findNoByPromoterTopicId(Long promoterTopicId);

    @Sql(value=" select sum(bets_gold) FROM t_bets where promoter_topic_id=? and bets= ?")
    BigDecimal findByPromoterTopicIdAndBets(Long promoterTopicId, String bets);

    @Sql(value=" select * FROM t_bets where promoter_topic_id=? and bets= ?")
    List<TBets> findListByPromoterTopicIdAndBets(Long promoterTopicId, String bets);

    @Sql(value=" select * FROM t_bets where promoter_topic_id=?")
    List<TBets> findByPromoterTopicId(Long promoterTopicId);

    @Sql(value=" select * FROM t_bets where user_id=? and TO_DAYS(create_time) = TO_DAYS(NOW())")
    List<TBets> findByUserIdToday(String userId);

    void queryPage(PageQuery query);

    @Sql(value="select count(id) from t_bets where promoter_topic_id=? and bets=0")
    Integer countBetsYesNumber(Long promoterTopicId);

    @Sql(value="select count(id) from t_bets where promoter_topic_id=? and bets=1")
    Integer countBetsNoNumber(Long promoterTopicId);

}