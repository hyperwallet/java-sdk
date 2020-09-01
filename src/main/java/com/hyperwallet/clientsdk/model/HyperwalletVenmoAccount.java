package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.hyperwallet.clientsdk.util.HyperwalletJsonConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletVenmoAccount extends HyperwalletBaseMonitor {

    private String token;
    private HyperwalletTransferMethod.Type type;
    private HyperwalletTransferMethod.Status status;
    private Date createdOn;
    private String transferMethodCountry;
    private String transferMethodCurrency;
    private Boolean isDefaultTransferMethod;
    private String accountId;
    private String userToken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        addField("token", token);
        this.token = token;
    }

    public HyperwalletVenmoAccount token(String token) {
        addField("token", token);
        this.token = token;
        return this;
    }

    public HyperwalletVenmoAccount clearToken() {
        clearField("token");
        this.token = null;
        return this;
    }

    public HyperwalletTransferMethod.Type getType() {
        return type;
    }

    public void setType(HyperwalletTransferMethod.Type type) {
        addField("type", type);
        this.type = type;
    }

    public HyperwalletVenmoAccount type(HyperwalletTransferMethod.Type type) {
        addField("type", type);
        this.type = type;
        return this;
    }

    public HyperwalletVenmoAccount clearType() {
        clearField("type");
        this.type = null;
        return this;
    }

    public HyperwalletTransferMethod.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletTransferMethod.Status status) {
        addField("status", status);
        this.status = status;
    }

    public HyperwalletVenmoAccount status(HyperwalletTransferMethod.Status status) {
        addField("status", status);
        this.status = status;
        return this;
    }

    public HyperwalletVenmoAccount clearStatus() {
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

    public HyperwalletVenmoAccount createdOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
        return this;
    }

    public HyperwalletVenmoAccount clearCreatedOn() {
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

    public HyperwalletVenmoAccount transferMethodCountry(String transferMethodCountry) {
        addField("transferMethodCountry", transferMethodCountry);
        this.transferMethodCountry = transferMethodCountry;
        return this;
    }


    public HyperwalletVenmoAccount clearTransferMethodCountry() {
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

    public HyperwalletVenmoAccount transferMethodCurrency(String transferMethodCurrency) {
        addField("transferMethodCurrency", transferMethodCurrency);
        this.transferMethodCurrency = transferMethodCurrency;
        return this;
    }

    public HyperwalletVenmoAccount clearTransferMethodCurrency() {
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

    public HyperwalletVenmoAccount isDefaultTransferMethod(Boolean isDefaultTransferMethod) {
        addField("isDefaultTransferMethod", isDefaultTransferMethod);
        this.isDefaultTransferMethod = isDefaultTransferMethod;
        return this;
    }

    public HyperwalletVenmoAccount clearIsDefaultTransferMethod() {
        clearField("isDefaultTransferMethod");
        this.isDefaultTransferMethod = null;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        addField("accountId", accountId);
        this.accountId = accountId;
    }

    public HyperwalletVenmoAccount accountId(String accountId) {
        addField("accountId", accountId);
        this.accountId = accountId;
        return this;
    }

    public HyperwalletVenmoAccount clearAccountId() {
        clearField("accountId");
        this.accountId = null;
        return this;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        addField("userToken", userToken);
        this.userToken = userToken;
    }

    public HyperwalletVenmoAccount userToken(String userToken) {
        addField("userToken", userToken);
        this.userToken = userToken;
        return this;
    }

    public HyperwalletVenmoAccount clearUserToken() {
        clearField("userToken");
        this.userToken = null;
        return this;
    }


}
