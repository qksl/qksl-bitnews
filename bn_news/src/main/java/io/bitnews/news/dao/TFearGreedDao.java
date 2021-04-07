package io.bitnews.news.dao;

import io.bitnews.model.news.po.TFearGreed;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TFearGreedDao extends BaseMapper<TFearGreed> {

    @Sql(value="SELECT COUNT(*) FROM t_coin_market WHERE TO_DAYS(update_time) = TO_DAYS(?)")
    int toTimeDays(String time);

    @Sql(value="SELECT * FROM t_fear_greed ORDER BY update_time DESC LIMIT ?")
    List<TFearGreed> query(int limit);

    @Sql(value="SELECT * FROM t_fear_greed ORDER BY update_time DESC LIMIT 1")
    TFearGreed lastest();

}