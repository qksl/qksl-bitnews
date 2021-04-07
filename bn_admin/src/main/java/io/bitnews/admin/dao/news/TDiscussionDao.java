package io.bitnews.admin.dao.news;

import io.bitnews.model.news.po.TDiscussion;
import org.beetl.sql.core.annotatoin.Param;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TDiscussionDao extends BaseMapper<TDiscussion> {

    void selectByPage(PageQuery query);

    List<TDiscussion> listEvent(@Param("status") String status, @Param("start") String start, @Param("end") String end);

    @Sql(value=" update t_discussion set status = 3 where id = ? ")
    void cancel(Long id);

    @Sql(value=" update t_discussion set status = 2 where id = ? ")
    void issue(Long id);

    @Sql(value=" update t_discussion set bull_count = bull_count+? where id = ? ")
    void bull(Integer num, Long id);

    @Sql(value=" update t_discussion set bear_count = bear_count+? where id = ? ")
    void bear(Integer num, Long id);

    @Sql(value = "SELECT * FROM t_discussion WHERE id!=? and tag=? ORDER BY update_time DESC LIMIT 4")
    List<TDiscussion> related(Long id, String tag);
}