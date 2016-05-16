package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HyperwalletError {

    public String code;
    public String fieldName;
    public String message;

    public HyperwalletError() {}

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

    public void setErrorCode(String errorCode) {
        this.code = errorCode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
