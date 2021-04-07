package io.bienews.common.helper;

import com.fasterxml.jackson.databind.util.BeanUtil;
import io.bitnews.common.model.BNPageResponse;
import org.beetl.sql.core.engine.PageQuery;

import java.lang.reflect.ParameterizedType;
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

    public static <T> BNPageResponse<T> createPage(List<T> list, long pageNum, long pageSize, long total, long totalRow) {
        BNPageResponse<T> rs = new BNPageResponse<>();
        rs.setList(list);
        rs.setPageNumber(pageNum);
        rs.setPageSize(pageSize);
        rs.setTotalPage(total);
        rs.setTotalRow(totalRow);
        return rs;
    }

    public static <T> BNPageResponse<T> createPage(PageQuery<T> pageQuery) {
        BNPageResponse<T> rs = new BNPageResponse<>();
        rs.setList(pageQuery.getList());
        rs = fillPage(rs, pageQuery);
        return rs;
    }

    private static <T> BNPageResponse<T> fillPage(BNPageResponse<T> rs, PageQuery pageQuery) {
        rs.setTotalRow(pageQuery.getTotalRow());
        rs.setPageNumber(Long.valueOf(pageQuery.getPageNumber()));
        rs.setPageSize(Long.valueOf(pageQuery.getPageSize()));
        rs.setTotalPage(Long.valueOf(pageQuery.getTotalPage()));
        return rs;
    }

}
