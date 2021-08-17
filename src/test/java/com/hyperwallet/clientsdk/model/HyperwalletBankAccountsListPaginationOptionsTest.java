package com.hyperwallet.clientsdk.model;


public class HyperwalletBankAccountsListPaginationOptionsTest extends BaseModelTest<HyperwalletBankAccountsListPaginationOptions> {

    @Override
    protected HyperwalletBankAccountsListPaginationOptions createBaseModel() {
        HyperwalletBankAccountsListPaginationOptions options = new HyperwalletBankAccountsListPaginationOptions();
        options
                .type(HyperwalletBankAccount.Type.BANK_ACCOUNT)
                .status(HyperwalletBankAccount.Status.ACTIVATED);

        return options;
    }

    @Override
    protected Class<HyperwalletBankAccountsListPaginationOptions> createModelClass() {
        return HyperwalletBankAccountsListPaginationOptions.class;
    }
}

