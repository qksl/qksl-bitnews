package io.bitnews.common.core.util;

import java.math.BigDecimal;

/**
 * Created by ywd on 2019/10/29.
 */
public class BigDecimalUtil {

    public static boolean isZero(BigDecimal bd) {
        if (null == bd || bd.compareTo(BigDecimal.ZERO) == 0) {
            return true;
        }
        return false;
    }

    public static boolean positive(BigDecimal bd) {
        if (bd.compareTo(BigDecimal.ZERO) == 1) {
            return true;
        }
        return false;
    }
}
