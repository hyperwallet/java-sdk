package com.hyperwallet.clientsdk.model;

public class HyperwalletWebhookNotificationPaginationOptions extends HyperwalletPaginationOptions {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HyperwalletWebhookNotificationPaginationOptions type(String type) {
        this.type = type;
        return this;
    }
}
