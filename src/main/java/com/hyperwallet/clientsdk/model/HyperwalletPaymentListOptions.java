package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletPaymentListOptions extends HyperwalletPaginationOptions {

    private Date releasedOn;
    private String currency;
    private String clientPaymentId;

    public Date getReleasedOn() {
        return releasedOn;
    }

    public void setReleasedOn(Date releasedOn) {
        this.releasedOn = releasedOn;
    }

    public HyperwalletPaymentListOptions releasedOn(Date releasedOn) {
        this.releasedOn = releasedOn;
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
}
