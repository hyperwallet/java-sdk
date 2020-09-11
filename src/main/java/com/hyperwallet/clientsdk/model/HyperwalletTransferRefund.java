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
public class HyperwalletTransferRefund extends HyperwalletBaseMonitor {

    public static enum Status {PENDING, FAILED, COMPLETED}

    private String token;
    private Status status;
    private String clientRefundId;
    private String sourceToken;
    private Double sourceAmount;
    private String sourceCurrency;
    private String destinationToken;
    private Double destinationAmount;
    private String destinationCurrency;
    private Date createdOn;
    private String notes;
    private String memo;
    private List<HyperwalletLink> links;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        addField("token", token);
        this.token = token;
    }

    public HyperwalletTransferRefund token(String token) {
        addField("token", token);
        this.token = token;
        return this;
    }

    public HyperwalletTransferRefund clearToken() {
        clearField("token");
        this.token = null;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        addField("status", status);
        this.status = status;
    }

    public HyperwalletTransferRefund status(Status status) {
        addField("status", status);
        this.status = status;
        return this;
    }

    public HyperwalletTransferRefund clearStatus() {
        clearField("status");
        this.status = null;
        return this;
    }

    public String getClientRefundId() {
        return clientRefundId;
    }

    public void setClientRefundId(String clientRefundId) {
        addField("clientRefundId", clientRefundId);
        this.clientRefundId = clientRefundId;
    }

    public HyperwalletTransferRefund clientRefundId(String clientRefundId) {
        addField("clientRefundId", clientRefundId);
        this.clientRefundId = clientRefundId;
        return this;
    }

    public HyperwalletTransferRefund clearClientRefundId() {
        clearField("clientRefundId");
        this.clientRefundId = null;
        return this;
    }

    public String getSourceToken() {
        return sourceToken;
    }

    public void setSourceToken(String sourceToken) {
        addField("sourceToken", sourceToken);
        this.sourceToken = sourceToken;
    }

    public HyperwalletTransferRefund sourceToken(String sourceToken) {
        addField("sourceToken", sourceToken);
        this.sourceToken = sourceToken;
        return this;
    }

    public HyperwalletTransferRefund clearSourceToken() {
        clearField("sourceToken");
        this.sourceToken = null;
        return this;
    }

    public Double getSourceAmount() {
        return sourceAmount;
    }

    public void setSourceAmount(Double sourceAmount) {
        addField("sourceAmount", sourceAmount);
        this.sourceAmount = sourceAmount;
    }

    public HyperwalletTransferRefund sourceAmount(Double sourceAmount) {
        addField("sourceAmount", sourceAmount);
        this.sourceAmount = sourceAmount;
        return this;
    }

    public HyperwalletTransferRefund clearSourceAmount() {
        clearField("sourceAmount");
        this.sourceAmount = null;
        return this;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        addField("sourceCurrency", sourceCurrency);
        this.sourceCurrency = sourceCurrency;
    }

    public HyperwalletTransferRefund sourceCurrency(String sourceCurrency) {
        addField("sourceCurrency", sourceCurrency);
        this.sourceCurrency = sourceCurrency;
        return this;
    }

    public HyperwalletTransferRefund clearSourceCurrency() {
        clearField("sourceCurrency");
        this.sourceCurrency = null;
        return this;
    }

    public String getDestinationToken() {
        return destinationToken;
    }

    public void setDestinationToken(String destinationToken) {
        addField("destinationToken", destinationToken);
        this.destinationToken = destinationToken;
    }

    public HyperwalletTransferRefund destinationToken(String destinationToken) {
        addField("destinationToken", destinationToken);
        this.destinationToken = destinationToken;
        return this;
    }

    public HyperwalletTransferRefund clearDestinationToken() {
        clearField("destinationToken");
        this.destinationToken = null;
        return this;
    }

    public Double getDestinationAmount() {
        return destinationAmount;
    }

    public void setDestinationAmount(Double destinationAmount) {
        addField("destinationAmount", destinationAmount);
        this.destinationAmount = destinationAmount;
    }

    public HyperwalletTransferRefund destinationAmount(Double destinationAmount) {
        addField("destinationAmount", destinationAmount);
        this.destinationAmount = destinationAmount;
        return this;
    }

    public HyperwalletTransferRefund clearDestinationAmount() {
        clearField("destinationAmount");
        this.destinationAmount = null;
        return this;
    }

    public String getDestinationCurrency() {
        return destinationCurrency;
    }

    public void setDestinationCurrency(String destinationCurrency) {
        addField("destinationCurrency", destinationCurrency);
        this.destinationCurrency = destinationCurrency;
    }

    public HyperwalletTransferRefund destinationCurrency(String destinationCurrency) {
        addField("destinationCurrency", destinationCurrency);
        this.destinationCurrency = destinationCurrency;
        return this;
    }

    public HyperwalletTransferRefund clearDestinationCurrency() {
        clearField("destinationCurrency");
        this.destinationCurrency = null;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
    }

    public HyperwalletTransferRefund createdOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
        return this;
    }

    public HyperwalletTransferRefund clearCreatedOn() {
        clearField("createdOn");
        this.createdOn = null;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        addField("notes", notes);
        this.notes = notes;
    }

    public HyperwalletTransferRefund notes(String notes) {
        addField("notes", notes);
        this.notes = notes;
        return this;
    }

    public HyperwalletTransferRefund clearNotes() {
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

    public HyperwalletTransferRefund memo(String memo) {
        addField("memo", memo);
        this.memo = memo;
        return this;
    }

    public HyperwalletTransferRefund clearMemo() {
        clearField("memo");
        this.memo = null;
        return this;
    }

    public List<HyperwalletLink> getLinks() {
        return links;
    }

    public void setLinks(List<HyperwalletLink> links) {
        addField("links", links);
        this.links = links;
    }

    public HyperwalletTransferRefund links(List<HyperwalletLink> links) {
        addField("links", links);
        this.links = links;
        return this;
    }

    public HyperwalletTransferRefund clearLinks() {
        clearField("links");
        this.links = null;
        return this;
    }
}
