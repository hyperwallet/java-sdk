package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletPayPalAccountTest extends BaseModelTest<HyperwalletPayPalAccount> {
    protected HyperwalletPayPalAccount createBaseModel() {
        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount();
        payPalAccount
                .token("test-token")
                .type(HyperwalletTransferMethod.Type.PAYPAL_ACCOUNT)
                .status(HyperwalletTransferMethod.Status.ACTIVATED)
                .createdOn(new Date())
                .transferMethodCountry("test-transfer-method-country")
                .transferMethodCurrency("test-transfer-method-currency")
                .isDefaultTransferMethod(Boolean.FALSE)
                .email("test-user-email")
                .userToken("test-user-token");
        return payPalAccount;
    }

    protected Class<HyperwalletPayPalAccount> createModelClass() {
        return HyperwalletPayPalAccount.class;
    }
}
