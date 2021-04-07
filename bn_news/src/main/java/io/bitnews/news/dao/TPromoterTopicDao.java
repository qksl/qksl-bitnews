package io.bitnews.news.dao;

import io.bitnews.model.news.po.TPromoterTopic;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TPromoterTopicDao extends BaseMapper<TPromoterTopic> {


    @Sql(value=" select * FROM t_promoter_topic where discussion_id=? and create_user_id = ?")
    List<TPromoterTopic> findByDiscussionIdAndUserID(Long disscussionId, Long userId);

    /**
     * 封盘
     *
     * @param id
     */
    @Sql(value = " update t_promoter_topic set status = 1 where id = ? ")
    void close(Long id);

    /**
     * 结算
     *
     * @param id
     */
    @Sql(value = " update t_promoter_topic set status = 2 where id = ? ")
    void settlement(Long id);

    /**
     * 流局
     *
     * @param id
     */
    @Sql(value = " update t_promoter_topic set status = 3 where id = ? ")
    void abandon(Long id);


    /**
     * 分页查询竞猜
     * @return
     */
    void latestByPage(PageQuery query);

    List<TPromoterTopic> queryByCondition(TPromoterTopic tPromoterTopic);
}