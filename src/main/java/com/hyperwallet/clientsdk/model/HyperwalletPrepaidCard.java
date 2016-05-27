package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.hyperwallet.clientsdk.HyperwalletJsonConfiguration;

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

    public HyperwalletTransferMethod.Type type;

    public HyperwalletPrepaidCard() {
        super();
        addField("type", type);
        type = HyperwalletTransferMethod.Type.PREPAID_CARD;
    }

    private String token;

    private HyperwalletTransferMethod.Status status;
    private Date createdOn;
    private String transferMethodCountry;
    private String transferMethodCurrency;

    private CardType cardType;
    private String cardPackage;
    private String cardNumber;
    private Brand cardBrand;

    private Date dateOfExpiry;

    private String userToken;

    public HyperwalletTransferMethod.Type getType() {
        return type;
    }

    public void setType(HyperwalletTransferMethod.Type type) {
        addField("type", type);
        this.type = type;
    }

    public HyperwalletPrepaidCard type(HyperwalletTransferMethod.Type type) {
        addField("type", type);
        this.type = type;
        return this;
    }

    public HyperwalletPrepaidCard clearType() {
        clearField("type");
        type = null;
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
        token = null;
        return this;
    }

    public HyperwalletTransferMethod.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletTransferMethod.Status status) {
        addField("status", status);
        this.status = status;
    }

    public HyperwalletPrepaidCard status(HyperwalletTransferMethod.Status status) {
        addField("status", status);
        this.status = status;
        return this;
    }

    public HyperwalletPrepaidCard clearStatus() {
        clearField("status");
        status = null;
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
        createdOn = null;
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
        transferMethodCountry = null;
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
        transferMethodCurrency = null;
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
        cardType = null;
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
        cardPackage = null;
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
        addField("dateOfExpiry", dateOfExpiry);
        dateOfExpiry = null;
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
        cardNumber = null;
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
        cardBrand = null;
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
        userToken = null;
        return this;
    }
}
