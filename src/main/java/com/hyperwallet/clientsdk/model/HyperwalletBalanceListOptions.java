package com.hyperwallet.clientsdk.model;

public class HyperwalletBalanceListOptions extends HyperwalletPaginationOptions {

    public String currency;

    public String getCurrency() {
        return currency;
    }

    public HyperwalletBalanceListOptions setCurrency(String currency) {
        this.currency = currency;
        return this;
    }
}
