package com.hyperwallet.clientsdk.model;


public class HyperwalletListPaginationOptionsTest extends BaseModelTest<HyperwalletListPaginationOptions> {

    @Override
    protected HyperwalletListPaginationOptions createBaseModel() {
        HyperwalletListPaginationOptions options = new HyperwalletListPaginationOptions();
        options
                .status(HyperwalletTransferMethod.Status.ACTIVATED);

        return options;
    }

    @Override
    protected Class<HyperwalletListPaginationOptions> createModelClass() {
        return HyperwalletListPaginationOptions.class;
    }
}

