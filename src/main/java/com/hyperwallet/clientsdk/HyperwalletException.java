package com.hyperwallet.clientsdk;

import cc.protea.util.http.Response;
import com.hyperwallet.clientsdk.model.HyperwalletError;
import com.hyperwallet.clientsdk.model.HyperwalletErrorList;

import java.util.List;

public class HyperwalletException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Response response = null;
    private String errorMessage;
    private String errorCode;
    private List<String> relatedResources;
    private HyperwalletErrorList hyperwalletErrorList;

    public HyperwalletException(final Exception e) {
        super(e);
    }

    public HyperwalletException(final Response response, final int code, final String message) {
        super(message);

        this.response = response;
        errorCode = Integer.toString(code);
        errorMessage = message;
    }

    public HyperwalletException(final Response response, final HyperwalletErrorList hyperwalletErrorList) {
        super(hyperwalletErrorList.getErrors().get(0).getMessage());

        this.response = response;
        this.hyperwalletErrorList = hyperwalletErrorList;
        HyperwalletError error = this.hyperwalletErrorList.getErrors().get(0);
        errorCode = error.getCode();
        errorMessage = error.getMessage();
        relatedResources = error.getRelatedResources();
    }

    public HyperwalletException(final String errorMessage) {
        super(errorMessage);

        this.errorMessage = errorMessage;
    }

    public Response getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public List<String> getRelatedResources() {
        return relatedResources;
    }

    public List<HyperwalletError> getHyperwalletErrors() {
        return hyperwalletErrorList != null ? hyperwalletErrorList.getErrors() : null;
    }
}
