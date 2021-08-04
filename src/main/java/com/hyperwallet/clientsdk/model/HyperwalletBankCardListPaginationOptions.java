package com.hyperwallet.clientsdk.model;

<<<<<<< HEAD
public class HyperwalletBankCardListPaginationOptions extends HyperwalletPaginationOptions{
    private HyperwalletBankCard.Type type;
=======
public class HyperwalletBankCardListPaginationOptions extends HyperwalletPaginationOptions {

>>>>>>> eeeca2b2e7d3148d6ebed160923ed7f7157002a0
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