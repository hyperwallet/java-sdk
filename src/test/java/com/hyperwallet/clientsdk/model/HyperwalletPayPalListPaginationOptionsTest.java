package com.hyperwallet.clientsdk.model;

public class HyperwalletPayPalListPaginationOptionsTest extends BaseModelTest<HyperwalletPayPalAccountListPaginationOptions> {

    @Override
    protected HyperwalletPayPalAccountListPaginationOptions createBaseModel() {
        HyperwalletPayPalAccountListPaginationOptions options = new HyperwalletPayPalAccountListPaginationOptions();
        options
                .status(HyperwalletPayPalAccount.Status.ACTIVATED);
        return options;
    }

    @Override
    protected Class<HyperwalletPayPalAccountListPaginationOptions> createModelClass() {
        return HyperwalletPayPalAccountListPaginationOptions.class;
    }
}
