package io.bitnews.model.admin.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.util.Date;


/*
 *
 * gen by beetlsql 2019-07-03
 */
@Table(name = "t_admin")
@NoArgsConstructor
@Data
public class TAdmin {

    /*
    自增ID
    */
    private Long id;
    /*
    密码
    */
    private String password;
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
