package com.hyperwallet.clientsdk.model;

public class HyperwalletReceiptPaginationOptions extends HyperwalletPaginationOptions {

    private HyperwalletReceipt.Type type;
    private String currency;

    public HyperwalletReceipt.Type getType() {
        return type;
    }

    public void setType(HyperwalletReceipt.Type type) {
        this.type = type;
    }

    public HyperwalletReceiptPaginationOptions type(HyperwalletReceipt.Type type) {
        this.type = type;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public HyperwalletReceiptPaginationOptions currency(String currency) {
        this.currency = currency;
        return this;
    }
}
