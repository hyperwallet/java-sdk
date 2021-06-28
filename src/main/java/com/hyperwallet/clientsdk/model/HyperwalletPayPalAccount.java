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
public class HyperwalletPayPalAccount extends HyperwalletBaseMonitor {

    public enum Status {ACTIVATED, INVALID, VERIFIED,DE_ACTIVATED}
    public enum Type {PAYPAL_ACCOUNT}
    private String token;
    private Type type;
    private Status status;
    private Date createdOn;
    private String transferMethodCountry;
    private String transferMethodCurrency;
    private Boolean isDefaultTransferMethod;
    private String email;
    private String userToken;
    private List<HyperwalletLink> links;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        addField("token", token);
        this.token = token;
    }

    public HyperwalletPayPalAccount token(String token) {
        addField("token", token);
        this.token = token;
        return this;
    }

    public HyperwalletPayPalAccount clearToken() {
        clearField("token");
        this.token = null;
        return this;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        addField("type", type);
        this.type = type;
    }

    public HyperwalletPayPalAccount type(Type type) {
        addField("type", type);
        this.type = type;
        return this;
    }

    public HyperwalletPayPalAccount clearType() {
        clearField("type");
        this.type = null;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        addField("status", status);
        this.status = status;
    }

    public HyperwalletPayPalAccount status(Status status) {
        addField("status", status);
        this.status = status;
        return this;
    }

    public HyperwalletPayPalAccount clearStatus() {
        clearField("status");
        this.status = null;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
    }

    public HyperwalletPayPalAccount createdOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
        return this;
    }

    public HyperwalletPayPalAccount clearCreatedOn() {
        clearField("createdOn");
        this.createdOn = null;
        return this;
    }

    public String getTransferMethodCountry() {
        return transferMethodCountry;
    }

    public void setTransferMethodCountry(String transferMethodCountry) {
        addField("transferMethodCountry", transferMethodCountry);
        this.transferMethodCountry = transferMethodCountry;
    }

    public HyperwalletPayPalAccount transferMethodCountry(String transferMethodCountry) {
        addField("transferMethodCountry", transferMethodCountry);
        this.transferMethodCountry = transferMethodCountry;
        return this;
    }

    public HyperwalletPayPalAccount clearTransferMethodCountry() {
        clearField("transferMethodCountry");
        this.transferMethodCountry = null;
        return this;
    }

    public String getTransferMethodCurrency() {
        return transferMethodCurrency;
    }

    public void setTransferMethodCurrency(String transferMethodCurrency) {
        addField("transferMethodCurrency", transferMethodCurrency);
        this.transferMethodCurrency = transferMethodCurrency;
    }

    public HyperwalletPayPalAccount transferMethodCurrency(String transferMethodCurrency) {
        addField("transferMethodCurrency", transferMethodCurrency);
        this.transferMethodCurrency = transferMethodCurrency;
        return this;
    }

    public HyperwalletPayPalAccount clearTransferMethodCurrency() {
        clearField("transferMethodCurrency");
        this.transferMethodCurrency = null;
        return this;
    }

    public Boolean getIsDefaultTransferMethod() {
        return isDefaultTransferMethod;
    }

    public void setIsDefaultTransferMethod(Boolean isDefaultTransferMethod) {
        addField("isDefaultTransferMethod", isDefaultTransferMethod);
        this.isDefaultTransferMethod = isDefaultTransferMethod;
    }

    public HyperwalletPayPalAccount isDefaultTransferMethod(Boolean isDefaultTransferMethod) {
        addField("isDefaultTransferMethod", isDefaultTransferMethod);
        this.isDefaultTransferMethod = isDefaultTransferMethod;
        return this;
    }

    public HyperwalletPayPalAccount clearIsDefaultTransferMethod() {
        clearField("isDefaultTransferMethod");
        this.isDefaultTransferMethod = null;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        addField("email", email);
        this.email = email;
    }

    public HyperwalletPayPalAccount email(String email) {
        addField("email", email);
        this.email = email;
        return this;
    }

    public HyperwalletPayPalAccount clearEmail() {
        clearField("email");
        this.email = null;
        return this;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        addField("userToken", userToken);
        this.userToken = userToken;
    }

    public HyperwalletPayPalAccount userToken(String userToken) {
        addField("userToken", userToken);
        this.userToken = userToken;
        return this;
    }

    public HyperwalletPayPalAccount clearUserToken() {
        clearField("userToken");
        this.userToken = null;
        return this;
    }

    public List<HyperwalletLink> getLinks() {
        return links;
    }

    public void setLinks(List<HyperwalletLink> links) {
        addField("links", links);
        this.links = links;
    }

    public HyperwalletPayPalAccount links(List<HyperwalletLink> links) {
        addField("links", links);
        this.links = links;
        return this;
    }

    public HyperwalletPayPalAccount clearLinks() {
        clearField("links");
        this.links = null;
        return this;
    }
}
