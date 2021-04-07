package io.bitnews.common.core.model;

import io.bitnews.common.core.util.Guid;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by ywd on 2019/7/12.
 */
@Data
public class BaseEntity<T> implements Serializable {

    protected String id;

    public BaseEntity() {
        this.id = Guid.generateStrId();
    }
}