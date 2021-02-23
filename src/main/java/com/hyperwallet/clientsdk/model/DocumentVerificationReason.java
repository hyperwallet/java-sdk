package com.hyperwallet.clientsdk.model;

public enum DocumentVerificationReason {
    NONE("EKYCDocumentRejectReason.NONE"),
    DOCUMENT_EXPIRED("EKYCDocumentRejectReason.DOCUMENT_EXPIRED"),
    DOCUMENT_NOT_SUPPORTED("EKYCDocumentRejectReason.DOCUMENT_NOT_SUPPORTED"),
    TIMEOUT_OR_DOCUMENT_NOT_SUPPORTED("EKYCDocumentRejectReason.TIMEOUT_OR_DOCUMENT_NOT_SUPPORTED"),
    DOCUMENT_NOT_RELATED_TO_USER("EKYCDocumentRejectReason.DOCUMENT_NOT_RELATED_TO_USER"),
    DOCUMENT_NOT_READABLE("EKYCDocumentRejectReason.DOCUMENT_NOT_READABLE"),
    DOCUMENT_NOT_DECISIVE("EKYCDocumentRejectReason.DOCUMENT_NOT_DECISIVE"),
    DOCUMENT_SUSPICIOUS("EKYCDocumentRejectReason.DOCUMENT_SUSPICIOUS"),
    DOCUMENT_VENDOR_UPLOAD_FAILED("EKYCDocumentRejectReason.DOCUMENT_VENDOR_UPLOAD_FAILED"),
    DOCUMENT_NOT_COMPLETE("EKYCDocumentRejectReason.DOCUMENT_NOT_COMPLETE"),
    DOCUMENT_CORRECTION_REQUIRED("EKYCDocumentRejectReason.DOCUMENT_CORRECTION_REQUIRED"),
    DOCUMENT_NOT_VALID_WITH_NOTES("EKYCDocumentRejectReason.DOCUMENT_NOT_VALID_WITH_NOTES"),
    DOCUMENT_TYPE_NOT_VALID("EKYCDocumentRejectReason.DOCUMENT_TYPE_NOT_VALID"),
    OTHER("EKYCDocumentRejectReason.OTHER");

    private String contentKeyName;

    DocumentVerificationReason(String contentKeyName) {
        this.contentKeyName = contentKeyName;
    }

    public String getContentKeyName() {
        return contentKeyName;
    }
}
