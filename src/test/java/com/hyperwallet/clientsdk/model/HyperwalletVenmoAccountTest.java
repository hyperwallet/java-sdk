package com.hyperwallet.clientsdk.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HyperwalletVenmoAccountTest extends BaseModelTest<HyperwalletVenmoAccount> {

    @Override
    protected HyperwalletVenmoAccount createBaseModel() {

        List<HyperwalletLink> hyperwalletLinkList = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLinkList.add(hyperwalletLink);

        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();
        venmoAccount
                .token("test-token")
                .type(HyperwalletTransferMethod.Type.VENMO_ACCOUNT)
                .accountId("9620766696")
                .status(HyperwalletTransferMethod.Status.ACTIVATED)
                .createdOn(new Date())
                .transferMethodCountry("test-transfer-method-country")
                .transferMethodCurrency("test-transfer-method-currency")
                .isDefaultTransferMethod(Boolean.FALSE)
                .links(hyperwalletLinkList)
                .userToken("test-user-token");
        return venmoAccount;
    }

    @Override
    protected Class<HyperwalletVenmoAccount> createModelClass() {
        return HyperwalletVenmoAccount.class;
    }

}
