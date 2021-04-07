package io.bitnews.model.external;

import lombok.Data;

/**
 * Created by ywd on 2020/3/1.
 */
@Data
public class MobileRegistry {
    String mobile;
    String code;
    String password;
    boolean isSetPassword;
}
