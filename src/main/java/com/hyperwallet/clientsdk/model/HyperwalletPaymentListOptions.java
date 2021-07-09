package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletPaymentListOptions extends HyperwalletPaginationOptions {

    private String clientPaymentId;
    private Date releaseDate;

    public String getClientPaymentId() {
        return clientPaymentId;
    }

    public void setClientPaymentId(String clientPaymentId) {
        this.clientPaymentId = clientPaymentId;
    }

    public HyperwalletPaymentListOptions clientPaymentId(String clientPaymentId) {
        this.clientPaymentId = clientPaymentId;
        return this;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public HyperwalletPaymentListOptions releaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }
}
