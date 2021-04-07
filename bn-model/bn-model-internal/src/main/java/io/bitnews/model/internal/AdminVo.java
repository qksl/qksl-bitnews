package io.bitnews.model.internal;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ywd on 2019/7/3.
 */
@Data
public class AdminVo implements Serializable {

    /*
	自增ID
	*/
    private Long id;

    /*
	用户名
	*/
    private String username;
    /*
    创建时间
    */
    private Date createTime;
    /*
    最近登录时间
    */
    private Date lastLoginTime;
}
