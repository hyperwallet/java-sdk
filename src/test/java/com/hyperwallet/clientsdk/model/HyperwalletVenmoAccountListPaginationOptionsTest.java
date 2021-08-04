package com.hyperwallet.clientsdk.model;

public class HyperwalletVenmoAccountListPaginationOptionsTest extends BaseModelTest<HyperwalletVenmoAccountListPaginationOptions> {

    @Override
    protected HyperwalletVenmoAccountListPaginationOptions createBaseModel() {
        HyperwalletVenmoAccountListPaginationOptions options = new HyperwalletVenmoAccountListPaginationOptions();
        options
                .status(HyperwalletVenmoAccount.Status.ACTIVATED)
                .type(HyperwalletVenmoAccount.Type.VENMO_ACCOUNT);

        return options;
    }

    @Override
    protected Class<HyperwalletVenmoAccountListPaginationOptions> createModelClass() {
        return HyperwalletVenmoAccountListPaginationOptions.class;
    }
}

