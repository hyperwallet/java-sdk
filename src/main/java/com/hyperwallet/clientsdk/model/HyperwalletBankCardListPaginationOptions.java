package com.hyperwallet.clientsdk.model;

public class HyperwalletBankCardListPaginationOptions extends HyperwalletPaginationOptions {

    //private HyperwalletBankCard.Type type;

    private HyperwalletBankCard.Status status;

    public HyperwalletBankCard.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletBankCard.Status status) {
        this.status = status;
    }

    public HyperwalletBankCardListPaginationOptions status(HyperwalletBankCard.Status status) {
        this.status = status;
        return this;
    }

}