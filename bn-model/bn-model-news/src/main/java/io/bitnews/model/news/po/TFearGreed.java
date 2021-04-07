package io.bitnews.model.news.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.util.Date;


/* 
* 
* gen by beetlsql 2019-11-27
*/
@Table(name="t_fear_greed")
@NoArgsConstructor
@Data
public class TFearGreed {
	
	/*
	自增ID
	*/
	private Long id ;
	/*
	恐慌值
	*/
	private Integer fearValue ;
	/*
	值类型
	*/
	private String valueClassification ;
	/*
	更新时间
	*/
	private Date updateTime ;

}
