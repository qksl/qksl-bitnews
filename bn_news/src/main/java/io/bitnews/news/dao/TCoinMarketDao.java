package io.bitnews.news.dao;

import io.bitnews.model.news.po.TCoinMarket;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TCoinMarketDao extends BaseMapper<TCoinMarket> {

    List<TCoinMarket> queryQuotes(@Param("coinCode") String coinCode, @Param("start") String start, @Param("end") String end);

    @Sql(value="SELECT * FROM t_coin_market WHERE coin_code = ? and update_time =(DATE_FORMAT(NOW(), '%Y-%m-%d %H:00:00') - INTERVAL ? " +
            "HOUR)")
    TCoinMarket queryAllMarketCapByTime(String coinCode, int before);
}