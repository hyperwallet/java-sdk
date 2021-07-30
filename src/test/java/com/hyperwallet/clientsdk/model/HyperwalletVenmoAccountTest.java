package com.hyperwallet.clientsdk.model;


import java.util.Date;

public class HyperwalletVenmoAccountTest extends BaseModelTest<HyperwalletVenmoAccount> {

    protected HyperwalletVenmoAccount createBaseModel() {
        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();
        venmoAccount
                .token("test-token")
                .type(HyperwalletVenmoAccount.Type.VENMO_ACCOUNT)
                .accountId("9620766696")
                .status(HyperwalletVenmoAccount.Status.ACTIVATED)
                .transition(HyperwalletVenmoAccount.Status.ACTIVATED)
                .createdOn(new Date())
                .transferMethodCountry("test-transfer-method-country")
                .transferMethodCurrency("test-transfer-method-currency")
                .isDefaultTransferMethod(Boolean.FALSE)
                .userToken("test-user-token");
        return venmoAccount;
    }

    protected Class<HyperwalletVenmoAccount> createModelClass() {
        return HyperwalletVenmoAccount.class;
    }

}
