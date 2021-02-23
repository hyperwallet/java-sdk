package com.hyperwallet.clientsdk.model;

public enum DocumentVerificationReason {
    DOCUMENT_EXPIRED("Document in expired"),
    DOCUMENT_NOT_RELATED_TO_PROFILE("Document does not match account information"),
    DOCUMENT_NOT_READABLE("Document is not readable"),
    DOCUMENT_NOT_DECISIVE("Decision cannot be made based on document. Alternative document required"),
    DOCUMENT_NOT_COMPLETE("Document is incomplete"),
    DOCUMENT_CORRECTION_REQUIRED("Document requires correction"),
    DOCUMENT_NOT_VALID_WITH_NOTES("Document is invalid and rejection details are noted on the account"),
    DOCUMENT_TYPE_NOT_VALID("Document type is not acceptable");


    private final String description;

    DocumentVerificationReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}