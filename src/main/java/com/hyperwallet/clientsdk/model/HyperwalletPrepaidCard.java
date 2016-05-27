package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletPrepaidCard {

    public enum Brand {VISA, MASTERCARD}

    public enum CardType {PERSONALIZED, INSTANT_ISSUE, VIRTUAL}

    public HyperwalletTransferMethod.Type type;

    public HyperwalletPrepaidCard() {
        super();
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
        this.type = type;
    }

    public HyperwalletPrepaidCard type(HyperwalletTransferMethod.Type type) {
        this.type = type;
        return this;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public HyperwalletPrepaidCard token(String token) {
        this.token = token;
        return this;
    }

    public HyperwalletTransferMethod.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletTransferMethod.Status status) {
        this.status = status;
    }

    public HyperwalletPrepaidCard status(HyperwalletTransferMethod.Status status) {
        this.status = status;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public HyperwalletPrepaidCard createdOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public String getTransferMethodCountry() {
        return transferMethodCountry;
    }

    public void setTransferMethodCountry(String transferMethodCountry) {
        this.transferMethodCountry = transferMethodCountry;
    }

    public HyperwalletPrepaidCard transferMethodCountry(String transferMethodCountry) {
        this.transferMethodCountry = transferMethodCountry;
        return this;
    }

    public String getTransferMethodCurrency() {
        return transferMethodCurrency;
    }

    public void setTransferMethodCurrency(String transferMethodCurrency) {
        this.transferMethodCurrency = transferMethodCurrency;
    }

    public HyperwalletPrepaidCard transferMethodCurrency(String transferMethodCurrency) {
        this.transferMethodCurrency = transferMethodCurrency;
        return this;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public HyperwalletPrepaidCard cardType(CardType cardType) {
        this.cardType = cardType;
        return this;
    }

    public String getCardPackage() {
        return cardPackage;
    }

    public void setCardPackage(String cardPackage) {
        this.cardPackage = cardPackage;
    }

    public HyperwalletPrepaidCard cardPackage(String cardPackage) {
        this.cardPackage = cardPackage;
        return this;
    }

    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public HyperwalletPrepaidCard dateOfExpiry(Date dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
        return this;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public HyperwalletPrepaidCard cardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        return this;
    }

    public Brand getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(Brand cardBrand) {
        this.cardBrand = cardBrand;
    }

    public HyperwalletPrepaidCard cardBrand(Brand cardBrand) {
        this.cardBrand = cardBrand;
        return this;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public HyperwalletPrepaidCard userToken(String userToken) {
        this.userToken = userToken;
        return this;
    }
}
