package com.sd.lawyer.beans;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * 分页请求参数
 *
 * @author 肖文杰 https://github.com/xwjie/
 */
public class PageReq implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 当前页
     */
    private int page = 1;
    /**
     * 每页显示记录数
     */
    private int showCount = 10;
    /**
     * 排序字段
     */
    private String sortField = "";
    /**
     * 排序逻辑：增序、降序
     */
    private String sort = "";
    /**
     * 关键词
     */
    private String keyword = "";

    public PageReq() {
        super();
    }

    public PageReq(int page, int showCount) {
        this.page = page;
        this.showCount = showCount;
    }

    public PageReq(int page, int showCount, String sortField, String sort, String keyword) {
        super();
        this.page = page;
        this.showCount = showCount;
        this.sortField = sortField;
        this.sort = sort;
        this.keyword = keyword;
    }

    public PageReq getPageable() {
        return new PageReq(page, showCount, sortField, sort, keyword);
    }

    public Pageable toPageable() {
        // pageable里面是从第0页开始的。
        Pageable pageable = null;

        if (StringUtils.isEmpty(sortField)) {
            pageable = new PageRequest(page - 1, showCount);
        } else {
            pageable = new PageRequest(page - 1, showCount,
                    sort.toLowerCase().startsWith("desc") ? Direction.DESC
                            : Direction.ASC,
                    sortField);
        }

        return pageable;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getShowCount() {
        return showCount;
    }

    public void setShowCount(int showCount) {
        this.showCount = showCount;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", showCount=" + showCount +
                ", sortField='" + sortField + '\'' +
                ", sort='" + sort + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
