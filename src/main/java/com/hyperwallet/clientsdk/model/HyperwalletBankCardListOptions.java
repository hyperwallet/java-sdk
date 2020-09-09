package com.hyperwallet.clientsdk.model;

public class HyperwalletBankCardListOptions  extends HyperwalletPaginationOptions {

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String currency) {
        this.status = status;
    }

    public HyperwalletBankCardListOptions status(String status) {
        this.status = status;
        return this;
    }

}
