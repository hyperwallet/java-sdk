package com.hyperwallet.clientsdk.model;

public class HyperwalletWebhookNotificationPaginationOptions extends HyperwalletPaginationOptions {

    private String programToken;
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

    public String getProgramToken() {
        return programToken;
    }

    public void setProgramToken(String programToken) {
        this.programToken = programToken;
    }

    public HyperwalletWebhookNotificationPaginationOptions programToken(String programToken) {
        this.programToken = programToken;
        return this;
    }
}
