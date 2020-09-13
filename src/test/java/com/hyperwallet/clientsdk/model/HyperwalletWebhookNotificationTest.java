package com.hyperwallet.clientsdk.model;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        assertThat(event.getLinks(), is(nullValue()));

        Date createdOn = new Date();

        event.setCreatedOn(createdOn);
        event.setToken("wbh-token");
        event.setObject("{ payment: token }");
        event.setType(HyperwalletWebhookNotification.Type.BANK_ACCOUNT_UPDATED.toString());
        List<HyperwalletLink> hyperwalletLinkList = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLinkList.add(hyperwalletLink);
        event.setLinks(hyperwalletLinkList);


        assertThat(event.getToken(), is(equalTo("wbh-token")));
        assertThat(event.getObject().toString(), is(equalTo("{ payment: token }")));
        assertThat(event.getCreatedOn(), is(equalTo(createdOn)));
        assertThat(event.getType(), is(equalTo(HyperwalletWebhookNotification.Type.BANK_ACCOUNT_UPDATED.toString())));
        assertThat(HyperwalletWebhookNotification.Type.BANK_ACCOUNT_UPDATED, is(equalTo(HyperwalletWebhookNotification.Type.getType(event.getType()))));
        assertThat(event.getLinks(), is(equalTo(hyperwalletLinkList)));
    }

}
