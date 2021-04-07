package io.bitnews.admin.dao.news;

import io.bitnews.model.news.po.TTokenHistory;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface TTokenHistoryDao extends BaseMapper<TTokenHistory> {
    //
    void queryPage(PageQuery query);
}