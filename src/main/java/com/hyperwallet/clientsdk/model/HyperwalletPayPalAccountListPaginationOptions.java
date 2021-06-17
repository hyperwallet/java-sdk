package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletPayPalAccountListPaginationOptions extends HyperwalletPaginationOptions{

    private HyperwalletPayPalAccount.Type type;
    private HyperwalletPayPalAccount.Status status;
    private Date createdOn;


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

    public HyperwalletPayPalAccount.Type getType() {
        return type;
    }

    public void setType(HyperwalletPayPalAccount.Type type) {
        this.type = type;
    }

    public HyperwalletPayPalAccountListPaginationOptions type(HyperwalletPayPalAccount.Type type) {
        this.type = type;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public HyperwalletPayPalAccountListPaginationOptions createdOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }
}
