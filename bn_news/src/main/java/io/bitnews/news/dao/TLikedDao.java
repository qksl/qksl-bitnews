package io.bitnews.news.dao;

import io.bitnews.model.news.po.TLiked;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TLikedDao extends BaseMapper<TLiked> {

    @Sql(value=" select * FROM t_liked where post_id=? and user_id=?")
    TLiked findByUnique(Long postId, Long userId);

    @Sql(value=" delete FROM t_liked where post_id=? and user_id=?")
    void deleteByPostAndUser(Long postId, Long userId);
}