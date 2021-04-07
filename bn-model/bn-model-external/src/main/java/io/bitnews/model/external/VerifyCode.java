package io.bitnews.model.external;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by ywd on 2020/3/2.
 */
@Data
public class VerifyCode implements Serializable {

    private String username;
    private String code;
    private int count;
    private long expire;
}
