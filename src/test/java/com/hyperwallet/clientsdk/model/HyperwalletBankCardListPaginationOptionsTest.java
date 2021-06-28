package com.hyperwallet.clientsdk.model;

public class HyperwalletBankCardListPaginationOptionsTest extends BaseModelTest<HyperwalletBankCardListPaginationOptions> {
    protected HyperwalletBankCardListPaginationOptions createBaseModel() {
        HyperwalletBankCardListPaginationOptions options = new HyperwalletBankCardListPaginationOptions();
        options
                .status(HyperwalletBankCard.Status.ACTIVATED);
        return options;
    }

    protected Class<HyperwalletBankCardListPaginationOptions> createModelClass() {
        return HyperwalletBankCardListPaginationOptions.class;
    }
}
