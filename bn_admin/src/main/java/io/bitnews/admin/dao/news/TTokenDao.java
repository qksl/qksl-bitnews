package io.bitnews.admin.dao.news;

import io.bitnews.model.news.po.TToken;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TTokenDao extends BaseMapper<TToken> {
    //
    @Sql(value=" select * FROM t_token where user_id=?")
    TToken findByUserId(Long userId);

    @Sql(value=" update t_token set token=token+? where user_id=?")
    void addToken(BigDecimal token, Long userId);

    @Sql(value=" update t_token set token=token-? where user_id=?")
    void subToken(BigDecimal token, Long userId);
}