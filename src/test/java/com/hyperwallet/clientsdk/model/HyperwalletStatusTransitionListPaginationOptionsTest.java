package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletStatusTransitionListPaginationOptionsTest extends BaseModelTest<HyperwalletStatusTransitionListPaginationOptions> {
    protected HyperwalletStatusTransitionListPaginationOptions createBaseModel() {
        HyperwalletStatusTransitionListPaginationOptions options = new HyperwalletStatusTransitionListPaginationOptions();
        options
                .transition(HyperwalletStatusTransition.Status.ACTIVATED);

        return options;
    }

    protected Class<HyperwalletStatusTransitionListPaginationOptions> createModelClass() {
        return HyperwalletStatusTransitionListPaginationOptions.class;
    }
}
