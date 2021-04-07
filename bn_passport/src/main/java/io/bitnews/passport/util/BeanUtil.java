package io.bitnews.passport.util;

import io.bitnews.common.model.BNPageResponse;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ywd on 2019/11/28.
 */
public class BeanUtil {

    public static <T, E> BNPageResponse<E> castPage(BNPageResponse<T> re, Class<E> ce)  {
        List<E> collect = null;
        try {
            E e = ce.newInstance();
            collect = re.getList().stream().map(sc -> {
                BeanUtils.copyProperties(sc, e);
                return e;
            }).collect(Collectors.toList());
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        BNPageResponse<E> rs = new BNPageResponse<>();
        BeanUtils.copyProperties(re, rs);
        rs.setList(collect);
        return rs;
    }
}
