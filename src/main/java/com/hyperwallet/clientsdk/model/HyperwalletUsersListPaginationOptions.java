package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.model.HyperwalletUser.TaxVerificationStatus;

public class HyperwalletUsersListPaginationOptions extends HyperwalletPaginationOptions{

    private String clientUserId;
    private String email;
    private String programToken;
    private HyperwalletUser.Status status;
    private HyperwalletUser.VerificationStatus verificationStatus;
    private HyperwalletUser.TaxVerificationStatus taxVerificationStatus;

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
    }

    public HyperwalletUsersListPaginationOptions clientUserId(String clientUserId) {
        this.clientUserId = clientUserId;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public HyperwalletUsersListPaginationOptions email(String email) {
        this.email = email;
        return this;
    }

    public String getProgramToken() {
        return programToken;
    }

    public void setProgramToken(String programToken) {
        this.programToken = programToken;
    }


    public HyperwalletUsersListPaginationOptions programToken(String programToken) {
        this.programToken = programToken;
        return this;
    }

    public HyperwalletUser.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletUser.Status status) {
        this.status = status;
    }

    public HyperwalletUsersListPaginationOptions status(HyperwalletUser.Status status) {
        this.status = status;
        return this;
    }
    public HyperwalletUser.VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(HyperwalletUser.VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public HyperwalletUsersListPaginationOptions verificationStatus(HyperwalletUser.VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
        return this;
    }

    public TaxVerificationStatus getTaxVerificationStatus() {
        return taxVerificationStatus;
    }

    public void setTaxVerificationStatus(TaxVerificationStatus taxVerificationStatus) {
        this.taxVerificationStatus = taxVerificationStatus;
    }

    public HyperwalletUsersListPaginationOptions taxVerificationStatus(HyperwalletUser.TaxVerificationStatus taxVerificationStatus) {
        this.taxVerificationStatus = taxVerificationStatus;
        return this;
    }
}
