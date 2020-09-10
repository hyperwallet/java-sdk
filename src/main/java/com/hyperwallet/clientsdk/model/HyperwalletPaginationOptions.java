package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletPaginationOptions {

    private Date createdBefore;
    private Date createdAfter;
    private String sortBy;
    private Integer offset;
    private Integer limit;

    public Date getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(Date createdBefore) {
        this.createdBefore = createdBefore;
    }

    public HyperwalletPaginationOptions createdBefore(Date createdBefore) {
        this.createdBefore = createdBefore;
        return this;
    }

    public Date getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(Date createdAfter) {
        this.createdAfter = createdAfter;
    }

    public HyperwalletPaginationOptions createdAfter(Date createdAfter) {
        this.createdAfter = createdAfter;
        return this;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public HyperwalletPaginationOptions sortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public HyperwalletPaginationOptions offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public HyperwalletPaginationOptions limit(Integer limit) {
        this.limit = limit;
        return this;
    }
}
