package com.hyperwallet.clientsdk.model;

public class HyperwalletTransferListOptions extends HyperwalletPaginationOptions {

    private String clientTransferId;
    private String sourceToken;
    private String destinationToken;

    public String getClientTransferId() {
        return clientTransferId;
    }

    public void setClientTransferId(String clientTransferId) {
        this.clientTransferId = clientTransferId;
    }

    public HyperwalletTransferListOptions clientTransferId(String clientTransferId) {
        this.clientTransferId = clientTransferId;
        return this;
    }

    public String getSourceToken() {
        return sourceToken;
    }

    public void setSourceToken(String sourceToken) {
        this.sourceToken = sourceToken;
    }

    public HyperwalletTransferListOptions sourceToken(String sourceToken) {
        this.sourceToken = sourceToken;
        return this;
    }

    public String getDestinationToken() {
        return destinationToken;
    }

    public void setDestinationToken(String destinationToken) {
        this.destinationToken = destinationToken;
    }

    public HyperwalletTransferListOptions destinationToken(String destinationToken) {
        this.destinationToken = destinationToken;
        return this;
    }
}
