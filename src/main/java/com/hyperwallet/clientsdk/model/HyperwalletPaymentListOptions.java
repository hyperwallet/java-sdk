package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletPaymentListOptions extends HyperwalletPaginationOptions {

    private Date releaseDate;
    private String clientPaymentId;
    private String currency;
    private String memo;

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public HyperwalletPaymentListOptions releaseDate(Date releasedOn) {
        this.releaseDate = releasedOn;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public HyperwalletPaymentListOptions currency(String currency) {
        this.currency = currency;
        return this;
    }

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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public HyperwalletPaymentListOptions memo(String memo) {
        this.memo = memo;
        return this;
    }
}
