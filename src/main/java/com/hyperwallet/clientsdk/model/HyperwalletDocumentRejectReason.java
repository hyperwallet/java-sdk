package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.hyperwallet.clientsdk.util.HyperwalletJsonConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletDocumentRejectReason {

    private DocumentVerificationReason name;
    private String description;

    public enum DocumentVerificationReason {
        DOCUMENT_EXPIRED,
        DOCUMENT_NOT_RELATED_TO_PROFILE,
        DOCUMENT_NOT_READABLE,
        DOCUMENT_NOT_DECISIVE,
        DOCUMENT_NOT_COMPLETE,
        DOCUMENT_CORRECTION_REQUIRED,
        DOCUMENT_NOT_VALID_WITH_NOTES,
        DOCUMENT_TYPE_NOT_VALID;
    }

    public DocumentVerificationReason getName() {
        return name;
    }

    public void setName(DocumentVerificationReason name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
