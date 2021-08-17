package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.model.HyperwalletVenmoAccount.Status;
import com.hyperwallet.clientsdk.model.HyperwalletVenmoAccount.Type;

public class HyperwalletVenmoAccountListPaginationOptions extends HyperwalletPaginationOptions {

    private HyperwalletVenmoAccount.Status status;
    private HyperwalletVenmoAccount.Type type;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public HyperwalletVenmoAccountListPaginationOptions status(HyperwalletVenmoAccount.Status status) {
        this.status = status;
        return this;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public HyperwalletVenmoAccountListPaginationOptions type(HyperwalletVenmoAccount.Type type) {
        this.type = type;
        return this;
    }
}
