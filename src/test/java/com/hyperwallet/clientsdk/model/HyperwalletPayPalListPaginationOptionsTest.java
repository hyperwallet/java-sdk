package com.hyperwallet.clientsdk.model;

public class HyperwalletPayPalListPaginationOptionsTest extends BaseModelTest<HyperwalletPayPalAccountListPaginationOptions>{
    protected HyperwalletPayPalAccountListPaginationOptions createBaseModel() {
        HyperwalletPayPalAccountListPaginationOptions options = new HyperwalletPayPalAccountListPaginationOptions();
        options
                .status(HyperwalletPayPalAccount.Status.ACTIVATED);
        return options;
    }

    protected Class<HyperwalletPayPalAccountListPaginationOptions> createModelClass() {
        return HyperwalletPayPalAccountListPaginationOptions.class;
    }
}
