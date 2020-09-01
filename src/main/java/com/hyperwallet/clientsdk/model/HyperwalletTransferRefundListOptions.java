package com.hyperwallet.clientsdk.model;

public class HyperwalletTransferRefundListOptions extends HyperwalletPaginationOptions {

    private String clientRefundId;
    private String sourceToken;
    private String status;

    public String getClientRefundId() {
        return clientRefundId;
    }

    public void setClientRefundId(String clientRefundId) {
        this.clientRefundId = clientRefundId;
    }

    public HyperwalletTransferRefundListOptions clientRefundId(String clientRefundId) {
        this.clientRefundId = clientRefundId;
        return this;
    }

    public String getSourceToken() {
        return sourceToken;
    }

    public void setSourceToken(String sourceToken) {
        this.sourceToken = sourceToken;
    }

    public HyperwalletTransferRefundListOptions sourceToken(String sourceToken) {
        this.sourceToken = sourceToken;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HyperwalletTransferRefundListOptions status(String status) {
        this.status = status;
        return this;
    }
}
