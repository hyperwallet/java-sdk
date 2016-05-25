package com.hyperwallet.clientsdk.model;

public class HyperwalletBalanceListOptions {

    private String currency;
    private String sortBy;
    private Integer offset;
    private Integer limit;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public HyperwalletBalanceListOptions currency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public HyperwalletBalanceListOptions sortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public HyperwalletBalanceListOptions offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public HyperwalletBalanceListOptions limit(Integer limit) {
        this.limit = limit;
        return this;
    }
}
