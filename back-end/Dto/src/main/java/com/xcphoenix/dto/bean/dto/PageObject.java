package com.xcphoenix.dto.bean.dto;

import com.github.pagehelper.PageInfo;
import lombok.Getter;

import java.util.List;

/**
 * @author      xuanc
 * @date        2019/12/3 上午9:53
 * @version     1.0
 */
@Getter
public class PageObject<T> {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 15;

    private int start;
    private int size;
    private long total;
    private boolean hasNextPage;
    private List<T> data;

    public PageObject(PageInfo<T> pageInfo) {
        this.start = pageInfo.getStartRow();
        this.size = pageInfo.getSize();
        this.total = pageInfo.getTotal();
        this.hasNextPage = pageInfo.isHasNextPage();
        this.data = pageInfo.getList();
    }

    public static int properPageSize(int originalSize) {
        return Math.min(Math.max(0, originalSize), MAX_PAGE_SIZE);
    }

    @Override
    public String toString() {
        return "PageObject{" +
                "start=" + start +
                ", size=" + size +
                ", total=" + total +
                ", hasNextPage=" + hasNextPage +
                ", data=" + data +
                '}';
    }
}
