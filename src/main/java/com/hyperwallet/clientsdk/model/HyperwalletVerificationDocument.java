package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.hyperwallet.clientsdk.util.HyperwalletJsonConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletVerificationDocument {

    private String category;

    private String type;

    private String status;

    private String country;

    private List<HyperwalletVerificationDocumentReason> reasons;

    public Map<String, String> uploadFiles;

    private Date createdOn;

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

    public Map<String, String> getUploadFiles() {
        return uploadFiles;
    }

    public void setUploadFiles(Map<String, String> uploadFiles) {
        this.uploadFiles = uploadFiles;
    }

    public HyperwalletVerificationDocument uploadFiles(Map<String, String> uploadFiles) {
        setUploadFiles(uploadFiles);
        return this;
    }

    public List<HyperwalletVerificationDocumentReason> getReasons() {
        return reasons;
    }

    public void setReasons(List<HyperwalletVerificationDocumentReason> reasons) {
        this.reasons = reasons;
    }

    public HyperwalletVerificationDocument reasons(List<HyperwalletVerificationDocumentReason> reasons) {
        setReasons(reasons);
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public HyperwalletVerificationDocument createdOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }
}
