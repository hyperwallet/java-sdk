package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class HyperwalletError {

    private String code;
    private String fieldName;
    private String message;
    private List<String> relatedResources;

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

    public HyperwalletError(String code, String fieldName, String message, List<String> relatedResources) {
        this.code = code;
        this.fieldName = fieldName;
        this.message = message;
        this.relatedResources = relatedResources;
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

    public List<String> getRelatedResources() {
        return relatedResources;
    }
}
