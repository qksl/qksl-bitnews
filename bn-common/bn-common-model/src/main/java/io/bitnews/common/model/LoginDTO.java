package io.bitnews.common.model;

import lombok.Data;

import java.util.Date;

@Data
public class LoginDTO {

    /*
	ID
	*/
    private Long id ;
    /*
    类别：0-普通用户; 1-认证用户
    */
    private String type ;
    /*
    类别：0-未设置密码; 1-正常; 2-禁封
    */
    private String status;
    /*
    用户名
    */
    private String username ;
    private String nickName;
    /*
    创建时间
    */
    private Date createTime ;

    private boolean isFirst;
    /*
    最近登录时间
    */
    private Date lastLoginTime ;

    private JWT token;
}