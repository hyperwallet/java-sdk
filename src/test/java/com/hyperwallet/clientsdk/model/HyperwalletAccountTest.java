package com.hyperwallet.clientsdk.model;

import java.util.Date;

/**
 * @author fkrauthan
 */
public class HyperwalletAccountTest extends BaseModelTest<HyperwalletAccount> {
    protected HyperwalletAccount createBaseModel() {
        HyperwalletAccount account = new HyperwalletAccount();
        account
                .token("test-token")
                .type(HyperwalletAccount.EType.FUNDING)
                .createdOn(new Date())
                .email("test-email");
        return account;
    }

    protected Class<HyperwalletAccount> createModelClass() {
        return HyperwalletAccount.class;
    }
}
