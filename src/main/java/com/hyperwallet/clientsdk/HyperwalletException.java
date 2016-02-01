package com.hyperwallet.clientsdk;

import cc.protea.util.http.Response;

class HyperwalletException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public Response response = null;
	public String errorMessage;
	public String errorCode;

	HyperwalletException(final Exception e) {
		super(e);
	}

	HyperwalletException(final Exception e, final Response response) {
		super(e);
		this.response = response;
	}

	HyperwalletException(final Exception e, final String errorCode, final String errorMessage) {
		super(e);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	HyperwalletException(final String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
