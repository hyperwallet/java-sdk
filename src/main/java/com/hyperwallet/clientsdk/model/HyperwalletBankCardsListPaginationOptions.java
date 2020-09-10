package com.hyperwallet.clientsdk.model;

public class HyperwalletBankCardsListPaginationOptions extends HyperwalletPaginationOptions{

    private HyperwalletTransferMethod.Status status;

    public HyperwalletTransferMethod.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletTransferMethod.Status status) {
        this.status = status;
    }

    public HyperwalletBankCardsListPaginationOptions status(HyperwalletTransferMethod.Status status) {
        this.status = status;
        return this;
    }

}
