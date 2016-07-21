package com.hyperwallet.clientsdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.model.*;
import com.hyperwallet.clientsdk.util.HyperwalletApiClient;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.testng.Assert.fail;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author fkrauthan
 */
public class HyperwalletTest {

    @Test
    public void testConstructor_noProgramToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        validateHyperwalletVariables(client, "test-username", "test-password", "https://api.sandbox.hyperwallet.com/rest/v3", null);
    }

    @Test
    public void testConstructor_withProgramToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token");
        validateHyperwalletVariables(client, "test-username", "test-password", "https://api.sandbox.hyperwallet.com/rest/v3", "test-program-token");
    }

    @Test
    public void testConstructor_defaultServer() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token", "");
        validateHyperwalletVariables(client, "test-username", "test-password", "https://api.sandbox.hyperwallet.com/rest/v3", "test-program-token");
    }

    @Test
    public void testConstructor_withCustomServer() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token", "http://test.de");
        validateHyperwalletVariables(client, "test-username", "test-password", "http://test.de/rest/v3", "test-program-token");
    }

    //--------------------------------------
    // TLS verification
    //--------------------------------------

    @Test
    public void testLisUser_noTLSIssues() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listUsers();
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("INCORRECT_LOGIN_CREDENTIALS")));
        }
    }

    //--------------------------------------
    // Users
    //--------------------------------------

    @Test
    public void testCreateUser_noUser() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createUser(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User is required")));
            assertThat(e.getMessage(), is(equalTo("User is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreateUser_userTokenSpecified() {
        HyperwalletUser user = new HyperwalletUser();
        user.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createUser(user);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token may not be present")));
            assertThat(e.getMessage(), is(equalTo("User token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreateUser_withoutProgramToken() throws Exception {
        HyperwalletUser user = new HyperwalletUser();
        user.setStatus(HyperwalletUser.Status.ACTIVATED);
        user.setCreatedOn(new Date());
        user.setFirstName("test-first-name");

        HyperwalletUser userResponse = new HyperwalletUser();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(userResponse);

        HyperwalletUser resp = client.createUser(user);
        assertThat(resp, is(equalTo(userResponse)));

        ArgumentCaptor<HyperwalletUser> argument = ArgumentCaptor.forClass(HyperwalletUser.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users"), argument.capture(), Mockito.eq(user.getClass()));

        HyperwalletUser apiClientUser = argument.getValue();
        assertThat(apiClientUser, is(notNullValue()));
        assertThat(apiClientUser.getFirstName(), is(equalTo("test-first-name")));
        assertThat(apiClientUser.getStatus(), is(nullValue()));
        assertThat(apiClientUser.getCreatedOn(), is(nullValue()));
        assertThat(apiClientUser.getProgramToken(), is(nullValue()));
    }

    @Test
    public void testCreateUser_withProgramTokenAddedByDefault() throws Exception {
        HyperwalletUser user = new HyperwalletUser();
        user.setStatus(HyperwalletUser.Status.ACTIVATED);
        user.setCreatedOn(new Date());
        user.setFirstName("test-first-name");

        HyperwalletUser userResponse = new HyperwalletUser();

        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(userResponse);

        HyperwalletUser resp = client.createUser(user);
        assertThat(resp, is(equalTo(userResponse)));

        ArgumentCaptor<HyperwalletUser> argument = ArgumentCaptor.forClass(HyperwalletUser.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users"), argument.capture(), Mockito.eq(user.getClass()));

        HyperwalletUser apiClientUser = argument.getValue();
        assertThat(apiClientUser, is(notNullValue()));
        assertThat(apiClientUser.getFirstName(), is(equalTo("test-first-name")));
        assertThat(apiClientUser.getStatus(), is(nullValue()));
        assertThat(apiClientUser.getCreatedOn(), is(nullValue()));
        assertThat(apiClientUser.getProgramToken(), is(equalTo("test-program-token")));
    }

    @Test
    public void testCreateUser_withProgramTokenInUserObject() throws Exception {
        HyperwalletUser user = new HyperwalletUser();
        user.setStatus(HyperwalletUser.Status.ACTIVATED);
        user.setCreatedOn(new Date());
        user.setFirstName("test-first-name");
        user.setProgramToken("test-program-token2");

        HyperwalletUser userResponse = new HyperwalletUser();

        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(userResponse);

        HyperwalletUser resp = client.createUser(user);
        assertThat(resp, is(equalTo(userResponse)));

        ArgumentCaptor<HyperwalletUser> argument = ArgumentCaptor.forClass(HyperwalletUser.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users"), argument.capture(), Mockito.eq(user.getClass()));

        HyperwalletUser apiClientUser = argument.getValue();
        assertThat(apiClientUser, is(notNullValue()));
        assertThat(apiClientUser.getFirstName(), is(equalTo("test-first-name")));
        assertThat(apiClientUser.getStatus(), is(nullValue()));
        assertThat(apiClientUser.getCreatedOn(), is(nullValue()));
        assertThat(apiClientUser.getProgramToken(), is(equalTo("test-program-token2")));
    }

    @Test
    public void testGetUser_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getUser(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetUser_successful() throws Exception {
        HyperwalletUser userResponse = new HyperwalletUser();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(userResponse);

        HyperwalletUser resp = client.getUser("test-user-token");
        assertThat(resp, is(equalTo(userResponse)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token", userResponse.getClass());
    }

    @Test
    public void testUpdateUser_noUser() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updateUser(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User is required")));
            assertThat(e.getMessage(), is(equalTo("User is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateUser_noUserToken() {
        HyperwalletUser user = new HyperwalletUser();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updateUser(user);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateUser_successful() throws Exception {
        HyperwalletUser user = new HyperwalletUser();
        user.setToken("test-user-token");
        user.setFirstName("test-first-name");

        HyperwalletUser userResponse = new HyperwalletUser();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.put(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(userResponse);

        HyperwalletUser resp = client.updateUser(user);
        assertThat(resp, is(equalTo(userResponse)));

        Mockito.verify(mockApiClient).put("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token", user, user.getClass());
    }

    @Test
    public void testListUsers_noParameters() throws Exception {
        HyperwalletList<HyperwalletUser> response = new HyperwalletList<HyperwalletUser>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletUser> resp = client.listUsers();
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListUsers_withParameters() throws Exception {
        HyperwalletList<HyperwalletUser> response = new HyperwalletList<HyperwalletUser>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletUser> resp = client.listUsers(options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5&limit=10"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListUsers_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletUser> response = new HyperwalletList<HyperwalletUser>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletUser> resp = client.listUsers(options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5"), Mockito.any(TypeReference.class));
    }

    //--------------------------------------
    // Prepaid Cards
    //--------------------------------------

    @Test
    public void testCreatePrepaidCard_noPrepaidCard() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPrepaidCard(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePrepaidCard_noUserToken() {
        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPrepaidCard(prepaidCard);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePrepaidCard_prepaidCardTokenSpecified() {
        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard();
        prepaidCard.setUserToken("test-user-token");
        prepaidCard.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPrepaidCard(prepaidCard);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePrepaidCard_noType() throws Exception {
        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard();
        prepaidCard.setUserToken("test-user-token");
        prepaidCard.setStatus(HyperwalletTransferMethod.Status.ACTIVATED);
        prepaidCard.setCreatedOn(new Date());
        prepaidCard.setCardType(HyperwalletPrepaidCard.CardType.VIRTUAL);
        prepaidCard.setTransferMethodCountry("test-transfer-method-country");
        prepaidCard.setTransferMethodCurrency("test-transfer-method-currency");
        prepaidCard.setCardNumber("test-card-number");
        prepaidCard.setCardBrand(HyperwalletPrepaidCard.Brand.VISA);
        prepaidCard.setDateOfExpiry(new Date());
        prepaidCard.setCardPackage("test-card-package");

        HyperwalletPrepaidCard prepaidCardResponse = new HyperwalletPrepaidCard();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(prepaidCardResponse);

        HyperwalletPrepaidCard resp = client.createPrepaidCard(prepaidCard);
        assertThat(resp, is(equalTo(prepaidCardResponse)));

        ArgumentCaptor<HyperwalletPrepaidCard> argument = ArgumentCaptor.forClass(HyperwalletPrepaidCard.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards"), argument.capture(), Mockito.eq(prepaidCard.getClass()));

        HyperwalletPrepaidCard apiClientPrepaidCard = argument.getValue();
        assertThat(apiClientPrepaidCard, is(notNullValue()));
        assertThat(apiClientPrepaidCard.getType(), is(equalTo(HyperwalletTransferMethod.Type.PREPAID_CARD)));
        assertThat(apiClientPrepaidCard.getCardPackage(), is(equalTo("test-card-package")));
        assertThat(apiClientPrepaidCard.getStatus(), is(nullValue()));
        assertThat(apiClientPrepaidCard.getCreatedOn(), is(nullValue()));
        assertThat(apiClientPrepaidCard.getCardType(), is(nullValue()));
        assertThat(apiClientPrepaidCard.getCardBrand(), is(nullValue()));
        assertThat(apiClientPrepaidCard.getTransferMethodCountry(), is(nullValue()));
        assertThat(apiClientPrepaidCard.getTransferMethodCurrency(), is(nullValue()));
        assertThat(apiClientPrepaidCard.getDateOfExpiry(), is(nullValue()));
    }

    @Test
    public void testCreatePrepaidCard_withType() throws Exception {
        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard();
        prepaidCard.setUserToken("test-user-token");
        prepaidCard.setType(HyperwalletTransferMethod.Type.BANK_ACCOUNT);
        prepaidCard.setStatus(HyperwalletTransferMethod.Status.ACTIVATED);
        prepaidCard.setCreatedOn(new Date());
        prepaidCard.setCardType(HyperwalletPrepaidCard.CardType.VIRTUAL);
        prepaidCard.setTransferMethodCountry("test-transfer-method-country");
        prepaidCard.setTransferMethodCurrency("test-transfer-method-currency");
        prepaidCard.setCardNumber("test-card-number");
        prepaidCard.setCardBrand(HyperwalletPrepaidCard.Brand.VISA);
        prepaidCard.setDateOfExpiry(new Date());
        prepaidCard.setCardPackage("test-card-package");

        HyperwalletPrepaidCard prepaidCardResponse = new HyperwalletPrepaidCard();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(prepaidCardResponse);

        HyperwalletPrepaidCard resp = client.createPrepaidCard(prepaidCard);
        assertThat(resp, is(equalTo(prepaidCardResponse)));

        ArgumentCaptor<HyperwalletPrepaidCard> argument = ArgumentCaptor.forClass(HyperwalletPrepaidCard.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards"), argument.capture(), Mockito.eq(prepaidCard.getClass()));

        HyperwalletPrepaidCard apiClientPrepaidCard = argument.getValue();
        assertThat(apiClientPrepaidCard, is(notNullValue()));
        assertThat(apiClientPrepaidCard.getType(), is(equalTo(HyperwalletTransferMethod.Type.BANK_ACCOUNT)));
        assertThat(apiClientPrepaidCard.getCardPackage(), is(equalTo("test-card-package")));
        assertThat(apiClientPrepaidCard.getStatus(), is(nullValue()));
        assertThat(apiClientPrepaidCard.getCreatedOn(), is(nullValue()));
        assertThat(apiClientPrepaidCard.getCardType(), is(nullValue()));
        assertThat(apiClientPrepaidCard.getCardBrand(), is(nullValue()));
        assertThat(apiClientPrepaidCard.getTransferMethodCountry(), is(nullValue()));
        assertThat(apiClientPrepaidCard.getTransferMethodCurrency(), is(nullValue()));
        assertThat(apiClientPrepaidCard.getDateOfExpiry(), is(nullValue()));
    }

    @Test
    public void testGetPrepaidCard_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPrepaidCard(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetPrepaidCard_noPrepaidCardToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPrepaidCard("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetPrepaidCard_successful() throws Exception {
        HyperwalletPrepaidCard prepaidCardResponse = new HyperwalletPrepaidCard();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(prepaidCardResponse);

        HyperwalletPrepaidCard resp = client.getPrepaidCard("test-user-token", "test-prepaid-card-token");
        assertThat(resp, is(equalTo(prepaidCardResponse)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards/test-prepaid-card-token", prepaidCardResponse.getClass());
    }

    @Test
    public void testListPrepaidCards_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPrepaidCards(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListPrepaidCards_noParameters() throws Exception {
        HyperwalletList<HyperwalletPrepaidCard> response = new HyperwalletList<HyperwalletPrepaidCard>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPrepaidCard> resp = client.listPrepaidCards("test-user-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPrepaidCards_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPrepaidCards(null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListPrepaidCards_withParameters() throws Exception {
        HyperwalletList<HyperwalletPrepaidCard> response = new HyperwalletList<HyperwalletPrepaidCard>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPrepaidCard> resp = client.listPrepaidCards("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5&limit=10"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPrepaidCards_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletPrepaidCard> response = new HyperwalletList<HyperwalletPrepaidCard>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPrepaidCard> resp = client.listPrepaidCards("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5"), Mockito.any(TypeReference.class));
    }

    @Test(dataProvider = "prepaidCardStatusTransitions")
    public void testPrepaidCardStatusTransitionMethods_noUserToken(String methodName, HyperwalletStatusTransition.Status expectedTransition) throws Exception {
        assertThat(expectedTransition, is(notNullValue()));

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            try {
                Method method = client.getClass().getMethod(methodName + "PrepaidCard", String.class, String.class);
                method.invoke(client, null, null);
            } catch (InvocationTargetException e) {
                throw (Exception) e.getCause();
            }
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test(dataProvider = "prepaidCardStatusTransitions")
    public void testPrepaidCardStatusTransitionMethods_noPrepaidCardToken(String methodName, HyperwalletStatusTransition.Status expectedTransition) throws Exception {
        assertThat(expectedTransition, is(notNullValue()));

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            try {
                Method method = client.getClass().getMethod(methodName + "PrepaidCard", String.class, String.class);
                method.invoke(client, "test-user-token", null);
            } catch (InvocationTargetException e) {
                throw (Exception) e.getCause();
            }
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test(dataProvider = "prepaidCardStatusTransitions")
    public void testPrepaidCardStatusTransitionMethods_successful(String methodName, HyperwalletStatusTransition.Status expectedTransition) throws Exception {
        HyperwalletStatusTransition statusTransitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(statusTransitionResponse);

        Method method = client.getClass().getMethod(methodName + "PrepaidCard", String.class, String.class);
        HyperwalletStatusTransition resp = (HyperwalletStatusTransition)method.invoke(client, "test-user-token", "test-prepaid-card-token");
        assertThat(resp, is(equalTo(statusTransitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards/test-prepaid-card-token/status-transitions"), argument.capture(), Mockito.eq(statusTransitionResponse.getClass()));

        HyperwalletStatusTransition apiClientStatusTransition = argument.getValue();
        assertThat(apiClientStatusTransition, is(notNullValue()));
        assertThat(apiClientStatusTransition.getTransition(), is(equalTo(expectedTransition)));
    }

    @Test
    public void testCreatePrepaidCardStatusTransition_noTransition() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPrepaidCardStatusTransition(null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition is required")));
            assertThat(e.getMessage(), is(equalTo("Transition is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePrepaidCardStatusTransition_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPrepaidCardStatusTransition(null, null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePrepaidCardStatusTransition_noPrepaidCardToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPrepaidCardStatusTransition("test-user-token", null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePrepaidCardStatusTransition_transitionTokenSpecified() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPrepaidCardStatusTransition("test-user-token", "test-prepaid-card-token", transition);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePrepaidCardStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setFromStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setCreatedOn(new Date());
        transition.setTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED);

        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.createPrepaidCardStatusTransition("test-user-token", "test-prepaid-card-token", transition);
        assertThat(resp, is(equalTo(transitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards/test-prepaid-card-token/status-transitions"), argument.capture(), Mockito.eq(transition.getClass()));

        HyperwalletStatusTransition apiClientTransition = argument.getValue();
        assertThat(apiClientTransition, is(notNullValue()));
        assertThat(apiClientTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
        assertThat(apiClientTransition.getFromStatus(), is(nullValue()));
        assertThat(apiClientTransition.getToStatus(), is(nullValue()));
        assertThat(apiClientTransition.getCreatedOn(), is(nullValue()));
    }

    @Test
    public void testGetPrepaidCardStatusTransition_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPrepaidCardStatusTransition(null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetPrepaidCardStatusTransition_noPrepaidCardToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPrepaidCardStatusTransition("test-user-token", null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetPrepaidCardStatusTransition_noTransitionToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPrepaidCardStatusTransition("test-user-token", "test-prepaid-card-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetPrepaidCardStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.getPrepaidCardStatusTransition("test-user-token", "test-prepaid-card-token", "test-status-transition-token");
        assertThat(resp, is(equalTo(transitionResponse)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards/test-prepaid-card-token/status-transitions/test-status-transition-token", transitionResponse.getClass());
    }

    @Test
    public void testListPrepaidCardStatusTransitions_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPrepaidCardStatusTransitions(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListPrepaidCardStatusTransitions_noParameters_noPrepaidCardToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPrepaidCardStatusTransitions("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListPrepaidCardStatusTransitions_noParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPrepaidCardStatusTransitions("test-user-token", "test-prepaid-card-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards/test-prepaid-card-token/status-transitions"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPrepaidCardStatusTransitions_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPrepaidCardStatusTransitions(null, null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListPrepaidCardStatusTransitions_withParameters_noPrepaidCardToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPrepaidCardStatusTransitions("test-user-token", null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListPrepaidCardStatisTransitions_withParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPrepaidCardStatusTransitions("test-user-token", "test-prepaid-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards/test-prepaid-card-token/status-transitions?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5&limit=10"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPrepaidCardStatusTransitions_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPrepaidCardStatusTransitions("test-user-token", "test-prepaid-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards/test-prepaid-card-token/status-transitions?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5"), Mockito.any(TypeReference.class));
    }
    
    //--------------------------------------
    // Bank Accounts
    //--------------------------------------

    @Test
    public void testCreateBankAccount_noBankAccount() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankAccount(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Account is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Account is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankAccount_noUserToken() {
        HyperwalletBankAccount bankAccount = new HyperwalletBankAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankAccount(bankAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankAccount_bankAccountTokenSpecified() {
        HyperwalletBankAccount bankAccount = new HyperwalletBankAccount();
        bankAccount.setUserToken("test-user-token");
        bankAccount.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankAccount(bankAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Account token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Bank Account token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankAccount_successful() throws Exception {
        HyperwalletBankAccount bankAccount = new HyperwalletBankAccount();
        bankAccount.setUserToken("test-user-token");
        bankAccount.setStatus(HyperwalletBankAccount.Status.ACTIVATED);
        bankAccount.setCreatedOn(new Date());
        bankAccount.setFirstName("test-first-name");

        HyperwalletBankAccount bankAccountResponse = new HyperwalletBankAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(bankAccountResponse);

        HyperwalletBankAccount resp = client.createBankAccount(bankAccount);
        assertThat(resp, is(equalTo(bankAccountResponse)));

        ArgumentCaptor<HyperwalletBankAccount> argument = ArgumentCaptor.forClass(HyperwalletBankAccount.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/bank-accounts"), argument.capture(), Mockito.eq(bankAccount.getClass()));

        HyperwalletBankAccount apiClientBankAccount = argument.getValue();
        assertThat(apiClientBankAccount, is(notNullValue()));
        assertThat(apiClientBankAccount.getFirstName(), is(equalTo("test-first-name")));
        assertThat(apiClientBankAccount.getStatus(), is(nullValue()));
        assertThat(apiClientBankAccount.getCreatedOn(), is(nullValue()));
    }

    @Test
    public void testGetBankAccount_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getBankAccount(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetBankAccount_noBankAccountToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getBankAccount("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetBankAccount_successful() throws Exception {
        HyperwalletBankAccount bankAccountResponse = new HyperwalletBankAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(bankAccountResponse);

        HyperwalletBankAccount resp = client.getBankAccount("test-user-token", "test-prepaid-card-token");
        assertThat(resp, is(equalTo(bankAccountResponse)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/bank-accounts/test-prepaid-card-token", bankAccountResponse.getClass());
    }


    @Test
    public void testUpdateBankAccount_noBankAccount() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updateBankAccount(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Account is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Account is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateBankAccount_noUserToken() {
        HyperwalletBankAccount bankAccount = new HyperwalletBankAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updateBankAccount(bankAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateBankAccount_noBankAccountToken() {
        HyperwalletBankAccount bankAccount = new HyperwalletBankAccount();
        bankAccount.setUserToken("test-user-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updateBankAccount(bankAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateBankAccount_successful() throws Exception {
        HyperwalletBankAccount bankAccount = new HyperwalletBankAccount();
        bankAccount.setToken("test-bank-account-token");
        bankAccount.setUserToken("test-user-token");
        bankAccount.setFirstName("test-first-name");

        HyperwalletBankAccount bankAccountResponse = new HyperwalletBankAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.put(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(bankAccountResponse);

        HyperwalletBankAccount resp = client.updateBankAccount(bankAccount);
        assertThat(resp, is(equalTo(bankAccountResponse)));

        ArgumentCaptor<HyperwalletBankAccount> argument = ArgumentCaptor.forClass(HyperwalletBankAccount.class);
        Mockito.verify(mockApiClient).put(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/bank-accounts/test-bank-account-token"), argument.capture(), Mockito.eq(bankAccount.getClass()));

        HyperwalletBankAccount apiClientBankAccount = argument.getValue();
        assertThat(apiClientBankAccount, is(notNullValue()));
        assertThat(apiClientBankAccount.getFirstName(), is(equalTo("test-first-name")));
    }

    @Test
    public void testListBankAccounts_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBankAccounts(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListBankAccounts_noParameters() throws Exception {
        HyperwalletList<HyperwalletBankAccount> response = new HyperwalletList<HyperwalletBankAccount>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBankAccount> resp = client.listBankAccounts("test-user-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/bank-accounts"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBankAccounts_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBankAccounts(null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListBankAccounts_withParameters() throws Exception {
        HyperwalletList<HyperwalletBankAccount> response = new HyperwalletList<HyperwalletBankAccount>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBankAccount> resp = client.listBankAccounts("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/bank-accounts?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5&limit=10"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBankAccounts_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletBankAccount> response = new HyperwalletList<HyperwalletBankAccount>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBankAccount> resp = client.listBankAccounts("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/bank-accounts?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testDeactivateBankAccount_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.deactivateBankAccount(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testDeactivateBankAccount_noBankAccountToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.deactivateBankAccount("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testDeactivateBankAccount_successful() throws Exception {
        HyperwalletStatusTransition statusTransitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(statusTransitionResponse);

        HyperwalletStatusTransition resp = client.deactivateBankAccount("test-user-token", "test-bank-account-token");
        assertThat(resp, is(equalTo(statusTransitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/bank-accounts/test-bank-account-token/status-transitions"), argument.capture(), Mockito.eq(statusTransitionResponse.getClass()));

        HyperwalletStatusTransition apiClientStatusTransition = argument.getValue();
        assertThat(apiClientStatusTransition, is(notNullValue()));
        assertThat(apiClientStatusTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
    }

    @Test
    public void testCreateBankAccountStatusTransition_noTransition() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankAccountStatusTransition(null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition is required")));
            assertThat(e.getMessage(), is(equalTo("Transition is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankAccountStatusTransition_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankAccountStatusTransition(null, null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankAccountStatusTransition_noBankAccountToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankAccountStatusTransition("test-user-token", null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankAccountStatusTransition_transitionTokenSpecified() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankAccountStatusTransition("test-user-token", "test-bank-account-token", transition);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankAccountStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setFromStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setCreatedOn(new Date());
        transition.setTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED);

        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.createBankAccountStatusTransition("test-user-token", "test-bank-account-token", transition);
        assertThat(resp, is(equalTo(transitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/bank-accounts/test-bank-account-token/status-transitions"), argument.capture(), Mockito.eq(transition.getClass()));

        HyperwalletStatusTransition apiClientTransition = argument.getValue();
        assertThat(apiClientTransition, is(notNullValue()));
        assertThat(apiClientTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
        assertThat(apiClientTransition.getFromStatus(), is(nullValue()));
        assertThat(apiClientTransition.getToStatus(), is(nullValue()));
        assertThat(apiClientTransition.getCreatedOn(), is(nullValue()));
    }


    @Test
    public void testListBankAccountStatusTransitions_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBankAccountStatusTransitions(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListBankAccountStatusTransitions_noParameters_noBankAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBankAccountStatusTransitions("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListBankAccountStatusTransitions_noParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listBankAccountStatusTransitions("test-user-token", "test-bank-account-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/bank-accounts/test-bank-account-token/status-transitions"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBankAccountStatusTransitions_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBankAccountStatusTransitions(null, null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListBankAccountStatusTransitions_withParameters_noBankAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBankAccountStatusTransitions("test-user-token", null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListBankAccountStatisTransitions_withParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listBankAccountStatusTransitions("test-user-token", "test-bank-account-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/bank-accounts/test-bank-account-token/status-transitions?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5&limit=10"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBankAccountStatusTransitions_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listBankAccountStatusTransitions("test-user-token", "test-bank-account-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/bank-accounts/test-bank-account-token/status-transitions?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5"), Mockito.any(TypeReference.class));
    }

    //--------------------------------------
    // Balances
    //--------------------------------------

    @Test
    public void testListBalancesForUser_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBalancesForUser(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListBalancesForUser_noParameters() throws Exception {
        HyperwalletList<HyperwalletBalance> response = new HyperwalletList<HyperwalletBalance>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBalance> resp = client.listBalancesForUser("test-user-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/balances"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBalancesForUser_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBalancesForUser(null, new HyperwalletBalanceListOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListBalancesForUser_withParameters() throws Exception {
        HyperwalletList<HyperwalletBalance> response = new HyperwalletList<HyperwalletBalance>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletBalanceListOptions options = new HyperwalletBalanceListOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .limit(10)
                .currency("test-currency");

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBalance> resp = client.listBalancesForUser("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/balances?currency=test-currency&sortBy=test-sort-by&offset=5&limit=10"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBalancesForUser_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletBalance> response = new HyperwalletList<HyperwalletBalance>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletBalanceListOptions options = new HyperwalletBalanceListOptions();
        options
                .sortBy("test-sort-by")
                .offset(5);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBalance> resp = client.listBalancesForUser("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/balances?sortBy=test-sort-by&offset=5"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBalancesForPrepaidCard_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBalancesForPrepaidCard(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListBalancesForPrepaidCard_noParameters_noPrepaidCardToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBalancesForPrepaidCard("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListBalancesForPrepaidCard_noParameters() throws Exception {
        HyperwalletList<HyperwalletBalance> response = new HyperwalletList<HyperwalletBalance>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBalance> resp = client.listBalancesForPrepaidCard("test-user-token", "test-prepaid-card-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards/test-prepaid-card-token/balances"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBalancesForPrepaidCard_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBalancesForPrepaidCard(null, null, new HyperwalletBalanceListOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListBalancesForPrepaidCard_withParameters_noPrepaidCardToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBalancesForPrepaidCard("test-user-token", null, new HyperwalletBalanceListOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListBalancesForPrepaidCard_withParameters() throws Exception {
        HyperwalletList<HyperwalletBalance> response = new HyperwalletList<HyperwalletBalance>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletBalanceListOptions options = new HyperwalletBalanceListOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .limit(10);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBalance> resp = client.listBalancesForPrepaidCard("test-user-token", "test-prepaid-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards/test-prepaid-card-token/balances?sortBy=test-sort-by&offset=5&limit=10"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBalancesForPrepaidCard_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletBalance> response = new HyperwalletList<HyperwalletBalance>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletBalanceListOptions options = new HyperwalletBalanceListOptions();
        options
                .sortBy("test-sort-by")
                .offset(5);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBalance> resp = client.listBalancesForPrepaidCard("test-user-token", "test-prepaid-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards/test-prepaid-card-token/balances?sortBy=test-sort-by&offset=5"), Mockito.any(TypeReference.class));
    }

    //--------------------------------------
    // Payments
    //--------------------------------------

    @Test
    public void testCreatePayment_noPayment() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPayment(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Payment is required")));
            assertThat(e.getMessage(), is(equalTo("Payment is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePayment_paymentTokenSpecified() {
        HyperwalletPayment payment = new HyperwalletPayment();
        payment.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPayment(payment);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Payment token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Payment token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePayment_withoutProgramToken() throws Exception {
        HyperwalletPayment payment = new HyperwalletPayment();
        payment.setCreatedOn(new Date());
        payment.setCurrency("test-currency");

        HyperwalletPayment paymentResponse = new HyperwalletPayment();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(paymentResponse);

        HyperwalletPayment resp = client.createPayment(payment);
        assertThat(resp, is(equalTo(paymentResponse)));

        ArgumentCaptor<HyperwalletPayment> argument = ArgumentCaptor.forClass(HyperwalletPayment.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/payments"), argument.capture(), Mockito.eq(payment.getClass()));

        HyperwalletPayment apiClientPayment = argument.getValue();
        assertThat(apiClientPayment, is(notNullValue()));
        assertThat(apiClientPayment.getCurrency(), is(equalTo("test-currency")));
        assertThat(apiClientPayment.getCreatedOn(), is(nullValue()));
        assertThat(apiClientPayment.getProgramToken(), is(nullValue()));
    }

    @Test
    public void testCreatePayment_withProgramTokenAddedByDefault() throws Exception {
        HyperwalletPayment payment = new HyperwalletPayment();
        payment.setCreatedOn(new Date());
        payment.setCurrency("test-currency");

        HyperwalletPayment paymentResponse = new HyperwalletPayment();

        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(paymentResponse);

        HyperwalletPayment resp = client.createPayment(payment);
        assertThat(resp, is(equalTo(paymentResponse)));

        ArgumentCaptor<HyperwalletPayment> argument = ArgumentCaptor.forClass(HyperwalletPayment.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/payments"), argument.capture(), Mockito.eq(payment.getClass()));

        HyperwalletPayment apiClientPayment = argument.getValue();
        assertThat(apiClientPayment, is(notNullValue()));
        assertThat(apiClientPayment.getCurrency(), is(equalTo("test-currency")));
        assertThat(apiClientPayment.getCreatedOn(), is(nullValue()));
        assertThat(apiClientPayment.getProgramToken(), is(equalTo("test-program-token")));
    }

    @Test
    public void testCreatePayment_withProgramTokenInPaymentObject() throws Exception {
        HyperwalletPayment payment = new HyperwalletPayment();
        payment.setCreatedOn(new Date());
        payment.setCurrency("test-currency");
        payment.setProgramToken("test-program-token2");

        HyperwalletPayment paymentResponse = new HyperwalletPayment();

        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(paymentResponse);

        HyperwalletPayment resp = client.createPayment(payment);
        assertThat(resp, is(equalTo(paymentResponse)));

        ArgumentCaptor<HyperwalletPayment> argument = ArgumentCaptor.forClass(HyperwalletPayment.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/payments"), argument.capture(), Mockito.eq(payment.getClass()));

        HyperwalletPayment apiClientPayment = argument.getValue();
        assertThat(apiClientPayment, is(notNullValue()));
        assertThat(apiClientPayment.getCurrency(), is(equalTo("test-currency")));
        assertThat(apiClientPayment.getCreatedOn(), is(nullValue()));
        assertThat(apiClientPayment.getProgramToken(), is(equalTo("test-program-token2")));
    }

    @Test
    public void testGetPayment_noPaymentToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPayment(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Payment token is required")));
            assertThat(e.getMessage(), is(equalTo("Payment token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetPayment_successful() throws Exception {
        HyperwalletPayment paymentResponse = new HyperwalletPayment();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(paymentResponse);

        HyperwalletPayment resp = client.getPayment("test-payment-token");
        assertThat(resp, is(equalTo(paymentResponse)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v3/payments/test-payment-token", paymentResponse.getClass());
    }

    @Test
    public void testListPayments_noParameters() throws Exception {
        HyperwalletList<HyperwalletPayment> response = new HyperwalletList<HyperwalletPayment>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPayment> resp = client.listPayments();
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/payments"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPayments_withParameters() throws Exception {
        HyperwalletList<HyperwalletPayment> response = new HyperwalletList<HyperwalletPayment>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaymentListOptions options = new HyperwalletPaymentListOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPayment> resp = client.listPayments(options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/payments?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5&limit=10"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPayments_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletPayment> response = new HyperwalletList<HyperwalletPayment>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaymentListOptions options = new HyperwalletPaymentListOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPayment> resp = client.listPayments(options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/payments?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5"), Mockito.any(TypeReference.class));
    }

    //--------------------------------------
    // Programs
    //--------------------------------------

    @Test
    public void testGetProgram_noProgramToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getProgram(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Program token is required")));
            assertThat(e.getMessage(), is(equalTo("Program token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetProgram_successful() throws Exception {
        HyperwalletProgram programResponse = new HyperwalletProgram();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(programResponse);

        HyperwalletProgram resp = client.getProgram("test-program-token");
        assertThat(resp, is(equalTo(programResponse)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v3/programs/test-program-token", programResponse.getClass());
    }

    //--------------------------------------
    // Program Accounts
    //--------------------------------------

    @Test
    public void testGetProgramAccount_noProgramToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getProgramAccount(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Program token is required")));
            assertThat(e.getMessage(), is(equalTo("Program token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetProgramAccount_noAccountToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getProgramAccount("test-program-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetProgramAccount_successful() throws Exception {
        HyperwalletAccount accountResponse = new HyperwalletAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(accountResponse);

        HyperwalletAccount resp = client.getProgramAccount("test-program-token", "test-account-token");
        assertThat(resp, is(equalTo(accountResponse)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v3/programs/test-program-token/accounts/test-account-token", accountResponse.getClass());
    }

    //--------------------------------------
    // Transfer Method Configurations
    //--------------------------------------

    @Test
    public void testGetTransferMethodConfiguration_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getTransferMethodConfiguration(null, null, null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetTransferMethodConfiguration_noCountry() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getTransferMethodConfiguration("test-user-token", null, null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Country is required")));
            assertThat(e.getMessage(), is(equalTo("Country is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetTransferMethodConfiguration_noCurrency() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getTransferMethodConfiguration("test-user-token", "test-country", null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Currency is required")));
            assertThat(e.getMessage(), is(equalTo("Currency is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetTransferMethodConfiguration_noType() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getTransferMethodConfiguration("test-user-token", "test-country", "test-currency", null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Type is required")));
            assertThat(e.getMessage(), is(equalTo("Type is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetTransferMethodConfiguration_noProfileType() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getTransferMethodConfiguration("test-user-token", "test-country", "test-currency", HyperwalletTransferMethod.Type.BANK_ACCOUNT, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Profile Type is required")));
            assertThat(e.getMessage(), is(equalTo("Profile Type is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testGetTransferMethodConfiguration_successful() throws Exception {
        HyperwalletTransferMethodConfiguration configResponse = new HyperwalletTransferMethodConfiguration();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(configResponse);

        HyperwalletTransferMethodConfiguration resp = client.getTransferMethodConfiguration("test-user-token", "test-country", "test-currency", HyperwalletTransferMethod.Type.BANK_ACCOUNT, HyperwalletUser.ProfileType.INDIVIDUAL);
        assertThat(resp, is(equalTo(configResponse)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v3/transfer-method-configurations?userToken=test-user-token&country=test-country&currency=test-currency&type=BANK_ACCOUNT&profileType=INDIVIDUAL", configResponse.getClass());
    }

    @Test
    public void testListTransferMethodConfigurations_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listTransferMethodConfigurations(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListTransferMethodConfigurations_noParameters_successful() throws Exception {
        HyperwalletList<HyperwalletTransferMethodConfiguration> response = new HyperwalletList<HyperwalletTransferMethodConfiguration>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletTransferMethodConfiguration> resp = client.listTransferMethodConfigurations("test-user-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/transfer-method-configurations?userToken=test-user-token"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListTransferMethodConfigurations_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listTransferMethodConfigurations(null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListTransferMethodConfigurations_withParameters_successful() throws Exception {
        HyperwalletList<HyperwalletTransferMethodConfiguration> response = new HyperwalletList<HyperwalletTransferMethodConfiguration>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletTransferMethodConfiguration> resp = client.listTransferMethodConfigurations("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/transfer-method-configurations?userToken=test-user-token&createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5&limit=10"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListTransferMethodConfigurations_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletTransferMethodConfiguration> response = new HyperwalletList<HyperwalletTransferMethodConfiguration>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletTransferMethodConfiguration> resp = client.listTransferMethodConfigurations("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/transfer-method-configurations?userToken=test-user-token&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5"), Mockito.any(TypeReference.class));
    }

    //--------------------------------------
    // Receipts
    //--------------------------------------

    @Test
    public void testListProgramAccountReceipts_noParameters_noProgramToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listProgramAccountReceipts(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Program token is required")));
            assertThat(e.getMessage(), is(equalTo("Program token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListProgramAccountReceipts_noParameters_noAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listProgramAccountReceipts("test-account-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListProgramAccountReceipts_noParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listProgramAccountReceipts("test-program-token", "test-account-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/programs/test-program-token/accounts/test-account-token/receipts"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListProgramAccountReceipts_withParameters_noProgramToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listProgramAccountReceipts(null, null, new HyperwalletReceiptPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Program token is required")));
            assertThat(e.getMessage(), is(equalTo("Program token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListProgramAccountReceipts_withParameters_noAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listProgramAccountReceipts("test-program-token", null, new HyperwalletReceiptPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListProgramAccountReceipts_withParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .type(HyperwalletReceipt.Type.ANNUAL_FEE)
                .sortBy("test-sort-by")
                .offset(5)
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listProgramAccountReceipts("test-program-token", "test-account-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/programs/test-program-token/accounts/test-account-token/receipts?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5&limit=10&type=ANNUAL_FEE"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListProgramAccountReceipts_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listProgramAccountReceipts("test-program-token", "test-account-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/programs/test-program-token/accounts/test-account-token/receipts?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListUserReceipts_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listUserReceipts(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListUserReceipts_noParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listUserReceipts("test-user-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/receipts"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListUserReceipts_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listUserReceipts(null, new HyperwalletReceiptPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListUserReceipts_withParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .type(HyperwalletReceipt.Type.ANNUAL_FEE)
                .sortBy("test-sort-by")
                .offset(5)
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listUserReceipts("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/receipts?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5&limit=10&type=ANNUAL_FEE"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListUserReceipts_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listUserReceipts("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/receipts?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPrepaidCardReceipts_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPrepaidCardReceipts(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListPrepaidCardReceipts_noParameters_noPrepaidCardToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPrepaidCardReceipts("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid card token is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListPrepaidCardReceipts_noParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listPrepaidCardReceipts("test-user-token", "test-prepaid-card-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards/test-prepaid-card-token/receipts"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPrepaidCardReceipts_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPrepaidCardReceipts(null, null, new HyperwalletReceiptPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListPrepaidCardReceipts_withParameters_noPrepaidCardToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPrepaidCardReceipts("test-user-token", null, new HyperwalletReceiptPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid card token is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
        }
    }

    @Test
    public void testListPrepaidCardReceipts_withParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .type(HyperwalletReceipt.Type.ANNUAL_FEE)
                .sortBy("test-sort-by")
                .offset(5)
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listPrepaidCardReceipts("test-user-token", "test-prepaid-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards/test-prepaid-card-token/receipts?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5&limit=10&type=ANNUAL_FEE"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPrepaidCardReceipts_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .sortBy("test-sort-by")
                .offset(5)
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listPrepaidCardReceipts("test-user-token", "test-prepaid-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v3/users/test-user-token/prepaid-cards/test-prepaid-card-token/receipts?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&offset=5"), Mockito.any(TypeReference.class));
    }

    //--------------------------------------
    // Internal utils
    //--------------------------------------

    private HyperwalletApiClient createAndInjectHyperwalletApiClientMock(Hyperwallet client) throws Exception {
        HyperwalletApiClient mock = Mockito.mock(HyperwalletApiClient.class);

        Field apiClientField = client.getClass().getDeclaredField("apiClient");
        apiClientField.setAccessible(true);
        apiClientField.set(client, mock);

        return mock;
    }

    private Date convertStringToDate(String date) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.parse(date);
    }

    private void validateHyperwalletVariables(Hyperwallet client, String expectedUsername, String expectedPassword, String expectedUrl, String expectedProgramToken) throws Exception {
        Field urlField = client.getClass().getDeclaredField("url");
        Field programTokenField = client.getClass().getDeclaredField("programToken");
        Field apiClientField = client.getClass().getDeclaredField("apiClient");

        urlField.setAccessible(true);
        String url = (String) urlField.get(client);

        programTokenField.setAccessible(true);
        String programToken = (String) programTokenField.get(client);

        apiClientField.setAccessible(true);
        HyperwalletApiClient apiClient = (HyperwalletApiClient) apiClientField.get(client);

        assertThat(apiClient, is(notNullValue()));

        Field usernameField = apiClient.getClass().getDeclaredField("username");
        Field passwordField = apiClient.getClass().getDeclaredField("password");
        Field versionField = apiClient.getClass().getDeclaredField("version");

        usernameField.setAccessible(true);
        String username = (String) usernameField.get(apiClient);

        passwordField.setAccessible(true);
        String password = (String) passwordField.get(apiClient);

        versionField.setAccessible(true);
        String version = (String) versionField.get(apiClient);


        assertThat(url, is(equalTo(expectedUrl)));
        assertThat(programToken, is(equalTo(expectedProgramToken)));
        assertThat(username, is(equalTo(expectedUsername)));
        assertThat(password, is(equalTo(expectedPassword)));
        assertThat(version, is(equalTo(Hyperwallet.VERSION)));
    }

    @DataProvider(name = "prepaidCardStatusTransitions")
    public Object[][] createPrepaidCardStatusTransitions() {
        return new Object[][] {
                { "suspend", HyperwalletStatusTransition.Status.SUSPENDED },
                { "unsuspend", HyperwalletStatusTransition.Status.UNSUSPENDED },
                { "lostOrStolen", HyperwalletStatusTransition.Status.LOST_OR_STOLEN },
                { "deactivate", HyperwalletStatusTransition.Status.DE_ACTIVATED },
                { "lock", HyperwalletStatusTransition.Status.LOCKED },
                { "unlock", HyperwalletStatusTransition.Status.UNLOCKED },
        };
    }


}
