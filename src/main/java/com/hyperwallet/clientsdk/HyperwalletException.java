package com.hyperwallet.clientsdk;

import cc.protea.util.http.Response;
import com.hyperwallet.clientsdk.model.HyperwalletError;
import com.hyperwallet.clientsdk.model.HyperwalletErrorList;

import java.util.List;

public class HyperwalletException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public Response response = null;
	public String errorMessage;
	public String errorCode;
    public HyperwalletErrorList hyperwalletErrorList;

	HyperwalletException(final Exception e) {
		super(e);
	}

    HyperwalletException(final Response response, final int code, final String message) {
        this.response = response;
        errorCode = Integer.toString(code);
        errorMessage = message;
    }

    HyperwalletException(final HyperwalletErrorList hyperwalletErrorList) {
        this.hyperwalletErrorList = hyperwalletErrorList;
        HyperwalletError error = this.hyperwalletErrorList.getErrors().get(0);
        errorCode = error.getCode();
        errorMessage = error.getMessage();
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

    public List<HyperwalletError> getHyperwalletErrors () {
        return hyperwalletErrorList != null ? hyperwalletErrorList.getErrors() : null;
    }
}
