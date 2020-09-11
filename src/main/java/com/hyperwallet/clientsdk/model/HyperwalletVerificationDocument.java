package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@JsonInclude(Include.NON_NULL)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class
HyperwalletVerificationDocument {

    private String category;
    private String type;
    private String status;
    private String country;

    public HyperwalletVerificationDocument() {

    }

    public HyperwalletVerificationDocument(String category, String type, String status) {
        this.category = category;
        this.type = type;
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public HyperwalletVerificationDocument category(String category) {
        setCategory(category);
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HyperwalletVerificationDocument type(String type) {
        setType(type);
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HyperwalletVerificationDocument status(String status) {
        setStatus(status);
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public HyperwalletVerificationDocument country(String country) {
        setCountry(country);
        return this;
    }
}
