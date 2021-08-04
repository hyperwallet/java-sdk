package com.hyperwallet.clientsdk.model;

public class HyperwalletBankCardListPaginationOptionsTest extends BaseModelTest<HyperwalletBankCardListPaginationOptions> {

    @Override
    protected HyperwalletBankCardListPaginationOptions createBaseModel() {
        HyperwalletBankCardListPaginationOptions options = new HyperwalletBankCardListPaginationOptions();
        options
                .status(HyperwalletBankCard.Status.ACTIVATED);
        return options;
    }

    @Override
    protected Class<HyperwalletBankCardListPaginationOptions> createModelClass() {
        return HyperwalletBankCardListPaginationOptions.class;
    }
}
