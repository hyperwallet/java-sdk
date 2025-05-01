package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletBalance {
    private String currency;
    private String amount;

    public HyperwalletBalance() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public HyperwalletBalance currency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public HyperwalletBalance amount(String amount) {
        this.amount = amount;
        return this;
    }
}
