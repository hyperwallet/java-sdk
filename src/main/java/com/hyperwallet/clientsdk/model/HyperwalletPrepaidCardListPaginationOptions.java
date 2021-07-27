package com.hyperwallet.clientsdk.model;

public class HyperwalletPrepaidCardListPaginationOptions extends HyperwalletPaginationOptions{

    private HyperwalletPrepaidCard.Status status;

    public HyperwalletPrepaidCard.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletPrepaidCard.Status status) {
        this.status = status;
    }

    public HyperwalletPrepaidCardListPaginationOptions status(HyperwalletPrepaidCard.Status status) {
        this.status = status;
        return this;
    }
}
