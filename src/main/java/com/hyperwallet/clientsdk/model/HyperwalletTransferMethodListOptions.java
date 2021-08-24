package com.hyperwallet.clientsdk.model;

public class HyperwalletTransferMethodListOptions extends HyperwalletPaginationOptions {

    private HyperwalletTransferMethod.Type type;
    private HyperwalletTransferMethod.Status status;

    public HyperwalletTransferMethod.Type getType() {
        return type;
    }

    public void setType(HyperwalletTransferMethod.Type type) {
        this.type = type;
    }

    public HyperwalletTransferMethodListOptions type(HyperwalletTransferMethod.Type type) {
        this.type = type;
        return this;
    }

    public HyperwalletTransferMethod.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletTransferMethod.Status status) {
        this.status = status;
    }

    public HyperwalletTransferMethodListOptions status(HyperwalletTransferMethod.Status status) {
        this.status = status;
        return this;
    }
}