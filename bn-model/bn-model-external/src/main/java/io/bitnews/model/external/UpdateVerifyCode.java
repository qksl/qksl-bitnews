package io.bitnews.model.external;

import lombok.Data;

@Data
public class UpdateVerifyCode {

    private String type;

    private String mail;

    private String phone;

    private String code;

}