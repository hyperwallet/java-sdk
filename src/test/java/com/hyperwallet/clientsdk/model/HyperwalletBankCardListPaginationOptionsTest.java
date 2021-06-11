package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.model.HyperwalletBankCard.Type;

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
