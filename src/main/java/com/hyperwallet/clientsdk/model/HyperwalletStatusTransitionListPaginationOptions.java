package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.model.HyperwalletStatusTransition.Status;

public class HyperwalletStatusTransitionListPaginationOptions extends HyperwalletPaginationOptions {

    private HyperwalletStatusTransition.Status transition;

    public Status getTransition() {
        return transition;
    }

    public void setTransition(Status transition) {
        this.transition = transition;
    }

    public HyperwalletStatusTransitionListPaginationOptions status(HyperwalletStatusTransition.Status transition) {
        this.transition = transition;
        return this;
    }

}
