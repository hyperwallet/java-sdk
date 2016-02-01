package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletPaymentListOptions extends HyperwalletPaginationOptions {

	public Date releasedOn;
	public String currency;

	public Date getReleasedOn() {
		return releasedOn;
	}

	public HyperwalletPaymentListOptions setReleasedOn(Date releasedOn) {
		this.releasedOn = releasedOn;
		return this;
	}

	public String getCurrency() {
		return currency;
	}

	public HyperwalletPaymentListOptions setCurrency(String currency) {
		this.currency = currency;
		return this;
	}
}
