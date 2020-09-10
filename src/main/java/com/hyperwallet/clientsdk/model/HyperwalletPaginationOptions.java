package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletPaginationOptions {
/*
    private String clientPaymentId;
    private String clientTransferId;
    private String clientUserId;
    private String currency;
    private String destinationToken;
    private String email;
    private String inventoryControlNumber;
    private Boolean isBusinessContact;
    private Boolean isDirector;
    private Boolean isUltimateBeneficialOwner;
    private String programToken;
    private String sourceToken;
    private String status;
    private String transition;
    private String type;
    private String userToken;
    private String verificationStatus;
*/
    private Date createdBefore;
    private Date createdAfter;
    private String sortBy;
    private Integer offset;
    private Integer limit;

/*
    public String getClientPaymentId() {
        return clientPaymentId;
    }

    public void setClientPaymentId(String clientPaymentId) {
        this.clientPaymentId = clientPaymentId;
    }

    public HyperwalletPaginationOptions clientPaymentId(String clientPaymentId) {
        this.clientPaymentId = clientPaymentId;
        return this;
    }

    public String getClientTransferId() {
        return clientTransferId;
    }

    public void setClientTransferId(String clientTransferId) {
        this.clientTransferId = clientTransferId;
    }

    public HyperwalletPaginationOptions clientTransferId(String clientTransferId) {
        this.clientTransferId = clientTransferId;
        return this;
    }

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public HyperwalletPaginationOptions clientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public HyperwalletPaginationOptions currency(String currency) {
        this.currency = currency;
        return this;
    }
    public String getDestinationToken() {
        return destinationToken;
    }

    public void setDestinationToken(String destinationToken) {
        this.destinationToken = destinationToken;
    }
    public HyperwalletPaginationOptions destinationToken(String destinationToken) {
        this.destinationToken = destinationToken;
        return this;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HyperwalletPaginationOptions email(String email) {
        this.destinationToken = email;
        return this;
    }


    public String getInventoryControlNumber() {
        return inventoryControlNumber;
    }

    public void setInventoryControlNumber(String inventoryControlNumber) {
        this.inventoryControlNumber = inventoryControlNumber;
    }


    public HyperwalletPaginationOptions inventoryControlNumber(String inventoryControlNumber) {
        this.inventoryControlNumber = inventoryControlNumber;
        return this;
    }

    public Boolean getIsBusinessContact() {
        return isBusinessContact;
    }

    public void setIsBusinessContact(Boolean isBusinessContact) {
        this.isBusinessContact = isBusinessContact;
    }

    public HyperwalletPaginationOptions isBusinessContact(Boolean isBusinessContact) {
        this.isBusinessContact = isBusinessContact;
        return this;
    }

    public Boolean getIsDirector() {
        return isDirector;
    }

    public void setIsDirector(Boolean isDirector) {
        this.isDirector = isDirector;
    }

    public HyperwalletPaginationOptions isDirector(Boolean isDirector) {
        this.isDirector = isDirector;
        return this;
    }

    public Boolean getIsUltimateBeneficialOwner() {
        return isUltimateBeneficialOwner;
    }

    public void setIsUltimateBeneficialOwner(Boolean isUltimateBeneficialOwner) {
        this.isUltimateBeneficialOwner = isUltimateBeneficialOwner;
    }


    public HyperwalletPaginationOptions isUltimateBeneficialOwner(Boolean isUltimateBeneficialOwner) {
        this.isUltimateBeneficialOwner = isUltimateBeneficialOwner;
        return this;
    }

    public String getProgramToken() {
        return programToken;
    }

    public void setProgramToken(String programToken) {
        this.programToken = programToken;
    }


    public HyperwalletPaginationOptions programToken(String programToken) {
        this.programToken = programToken;
        return this;
    }

    public String getSourceToken() {
        return sourceToken;
    }

    public void setSourceToken(String sourceToken) {
        this.sourceToken = sourceToken;
    }

    public HyperwalletPaginationOptions sourceToken(String sourceToken) {
        this.sourceToken = sourceToken;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public HyperwalletPaginationOptions status(String status) {
        this.transition = status;
        return this;
    }

    public String getTransition() {
        return transition;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

    public HyperwalletPaginationOptions transition(String transition) {
        this.transition = transition;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HyperwalletPaginationOptions type(String type) {
        this.type = type;
        return this;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }
    public HyperwalletPaginationOptions userToken(String userToken) {
        this.userToken = userToken;
        return this;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public HyperwalletPaginationOptions verificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
        return this;
    }
*/
    public Date getCreatedBefore() {
        return createdBefore;
    }

    public void setCreatedBefore(Date createdBefore) {
        this.createdBefore = createdBefore;
    }

    public HyperwalletPaginationOptions createdBefore(Date createdBefore) {
        this.createdBefore = createdBefore;
        return this;
    }

    public Date getCreatedAfter() {
        return createdAfter;
    }

    public void setCreatedAfter(Date createdAfter) {
        this.createdAfter = createdAfter;
    }

    public HyperwalletPaginationOptions createdAfter(Date createdAfter) {
        this.createdAfter = createdAfter;
        return this;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public HyperwalletPaginationOptions sortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public HyperwalletPaginationOptions offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public HyperwalletPaginationOptions limit(Integer limit) {
        this.limit = limit;
        return this;
    }
}
