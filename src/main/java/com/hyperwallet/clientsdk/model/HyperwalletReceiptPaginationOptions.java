package com.hyperwallet.clientsdk.model;

public class HyperwalletReceiptPaginationOptions extends HyperwalletPaginationOptions {

    private HyperwalletReceipt.Type type;

    public HyperwalletReceipt.Type getType() {
        return type;
    }

    public void setType(HyperwalletReceipt.Type type) {
        this.type = type;
    }

    public HyperwalletReceiptPaginationOptions type(HyperwalletReceipt.Type type) {
        this.type = type;
        return this;
    }

}
