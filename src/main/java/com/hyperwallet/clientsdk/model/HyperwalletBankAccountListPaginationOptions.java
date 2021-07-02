package com.hyperwallet.clientsdk.model;

public class HyperwalletBankAccountListPaginationOptions extends HyperwalletPaginationOptions{

    private HyperwalletBankAccount.Type type;
    private HyperwalletBankAccount.Status status;
    private HyperwalletBankAccount.Status transition;

    public HyperwalletBankAccount.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletBankAccount.Status status) {
        this.status = status;
    }

    public HyperwalletBankAccountListPaginationOptions status(HyperwalletBankAccount.Status status) {
        this.status = status;
        return this;
    }

    public HyperwalletBankAccount.Status getTransition() {
        return transition;
    }

    public void setTransition(HyperwalletBankAccount.Status transition) {
        this.transition = transition;
    }

    public HyperwalletBankAccountListPaginationOptions transition(HyperwalletBankAccount.Status transition) {
        this.transition = transition;
        return this;
    }

    public HyperwalletBankAccount.Type getType() {
        return type;
    }

    public void setType(HyperwalletBankAccount.Type type) {
        this.type = type;
    }

    public HyperwalletBankAccountListPaginationOptions type(HyperwalletBankAccount.Type type) {
        this.type = type;
        return this;
    }
}
