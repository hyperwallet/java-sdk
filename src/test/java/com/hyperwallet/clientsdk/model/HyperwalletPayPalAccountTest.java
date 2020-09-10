package com.hyperwallet.clientsdk.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HyperwalletPayPalAccountTest extends BaseModelTest<HyperwalletPayPalAccount> {
    protected HyperwalletPayPalAccount createBaseModel() {
        List<HyperwalletLink> hyperwalletLinkList = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLinkList.add(hyperwalletLink);
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
                .links(hyperwalletLinkList)
                .userToken("test-user-token");
        return payPalAccount;
    }

    protected Class<HyperwalletPayPalAccount> createModelClass() {
        return HyperwalletPayPalAccount.class;
    }
}
