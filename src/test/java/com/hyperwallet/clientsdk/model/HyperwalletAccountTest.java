package com.hyperwallet.clientsdk.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fkrauthan
 */
public class HyperwalletAccountTest extends BaseModelTest<HyperwalletAccount> {
    protected HyperwalletAccount createBaseModel() {
        List<HyperwalletLink> hyperwalletLinkList = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLinkList.add(hyperwalletLink);
        HyperwalletAccount account = new HyperwalletAccount();
        account
                .token("test-token")
                .type(HyperwalletAccount.EType.FUNDING)
                .createdOn(new Date())
                .links(hyperwalletLinkList)
                .email("test-email");
        return account;
    }

    protected Class<HyperwalletAccount> createModelClass() {
        return HyperwalletAccount.class;
    }
}
