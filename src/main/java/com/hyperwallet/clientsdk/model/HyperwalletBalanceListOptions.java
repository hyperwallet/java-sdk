package com.hyperwallet.clientsdk.model;

public class HyperwalletBalanceListOptions {

    public String currency;
    public String sortBy;
    public Integer offset;
    public Integer limit;

    public String getCurrency() {
        return currency;
    }

    public HyperwalletBalanceListOptions setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getSortBy() {
        return sortBy;
    }

    public HyperwalletBalanceListOptions setSortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    public Integer getOffset() {
        return offset;
    }

    public HyperwalletBalanceListOptions setOffset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public HyperwalletBalanceListOptions setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }
}
