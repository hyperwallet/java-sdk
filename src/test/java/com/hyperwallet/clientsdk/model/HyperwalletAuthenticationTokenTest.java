package com.hyperwallet.clientsdk.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class HyperwalletAuthenticationTokenTest{

    @Test
    public void testAuthenticationToken() {

        HyperwalletAuthenticationToken authenticationToken = new HyperwalletAuthenticationToken();
        assertThat(authenticationToken.getValue(), is(nullValue()));

    }

}
