package com.hyperwallet.clientsdk.model;

public class HyperwalletVenmoAccountListPaginationOptionsTest extends BaseModelTest<HyperwalletVenmoAccountListPaginationOptions> {
    protected HyperwalletVenmoAccountListPaginationOptions createBaseModel() {
        HyperwalletVenmoAccountListPaginationOptions options = new HyperwalletVenmoAccountListPaginationOptions();
        options
                .status(HyperwalletVenmoAccount.Status.ACTIVATED)
                .type(HyperwalletVenmoAccount.Type.VENMO_ACCOUNT);

        return options;
    }

    protected Class<HyperwalletVenmoAccountListPaginationOptions> createModelClass() {
        return HyperwalletVenmoAccountListPaginationOptions.class;
    }
}

