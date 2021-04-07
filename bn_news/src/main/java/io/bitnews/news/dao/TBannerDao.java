package io.bitnews.news.dao;

import io.bitnews.model.news.po.TBanner;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TBannerDao extends BaseMapper<TBanner> {

    @Sql(value=" update t_banner set picture_order = ? where id = ? ")
    void updatePictureOrder(Integer order, Long id);

    @Sql(value=" select * FROM t_banner where type = ?")
    List<TBanner> queryByType(String type);

    void queryByPage(PageQuery<TBanner> query);

    @Sql(value=" select count(*) FROM t_banner where type = ?")
    Long count(String type);
}