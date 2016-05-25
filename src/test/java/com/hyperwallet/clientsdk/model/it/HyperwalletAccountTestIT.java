package com.hyperwallet.clientsdk.model.it;

import com.hyperwallet.clientsdk.BaseTest;
import com.hyperwallet.clientsdk.model.HyperwalletAccount;
import org.junit.Test;

/**
 * Created by rbao on 5/20/16.
 */
public class HyperwalletAccountTestIT extends BaseTest {

    @Test
    public void testGetAccountIT() throws Exception {
        if (hyperwallet == null) {
            return;
        }

        //Todo Integration Test is not ready yet, need to back change expected value.
        HyperwalletAccount account = hyperwallet.getProgramAccount(programToken,accountToken);

    }

}