package com.hyperwallet.clientsdk.model;

public class HyperwalletStatusTransitionListPaginationOptionsTest extends BaseModelTest<HyperwalletStatusTransitionListPaginationOptions> {

    @Override
    protected HyperwalletStatusTransitionListPaginationOptions createBaseModel() {
        HyperwalletStatusTransitionListPaginationOptions options = new HyperwalletStatusTransitionListPaginationOptions();
        options
                .transition(HyperwalletStatusTransition.Status.ACTIVATED);

        return options;
    }

    @Override
    protected Class<HyperwalletStatusTransitionListPaginationOptions> createModelClass() {
        return HyperwalletStatusTransitionListPaginationOptions.class;
    }
}
