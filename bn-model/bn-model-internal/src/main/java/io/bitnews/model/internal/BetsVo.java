package io.bitnews.model.internal;

import io.bitnews.model.external.JoinVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ywd on 2019/7/30.
 */
@NoArgsConstructor
@Data
public class BetsVo extends JoinVo {
    /*
    参与用户ID
    */
    protected Long userId;


}
