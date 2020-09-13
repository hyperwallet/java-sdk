package com.hyperwallet.clientsdk.model;

/**
 * @author fkrauthan
 */
public class HyperwalletBankAccountsListPaginationOptionsTest extends BaseModelTest<HyperwalletBankAccountsListPaginationOptions> {
    protected HyperwalletBankAccountsListPaginationOptions createBaseModel() {
        HyperwalletBankAccountsListPaginationOptions options = new HyperwalletBankAccountsListPaginationOptions();
        options
                .type(HyperwalletBankAccount.Type.BANK_ACCOUNT)
                .status(HyperwalletBankAccount.Status.ACTIVATED);

        return options;
    }

    protected Class<HyperwalletBankAccountsListPaginationOptions> createModelClass() {
        return HyperwalletBankAccountsListPaginationOptions.class;
    }
}

