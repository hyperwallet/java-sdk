package com.hyperwallet.clientsdk.model;

import org.testng.annotations.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HyperwalletAuthenticationTokenTest{
    @Test
    public void testHyperwalletAuthenticationToken() {
        HyperwalletAuthenticationToken authenticationToken = new HyperwalletAuthenticationToken();
        authenticationToken.setValue("test-token");
        assertThat(authenticationToken.getValue(), is(equalTo("test-token")));
    }
}
