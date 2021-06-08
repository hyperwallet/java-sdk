package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletBankCardListPaginationOptions extends HyperwalletPaginationOptions{

    private HyperwalletBankCard.Type type;
    private HyperwalletBankCard.Status status;
    private Date createdOn;


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

    public HyperwalletBankCard.Type getType() {
        return type;
    }

    public void setType(HyperwalletBankCard.Type type) {
        this.type = type;
    }

    public HyperwalletBankCardListPaginationOptions type(HyperwalletBankCard.Type type) {
        this.type = type;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public HyperwalletBankCardListPaginationOptions createdOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }
}
