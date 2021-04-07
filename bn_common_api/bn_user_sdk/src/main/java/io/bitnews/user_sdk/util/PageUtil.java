package io.bitnews.user_sdk.util;

import io.bitnews.user_sdk.model.BNPageResponse;

import java.util.List;

/**
 * Created by ywd on 2019/7/3.
 */
public class PageUtil<T> {

    public BNPageResponse<T> genPage(List<T> list, String pageNum, String pageSize, long total) {
        long pn = Long.parseLong(pageNum);
        long ps = Long.parseLong(pageSize);
        BNPageResponse<T> rs = new BNPageResponse<>();
        rs.setList(list);
        rs.setPageNumber(pn);
        rs.setPageSize(ps);
        rs.setTotalPage(total/ps+1);
        rs.setTotalRow(total);
        return rs;
    }
}
