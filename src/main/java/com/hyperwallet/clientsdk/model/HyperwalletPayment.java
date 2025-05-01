package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.hyperwallet.clientsdk.util.HyperwalletJsonConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletPayment extends HyperwalletBaseMonitor {

    private String token;
    private String status;
    private String reasonCode;
    private Date createdOn;
    private String amount;
    private String currency;
    private String notes;
    private String memo;
    private String purpose;
    private Date releaseOn;
    private String destinationToken;
    private String programToken;
    private String clientPaymentId;
    private Date expiresOn;
    private List<HyperwalletLink> links;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        addField("token", token);
        this.token = token;
    }

    public HyperwalletPayment token(String token) {
        addField("token", token);
        this.token = token;
        return this;
    }

    public HyperwalletPayment clearToken() {
        clearField("token");
        this.token = null;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        addField("status", status);
        this.status = status;
    }

    public HyperwalletPayment status(String status) {
        addField("status", status);
        this.status = status;
        return this;
    }

    public HyperwalletPayment clearStatus() {
        clearField("status");
        status = null;
        return this;
    }

     public String getReasonCode() {
        return this.reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        addField("reasonCode", reasonCode);
        this.reasonCode = reasonCode;
    }

    public HyperwalletPayment reasonCode(String reasonCode) {
        addField("reasonCode", reasonCode);
        this.reasonCode = reasonCode;
        return this;
    }

    public HyperwalletPayment clearReasonCode() {
        clearField("reasonCode");
        reasonCode = null;
        return this;
    }

    public String getClientPaymentId() {
        return clientPaymentId;
    }

    public void setClientPaymentId(String clientPaymentId) {
        addField("clientPaymentId", clientPaymentId);
        this.clientPaymentId = clientPaymentId;
    }

    public HyperwalletPayment clientPaymentId(String clientPaymentId) {
        addField("clientPaymentId", clientPaymentId);
        this.clientPaymentId = clientPaymentId;
        return this;
    }

    public HyperwalletPayment clearClientPaymentId() {
        clearField("clientPaymentId");
        this.clientPaymentId = null;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
    }

    public HyperwalletPayment createdOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
        return this;
    }

    public HyperwalletPayment clearCreatedOn() {
        clearField("createdOn");
        this.createdOn = null;
        return this;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        addField("amount", amount);
        this.amount = amount;
    }

    public HyperwalletPayment amount(String amount) {
        addField("amount", amount);
        this.amount = amount;
        return this;
    }

    public HyperwalletPayment clearAmount() {
        clearField("amount");
        this.amount = null;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        addField("currency", currency);
        this.currency = currency;
    }

    public HyperwalletPayment currency(String currency) {
        addField("currency", currency);
        this.currency = currency;
        return this;
    }

    public HyperwalletPayment clearCurrency() {
        clearField("currency");
        this.currency = null;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        addField("notes", notes);
        this.notes = notes;
    }

    public HyperwalletPayment notes(String notes) {
        addField("notes", notes);
        this.notes = notes;
        return this;
    }

    public HyperwalletPayment clearNotes() {
        clearField("notes");
        this.notes = null;
        return this;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        addField("memo", memo);
        this.memo = memo;
    }

    public HyperwalletPayment memo(String memo) {
        addField("memo", memo);
        this.memo = memo;
        return this;
    }

    public HyperwalletPayment clearMemo() {
        clearField("memo");
        this.memo = null;
        return this;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        addField("purpose", purpose);
        this.purpose = purpose;
    }

    public HyperwalletPayment purpose(String purpose) {
        addField("purpose", purpose);
        this.purpose = purpose;
        return this;
    }

    public HyperwalletPayment clearPurpose() {
        clearField("purpose");
        this.purpose = null;
        return this;
    }

    public Date getReleaseOn() {
        return releaseOn;
    }

    public void setReleaseOn(Date releaseOn) {
        addField("releaseOn", releaseOn);
        this.releaseOn = releaseOn;
    }

    public HyperwalletPayment releaseOn(Date releaseOn) {
        addField("releaseOn", releaseOn);
        this.releaseOn = releaseOn;
        return this;
    }

    public HyperwalletPayment clearReleaseOn() {
        clearField("releaseOn");
        this.releaseOn = null;
        return this;
    }

    public String getDestinationToken() {
        return destinationToken;
    }

    public void setDestinationToken(String destinationToken) {
        addField("destinationToken", destinationToken);
        this.destinationToken = destinationToken;
    }

    public HyperwalletPayment destinationToken(String destinationToken) {
        addField("destinationToken", destinationToken);
        this.destinationToken = destinationToken;
        return this;
    }

    public HyperwalletPayment clearDestinationToken() {
        clearField("destinationToken");
        this.destinationToken = null;
        return this;
    }

    public String getProgramToken() {
        return programToken;
    }

    public void setProgramToken(String programToken) {
        addField("programToken", programToken);
        this.programToken = programToken;
    }

    public HyperwalletPayment programToken(String programToken) {
        addField("programToken", programToken);
        this.programToken = programToken;
        return this;
    }

    public HyperwalletPayment clearProgramToken() {
        clearField("programToken");
        this.programToken = null;
        return this;
    }

    public Date getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(Date expiresOn) {
        addField("expiresOn", expiresOn);
        this.expiresOn = expiresOn;
    }

    public HyperwalletPayment expiresOn(Date expiresOn) {
        addField("expiresOn", expiresOn);
        this.expiresOn = expiresOn;
        return this;
    }

    public HyperwalletPayment clearExpiresOn() {
        clearField("expiresOn");
        this.expiresOn = null;
        return this;
    }

    public List<HyperwalletLink> getLinks() {
        return links;
    }

    public void setLinks(List<HyperwalletLink> links) {
        addField("links", links);
        this.links = links;
    }

    public HyperwalletPayment links(List<HyperwalletLink> links) {
        addField("links", links);
        this.links = links;
        return this;
    }

    public HyperwalletPayment clearLinks() {
        clearField("links");
        this.links = null;
        return this;
    }
}
