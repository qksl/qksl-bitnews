package io.bitnews.news.dao;

import io.bitnews.model.news.po.TTasksComplete;
import org.beetl.sql.core.annotatoin.Sql;
import org.beetl.sql.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TTasksCompleteDao extends BaseMapper<TTasksComplete> {
    //

    @Sql(value=" select * FROM t_tasks_complete where task_id=? and user_id=? and TO_DAYS(create_time) = TO_DAYS(NOW())")
    TTasksComplete queryDailyTasks(String taskId, String userId);

    @Sql(value=" select * FROM t_tasks_complete where task_id=? and user_id=?")
    TTasksComplete queryGrowUpTasks(String taskId, String userId);

    @Sql(value=" select * FROM t_tasks_complete where task_id=?")
    List<TTasksComplete> queryByTaskId(String taskId);
}