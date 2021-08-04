package com.hyperwallet.clientsdk.model;

public class HyperwalletPayPalAccountListPaginationOptions extends HyperwalletPaginationOptions {

    private HyperwalletPayPalAccount.Status status;

    public HyperwalletPayPalAccount.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletPayPalAccount.Status status) {
        this.status = status;
    }

    public HyperwalletPayPalAccountListPaginationOptions status(HyperwalletPayPalAccount.Status status) {
        this.status = status;
        return this;
    }
}
