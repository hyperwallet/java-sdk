package com.hyperwallet.clientsdk.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HyperwalletVenmoAccountTest extends BaseModelTest<HyperwalletVenmoAccount> {

    protected HyperwalletVenmoAccount createBaseModel() {

        List<HyperwalletLink> hyperwalletLinkList = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLinkList.add(hyperwalletLink);

        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();
        venmoAccount
                .token("test-token")
                .type(HyperwalletVenmoAccount.Type.VENMO_ACCOUNT)
                .accountId("9620766696")
                .status(HyperwalletVenmoAccount.Status.ACTIVATED)
                .createdOn(new Date())
                .transferMethodCountry("test-transfer-method-country")
                .transferMethodCurrency("test-transfer-method-currency")
                .isDefaultTransferMethod(Boolean.FALSE)
                .links(hyperwalletLinkList)
                .userToken("test-user-token");
        return venmoAccount;
    }

    protected Class<HyperwalletVenmoAccount> createModelClass() {
        return HyperwalletVenmoAccount.class;
    }

}
