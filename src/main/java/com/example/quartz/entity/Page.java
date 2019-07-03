package com.example.quartz.entity;

import java.util.List;

public class Page<T> {

    private Integer pageNum;

    private Integer pageSize;

    private Integer count;

    private Integer pageCount;

    private Integer totalCount;

    private List<T> resultList;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;

        if (totalCount % pageSize == 0) {
            this.pageCount = totalCount / pageSize;
        } else {
            this.pageCount = totalCount / pageSize + 1;
        }
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }
}
