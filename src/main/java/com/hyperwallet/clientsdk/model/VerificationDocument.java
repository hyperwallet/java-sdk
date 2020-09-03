package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@JsonInclude(Include.NON_NULL)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VerificationDocument {

    private String category;
    private String type;
    private String status;
    private String country;

    public VerificationDocument() {

    }

    public VerificationDocument(String category, String type, String status) {
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

    public VerificationDocument category(String category) {
        setCategory(category);
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public VerificationDocument type(String type) {
        setType(type);
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VerificationDocument status(String status) {
        setStatus(status);
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public VerificationDocument country(String country) {
        setCountry(country);
        return this;
    }
}
