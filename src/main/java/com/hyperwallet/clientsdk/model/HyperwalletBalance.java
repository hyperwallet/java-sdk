package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletBalance {
    public String currency;
    public Double amount;

    public HyperwalletBalance () {}

    public String getCurrency() {
        return currency;
    }

    public HyperwalletBalance setCurrency(String currency){
        this.currency = currency;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public HyperwalletBalance setAmount(Double amount) {
        this.amount = amount;
        return this;
    }
}
