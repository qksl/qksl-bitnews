package io.bitnews.admin.dao.news;

import io.bitnews.model.news.po.TPost;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TPostDao extends BaseMapper<TPost> {

    TPost findMostLike(@Param("discussion_id") Long discussion_id);

    @Sql(value=" select liked_sum FROM t_post where id = ?")
    Integer findLikeById(Long id);

    @Sql(value=" update t_post set liked_sum = liked_sum + ? where id = ? ")
    void updateLiked(Integer num, Long id);

    @Sql(value = " SELECT * FROM t_post WHERE discussion_id=? ORDER BY liked_sum DESC LIMIT 5 ")
    List<TPost> hots(Long discussionId);

    void queryByPage(PageQuery<TPost> query);
}