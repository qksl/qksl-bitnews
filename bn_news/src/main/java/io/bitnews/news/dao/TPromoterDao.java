package io.bitnews.news.dao;

import io.bitnews.model.news.po.TPromoter;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TPromoterDao extends BaseMapper<TPromoter> {

}