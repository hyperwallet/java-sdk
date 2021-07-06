package com.hyperwallet.clientsdk.model;


public class HyperwalletPrepaidCardListPaginationOptionsTest extends BaseModelTest<HyperwalletPrepaidCardListPaginationOptions> {
    protected HyperwalletPrepaidCardListPaginationOptions createBaseModel() {
        HyperwalletPrepaidCardListPaginationOptions options = new HyperwalletPrepaidCardListPaginationOptions();
        options
                .status(HyperwalletPrepaidCard.Status.ACTIVATED);

        return options;
    }

    protected Class<HyperwalletPrepaidCardListPaginationOptions> createModelClass() {
        return HyperwalletPrepaidCardListPaginationOptions.class;
    }
}

