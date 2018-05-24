package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hyperwallet.clientsdk.util.HyperwalletJsonConfiguration;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletBankCard extends HyperwalletBaseMonitor {

    public enum Brand {VISA, MASTERCARD}

    public enum CardType {DEBIT}

    private HyperwalletTransferMethod.Type type;
    private String token;
    private HyperwalletTransferMethod.Status status;
    private Date createdOn;

    private String transferMethodCountry;
    private String transferMethodCurrency;
    private Brand cardBrand;
    private String cardNumber;
    private CardType cardType;
    @JsonFormat(pattern = "yyyy-MM", timezone = "UTC")
    private Date dateOfExpiry;

    private String userToken;

    public HyperwalletTransferMethod.Type getType() {
        return type;
    }

    public void setType(HyperwalletTransferMethod.Type type) {
        addField("type", type);
        this.type = type;
    }

    public HyperwalletBankCard type(HyperwalletTransferMethod.Type type) {
        addField("type", type);
        this.type = type;
        return this;
    }

    public HyperwalletBankCard clearType() {
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

    public HyperwalletBankCard token(String token) {
        addField("token", token);
        this.token = token;
        return this;
    }

    public HyperwalletBankCard clearToken() {
        clearField("token");
        this.token = null;
        return this;
    }

    public HyperwalletTransferMethod.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletTransferMethod.Status status) {
        addField("status", status);
        this.status = status;
    }

    public HyperwalletBankCard status(HyperwalletTransferMethod.Status status) {
        addField("status", status);
        this.status = status;
        return this;
    }

    public HyperwalletBankCard clearStatus() {
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

    public HyperwalletBankCard createdOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
        return this;
    }

    public HyperwalletBankCard clearCreatedOn() {
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

    public HyperwalletBankCard transferMethodCountry(String transferMethodCountry) {
        addField("transferMethodCountry", transferMethodCountry);
        this.transferMethodCountry = transferMethodCountry;
        return this;
    }

    public HyperwalletBankCard clearTransferMethodCountry() {
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

    public HyperwalletBankCard transferMethodCurrency(String transferMethodCurrency) {
        addField("transferMethodCurrency", transferMethodCurrency);
        this.transferMethodCurrency = transferMethodCurrency;
        return this;
    }

    public HyperwalletBankCard clearTransferMethodCurrency() {
        clearField("transferMethodCurrency");
        this.transferMethodCurrency = null;
        return this;
    }

    public HyperwalletBankCard.CardType getCardType() {
        return cardType;
    }

    public void setCardType(HyperwalletBankCard.CardType cardType) {
        addField("cardType", cardType);
        this.cardType = cardType;
    }

    public HyperwalletBankCard cardType(HyperwalletBankCard.CardType cardType) {
        addField("cardType", cardType);
        this.cardType = cardType;
        return this;
    }

    public HyperwalletBankCard clearCardType() {
        clearField("cardType");
        this.cardType = null;
        return this;
    }


    public Date getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(Date dateOfExpiry) {
        addField("dateOfExpiry", dateOfExpiry);
        this.dateOfExpiry = dateOfExpiry;
    }

    public HyperwalletBankCard dateOfExpiry(Date dateOfExpiry) {
        addField("dateOfExpiry", dateOfExpiry);
        this.dateOfExpiry = dateOfExpiry;
        return this;
    }

    public HyperwalletBankCard clearDateOfExpiry() {
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

    public HyperwalletBankCard cardNumber(String cardNumber) {
        addField("cardNumber", cardNumber);
        this.cardNumber = cardNumber;
        return this;
    }

    public HyperwalletBankCard clearCardNumber() {
        clearField("cardNumber");
        this.cardNumber = null;
        return this;
    }

    public HyperwalletBankCard.Brand getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(HyperwalletBankCard.Brand cardBrand) {
        addField("cardBrand", cardBrand);
        this.cardBrand = cardBrand;
    }

    public HyperwalletBankCard cardBrand(HyperwalletBankCard.Brand cardBrand) {
        addField("cardBrand", cardBrand);
        this.cardBrand = cardBrand;
        return this;
    }

    public HyperwalletBankCard clearCardBrand() {
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

    public HyperwalletBankCard userToken(String userToken) {
        addField("userToken", userToken);
        this.userToken = userToken;
        return this;
    }

    public HyperwalletBankCard clearUserToken() {
        clearField("userToken");
        this.userToken = null;
        return this;
    }
}
