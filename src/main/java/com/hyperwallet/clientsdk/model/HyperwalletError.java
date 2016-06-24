package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HyperwalletError {

    private String code;
    private String fieldName;
    private String message;

    public HyperwalletError() {
    }

    public HyperwalletError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public HyperwalletError(String code, String fieldName, String message) {
        this.code = code;
        this.fieldName = fieldName;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }
}
