package com.hyperwallet.clientsdk.model;

public class HyperwalletBankAccountListPaginationOptionsTest extends BaseModelTest<HyperwalletBankAccountListPaginationOptions> {
    protected HyperwalletBankAccountListPaginationOptions createBaseModel() {
        HyperwalletBankAccountListPaginationOptions options = new HyperwalletBankAccountListPaginationOptions();
        options
                .type(HyperwalletBankAccount.Type.BANK_ACCOUNT)
                .status(HyperwalletBankAccount.Status.ACTIVATED)
                .transition(HyperwalletBankAccount.Status.ACTIVATED);
        return options;
    }

    protected Class<HyperwalletBankAccountListPaginationOptions> createModelClass() {
        return HyperwalletBankAccountListPaginationOptions.class;
    }
}

