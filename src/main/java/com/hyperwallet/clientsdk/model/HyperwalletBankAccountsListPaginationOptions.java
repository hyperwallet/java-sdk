package com.hyperwallet.clientsdk.model;

public class HyperwalletBankAccountsListPaginationOptions extends HyperwalletPaginationOptions{

    private HyperwalletBankAccount.Type type;
    private HyperwalletBankAccount.Status status;


    public HyperwalletBankAccount.Type getType() {
        return type;
    }

    public void setType(HyperwalletBankAccount.Type type) {
        this.type = type;
    }

    public HyperwalletBankAccountsListPaginationOptions type(HyperwalletBankAccount.Type type) {
        this.type = type;
        return this;
    }


    public HyperwalletBankAccount.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletBankAccount.Status status) {
        this.status = status;
    }

    public HyperwalletBankAccountsListPaginationOptions status(HyperwalletBankAccount.Status status) {
        this.status = status;
        return this;
    }

}
