package io.bitnews.model.internal;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by ywd on 2019/11/27.
 */
@NoArgsConstructor
@Data
public class FearGreedVo {

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
