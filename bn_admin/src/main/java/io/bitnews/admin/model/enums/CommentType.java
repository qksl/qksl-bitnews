package io.bitnews.admin.model.enums;

import lombok.Getter;

/**
 * Created by ywd on 2020/5/25.
 */
@Getter
public enum CommentType {

    Bull("1", "利好"),
    Bear("2", "利空");

    private String code;
    private String name;

    CommentType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
