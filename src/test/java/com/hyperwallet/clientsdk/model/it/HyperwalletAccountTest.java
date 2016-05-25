package com.hyperwallet.clientsdk.model.it;

import com.hyperwallet.clientsdk.BaseTest;
import com.hyperwallet.clientsdk.model.HyperwalletAccount;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by rbao on 5/20/16.
 */
public class HyperwalletAccountTest extends BaseTest {

    @Test
    public void testGetAccountIT() throws Exception {
        if (hyperwallet == null) {
            return;
        }

        //Todo Integration Test is not ready yet, need to back change expected value.
        HyperwalletAccount account = hyperwallet.getAccount(programToken,accountToken);
        Assert.assertNotNull(account);
        Assert.assertEquals(accountToken, account.getToken());
        Assert.assertEquals("email",account.getEmail());
        Assert.assertEquals(HyperwalletAccount.EType.FUNDING,account.getToken());

    }

}