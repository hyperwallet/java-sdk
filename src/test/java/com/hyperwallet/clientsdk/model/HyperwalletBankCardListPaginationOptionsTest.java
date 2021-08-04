package com.hyperwallet.clientsdk.model;

<<<<<<< HEAD
import com.hyperwallet.clientsdk.model.HyperwalletBankCard.Type;

public class HyperwalletBankCardListPaginationOptionsTest extends BaseModelTest<HyperwalletBankCardListPaginationOptions> {
    protected HyperwalletBankCardListPaginationOptions createBaseModel() {
        HyperwalletBankCardListPaginationOptions options = new HyperwalletBankCardListPaginationOptions();
        options
                .type(Type.BANK_CARD)
=======
public class HyperwalletBankCardListPaginationOptionsTest extends BaseModelTest<HyperwalletBankCardListPaginationOptions> {

    @Override
    protected HyperwalletBankCardListPaginationOptions createBaseModel() {
        HyperwalletBankCardListPaginationOptions options = new HyperwalletBankCardListPaginationOptions();
        options
>>>>>>> eeeca2b2e7d3148d6ebed160923ed7f7157002a0
                .status(HyperwalletBankCard.Status.ACTIVATED);
        return options;
    }

<<<<<<< HEAD
=======
    @Override
>>>>>>> eeeca2b2e7d3148d6ebed160923ed7f7157002a0
    protected Class<HyperwalletBankCardListPaginationOptions> createModelClass() {
        return HyperwalletBankCardListPaginationOptions.class;
    }
}
