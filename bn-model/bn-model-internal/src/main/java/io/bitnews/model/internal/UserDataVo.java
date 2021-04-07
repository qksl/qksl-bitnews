package io.bitnews.model.internal;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by ywd on 2019/11/1.
 */
@Data
public class UserDataVo implements Serializable {

    /**
     * 新增用户
     */
    Long newIncUser;
    /**
     * 现有用户
     */
    Long nowUser;
    /**
     * 活跃用户
     */
    Long activeUser;
}
