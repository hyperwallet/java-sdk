package com.hyperwallet.clientsdk.model;

public class HyperwalletWebhookNotificationPaginationOptions extends HyperwalletPaginationOptions {

    private String type;
    private String programToken;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProgramToken() {
        return programToken;
    }

    public void setProgramToken(String programToken) {
        this.programToken = programToken;
    }

    public HyperwalletWebhookNotificationPaginationOptions type(String type) {
        this.type = type;
        return this;
    }

    public HyperwalletWebhookNotificationPaginationOptions programToken(String programToken) {
        this.programToken = type;
        return this;
    }
}
