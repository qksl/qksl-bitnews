package io.bitnews.common.core.model;

/**
 * Created by ywd on 2019/8/26.
 */
public class PageHelper {

    private int pageNum;
    private int pageSize;

    public PageHelper(String pageNum, String pageSize) {
        this.pageNum = Integer.parseInt(pageNum);
        this.pageSize = Integer.parseInt(pageSize);
    }

    public int getStart() {
        return pageNum * pageSize;
    }

    public int getOffset() {
        return pageNum * pageSize + pageSize;
    }
}
