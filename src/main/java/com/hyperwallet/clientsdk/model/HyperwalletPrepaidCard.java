package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletPrepaidCard {

	public enum Brand { VISA, MASTERCARD }
	public enum CardType { PERSONALIZED, INSTANT_ISSUE, VIRTUAL }

	public HyperwalletTransferMethod.Type type = HyperwalletTransferMethod.Type.PREPAID_CARD;

	public String token;

	public HyperwalletTransferMethod.Status status;
	public Date createdOn;
	public String transferMethodCountry;
	public String transferMethodCurrency;

	public CardType cardType;
	public String cardPackage;
	public String cardNumber;
	public Brand cardBrand;

	public Date dateOfExpiry;

	public String userToken;

	public HyperwalletTransferMethod.Type getType() {
		return type;
	}

	public String getToken() {
		return token;
	}

	public HyperwalletPrepaidCard setToken(String token) {
		this.token = token;
		return this;
	}

	public HyperwalletTransferMethod.Status getStatus() {
		return status;
	}

	public HyperwalletPrepaidCard setStatus(HyperwalletTransferMethod.Status status) {
		this.status = status;
		return this;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public HyperwalletPrepaidCard setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
		return this;
	}

	public String getTransferMethodCountry() {
		return transferMethodCountry;
	}

	public HyperwalletPrepaidCard setTransferMethodCountry(String transferMethodCountry) {
		this.transferMethodCountry = transferMethodCountry;
		return this;
	}

	public String getTransferMethodCurrency() {
		return transferMethodCurrency;
	}

	public HyperwalletPrepaidCard setTransferMethodCurrency(String transferMethodCurrency) {
		this.transferMethodCurrency = transferMethodCurrency;
		return this;
	}

	public CardType getCardType() {
		return cardType;
	}

	public HyperwalletPrepaidCard setCardType(CardType cardType) {
		this.cardType = cardType;
		return this;
	}

	public String getCardPackage() {
		return cardPackage;
	}

	public HyperwalletPrepaidCard setCardPackage(String cardPackage) {
		this.cardPackage = cardPackage;
		return this;
	}

	public Date getDateOfExpiry() {
		return dateOfExpiry;
	}

	public HyperwalletPrepaidCard setDateOfExpiry(Date dateOfExpiry) {
		this.dateOfExpiry = dateOfExpiry;
		return this;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public HyperwalletPrepaidCard setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
		return this;
	}

	public Brand getCardBrand() {
		return cardBrand;
	}

	public HyperwalletPrepaidCard setCardBrand(Brand cardBrand) {
		this.cardBrand = cardBrand;
		return this;
	}

	public String getUserToken() {
		return userToken;
	}

	public HyperwalletPrepaidCard setUserToken(String userToken) {
		this.userToken = userToken;
		return this;
	}


}
