package io.bitnews.common.core.util;

import io.bitnews.common.core.model.BNPageResponse;

import java.util.List;

/**
 * Created by ywd on 2019/7/3.
 */
public class PageUtil {

    public static <T> BNPageResponse<T> genPage(List<T> list) {
        return genPage(list, "0", "10",0);
    }

    public static <T> BNPageResponse<T> genPage(List<T> list, String pageNum, String pageSize, long total) {
        long pn = Long.parseLong(pageNum);
        long ps = Long.parseLong(pageSize);
        BNPageResponse<T> rs = new BNPageResponse<>();
        rs.setList(list);
        rs.setPageNumber(pn);
        rs.setPageSize(ps);
        long tp = total/ps;
        tp = tp>0?tp+1:tp;
        rs.setTotalPage(tp);
        rs.setTotalRow(total);
        return rs;
    }

    public static <T> BNPageResponse<T> createPage(List<T> list, long pageNum, long pageSize, long total) {
        BNPageResponse<T> rs = new BNPageResponse<>();
        rs.setList(list);
        rs.setPageNumber(pageNum);
        rs.setPageSize(pageSize);
        rs.setTotalPage(total);
        rs.setTotalRow(total);
        return rs;
    }
}
