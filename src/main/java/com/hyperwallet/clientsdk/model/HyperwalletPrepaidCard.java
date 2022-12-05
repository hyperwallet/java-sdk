package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hyperwallet.clientsdk.util.HyperwalletJsonConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletPrepaidCard extends HyperwalletBaseMonitor {

    public enum Brand {VISA, MASTERCARD}
    public enum CardType {PERSONALIZED, INSTANT_ISSUE, VIRTUAL}
    public enum Status {QUEUED, PRE_ACTIVATED, ACTIVATED, DECLINED, LOCKED, SUSPENDED, LOST_OR_STOLEN, DE_ACTIVATED, COMPLIANCE_HOLD, KYC_HOLD}
    public enum Type {PREPAID_CARD}

    private Type type;
    private String token;
    private Status status;
    private Status transition;

    private Date createdOn;
    private String transferMethodCountry;
    private String transferMethodCurrency;
    private CardType cardType;
    private String cardPackage;
    private String cardNumber;
    private Brand cardBrand;
    @JsonFormat(pattern = "yyyy-MM", timezone = "UTC")
    private Date dateOfExpiry;

    private String userToken;
    private Boolean isDefaultTransferMethod;


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        addField("type", type);
        this.type = type;
    }

    public HyperwalletPrepaidCard type(Type type) {
        addField("type", type);
        this.type = type;
        return this;
    }

    public HyperwalletPrepaidCard clearType() {
        clearField("type");
        this.type = null;
        return this;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        addField("token", token);
        this.token = token;
    }

    public HyperwalletPrepaidCard token(String token) {
        addField("token", token);
        this.token = token;
        return this;
    }

    public HyperwalletPrepaidCard clearToken() {
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

    public HyperwalletPrepaidCard status(Status status) {
        addField("status", status);
        this.status = status;
        return this;
    }

    public HyperwalletPrepaidCard clearStatus() {
        clearField("status");
        this.status = null;
        return this;
    }

    public Status getTransition() {
        return transition;
    }

    public void setTransition(Status transition) {
        addField("transition", transition);
        this.transition = transition;
    }

    public HyperwalletPrepaidCard transition(Status transition) {
        addField("transition", transition);
        this.transition = transition;
        return this;
    }

    public HyperwalletPrepaidCard clearTransition() {
        clearField("transition");
        this.transition = null;
        return this;
    }


    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
    }

    public HyperwalletPrepaidCard createdOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
        return this;
    }

    public HyperwalletPrepaidCard clearCreatedOn() {
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

    public HyperwalletPrepaidCard transferMethodCountry(String transferMethodCountry) {
        addField("transferMethodCountry", transferMethodCountry);
        this.transferMethodCountry = transferMethodCountry;
        return this;
    }

    public HyperwalletPrepaidCard clearTransferMethodCountry() {
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

    public HyperwalletPrepaidCard transferMethodCurrency(String transferMethodCurrency) {
        addField("transferMethodCurrency", transferMethodCurrency);
        this.transferMethodCurrency = transferMethodCurrency;
        return this;
    }

    public HyperwalletPrepaidCard clearTransferMethodCurrency() {
        clearField("transferMethodCurrency");
        this.transferMethodCurrency = null;
        return this;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        addField("cardType", cardType);
        this.cardType = cardType;
    }

    public HyperwalletPrepaidCard cardType(CardType cardType) {
        addField("cardType", cardType);
        this.cardType = cardType;
        return this;
    }

    public HyperwalletPrepaidCard clearCardType() {
        clearField("cardType");
        this.cardType = null;
        return this;
    }

    public String getCardPackage() {
        return cardPackage;
    }

    public void setCardPackage(String cardPackage) {
        addField("cardPackage", cardPackage);
        this.cardPackage = cardPackage;
    }

    public HyperwalletPrepaidCard cardPackage(String cardPackage) {
        addField("cardPackage", cardPackage);
        this.cardPackage = cardPackage;
        return this;
    }

    public HyperwalletPrepaidCard clearCardPackage() {
        clearField("cardPackage");
        this.cardPackage = null;
        return this;
    }

    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(Date dateOfExpiry) {
        addField("dateOfExpiry", dateOfExpiry);
        this.dateOfExpiry = dateOfExpiry;
    }

    public HyperwalletPrepaidCard dateOfExpiry(Date dateOfExpiry) {
        addField("dateOfExpiry", dateOfExpiry);
        this.dateOfExpiry = dateOfExpiry;
        return this;
    }

    public HyperwalletPrepaidCard clearDateOfExpiry() {
        clearField("dateOfExpiry");
        this.dateOfExpiry = null;
        return this;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        addField("cardNumber", cardNumber);
        this.cardNumber = cardNumber;
    }

    public HyperwalletPrepaidCard cardNumber(String cardNumber) {
        addField("cardNumber", cardNumber);
        this.cardNumber = cardNumber;
        return this;
    }

    public HyperwalletPrepaidCard clearCardNumber() {
        clearField("cardNumber");
        this.cardNumber = null;
        return this;
    }

    public Brand getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(Brand cardBrand) {
        addField("cardBrand", cardBrand);
        this.cardBrand = cardBrand;
    }

    public HyperwalletPrepaidCard cardBrand(Brand cardBrand) {
        addField("cardBrand", cardBrand);
        this.cardBrand = cardBrand;
        return this;
    }

    public HyperwalletPrepaidCard clearCardBrand() {
        clearField("cardBrand");
        this.cardBrand = null;
        return this;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        addField("userToken", userToken);
        this.userToken = userToken;
    }

    public HyperwalletPrepaidCard userToken(String userToken) {
        addField("userToken", userToken);
        this.userToken = userToken;
        return this;
    }

    public HyperwalletPrepaidCard clearUserToken() {
        clearField("userToken");
        this.userToken = null;
        return this;
    }

    public Boolean getIsDefaultTransferMethod() {
        return isDefaultTransferMethod;
    }

    public void setIsDefaultTransferMethod(Boolean isDefaultTransferMethod) {
        addField("isDefaultTransferMethod", isDefaultTransferMethod);
        this.isDefaultTransferMethod = isDefaultTransferMethod;
    }

    public HyperwalletPrepaidCard isDefaultTransferMethod(Boolean isDefaultTransferMethod) {
        addField("isDefaultTransferMethod", isDefaultTransferMethod);
        this.isDefaultTransferMethod = isDefaultTransferMethod;
        return this;
    }

    public HyperwalletPrepaidCard clearIsDefaultTransferMethod() {
        clearField("isDefaultTransferMethod");
        this.isDefaultTransferMethod = null;
        return this;
    }
}
