package io.bitnews.news.dao;

import io.bitnews.model.news.po.TTasks;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TTasksDao extends BaseMapper<TTasks> {
    //
    void queryByPage(PageQuery query);

    @Sql(value=" select * FROM t_tasks where type=? order by id asc")
    List<TTasks> queryTasks(String type);

    @Sql(value=" select * FROM t_tasks where unique_mark=?")
    TTasks queryTaskByMark(String mark);

}