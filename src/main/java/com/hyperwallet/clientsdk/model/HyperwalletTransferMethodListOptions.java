package com.hyperwallet.clientsdk.model;

public class HyperwalletTransferMethodListOptions extends HyperwalletPaginationOptions {

    private String type;
    private String status;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HyperwalletTransferMethodListOptions type(String type) {
        this.type = type;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HyperwalletTransferMethodListOptions status(String status) {
        this.status = status;
        return this;
    }
}
