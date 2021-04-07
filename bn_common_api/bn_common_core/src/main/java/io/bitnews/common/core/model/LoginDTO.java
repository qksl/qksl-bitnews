package io.bitnews.common.core.model;

import lombok.Data;

import java.util.Date;

@Data
public class LoginDTO {

    /*
	ID
	*/
    private Long id ;
    /*
    邮箱
    */
    private String email ;
    /*
    类别：0-普通用户; 1-认证用户
    */
    private String type ;
    /*
    用户名
    */
    private String username ;
    /*
    微信ID
    */
    private String weixinId ;
    /*
    创建时间
    */
    private Date createTime ;
    /*
    最近登录时间
    */
    private Date lastLoginTime ;

    private JWT token;
}