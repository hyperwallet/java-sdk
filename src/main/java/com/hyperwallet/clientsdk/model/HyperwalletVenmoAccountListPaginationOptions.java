package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletVenmoAccountListPaginationOptions extends HyperwalletPaginationOptions{

    private HyperwalletVenmoAccount.Type type;
    private HyperwalletVenmoAccount.Status status;
    private Date createdOn;


    public HyperwalletVenmoAccount.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletVenmoAccount.Status status) {
        this.status = status;
    }

    public HyperwalletVenmoAccountListPaginationOptions status(HyperwalletVenmoAccount.Status status) {
        this.status = status;
        return this;
    }

    public HyperwalletVenmoAccount.Type getType() {
        return type;
    }

    public void setType(HyperwalletVenmoAccount.Type type) {
        this.type = type;
    }

    public HyperwalletVenmoAccountListPaginationOptions type(HyperwalletVenmoAccount.Type type) {
        this.type = type;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public HyperwalletVenmoAccountListPaginationOptions createdOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }
}
