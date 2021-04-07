package io.bitnews.model.news.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.util.Date;


/*
 *
 * gen by beetlsql 2019-12-13
 */
@Table(name = "t_tasks")
@NoArgsConstructor
@Data
public class TTasks {

    /*
    自增ID
    */
    private Long id;
    /*
    任务奖励
    */
    private Integer reward;
    /*
    内容
    */
    private String context;
    /*
    描叙
    */
    private String desc;
    /*
	排序
	*/
    private Integer sortNum;
    /*
    状态：0-正常, 1-关闭
    */
    private String status;
    /*
    类型: 0-每日, 1-成长, 2-运营
    */
    private String type;
    /*
	唯一标识
	*/
    private String uniqueMark;
    /*
    创建时间
    */
    private Date createTime;


}
