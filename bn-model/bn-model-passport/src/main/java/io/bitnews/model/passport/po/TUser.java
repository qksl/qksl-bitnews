package io.bitnews.model.passport.po;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.beetl.sql.core.annotatoin.Table;

import java.io.Serializable;
import java.util.Date;


/*
 *
 * gen by beetlsql 2019-07-03
 */
@Table(name = "t_user")
@NoArgsConstructor
@Data
public class TUser implements Serializable {

    /*
    ID
    */
    private Long id;
    /*
    邮箱
    */
    private String email;
    /*
    手机号
    */
    private String phoneNumber;
    /*
    密码
    */
    private String password;
    /*
    类别：0-普通用户; 1-认证用户
    */
    private String type;
    /*
    类别：1-正常; 2-禁封
    */
    private String status;
    /*
    类别：0-是; 1-不是
    */
    private String firstLogin;
    /*
    用户头像
    */
    private String txPicture;
    /*
	个人签名
	*/
    private String txSignature;
    /*
    用户名
    */
    private String username;
    /*
	昵称
	*/
    private String nickName;
    /*
    微信ID
    */
    private String weixinId;
    /*
    创建时间
    */
    private Date createTime;
    /*
    最近登录时间
    */
    private Date lastLoginTime;

}
