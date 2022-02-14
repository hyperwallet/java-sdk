package com.hyperwallet.clientsdk;

import com.hyperwallet.clientsdk.model.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class HyperwalletFT {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z");

    private Hyperwallet client;
    // Fill in with appropriate data to perform functional tests
    private final String username = "";
    private final String password = "";
    private final String baseURL = "";
    private final String prgmToken = "";


    @BeforeMethod
    public void setUp() {
        if (!username.isEmpty())
            client = new Hyperwallet(username, password, prgmToken, baseURL);
    }

    @Test
    public void testListWebhookEvents() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletList<HyperwalletWebhookNotification> returnValue;

            try {
                returnValue = client.listWebhookEvents();
            } catch (Exception e) {
                throw e;
            }

            assertThat(returnValue.hasNextPage(), is(equalTo(true)));
            assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
            assertThat(returnValue.getLimit(), is(equalTo(100)));
        }
    }

    @Test
    public void testProxyListWebhookEvents() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletList<HyperwalletWebhookNotification> returnValue;

            try {
                client.setHyperwalletProxy("localhost", 9090);
                returnValue = client.listWebhookEvents();
            } catch (Exception e) {
                throw e;
            }

            assertThat(returnValue.hasNextPage(), is(equalTo(true)));
            assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
            assertThat(returnValue.getLimit(), is(equalTo(100)));
        }
    }

    @Test
    public void testCreateUserWithInvalidToken() throws Exception {
        if (!username.isEmpty()) {
            Date dateOfBirth = dateFormat.parse("2000-09-08T15:01:07 UTC");

            HyperwalletUser hyperwalletUser = new HyperwalletUser()
                    .addressLine1("1234 IndividualAddress St")
                    .city("Test")
                    .clientUserId("1234")
                    .country("US")
                    .dateOfBirth(dateOfBirth)
                    .email("abc@company.com")
                    .firstName("John")
                    .lastName("Smith")
                    .postalCode("12345")
                    .profileType(HyperwalletUser.ProfileType.INDIVIDUAL)
                    .governmentId("333333333")
                    .phoneNumber("605-555-1323")
                    .programToken("invalidToken")
                    .stateProvince("CA")
                    .governmentIdType(HyperwalletUser.GovernmentIdType.PASSPORT);

            HyperwalletUser returnValue;
            try {
                returnValue = client.createUser(hyperwalletUser);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("You must provide a valid program token")));
            }
        }
    }

    @Test
    public void testProxyCreateUserWithInvalidToken() throws Exception {
        if (!username.isEmpty()) {
            Date dateOfBirth = dateFormat.parse("2000-09-08T15:01:07 UTC");

            HyperwalletUser hyperwalletUser = new HyperwalletUser()
                    .addressLine1("1234 IndividualAddress St")
                    .city("Test")
                    .clientUserId("1234")
                    .country("US")
                    .dateOfBirth(dateOfBirth)
                    .email("abc@company.com")
                    .firstName("John")
                    .lastName("Smith")
                    .postalCode("12345")
                    .profileType(HyperwalletUser.ProfileType.INDIVIDUAL)
                    .governmentId("333333333")
                    .phoneNumber("605-555-1323")
                    .programToken("invalidToken")
                    .stateProvince("CA")
                    .governmentIdType(HyperwalletUser.GovernmentIdType.PASSPORT);

            HyperwalletUser returnValue;
            try {
                client.setHyperwalletProxy("localhost", 9090);
                returnValue = client.createUser(hyperwalletUser);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("You must provide a valid program token")));
            }
        }
    }
}
