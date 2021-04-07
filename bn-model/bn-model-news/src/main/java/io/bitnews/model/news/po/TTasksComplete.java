package io.bitnews.model.news.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.util.Date;


/*
 *
 * gen by beetlsql 2019-12-13
 */
@Table(name = "t_tasks_complete")
@NoArgsConstructor
@Data
public class TTasksComplete {

    /*
    自增ID
    */
    private Long id;
    /*
    任务ID
    */
    private Long taskId;
    /*
    用户ID
    */
    private Long userId;
    /*
    创建时间
    */
    private Date createTime;

}
