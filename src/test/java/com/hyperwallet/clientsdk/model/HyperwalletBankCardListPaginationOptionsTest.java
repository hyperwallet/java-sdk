package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletBankCardListPaginationOptionsTest extends BaseModelTest<HyperwalletBankCardListPaginationOptions> {
    protected HyperwalletBankCardListPaginationOptions createBaseModel() {
        HyperwalletBankCardListPaginationOptions options = new HyperwalletBankCardListPaginationOptions();
        options
                .createdOn(new Date())
                .type(HyperwalletBankCard.Type.BANK_CARD)
                .status(HyperwalletBankCard.Status.ACTIVATED);
        return options;
    }

    protected Class<HyperwalletBankCardListPaginationOptions> createModelClass() {
        return HyperwalletBankCardListPaginationOptions.class;
    }
}

