package com.hyperwallet.clientsdk.model;

import org.testng.annotations.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HyperwalletWebhookNotificationTest {

    @Test
    public void testHyperwalletWebhookEvent() {
        HyperwalletWebhookNotification event = new HyperwalletWebhookNotification();
        assertThat(event.getType(), is(nullValue()));
        assertThat(event.getCreatedOn(), is(nullValue()));
        assertThat(event.getObject(), is(nullValue()));
        assertThat(event.getToken(), is(nullValue()));

        Date createdOn = new Date();

        event.setCreatedOn(createdOn);
        event.setToken("wbh-token");
        event.setObject("{ payment: token }");
        event.setType(HyperwalletWebhookNotification.Type.BANK_ACCOUNT_UPDATED.toString());

        assertThat(event.getToken(), is(equalTo("wbh-token")));
        assertThat(event.getObject().toString(), is(equalTo("{ payment: token }")));
        assertThat(event.getCreatedOn(), is(equalTo(createdOn)));
        assertThat(event.getType(), is(equalTo(HyperwalletWebhookNotification.Type.BANK_ACCOUNT_UPDATED.toString())));
        assertThat(HyperwalletWebhookNotification.Type.BANK_ACCOUNT_UPDATED, is(equalTo(HyperwalletWebhookNotification.Type.getType(event.getType()))));
    }

}
