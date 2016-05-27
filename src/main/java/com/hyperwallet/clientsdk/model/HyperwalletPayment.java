package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletPayment {

    private String token;

    private Date createdOn;
    private Double amount;
    private String currency;
    private String description;
    private String memo;
    private String purpose;
    private Date releaseOn;
    private String destinationToken;
    private String programToken;
    private String clientPaymentId;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public HyperwalletPayment token(String token) {
        this.token = token;
        return this;
    }

    public String getClientPaymentId() {
        return clientPaymentId;
    }

    public void setClientPaymentId(String clientPaymentId) {
        this.clientPaymentId = clientPaymentId;
    }

    public HyperwalletPayment clientPaymentId(String clientPaymentId) {
        this.clientPaymentId = clientPaymentId;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
       this.createdOn = createdOn;
    }

    public HyperwalletPayment createdOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public HyperwalletPayment amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public HyperwalletPayment currency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HyperwalletPayment description(String description) {
        this.description = description;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public HyperwalletPayment memo(String memo) {
        this.memo = memo;
        return this;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public HyperwalletPayment purpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    public Date getReleaseOn() {
        return releaseOn;
    }

    public void setReleaseOn(Date releaseOn) {
        this.releaseOn = releaseOn;
    }

    public HyperwalletPayment releaseOn(Date releaseOn) {
        this.releaseOn = releaseOn;
        return this;
    }

    public String getDestinationToken() {
        return destinationToken;
    }

    public void setDestinationToken(String destinationToken) {
        this.destinationToken = destinationToken;
    }

    public HyperwalletPayment destinationToken(String destinationToken) {
        this.destinationToken = destinationToken;
        return this;
    }

    public String getProgramToken() {
        return programToken;
    }

    public void setProgramToken(String programToken) {
        this.programToken = programToken;
    }

    public HyperwalletPayment programToken(String programToken) {
        this.programToken = programToken;
        return this;
    }
}
