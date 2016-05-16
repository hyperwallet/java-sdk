package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletPayment {

	public String token;

	public Date createdOn;
	public Double amount;
	public String currency;
	public String description;
	public String memo;
	public String purpose;
	public Date releaseOn;
	public String destinationToken;
	public String programToken;
	public String clientPaymentId;


	public String getToken() {
		return token;
	}

	public HyperwalletPayment setToken(String token) {
		this.token = token;
		return this;
	}

	public String getClientPaymentId() {
		return clientPaymentId;
	}

	public HyperwalletPayment setClientPaymentId(String clientPaymentId) {
		this.clientPaymentId = clientPaymentId;
		return this;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public HyperwalletPayment setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
		return this;
	}

	public Double getAmount() {
		return amount;
	}

	public HyperwalletPayment setAmount(Double amount) {
		this.amount = amount;
		return this;
	}

	public String getCurrency() {
		return currency;
	}

	public HyperwalletPayment setCurrency(String currency) {
		this.currency = currency;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public HyperwalletPayment setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getMemo() {
		return memo;
	}

	public HyperwalletPayment setMemo(String memo) {
		this.memo = memo;
		return this;
	}

	public String getPurpose() {
		return purpose;
	}

	public HyperwalletPayment setPurpose(String purpose) {
		this.purpose = purpose;
		return this;
	}

	public Date getReleaseOn() {
		return releaseOn;
	}

	public HyperwalletPayment setReleaseOn(Date releaseOn) {
		this.releaseOn = releaseOn;
		return this;
	}

	public String getDestinationToken() {
		return destinationToken;
	}

	public HyperwalletPayment setDestinationToken(String destinationToken) {
		this.destinationToken = destinationToken;
		return this;
	}

	public String getProgramToken() {
		return programToken;
	}

	public HyperwalletPayment setProgramToken(String programToken) {
		this.programToken = programToken;
		return this;
	}
}
