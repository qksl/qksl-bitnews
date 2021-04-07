package io.bitnews.model.internal;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ywd on 2019/7/11.
 */
@Data
public class UserVo implements Serializable {

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
	类别：1-正常; 2-禁封
	*/
    private String status;
    /*
    类别：0-普通用户; 1-认证用户
    */
    private String type;
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

    /*
     * token
     */
    private BigDecimal token;
}
