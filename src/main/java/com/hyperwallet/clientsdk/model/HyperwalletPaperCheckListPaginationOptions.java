package com.hyperwallet.clientsdk.model;


public class HyperwalletPaperCheckListPaginationOptions extends HyperwalletPaginationOptions{

    private HyperwalletPaperCheck.Status status;

    public HyperwalletPaperCheck.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletPaperCheck.Status status) {

        this.status = status;
    }

    public HyperwalletPaperCheckListPaginationOptions status(HyperwalletPaperCheck.Status status) {
        this.status = status;
        return this;
    }

}

