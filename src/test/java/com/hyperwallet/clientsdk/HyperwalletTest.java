package com.hyperwallet.clientsdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.model.*;
import com.hyperwallet.clientsdk.model.HyperwalletDocument.ECountryCode;
import com.hyperwallet.clientsdk.model.HyperwalletDocument.EDocumentCategory;
import com.hyperwallet.clientsdk.model.HyperwalletDocument.EIdentityVerificationType;
import com.hyperwallet.clientsdk.model.HyperwalletDocument.EKycDocumentVerificationStatus;
import com.hyperwallet.clientsdk.model.HyperwalletUser.VerificationStatus;
import com.hyperwallet.clientsdk.util.HyperwalletApiClient;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * @author fkrauthan
 */
public class HyperwalletTest {

    @Test
    public void testConstructor_noProgramToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        validateHyperwalletVariables(client, "test-username", "test-password", "https://api.sandbox.hyperwallet.com/rest/v4", null);
    }

    @Test
    public void testConstructor_withProgramToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token");
        validateHyperwalletVariables(client, "test-username", "test-password", "https://api.sandbox.hyperwallet.com/rest/v4", "test-program-token");
    }

    @Test
    public void testConstructor_defaultServer() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token", "", null);
        validateHyperwalletVariables(client, "test-username", "test-password", "https://api.sandbox.hyperwallet.com/rest/v4", "test-program-token");
    }

    @Test
    public void testConstructor_withCustomServer() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token", "http://test.de", null);
        validateHyperwalletVariables(client, "test-username", "test-password", "http://test.de/rest/v4", "test-program-token");
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateUser_withoutProgramToken() throws Exception {
        HyperwalletUser user = new HyperwalletUser();
        user.setStatus(HyperwalletUser.Status.ACTIVATED);
        user.setVerificationStatus(VerificationStatus.VERIFIED);
        user.setCreatedOn(new Date());
        user.setFirstName("test-first-name");
        user.setBusinessOperatingName("test-business-operating-name");

        HyperwalletUser userResponse = new HyperwalletUser();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(user);

        HyperwalletUser resp = client.createUser(user);
        assertThat(resp, is(equalTo(user)));
        assertThat(resp.getVerificationStatus(), is(equalTo(VerificationStatus.VERIFIED)));

        ArgumentCaptor<HyperwalletUser> argument = ArgumentCaptor.forClass(HyperwalletUser.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users"), argument.capture(), Mockito.eq(user.getClass()));

        HyperwalletUser apiClientUser = argument.getValue();
        assertThat(apiClientUser, is(notNullValue()));
        assertThat(apiClientUser.getFirstName(), is(equalTo("test-first-name")));
        assertThat(apiClientUser.getBusinessOperatingName(), is(equalTo("test-business-operating-name")));
        assertThat(apiClientUser.getStatus(), is(nullValue()));
        assertThat(apiClientUser.getVerificationStatus(), is(VerificationStatus.VERIFIED));
        assertThat(apiClientUser.getCreatedOn(), is(nullValue()));
        assertThat(apiClientUser.getProgramToken(), is(nullValue()));
    }

    @Test
    public void testCreateUser_withProgramTokenAddedByDefault() throws Exception {
        HyperwalletUser user = new HyperwalletUser();
        user.setStatus(HyperwalletUser.Status.ACTIVATED);
        user.setCreatedOn(new Date());
        user.setFirstName("test-first-name");
        user.setBusinessOperatingName("test-business-operating-name");

        HyperwalletUser userResponse = new HyperwalletUser();

        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(userResponse);

        HyperwalletUser resp = client.createUser(user);
        assertThat(resp, is(equalTo(userResponse)));

        ArgumentCaptor<HyperwalletUser> argument = ArgumentCaptor.forClass(HyperwalletUser.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users"), argument.capture(), Mockito.eq(user.getClass()));

        HyperwalletUser apiClientUser = argument.getValue();
        assertThat(apiClientUser, is(notNullValue()));
        assertThat(apiClientUser.getFirstName(), is(equalTo("test-first-name")));
        assertThat(apiClientUser.getBusinessOperatingName(), is(equalTo("test-business-operating-name")));
        assertThat(apiClientUser.getStatus(), is(nullValue()));
        assertThat(apiClientUser.getVerificationStatus(), is(nullValue()));
        assertThat(apiClientUser.getCreatedOn(), is(nullValue()));
        assertThat(apiClientUser.getProgramToken(), is(equalTo("test-program-token")));
    }

    @Test
    public void testCreateUser_withProgramTokenInUserObject() throws Exception {
        HyperwalletUser user = new HyperwalletUser();
        user.setStatus(HyperwalletUser.Status.ACTIVATED);
        user.setCreatedOn(new Date());
        user.setFirstName("test-first-name");
        user.setBusinessOperatingName("test-business-operating-name");
        user.setProgramToken("test-program-token2");

        HyperwalletUser userResponse = new HyperwalletUser();

        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(userResponse);

        HyperwalletUser resp = client.createUser(user);
        assertThat(resp, is(equalTo(userResponse)));

        ArgumentCaptor<HyperwalletUser> argument = ArgumentCaptor.forClass(HyperwalletUser.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users"), argument.capture(), Mockito.eq(user.getClass()));

        HyperwalletUser apiClientUser = argument.getValue();
        assertThat(apiClientUser, is(notNullValue()));
        assertThat(apiClientUser.getFirstName(), is(equalTo("test-first-name")));
        assertThat(apiClientUser.getBusinessOperatingName(), is(equalTo("test-business-operating-name")));
        assertThat(apiClientUser.getStatus(), is(nullValue()));
        assertThat(apiClientUser.getVerificationStatus(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token", userResponse.getClass());
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient).put("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token", user, user.getClass());
    }

    @Test
    public void testListUsers_noParameters() throws Exception {
        HyperwalletList<HyperwalletUser> response = new HyperwalletList<HyperwalletUser>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletUser> resp = client.listUsers();
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListUsers_withParameters() throws Exception {
        HyperwalletList<HyperwalletUser> response = new HyperwalletList<HyperwalletUser>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletUser> resp = client.listUsers(options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListUsers_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletUser> response = new HyperwalletList<HyperwalletUser>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletUser> resp = client.listUsers(options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                        Mockito.any(TypeReference.class));
    }

    @Test
    public void testGetUserStatusTransition_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getUserStatusTransition(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetUserStatusTransition_noTransitionToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getUserStatusTransition("test-user-token",  null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetUserAuthenticationToken_successful() throws Exception {
        HyperwalletAuthenticationToken hyperwalletAuthenticationToken = new HyperwalletAuthenticationToken();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.any(), Mockito.any(Class.class))).thenReturn(hyperwalletAuthenticationToken);

        HyperwalletAuthenticationToken resp = client.getAuthenticationToken("test-user-token");
        assertThat(resp, is(equalTo(hyperwalletAuthenticationToken)));

        Mockito.verify(mockApiClient)
                .post("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/authentication-token", null,
                        hyperwalletAuthenticationToken.getClass());
    }

    @Test
    public void testGetUserAuthenticationToken_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getAuthenticationToken(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetUserStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.getUserStatusTransition("test-user-token","test-status-transition-token");
        assertThat(resp, is(equalTo(transitionResponse)));

        Mockito.verify(mockApiClient)
                .get("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/status-transitions/test-status-transition-token",
                        transitionResponse.getClass());
    }

    @Test
    public void testListUserStatusTransitions_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listUserStatusTransitions(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListUserStatusTransitions_noParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listUserStatusTransitions("test-user-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/status-transitions"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListUserStatusTransitions_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listUserStatusTransitions(null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }


    @Test
    public void testListUserStatisTransitions_withParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
            .sortBy("test-sort-by")
            .limit(10)
            .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
            .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listUserStatusTransitions("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/status-transitions?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListUserStatusTransitions_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
            .sortBy("test-sort-by")
            .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listUserStatusTransitions("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/status-transitions?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
    }

    //--------------------------------------
    // Prepaid Cards
    //--------------------------------------

    @Test
    public void testCreatePrepaidCard_noPrepaidCard() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createOrReplacePrepaidCard(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePrepaidCard_noUserToken() {
        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createOrReplacePrepaidCard(prepaidCard);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePrepaidCard_prepaidCardTokenSpecified() {
        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard();
        prepaidCard.setUserToken("test-user-token");
        prepaidCard.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createOrReplacePrepaidCard(prepaidCard);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        HyperwalletPrepaidCard resp = client.createOrReplacePrepaidCard(prepaidCard);
        assertThat(resp, is(equalTo(prepaidCardResponse)));

        ArgumentCaptor<HyperwalletPrepaidCard> argument = ArgumentCaptor.forClass(HyperwalletPrepaidCard.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards"), argument.capture(),
                        Mockito.eq(prepaidCard.getClass()));

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

        HyperwalletPrepaidCard resp = client.createOrReplacePrepaidCard(prepaidCard);
        assertThat(resp, is(equalTo(prepaidCardResponse)));

        ArgumentCaptor<HyperwalletPrepaidCard> argument = ArgumentCaptor.forClass(HyperwalletPrepaidCard.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards"), argument.capture(),
                        Mockito.eq(prepaidCard.getClass()));

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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token",
                prepaidCardResponse.getClass());
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards"), Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPrepaidCard> resp = client.listPrepaidCards("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPrepaidCards_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletPrepaidCard> response = new HyperwalletList<HyperwalletPrepaidCard>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPrepaidCard> resp = client.listPrepaidCards("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token/status-transitions"),
                argument.capture(), Mockito.eq(statusTransitionResponse.getClass()));

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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token/status-transitions"),
                argument.capture(), Mockito.eq(transition.getClass()));

        HyperwalletStatusTransition apiClientTransition = argument.getValue();
        assertThat(apiClientTransition, is(notNullValue()));
        assertThat(apiClientTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
        assertThat(apiClientTransition.getFromStatus(), is(nullValue()));
        assertThat(apiClientTransition.getToStatus(), is(nullValue()));
        assertThat(apiClientTransition.getCreatedOn(), is(nullValue()));
    }

    @Test
    public void testUpdatePrepaidCard_noPrepaidCard() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updatePrepaidCard(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdatePrepaidCard_noUserToken() {
        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updatePrepaidCard(prepaidCard);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdatePrepaidCard_noPrepaidAccountToken() {
        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard();
        prepaidCard.setUserToken("test-user-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updatePrepaidCard(prepaidCard);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdatePrepaidCardt_successful() throws Exception {
        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard();
        prepaidCard.setToken("test-prepaid-card-token");
        prepaidCard.setUserToken("test-user-token");
        prepaidCard.setTransferMethodCountry("UK");
        prepaidCard.setTransferMethodCurrency("GBR");

        HyperwalletPrepaidCard prepaidCardResponse = new HyperwalletPrepaidCard();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.put(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(prepaidCardResponse);

        HyperwalletPrepaidCard resp = client.updatePrepaidCard(prepaidCard);
        assertThat(resp, is(equalTo(prepaidCardResponse)));

        ArgumentCaptor<HyperwalletPrepaidCard> argument = ArgumentCaptor.forClass(HyperwalletPrepaidCard.class);
        Mockito.verify(mockApiClient).put(
                Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token"),
                argument.capture(),
                Mockito.eq(prepaidCard.getClass()));

        HyperwalletPrepaidCard apiClientBankAccount = argument.getValue();
        assertThat(apiClientBankAccount, is(notNullValue()));
        assertThat(apiClientBankAccount.getTransferMethodCountry(), is(equalTo("UK")));
        assertThat(apiClientBankAccount.getTransferMethodCurrency(), is(equalTo("GBR")));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient)
                .get("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token/status-transitions/test-status-transition-token",
                        transitionResponse.getClass());
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token/status-transitions"),
                Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPrepaidCardStatusTransitions("test-user-token", "test-prepaid-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token/status-transitions?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPrepaidCardStatusTransitions_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPrepaidCardStatusTransitions("test-user-token", "test-prepaid-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token/status-transitions?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
    }

    //--------------------------------------
    // Bank Cards
    //--------------------------------------

    @Test
    public void testCreateBankCard_noBankCard() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankCard(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Card is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Card is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankCard_noUserToken() {
        HyperwalletBankCard bankCard = new HyperwalletBankCard();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankCard(bankCard);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankCard_prepaidCardTokenSpecified() {
        HyperwalletBankCard bankCard = new HyperwalletBankCard();
        bankCard.setUserToken("test-user-token");
        bankCard.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankCard(bankCard);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Card token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Bank Card token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankCard_noType() throws Exception {
        HyperwalletBankCard bankCard = new HyperwalletBankCard();
        bankCard.setUserToken("test-user-token");
        bankCard.setStatus(HyperwalletTransferMethod.Status.ACTIVATED);
        bankCard.setCreatedOn(new Date());
        bankCard.setCardType(HyperwalletBankCard.CardType.DEBIT);
        bankCard.setTransferMethodCountry("test-transfer-method-country");
        bankCard.setTransferMethodCurrency("test-transfer-method-currency");
        bankCard.setCardNumber("test-card-number");
        bankCard.setCardBrand(HyperwalletBankCard.Brand.VISA);
        bankCard.setDateOfExpiry(new Date());
        bankCard.setCvv("cvv");

        HyperwalletBankCard bankCardResponse = new HyperwalletBankCard();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(bankCardResponse);

        HyperwalletBankCard resp = client.createBankCard(bankCard);
        assertThat(resp, is(equalTo(bankCardResponse)));

        ArgumentCaptor<HyperwalletBankCard> argument = ArgumentCaptor.forClass(HyperwalletBankCard.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards"), argument.capture(),
                        Mockito.eq(bankCard.getClass()));

        HyperwalletBankCard apiClientBankCard = argument.getValue();
        assertThat(apiClientBankCard, is(notNullValue()));
        assertThat(apiClientBankCard.getType(), is(equalTo(HyperwalletTransferMethod.Type.BANK_CARD)));
        assertThat(apiClientBankCard.getStatus(), is(nullValue()));
        assertThat(apiClientBankCard.getCreatedOn(), is(nullValue()));
        assertThat(apiClientBankCard.getCardType(), is(nullValue()));
        assertThat(apiClientBankCard.getCardBrand(), is(nullValue()));
    }

    @Test
    public void testCreateBankCard_withType() throws Exception {
        HyperwalletBankCard bankCard = new HyperwalletBankCard();
        bankCard.setUserToken("test-user-token");
        bankCard.setType(HyperwalletTransferMethod.Type.BANK_ACCOUNT);
        bankCard.setStatus(HyperwalletTransferMethod.Status.ACTIVATED);
        bankCard.setCreatedOn(new Date());
        bankCard.setCardType(HyperwalletBankCard.CardType.DEBIT);
        bankCard.setTransferMethodCountry("test-transfer-method-country");
        bankCard.setTransferMethodCurrency("test-transfer-method-currency");
        bankCard.setCardNumber("test-card-number");
        bankCard.setCardBrand(HyperwalletBankCard.Brand.VISA);
        bankCard.setDateOfExpiry(new Date());
        bankCard.setCvv("cvv");

        HyperwalletBankCard bankCardResponse = new HyperwalletBankCard();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(bankCardResponse);

        HyperwalletBankCard resp = client.createBankCard(bankCard);
        assertThat(resp, is(equalTo(bankCardResponse)));

        ArgumentCaptor<HyperwalletBankCard> argument = ArgumentCaptor.forClass(HyperwalletBankCard.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards"), argument.capture(),
                        Mockito.eq(bankCard.getClass()));

        HyperwalletBankCard apiClienBankCard = argument.getValue();
        assertThat(apiClienBankCard, is(notNullValue()));
        assertThat(apiClienBankCard.getType(), is(equalTo(HyperwalletTransferMethod.Type.BANK_ACCOUNT)));
        assertThat(apiClienBankCard.getStatus(), is(nullValue()));
        assertThat(apiClienBankCard.getCreatedOn(), is(nullValue()));
        assertThat(apiClienBankCard.getCardType(), is(nullValue()));
        assertThat(apiClienBankCard.getCardBrand(), is(nullValue()));
    }

    @Test
    public void testUpdateBankCard_noBankCard() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updateBankCard(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Card is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Card is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateBankCard_noUserToken() {
        HyperwalletBankCard bankCard = new HyperwalletBankCard();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updateBankCard(bankCard);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateBankCard_noBankAccountToken() {
        HyperwalletBankCard bankCard = new HyperwalletBankCard();
        bankCard.setUserToken("test-user-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updateBankCard(bankCard);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateBankCardt_successful() throws Exception {
        HyperwalletBankCard bankCard = new HyperwalletBankCard();
        bankCard.setToken("test-bank-card-token");
        bankCard.setUserToken("test-user-token");
        bankCard.setTransferMethodCountry("UK");
        bankCard.setTransferMethodCurrency("GBR");

        HyperwalletBankCard bankCardResponse = new HyperwalletBankCard();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.put(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(bankCardResponse);

        HyperwalletBankCard resp = client.updateBankCard(bankCard);
        assertThat(resp, is(equalTo(bankCardResponse)));

        ArgumentCaptor<HyperwalletBankCard> argument = ArgumentCaptor.forClass(HyperwalletBankCard.class);
        Mockito.verify(mockApiClient)
                .put(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards/test-bank-card-token"),
                        argument.capture(), Mockito.eq(bankCard.getClass()));

        HyperwalletBankCard apiClientBankAccount = argument.getValue();
        assertThat(apiClientBankAccount, is(notNullValue()));
        assertThat(apiClientBankAccount.getTransferMethodCountry(), is(equalTo("UK")));
        assertThat(apiClientBankAccount.getTransferMethodCurrency(), is(equalTo("GBR")));
    }

    @Test
    public void testGetBankCard_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getBankCard(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetBankCard_noBankCardToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getBankCard("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetBankCard_successful() throws Exception {
        HyperwalletBankCard bankCardResponse = new HyperwalletBankCard();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(bankCardResponse);

        HyperwalletBankCard resp = client.getBankCard("test-user-token", "test-bank-card-token");
        assertThat(resp, is(equalTo(bankCardResponse)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards/test-bank-card-token",
                bankCardResponse.getClass());
    }

    @Test
    public void testListBankCards_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBankCards(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListBankCards_noParameters() throws Exception {
        HyperwalletList<HyperwalletBankCard> response = new HyperwalletList<HyperwalletBankCard>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBankCard> resp = client.listBankCards("test-user-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBankCards_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBankCards(null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListBankCards_withParameters() throws Exception {
        HyperwalletList<HyperwalletBankCard> response = new HyperwalletList<HyperwalletBankCard>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
            .sortBy("test-sort-by")
            .limit(10)
            .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
            .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBankCard> resp = client.listBankCards("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBankCards_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletBankCard> response = new HyperwalletList<HyperwalletBankCard>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
            .sortBy("test-sort-by")
            .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBankCard> resp = client.listBankCards("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testDeactivateBankCardStatusTransition_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.deactivateBankCard(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testDeactivateBankCardStatusTransition_noBankCardToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.deactivateBankCard("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testDeactivateBankCardStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition statusTransitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(statusTransitionResponse);

        HyperwalletStatusTransition resp = client.deactivateBankCard("test-user-token", "test-bank-card-token");
        assertThat(resp, is(equalTo(statusTransitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards/test-bank-card-token/status-transitions"),
                argument.capture(), Mockito.eq(statusTransitionResponse.getClass()));

        HyperwalletStatusTransition apiClientStatusTransition = argument.getValue();
        assertThat(apiClientStatusTransition, is(notNullValue()));
        assertThat(apiClientStatusTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
    }

    @Test
    public void testDeactivateBankCardStatusTransition_withNotes_successful() throws Exception {
        HyperwalletStatusTransition statusTransitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(statusTransitionResponse);

        HyperwalletStatusTransition resp = client.deactivateBankCard("test-user-token", "test-bank-card-token", "test-notes");
        assertThat(resp, is(equalTo(statusTransitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards/test-bank-card-token/status-transitions"),
                argument.capture(), Mockito.eq(statusTransitionResponse.getClass()));

        HyperwalletStatusTransition apiClientStatusTransition = argument.getValue();
        assertThat(apiClientStatusTransition, is(notNullValue()));
        assertThat(apiClientStatusTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
        assertThat(apiClientStatusTransition.getNotes(), is(equalTo("test-notes")));
    }

    @Test
    public void testCreateBankCardStatusTransition_noTransition() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankCardStatusTransition(null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition is required")));
            assertThat(e.getMessage(), is(equalTo("Transition is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankCardStatusTransition_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankCardStatusTransition(null, null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankCardStatusTransition_noBankCardToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankCardStatusTransition("test-user-token", null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankCardStatusTransition_transitionTokenSpecified() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBankCardStatusTransition("test-user-token", "test-bank-card-token", transition);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankCardStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setFromStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setCreatedOn(new Date());
        transition.setTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED);

        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.createBankCardStatusTransition("test-user-token", "test-bank-card-token", transition);
        assertThat(resp, is(equalTo(transitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards/test-bank-card-token/status-transitions"),
                argument.capture(), Mockito.eq(transition.getClass()));

        HyperwalletStatusTransition apiClientTransition = argument.getValue();
        assertThat(apiClientTransition, is(notNullValue()));
        assertThat(apiClientTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
        assertThat(apiClientTransition.getFromStatus(), is(nullValue()));
        assertThat(apiClientTransition.getToStatus(), is(nullValue()));
        assertThat(apiClientTransition.getCreatedOn(), is(nullValue()));
    }

    @Test
    public void testGetBankCardStatusTransition_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getBankCardStatusTransition(null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetBankCardStatusTransition_noBankCardToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getBankCardStatusTransition("test-user-token", null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetBankCardStatusTransition_noTransitionToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getBankCardStatusTransition("test-user-token", "test-bank-card-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetBankCardStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.getBankCardStatusTransition("test-user-token", "test-bank-card-token", "test-status-transition-token");
        assertThat(resp, is(equalTo(transitionResponse)));

        Mockito.verify(mockApiClient)
                .get("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards/test-bank-card-token/status-transitions/test-status-transition-token",
                        transitionResponse.getClass());
    }

    @Test
    public void testListBankCardStatusTransitions_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBankCardStatusTransitions(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListBankCardStatusTransitions_noParameters_noBankCardToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBankCardStatusTransitions("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListBankCardStatusTransitions_noParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listBankCardStatusTransitions("test-user-token", "test-bank-card-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards/test-bank-card-token/status-transitions"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBankCardStatusTransitions_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBankCardStatusTransitions(null, null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListBankCardStatusTransitions_withParameters_noBankCardToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBankCardStatusTransitions("test-user-token", null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getMessage(), is(equalTo("Bank Card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListBankCardStatisTransitions_withParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
            .sortBy("test-sort-by")
            .limit(10)
            .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
            .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listBankCardStatusTransitions("test-user-token", "test-bank-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards/test-bank-card-token/status-transitions"
                        + "?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBankCardStatusTransitions_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
            .sortBy("test-sort-by")
            .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listBankCardStatusTransitions("test-user-token", "test-bank-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-cards/test-bank-card-token/status-transitions?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
    }

    //--------------------------------------
    // Paper Checks
    //--------------------------------------

    @Test
    public void testCreatePaperCheck_noPaperCheck() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPaperCheck(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Paper Check is required")));
            assertThat(e.getMessage(), is(equalTo("Paper Check is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePaperCheck_noUserToken() {
        HyperwalletPaperCheck paperCheck = new HyperwalletPaperCheck();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPaperCheck(paperCheck);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePaperCheck_prepaidCardTokenSpecified() {
        HyperwalletPaperCheck paperCheck = new HyperwalletPaperCheck();
        paperCheck.setUserToken("test-user-token");
        paperCheck.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPaperCheck(paperCheck);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Paper Check token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Paper Check token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePaperCheck_noType() throws Exception {
        HyperwalletPaperCheck paperCheck = new HyperwalletPaperCheck();
        paperCheck.setUserToken("test-user-token");
        paperCheck.setStatus(HyperwalletTransferMethod.Status.ACTIVATED);
        paperCheck.setCreatedOn(new Date());
        paperCheck.setAddressLine1("test-address-line1");

        HyperwalletPaperCheck paperCheckResponse = new HyperwalletPaperCheck();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(paperCheckResponse);

        HyperwalletPaperCheck resp = client.createPaperCheck(paperCheck);
        assertThat(resp, is(equalTo(paperCheckResponse)));

        ArgumentCaptor<HyperwalletPaperCheck> argument = ArgumentCaptor.forClass(HyperwalletPaperCheck.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks"), argument.capture(),
                        Mockito.eq(paperCheck.getClass()));

        HyperwalletPaperCheck apiClientPaperCheck = argument.getValue();
        assertThat(apiClientPaperCheck, is(notNullValue()));
        assertThat(apiClientPaperCheck.getType(), is(equalTo(HyperwalletTransferMethod.Type.PAPER_CHECK)));
        assertThat(apiClientPaperCheck.getStatus(), is(nullValue()));
        assertThat(apiClientPaperCheck.getCreatedOn(), is(nullValue()));
    }

    @Test
    public void testCreatePaperCheck_withType() throws Exception {
        HyperwalletPaperCheck paperCheck = new HyperwalletPaperCheck();
        paperCheck.setUserToken("test-user-token");
        paperCheck.setType(HyperwalletTransferMethod.Type.PAPER_CHECK);
        paperCheck.setStatus(HyperwalletTransferMethod.Status.ACTIVATED);
        paperCheck.setCreatedOn(new Date());
        paperCheck.setAddressLine1("test-address-line1");

        HyperwalletPaperCheck paperCheckResponse = new HyperwalletPaperCheck();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(paperCheckResponse);

        HyperwalletPaperCheck resp = client.createPaperCheck(paperCheck);
        assertThat(resp, is(equalTo(paperCheckResponse)));

        ArgumentCaptor<HyperwalletPaperCheck> argument = ArgumentCaptor.forClass(HyperwalletPaperCheck.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks"), argument.capture(),
                        Mockito.eq(paperCheck.getClass()));

        HyperwalletPaperCheck apiClienPaperCheck = argument.getValue();
        assertThat(apiClienPaperCheck, is(notNullValue()));
        assertThat(apiClienPaperCheck.getType(), is(equalTo(HyperwalletTransferMethod.Type.PAPER_CHECK)));
        assertThat(apiClienPaperCheck.getStatus(), is(nullValue()));
        assertThat(apiClienPaperCheck.getCreatedOn(), is(nullValue()));
    }

    @Test
    public void testUpdatePaperCheck_noPaperCheck() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updatePaperCheck(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Paper Check is required")));
            assertThat(e.getMessage(), is(equalTo("Paper Check is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdatePaperCheck_noUserToken() {
        HyperwalletPaperCheck paperCheck = new HyperwalletPaperCheck();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updatePaperCheck(paperCheck);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdatePaperCheck_noBankAccountToken() {
        HyperwalletPaperCheck paperCheck = new HyperwalletPaperCheck();
        paperCheck.setUserToken("test-user-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updatePaperCheck(paperCheck);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdatePaperCheck_successful() throws Exception {
        HyperwalletPaperCheck paperCheck = new HyperwalletPaperCheck();
        paperCheck.setToken("test-bank-card-token");
        paperCheck.setUserToken("test-user-token");
        paperCheck.setAddressLine1("test-address-line1");
        paperCheck.setBusinessOperatingName("test-bank-operating-name");

        HyperwalletPaperCheck paperCheckResponse = new HyperwalletPaperCheck();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.put(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(paperCheckResponse);

        HyperwalletPaperCheck resp = client.updatePaperCheck(paperCheck);
        assertThat(resp, is(equalTo(paperCheckResponse)));

        ArgumentCaptor<HyperwalletPaperCheck> argument = ArgumentCaptor.forClass(HyperwalletPaperCheck.class);
        Mockito.verify(mockApiClient)
                .put(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks/test-bank-card-token"),
                        argument.capture(), Mockito.eq(paperCheck.getClass()));

        HyperwalletPaperCheck apiClientBankAccount = argument.getValue();
        assertThat(apiClientBankAccount, is(notNullValue()));
        assertThat(apiClientBankAccount.getAddressLine1(), is(equalTo("test-address-line1")));
        assertThat(apiClientBankAccount.getBusinessOperatingName(), is(equalTo("test-bank-operating-name")));
    }

    @Test
    public void testGetPaperCheck_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPaperCheck(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetPaperCheck_noPaperCheckToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPaperCheck("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetPaperCheck_successful() throws Exception {
        HyperwalletPaperCheck paperCheckResponse = new HyperwalletPaperCheck();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(paperCheckResponse);

        HyperwalletPaperCheck resp = client.getPaperCheck("test-user-token", "test-bank-card-token");
        assertThat(resp, is(equalTo(paperCheckResponse)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks/test-bank-card-token",
                paperCheckResponse.getClass());
    }

    @Test
    public void testListPaperChecks_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPaperChecks(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListPaperChecks_noParameters() throws Exception {
        HyperwalletList<HyperwalletPaperCheck> response = new HyperwalletList<HyperwalletPaperCheck>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPaperCheck> resp = client.listPaperChecks("test-user-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPaperChecks_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPaperChecks(null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListPaperChecks_withParameters() throws Exception {
        HyperwalletList<HyperwalletPaperCheck> response = new HyperwalletList<HyperwalletPaperCheck>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
            .sortBy("test-sort-by")
            .limit(10)
            .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
            .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPaperCheck> resp = client.listPaperChecks("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPaperChecks_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletPaperCheck> response = new HyperwalletList<HyperwalletPaperCheck>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
            .sortBy("test-sort-by")
            .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPaperCheck> resp = client.listPaperChecks("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testDeactivatePaperCheckStatusTransition_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.deactivatePaperCheck(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testDeactivatePaperCheckStatusTransition_noPaperCheckToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.deactivatePaperCheck("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testDeactivatePaperCheckStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition statusTransitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(statusTransitionResponse);

        HyperwalletStatusTransition resp = client.deactivatePaperCheck("test-user-token", "test-bank-card-token");
        assertThat(resp, is(equalTo(statusTransitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks/test-bank-card-token/status-transitions"),
                argument.capture(), Mockito.eq(statusTransitionResponse.getClass()));

        HyperwalletStatusTransition apiClientStatusTransition = argument.getValue();
        assertThat(apiClientStatusTransition, is(notNullValue()));
        assertThat(apiClientStatusTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
    }

    @Test
    public void testDeactivatePaperCheckStatusTransition_withNotes_successful() throws Exception {
        HyperwalletStatusTransition statusTransitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(statusTransitionResponse);

        HyperwalletStatusTransition resp = client.deactivatePaperCheck("test-user-token", "test-bank-card-token", "test-notes");
        assertThat(resp, is(equalTo(statusTransitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks/test-bank-card-token/status-transitions"),
                argument.capture(), Mockito.eq(statusTransitionResponse.getClass()));

        HyperwalletStatusTransition apiClientStatusTransition = argument.getValue();
        assertThat(apiClientStatusTransition, is(notNullValue()));
        assertThat(apiClientStatusTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
        assertThat(apiClientStatusTransition.getNotes(), is(equalTo("test-notes")));
    }

    @Test
    public void testCreatePaperCheckStatusTransition_noTransition() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPaperCheckStatusTransition(null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition is required")));
            assertThat(e.getMessage(), is(equalTo("Transition is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePaperCheckStatusTransition_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPaperCheckStatusTransition(null, null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePaperCheckStatusTransition_noPaperCheckToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPaperCheckStatusTransition("test-user-token", null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePaperCheckStatusTransition_transitionTokenSpecified() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPaperCheckStatusTransition("test-user-token", "test-bank-card-token", transition);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePaperCheckStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setFromStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setCreatedOn(new Date());
        transition.setTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED);

        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.createPaperCheckStatusTransition("test-user-token", "test-bank-card-token", transition);
        assertThat(resp, is(equalTo(transitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks/test-bank-card-token/status-transitions"),
                argument.capture(), Mockito.eq(transition.getClass()));

        HyperwalletStatusTransition apiClientTransition = argument.getValue();
        assertThat(apiClientTransition, is(notNullValue()));
        assertThat(apiClientTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
        assertThat(apiClientTransition.getFromStatus(), is(nullValue()));
        assertThat(apiClientTransition.getToStatus(), is(nullValue()));
        assertThat(apiClientTransition.getCreatedOn(), is(nullValue()));
    }

    @Test
    public void testGetPaperCheckStatusTransition_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPaperCheckStatusTransition(null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetPaperCheckStatusTransition_noPaperCheckToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPaperCheckStatusTransition("test-user-token", null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetPaperCheckStatusTransition_noTransitionToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPaperCheckStatusTransition("test-user-token", "test-bank-card-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetPaperCheckStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.getPaperCheckStatusTransition("test-user-token", "test-bank-card-token", "test-status-transition-token");
        assertThat(resp, is(equalTo(transitionResponse)));

        Mockito.verify(mockApiClient)
                .get("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks/test-bank-card-token/status-transitions/test-status-transition-token",
                        transitionResponse.getClass());
    }

    @Test
    public void testListPaperCheckStatusTransitions_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPaperCheckStatusTransitions(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListPaperCheckStatusTransitions_noParameters_noPaperCheckToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPaperCheckStatusTransitions("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListPaperCheckStatusTransitions_noParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPaperCheckStatusTransitions("test-user-token", "test-bank-card-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks/test-bank-card-token/status-transitions"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPaperCheckStatusTransitions_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPaperCheckStatusTransitions(null, null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListPaperCheckStatusTransitions_withParameters_noPaperCheckToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPaperCheckStatusTransitions("test-user-token", null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getMessage(), is(equalTo("Paper Check token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListPaperCheckStatisTransitions_withParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
            .sortBy("test-sort-by")
            .limit(10)
            .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
            .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPaperCheckStatusTransitions("test-user-token", "test-bank-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks/test-bank-card-token/status-transitions?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPaperCheckStatusTransitions_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
            .sortBy("test-sort-by")
            .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPaperCheckStatusTransitions("test-user-token", "test-bank-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paper-checks/test-bank-card-token/status-transitions?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
    }

    //--------------------------------------
    // Transfers
    //--------------------------------------

    @Test
    public void testCreateTransfer_noTransfer() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createTransfer(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transfer is required")));
            assertThat(e.getMessage(), is(equalTo("Transfer is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateTransfer_noSourceToken() {
        HyperwalletTransfer transfer = new HyperwalletTransfer();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createTransfer(transfer);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Source token is required")));
            assertThat(e.getMessage(), is(equalTo("Source token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateTransfer_noDestinationToken() {
        HyperwalletTransfer transfer = new HyperwalletTransfer();
        transfer.setSourceToken("test-source-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createTransfer(transfer);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Destination token is required")));
            assertThat(e.getMessage(), is(equalTo("Destination token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateTransfer_noClientTransferId() {
        HyperwalletTransfer transfer = new HyperwalletTransfer();
        transfer.setSourceToken("test-source-token");
        transfer.setDestinationToken("test-destination-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createTransfer(transfer);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("ClientTransferId is required")));
            assertThat(e.getMessage(), is(equalTo("ClientTransferId is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateTransfer_successful() throws Exception {
        HyperwalletTransfer transfer = new HyperwalletTransfer();
        transfer.setSourceToken("test-source-token");
        transfer.setDestinationToken("test-destination-token");
        transfer.setStatus(HyperwalletTransfer.Status.QUOTED);
        transfer.setCreatedOn(new Date());
        transfer.setClientTransferId("test-client-transfer-id");
        transfer.setSourceCurrency("USD");

        HyperwalletTransfer transferResponse = new HyperwalletTransfer();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(transferResponse);

        HyperwalletTransfer resp = client.createTransfer(transfer);
        assertThat(resp, is(equalTo(transferResponse)));

        ArgumentCaptor<HyperwalletTransfer> argument = ArgumentCaptor.forClass(HyperwalletTransfer.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/transfers"), argument.capture(), Mockito.eq(transfer.getClass()));

        HyperwalletTransfer apiTransfer = argument.getValue();
        assertThat(apiTransfer, is(notNullValue()));
        assertThat(apiTransfer.getSourceToken(), is(equalTo("test-source-token")));
        assertThat(apiTransfer.getClientTransferId(), is(equalTo("test-client-transfer-id")));
        assertThat(apiTransfer.getSourceCurrency(), is(equalTo("USD")));
        assertThat(apiTransfer.getStatus(), is(nullValue()));
        assertThat(apiTransfer.getCreatedOn(), is(nullValue()));
        assertThat(apiTransfer.getExpiresOn(), is(nullValue()));
    }

    @Test
    public void testGetTransfer_noTransferToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getTransfer(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transfer token is required")));
            assertThat(e.getMessage(), is(equalTo("Transfer token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetTransfer_successful() throws Exception {
        HyperwalletTransfer transfer = new HyperwalletTransfer();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(transfer);

        HyperwalletTransfer resp = client.getTransfer("test-transfer-token");
        assertThat(resp, is(equalTo(transfer)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v4/transfers/test-transfer-token", transfer.getClass());
    }

    @Test
    public void testListTransfer_noParameters() throws Exception {
        HyperwalletList<HyperwalletTransfer> response = new HyperwalletList<HyperwalletTransfer>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletTransfer> resp = client.listTransfers();
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/transfers"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListTransfer_withParameters() throws Exception {
        HyperwalletList<HyperwalletTransfer> response = new HyperwalletList<HyperwalletTransfer>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletTransferListOptions options = new HyperwalletTransferListOptions();
        options
                .sourceToken("test-source-token")
                .destinationToken("test-destination-token")
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletTransfer> resp = client.listTransfers(options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/transfers?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10&sourceToken=test-source-token&destinationToken=test-destination-token"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListTransfer_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletTransfer> response = new HyperwalletList<HyperwalletTransfer>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletTransferListOptions options = new HyperwalletTransferListOptions();
        options
                .sourceToken("test-source-token")
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletTransfer> resp = client.listTransfers(options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/transfers?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&sourceToken=test-source-token"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testCreateTransferStatusTransition_noTransition() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createTransferStatusTransition(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition is required")));
            assertThat(e.getMessage(), is(equalTo("Transition is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateTransferStatusTransition_noTransferToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createTransferStatusTransition(null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transfer token is required")));
            assertThat(e.getMessage(), is(equalTo("Transfer token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateTransferStatusTransition_transitionTokenSpecified() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createTransferStatusTransition("test-transfer-token", transition);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateTransferStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setFromStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setCreatedOn(new Date());
        transition.setTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED);

        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.createTransferStatusTransition("test-transfer-token", transition);
        assertThat(resp, is(equalTo(transitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/transfers/test-transfer-token/status-transitions"), argument.capture(),
                        Mockito.eq(transition.getClass()));

        HyperwalletStatusTransition apiClientTransition = argument.getValue();
        assertThat(apiClientTransition, is(notNullValue()));
        assertThat(apiClientTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
        assertThat(apiClientTransition.getFromStatus(), is(nullValue()));
        assertThat(apiClientTransition.getToStatus(), is(nullValue()));
        assertThat(apiClientTransition.getCreatedOn(), is(nullValue()));
    }

    //--------------------------------------
    // PayPal Accounts
    //--------------------------------------

    @Test
    public void testCreatePayPalAccount_noPayPalAccount() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPayPalAccount(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("PayPal Account is required")));
            assertThat(e.getMessage(), is(equalTo("PayPal Account is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePayPalAccount_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount();
        try {
            client.createPayPalAccount(payPalAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePayPalAccount_noTransferMethodCountry() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount();
        payPalAccount.setUserToken("test-user-token");
        try {
            client.createPayPalAccount(payPalAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transfer Method Country is required")));
            assertThat(e.getMessage(), is(equalTo("Transfer Method Country is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePayPalAccount_noTransferMethodCurrency() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount();
        payPalAccount.setUserToken("test-user-token");
        payPalAccount.setTransferMethodCountry("test-transfer-method-country");
        try {
            client.createPayPalAccount(payPalAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transfer Method Currency is required")));
            assertThat(e.getMessage(), is(equalTo("Transfer Method Currency is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePayPalAccount_noEmail() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount();
        payPalAccount.setUserToken("test-user-token");
        payPalAccount.setTransferMethodCountry("test-transfer-method-country");
        payPalAccount.setTransferMethodCurrency("test-transfer-method-currency");
        try {
            client.createPayPalAccount(payPalAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Email is required")));
            assertThat(e.getMessage(), is(equalTo("Email is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePayPalAccount_withPayPalAccountToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount();
        payPalAccount.setUserToken("test-user-token");
        payPalAccount.setTransferMethodCountry("test-transfer-method-country");
        payPalAccount.setTransferMethodCurrency("test-transfer-method-currency");
        payPalAccount.setEmail("test-email");
        payPalAccount.setToken("test-token");
        try {
            client.createPayPalAccount(payPalAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("PayPal Account token may not be present")));
            assertThat(e.getMessage(), is(equalTo("PayPal Account token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePayPalAccount_noType() throws Exception {
        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount();
        payPalAccount.setUserToken("test-user-token");
        payPalAccount.setTransferMethodCountry("test-transfer-method-country");
        payPalAccount.setTransferMethodCurrency("test-transfer-method-currency");
        payPalAccount.setEmail("test-email");
        payPalAccount.setStatus(HyperwalletTransferMethod.Status.ACTIVATED);
        payPalAccount.setCreatedOn(new Date());

        HyperwalletPayPalAccount payPalAccountResponse = new HyperwalletPayPalAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(payPalAccountResponse);

        HyperwalletPayPalAccount resp = client.createPayPalAccount(payPalAccount);
        assertThat(resp, is(equalTo(payPalAccountResponse)));

        ArgumentCaptor<HyperwalletPayPalAccount> argument = ArgumentCaptor.forClass(HyperwalletPayPalAccount.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paypal-accounts"), argument.capture(),
                        Mockito.eq(payPalAccount.getClass()));

        HyperwalletPayPalAccount apiPayPalAccount = argument.getValue();
        assertThat(apiPayPalAccount, is(notNullValue()));
        assertThat(apiPayPalAccount.getUserToken(), is(equalTo("test-user-token")));
        assertThat(apiPayPalAccount.getTransferMethodCountry(), is(equalTo("test-transfer-method-country")));
        assertThat(apiPayPalAccount.getTransferMethodCurrency(), is(equalTo("test-transfer-method-currency")));
        assertThat(apiPayPalAccount.getStatus(), is(nullValue()));
        assertThat(apiPayPalAccount.getCreatedOn(), is(nullValue()));
        assertThat(apiPayPalAccount.getType(), is(HyperwalletTransferMethod.Type.PAYPAL_ACCOUNT));
    }

    @Test
    public void testCreatePayPalAccount_withType() throws Exception {
        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount();
        payPalAccount.setUserToken("test-user-token");
        payPalAccount.setTransferMethodCountry("test-transfer-method-country");
        payPalAccount.setTransferMethodCurrency("test-transfer-method-currency");
        payPalAccount.setStatus(HyperwalletTransferMethod.Status.ACTIVATED);
        payPalAccount.setType(HyperwalletTransferMethod.Type.PAYPAL_ACCOUNT);
        payPalAccount.setEmail("test-email");
        payPalAccount.setCreatedOn(new Date());

        HyperwalletPayPalAccount payPalAccountResponse = new HyperwalletPayPalAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(payPalAccountResponse);

        HyperwalletPayPalAccount resp = client.createPayPalAccount(payPalAccount);
        assertThat(resp, is(equalTo(payPalAccountResponse)));

        ArgumentCaptor<HyperwalletPayPalAccount> argument = ArgumentCaptor.forClass(HyperwalletPayPalAccount.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paypal-accounts"), argument.capture(),
                        Mockito.eq(payPalAccount.getClass()));

        HyperwalletPayPalAccount apiPayPalAccount = argument.getValue();
        assertThat(apiPayPalAccount, is(notNullValue()));
        assertThat(apiPayPalAccount.getUserToken(), is(equalTo("test-user-token")));
        assertThat(apiPayPalAccount.getTransferMethodCountry(), is(equalTo("test-transfer-method-country")));
        assertThat(apiPayPalAccount.getTransferMethodCurrency(), is(equalTo("test-transfer-method-currency")));
        assertThat(apiPayPalAccount.getStatus(), is(nullValue()));
        assertThat(apiPayPalAccount.getCreatedOn(), is(nullValue()));
        assertThat(apiPayPalAccount.getType(), is(HyperwalletTransferMethod.Type.PAYPAL_ACCOUNT));
    }

    @Test
    public void testGetPayPalAccount_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPayPalAccount(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetPayPalAccount_noPayPalAccountToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPayPalAccount("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetPayPalAccount_successful() throws Exception {
        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(payPalAccount);

        HyperwalletPayPalAccount resp = client.getPayPalAccount("test-user-token", "test-paypal-account-token");
        assertThat(resp, is(equalTo(payPalAccount)));

        Mockito.verify(mockApiClient)
                .get("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paypal-accounts/test-paypal-account-token",
                        payPalAccount.getClass());
    }

    @Test
    public void testListPayPalAccount_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPayPalAccounts(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListPayPalAccount_noListOptions() throws Exception {
        HyperwalletList<HyperwalletPayPalAccount> response = new HyperwalletList<HyperwalletPayPalAccount>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPayPalAccount> resp = client.listPayPalAccounts("test-user-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paypal-accounts"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListTransfer_withListOptions() throws Exception {
        HyperwalletList<HyperwalletPayPalAccount> response = new HyperwalletList<HyperwalletPayPalAccount>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPayPalAccount> resp = client.listPayPalAccounts("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paypal-accounts?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testDeactivatePayPalAccountStatusTransition_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.deactivatePayPalAccount(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testDeactivatePayPalAccountStatusTransition_noPayPalAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.deactivatePayPalAccount("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testDeactivatePayPalAccountStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition statusTransitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(statusTransitionResponse);

        HyperwalletStatusTransition resp = client.deactivatePayPalAccount("test-user-token", "test-paypal-account-token");
        assertThat(resp, is(equalTo(statusTransitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paypal-accounts/test-paypal-account-token/status-transitions"),
                argument.capture(), Mockito.eq(statusTransitionResponse.getClass()));

        HyperwalletStatusTransition apiClientStatusTransition = argument.getValue();
        assertThat(apiClientStatusTransition, is(notNullValue()));
        assertThat(apiClientStatusTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
    }

    @Test
    public void testDeactivatePayPalAccountStatusTransition_withNotes_successful() throws Exception {
        HyperwalletStatusTransition statusTransitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(statusTransitionResponse);

        HyperwalletStatusTransition resp = client.deactivatePayPalAccount("test-user-token", "test-paypal-account-token", "test-notes");
        assertThat(resp, is(equalTo(statusTransitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paypal-accounts/test-paypal-account-token/status-transitions"),
                argument.capture(), Mockito.eq(statusTransitionResponse.getClass()));

        HyperwalletStatusTransition apiClientStatusTransition = argument.getValue();
        assertThat(apiClientStatusTransition, is(notNullValue()));
        assertThat(apiClientStatusTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
        assertThat(apiClientStatusTransition.getNotes(), is(equalTo("test-notes")));
    }

    @Test
    public void testCreatePayPalAccountStatusTransition_noTransition() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPayPalAccountStatusTransition(null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition is required")));
            assertThat(e.getMessage(), is(equalTo("Transition is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePayPalAccountStatusTransition_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPayPalAccountStatusTransition(null, null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePayPalAccountStatusTransition_noPayPalAccountToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPayPalAccountStatusTransition("test-user-token", null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePayPalAccountStatusTransition_transitionTokenSpecified() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPayPalAccountStatusTransition("test-user-token", "test-paypal-account-token", transition);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePayPalAccountStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setFromStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setCreatedOn(new Date());
        transition.setTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED);

        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.createPayPalAccountStatusTransition("test-user-token", "test-paypal-account-token", transition);
        assertThat(resp, is(equalTo(transitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paypal-accounts/test-paypal-account-token/status-transitions"),
                argument.capture(), Mockito.eq(transition.getClass()));

        HyperwalletStatusTransition apiClientTransition = argument.getValue();
        assertThat(apiClientTransition, is(notNullValue()));
        assertThat(apiClientTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
        assertThat(apiClientTransition.getFromStatus(), is(nullValue()));
        assertThat(apiClientTransition.getToStatus(), is(nullValue()));
        assertThat(apiClientTransition.getCreatedOn(), is(nullValue()));
    }

    @Test
    public void testGetPayPalAccountStatusTransition_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPayPalAccountStatusTransition(null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetPayPalAccountStatusTransition_noPayPalAccountToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPayPalAccountStatusTransition("test-user-token", null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetPayPalAccountStatusTransition_noTransitionToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPayPalAccountStatusTransition("test-user-token", "test-paypal-account-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetPayPalAccountStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.getPayPalAccountStatusTransition("test-user-token", "test-paypal-account-token", "test-status-transition-token");
        assertThat(resp, is(equalTo(transitionResponse)));

        Mockito.verify(mockApiClient)
                .get("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paypal-accounts/test-paypal-account-token/status-transitions/test-status-transition-token",
                        transitionResponse.getClass());
    }

    @Test
    public void testListPayPalAccountStatusTransitions_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPayPalAccountStatusTransitions(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListPayPalAccountStatusTransitions_noParameters_noPayPalAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPayPalAccountStatusTransitions("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListPayPalAccountStatusTransitions_noParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPayPalAccountStatusTransitions("test-user-token", "test-paypal-account-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paypal-accounts/test-paypal-account-token/status-transitions"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPayPalAccountStatusTransitions_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPayPalAccountStatusTransitions(null, null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListPayPalAccountStatusTransitions_withParameters_noPayPalAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPayPalAccountStatusTransitions("test-user-token", null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListPayPalAccountStatusTransitions_withParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPayPalAccountStatusTransitions("test-user-token", "test-paypal-account-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paypal-accounts/test-paypal-account-token/status-transitions?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPayPalAccountStatusTransitions_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPayPalAccountStatusTransitions("test-user-token", "test-paypal-account-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paypal-accounts/test-paypal-account-token/status-transitions?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testUpdatePayPalAccount_noPayPalAccount() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updatePayPalAccount(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("PayPal Account is required")));
            assertThat(e.getMessage(), is(equalTo("PayPal Account is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }


    @Test
    public void testUpdatePayPalAccount_noUserToken() {
        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updatePayPalAccount(payPalAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdatePayPalAccount_noPayPalAccountToken() {
        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount();
        payPalAccount.setUserToken("test-user-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updatePayPalAccount(payPalAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getMessage(), is(equalTo("PayPal Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdatePayPalAccount_successful() throws Exception {
        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount();
        payPalAccount.setToken("test-paypal-account-token");
        payPalAccount.setUserToken("test-user-token");

        HyperwalletPayPalAccount payPalAccountResponse = new HyperwalletPayPalAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.put(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(payPalAccountResponse);

        HyperwalletPayPalAccount resp = client.updatePayPalAccount(payPalAccount);
        assertThat(resp, is(equalTo(payPalAccountResponse)));

        ArgumentCaptor<HyperwalletPayPalAccount> argument = ArgumentCaptor.forClass(HyperwalletPayPalAccount.class);
        Mockito.verify(mockApiClient)
                .put(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/paypal-accounts/test-paypal-account-token"),
                        argument.capture(), Mockito.eq(payPalAccount.getClass()));

        HyperwalletPayPalAccount hyperwalletPayPalAccount = argument.getValue();
        assertThat(hyperwalletPayPalAccount, is(notNullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBankAccount_successful() throws Exception {
        HyperwalletBankAccount bankAccount = new HyperwalletBankAccount();
        bankAccount.setUserToken("test-user-token");
        bankAccount.setStatus(HyperwalletBankAccount.Status.ACTIVATED);
        bankAccount.setCreatedOn(new Date());
        bankAccount.setFirstName("test-first-name");
        bankAccount.setBusinessOperatingName("test-business-operating-name");

        HyperwalletBankAccount bankAccountResponse = new HyperwalletBankAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(bankAccountResponse);

        HyperwalletBankAccount resp = client.createBankAccount(bankAccount);
        assertThat(resp, is(equalTo(bankAccountResponse)));

        ArgumentCaptor<HyperwalletBankAccount> argument = ArgumentCaptor.forClass(HyperwalletBankAccount.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-accounts"), argument.capture(),
                        Mockito.eq(bankAccount.getClass()));

        HyperwalletBankAccount apiClientBankAccount = argument.getValue();
        assertThat(apiClientBankAccount, is(notNullValue()));
        assertThat(apiClientBankAccount.getFirstName(), is(equalTo("test-first-name")));
        assertThat(apiClientBankAccount.getBusinessOperatingName(), is(equalTo("test-business-operating-name")));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-accounts/test-prepaid-card-token",
                bankAccountResponse.getClass());
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateBankAccount_successful() throws Exception {
        HyperwalletBankAccount bankAccount = new HyperwalletBankAccount();
        bankAccount.setToken("test-bank-account-token");
        bankAccount.setUserToken("test-user-token");
        bankAccount.setFirstName("test-first-name");
        bankAccount.setBusinessOperatingName("test-business-operating-name");

        HyperwalletBankAccount bankAccountResponse = new HyperwalletBankAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.put(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(bankAccountResponse);

        HyperwalletBankAccount resp = client.updateBankAccount(bankAccount);
        assertThat(resp, is(equalTo(bankAccountResponse)));

        ArgumentCaptor<HyperwalletBankAccount> argument = ArgumentCaptor.forClass(HyperwalletBankAccount.class);
        Mockito.verify(mockApiClient)
                .put(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-accounts/test-bank-account-token"),
                        argument.capture(), Mockito.eq(bankAccount.getClass()));

        HyperwalletBankAccount apiClientBankAccount = argument.getValue();
        assertThat(apiClientBankAccount, is(notNullValue()));
        assertThat(apiClientBankAccount.getFirstName(), is(equalTo("test-first-name")));
        assertThat(apiClientBankAccount.getBusinessOperatingName(), is(equalTo("test-business-operating-name")));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-accounts"), Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBankAccount> resp = client.listBankAccounts("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-accounts?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBankAccounts_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletBankAccount> response = new HyperwalletList<HyperwalletBankAccount>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBankAccount> resp = client.listBankAccounts("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-accounts?createdBefore=2016-06-29T17:58:26Z"
                        + "&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-accounts/test-bank-account-token/status-transitions"),
                argument.capture(), Mockito.eq(statusTransitionResponse.getClass()));

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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-accounts/test-bank-account-token/status-transitions"),
                argument.capture(), Mockito.eq(transition.getClass()));

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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-accounts/test-bank-account-token/status-transitions"),
                Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listBankAccountStatusTransitions("test-user-token", "test-bank-account-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-accounts/test-bank-account-token/status-transitions?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBankAccountStatusTransitions_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listBankAccountStatusTransitions("test-user-token", "test-bank-account-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/bank-accounts/test-bank-account-token/status-transitions?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/balances"), Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
                .limit(10)
                .currency("test-currency");

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBalance> resp = client.listBalancesForUser("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/balances?currency=test-currency&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBalancesForUser_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletBalance> response = new HyperwalletList<HyperwalletBalance>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletBalanceListOptions options = new HyperwalletBalanceListOptions();
        options
                .sortBy("test-sort-by");

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBalance> resp = client.listBalancesForUser("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/balances?sortBy=test-sort-by"),
                        Mockito.any(TypeReference.class));
    }


    @Test
    public void testListBalancesForProgramAccount_noParameters_noProgramToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBalancesForAccount(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Program token is required")));
            assertThat(e.getMessage(), is(equalTo("Program token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListBalancesForProgramAccount_noParameters_noAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBalancesForAccount("prg-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListBalancesForProgramAccount_noParameters() throws Exception {
        HyperwalletList<HyperwalletBalance> response = new HyperwalletList<HyperwalletBalance>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBalance> resp = client.listBalancesForAccount("test-prg-token", "test-acct-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/programs/test-prg-token/accounts/test-acct-token/balances"),
                        Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBalancesForProgramAccount_withParameters_noProgramToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBalancesForAccount(null, null, new HyperwalletBalanceListOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Program token is required")));
            assertThat(e.getMessage(), is(equalTo("Program token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListBalancesForProgramAccount_withParameters_noAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBalancesForAccount("prg-token", null, new HyperwalletBalanceListOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListBalancesForProgramAccount_withParameters() throws Exception {
        HyperwalletList<HyperwalletBalance> response = new HyperwalletList<HyperwalletBalance>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletBalanceListOptions options = new HyperwalletBalanceListOptions();
        options
                .sortBy("test-sort-by")
                .limit(10)
                .currency("test-currency");

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBalance> resp = client.listBalancesForAccount("test-prg-token", "test-acct-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/programs/test-prg-token/accounts/test-acct-token/balances?currency=test-currency&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBalancesForProgramAccount_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletBalance> response = new HyperwalletList<HyperwalletBalance>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletBalanceListOptions options = new HyperwalletBalanceListOptions();
        options
                .sortBy("test-sort-by");

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBalance> resp = client.listBalancesForAccount("test-prg-token", "test-acct-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/programs/test-prg-token/accounts/test-acct-token/balances?sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token/balances"),
                        Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
                .limit(10);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBalance> resp = client.listBalancesForPrepaidCard("test-user-token", "test-prepaid-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token/balances?sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListBalancesForPrepaidCard_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletBalance> response = new HyperwalletList<HyperwalletBalance>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletBalanceListOptions options = new HyperwalletBalanceListOptions();
        options
                .sortBy("test-sort-by");

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBalance> resp = client.listBalancesForPrepaidCard("test-user-token", "test-prepaid-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token/balances?sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/payments"), argument.capture(), Mockito.eq(payment.getClass()));

        HyperwalletPayment apiClientPayment = argument.getValue();
        assertThat(apiClientPayment, is(notNullValue()));
        assertThat(apiClientPayment.getStatus(), is(nullValue()));
        assertThat(apiClientPayment.getCurrency(), is(equalTo("test-currency")));
        assertThat(apiClientPayment.getCreatedOn(), is(nullValue()));
        assertThat(apiClientPayment.getProgramToken(), is(nullValue()));
    }

    @Test
    public void testCreatePayment_withProgramTokenAddedByDefault() throws Exception {
        HyperwalletPayment payment = new HyperwalletPayment();
        payment.setCreatedOn(new Date());
        payment.setCurrency("test-currency");
        payment.setStatus("COMPLETED");

        HyperwalletPayment paymentResponse = new HyperwalletPayment();

        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(paymentResponse);

        HyperwalletPayment resp = client.createPayment(payment);
        assertThat(resp, is(equalTo(paymentResponse)));

        ArgumentCaptor<HyperwalletPayment> argument = ArgumentCaptor.forClass(HyperwalletPayment.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/payments"), argument.capture(), Mockito.eq(payment.getClass()));

        HyperwalletPayment apiClientPayment = argument.getValue();
        assertThat(apiClientPayment, is(notNullValue()));
        assertThat(apiClientPayment.getStatus(), is(notNullValue()));
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
        payment.setStatus("COMPLETED");

        HyperwalletPayment paymentResponse = new HyperwalletPayment();

        Hyperwallet client = new Hyperwallet("test-username", "test-password", "test-program-token");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(paymentResponse);

        HyperwalletPayment resp = client.createPayment(payment);
        assertThat(resp, is(equalTo(paymentResponse)));

        ArgumentCaptor<HyperwalletPayment> argument = ArgumentCaptor.forClass(HyperwalletPayment.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/payments"), argument.capture(), Mockito.eq(payment.getClass()));

        HyperwalletPayment apiClientPayment = argument.getValue();
        assertThat(apiClientPayment, is(notNullValue()));
        assertThat(apiClientPayment.getStatus(), is(notNullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v4/payments/test-payment-token", paymentResponse.getClass());
    }

    @Test
    public void testListPayments_noParameters() throws Exception {
        HyperwalletList<HyperwalletPayment> response = new HyperwalletList<HyperwalletPayment>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPayment> resp = client.listPayments();
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/payments"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPayments_withParameters() throws Exception {
        HyperwalletList<HyperwalletPayment> response = new HyperwalletList<HyperwalletPayment>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaymentListOptions options = new HyperwalletPaymentListOptions();
        options
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPayment> resp = client.listPayments(options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/payments?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPayments_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletPayment> response = new HyperwalletList<HyperwalletPayment>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaymentListOptions options = new HyperwalletPaymentListOptions();
        options
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletPayment> resp = client.listPayments(options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/payments?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v4/programs/test-program-token", programResponse.getClass());
    }

    @Test
    public void testCreatePaymentStatusTransition_noTransition() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPaymentStatusTransition(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition is required")));
            assertThat(e.getMessage(), is(equalTo("Transition is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePaymentStatusTransition_noPaymentToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPaymentStatusTransition( null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Payment token is required")));
            assertThat(e.getMessage(), is(equalTo("Payment token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePaymentStatusTransition_transitionTokenSpecified() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createPaymentStatusTransition("test-payment-token", transition);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreatePaymentStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setFromStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setCreatedOn(new Date());
        transition.setTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED);

        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.createPaymentStatusTransition("test-payment-token", transition);
        assertThat(resp, is(equalTo(transitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/payments/test-payment-token/status-transitions"), argument.capture(),
                        Mockito.eq(transition.getClass()));

        HyperwalletStatusTransition apiClientTransition = argument.getValue();
        assertThat(apiClientTransition, is(notNullValue()));
        assertThat(apiClientTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
        assertThat(apiClientTransition.getFromStatus(), is(nullValue()));
        assertThat(apiClientTransition.getToStatus(), is(nullValue()));
        assertThat(apiClientTransition.getCreatedOn(), is(nullValue()));
    }

    @Test
    public void testGetPaymentStatusTransition_noPaymentToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPaymentStatusTransition(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Payment token is required")));
            assertThat(e.getMessage(), is(equalTo("Payment token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetPaymentStatusTransition_noTransitionToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getPaymentStatusTransition("test-payment-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetPaymentStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.getPaymentStatusTransition("test-payment-token", "test-status-transition-token");
        assertThat(resp, is(equalTo(transitionResponse)));

        Mockito.verify(mockApiClient)
                .get("https://api.sandbox.hyperwallet.com/rest/v4/payments/test-payment-token/status-transitions/test-status-transition-token",
                        transitionResponse.getClass());
    }

    @Test
    public void testListPaymentStatusTransitions_noParameters_noPaymentToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPaymentStatusTransitions(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Payment token is required")));
            assertThat(e.getMessage(), is(equalTo("Payment token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListPaymentStatusTransitions_noParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPaymentStatusTransitions("test-payment-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/payments/test-payment-token/status-transitions"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPaymentStatusTransitions_withParameters_noPaymentToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listPaymentStatusTransitions(null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Payment token is required")));
            assertThat(e.getMessage(), is(equalTo("Payment token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListPaymentStatisTransitions_withParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
            .sortBy("test-sort-by")
            .limit(10)
            .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
            .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPaymentStatusTransitions("test-payment-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/payments/test-payment-token/status-transitions?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListPaymentStatusTransitions_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
            .sortBy("test-sort-by")
            .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listPaymentStatusTransitions("test-payment-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/payments/test-payment-token/status-transitions?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v4/programs/test-program-token/accounts/test-account-token",
                accountResponse.getClass());
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient)
                .get("https://api.sandbox.hyperwallet.com/rest/v4/transfer-method-configurations?userToken=test-user-token&country=test-country&currency=test-currency&type=BANK_ACCOUNT&profileType=INDIVIDUAL",
                        configResponse.getClass());
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/transfer-method-configurations?userToken=test-user-token"),
                        Mockito.any(TypeReference.class));
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
            assertThat(e.getRelatedResources(), is(nullValue()));
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
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletTransferMethodConfiguration> resp = client.listTransferMethodConfigurations("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/transfer-method-configurations?userToken=test-user-token&createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListTransferMethodConfigurations_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletTransferMethodConfiguration> response = new HyperwalletList<HyperwalletTransferMethodConfiguration>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletTransferMethodConfiguration> resp = client.listTransferMethodConfigurations("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/transfer-method-configurations?userToken=test-user-token&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
    }

    //--------------------------------------
    // Receipts
    //--------------------------------------

    @Test
    public void testListReceiptsForProgramAccount_noParameters_noProgramToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listReceiptsForProgramAccount(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Program token is required")));
            assertThat(e.getMessage(), is(equalTo("Program token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListReceiptsForProgramAccount_noParameters_noAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listReceiptsForProgramAccount("test-account-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListReceiptsForProgramAccount_noParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listReceiptsForProgramAccount("test-program-token", "test-account-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/programs/test-program-token/accounts/test-account-token/receipts"),
                        Mockito.any(TypeReference.class));
    }

    @Test
    public void testListReceiptsForProgramAccount_withParameters_noProgramToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listReceiptsForProgramAccount(null, null, new HyperwalletReceiptPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Program token is required")));
            assertThat(e.getMessage(), is(equalTo("Program token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListReceiptsForProgramAccount_withParameters_noAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listReceiptsForProgramAccount("test-program-token", null, new HyperwalletReceiptPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListReceiptsForProgramAccount_withParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .type(HyperwalletReceipt.Type.ANNUAL_FEE)
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listReceiptsForProgramAccount("test-program-token", "test-account-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/programs/test-program-token/accounts/test-account-token/receipts?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10&type=ANNUAL_FEE"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListReceiptsForProgramAccount_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listReceiptsForProgramAccount("test-program-token", "test-account-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/programs/test-program-token/accounts/test-account-token/receipts?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListReceiptsForUser_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listReceiptsForUser(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListReceiptsForUser_noParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listReceiptsForUser("test-user-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/receipts"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListReceiptsForUser_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listReceiptsForUser(null, new HyperwalletReceiptPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListReceiptsForUser_withParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .type(HyperwalletReceipt.Type.ANNUAL_FEE)
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listReceiptsForUser("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/receipts?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10&type=ANNUAL_FEE"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListReceiptsForUser_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listReceiptsForUser("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/receipts?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListReceiptsForPrepaidCard_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listReceiptsForPrepaidCard(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListReceiptsForPrepaidCard_noParameters_noPrepaidCardToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listReceiptsForPrepaidCard("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid card token is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListReceiptsForPrepaidCard_noParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listReceiptsForPrepaidCard("test-user-token", "test-prepaid-card-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token/receipts"),
                        Mockito.any(TypeReference.class));
    }

    @Test
    public void testListReceiptsForPrepaidCard_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listReceiptsForPrepaidCard(null, null, new HyperwalletReceiptPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListReceiptsForPrepaidCard_withParameters_noPrepaidCardToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listReceiptsForPrepaidCard("test-user-token", null, new HyperwalletReceiptPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Prepaid card token is required")));
            assertThat(e.getMessage(), is(equalTo("Prepaid card token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListReceiptsForPrepaidCard_withParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .type(HyperwalletReceipt.Type.ANNUAL_FEE)
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listReceiptsForPrepaidCard("test-user-token", "test-prepaid-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token/receipts?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&limit=10&type=ANNUAL_FEE"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListReceiptsForPrepaidCard_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletReceipt> response = new HyperwalletList<HyperwalletReceipt>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletReceipt> resp = client.listReceiptsForPrepaidCard("test-user-token", "test-prepaid-card-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/prepaid-cards/test-prepaid-card-token/receipts?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
    }

    //--------------------------------------
    // Webhook Notification
    //--------------------------------------

    @Test
    public void testGetWebhookEvent_NoToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getWebhookEvent(null);
            fail("This must not be called, due to expecting HyperwalletException after client.getWebhookEvent(null)");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Webhook token is required")));
            assertThat(e.getMessage(), is(equalTo("Webhook token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetWebhookEvent_Successful() throws Exception {
        HyperwalletWebhookNotification event = new HyperwalletWebhookNotification();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(event);

        HyperwalletWebhookNotification resp = client.getWebhookEvent("test-webhook-token");
        assertThat(resp, is(equalTo(event)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v4/webhook-notifications/test-webhook-token", event.getClass());
    }

    @Test
    public void testListWebhookEvents_noParameters() throws Exception {
        HyperwalletList<HyperwalletWebhookNotification> response = new HyperwalletList<HyperwalletWebhookNotification>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletWebhookNotification> resp = client.listWebhookEvents();
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient)
                .get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/webhook-notifications"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListWebhookEvents_withParameters() throws Exception {
        HyperwalletList<HyperwalletWebhookNotification> response = new HyperwalletList<HyperwalletWebhookNotification>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletWebhookNotificationPaginationOptions options = new HyperwalletWebhookNotificationPaginationOptions();
        options
                .type(HyperwalletWebhookNotification.Type.PREPAID_CARD_CREATED.toString())
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2016-08-24T13:56:26Z"))
                .createdBefore(convertStringToDate("2016-08-24T13:56:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletWebhookNotification> resp = client.listWebhookEvents(options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/webhook-notifications?createdAfter=2016-08-24T13:56:26Z&createdBefore=2016-08-24T13:56:26Z&sortBy=test-sort-by&limit=10&type=USERS.PREPAID_CARDS.CREATED"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListWebhookEvents_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletWebhookNotification> response = new HyperwalletList<HyperwalletWebhookNotification>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletWebhookNotificationPaginationOptions options = new HyperwalletWebhookNotificationPaginationOptions();
        options
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-08-24T13:56:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletWebhookNotification> resp = client.listWebhookEvents(options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/webhook-notifications?createdBefore=2016-08-24T13:56:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
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

    //--------------------------------------
    // Transfer Method
    //--------------------------------------

    @Test
    public void testTransferMethod_noTransferMethod() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        String jsonCacheToken = "token123-123-123";
        try {
            client.createTransferMethod(jsonCacheToken, (HyperwalletTransferMethod)null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateTransferMethod_noJsonCacheToken() {
        {
            HyperwalletTransferMethod transferMethod = new HyperwalletTransferMethod();
            transferMethod.setUserToken("test123123");

            Hyperwallet client = new Hyperwallet("test-username", "test-password");
            try {
                client.createTransferMethod(null, transferMethod);
                fail("Expect HyperwalletException");
            } catch (HyperwalletException e) {
                assertThat(e.getErrorCode(), is(nullValue()));
                assertThat(e.getResponse(), is(nullValue()));
                assertThat(e.getErrorMessage(), is(equalTo("JSON token is required")));
                assertThat(e.getMessage(), is(equalTo("JSON token is required")));
                assertThat(e.getHyperwalletErrors(), is(nullValue()));
                assertThat(e.getRelatedResources(), is(nullValue()));
            }
        }
    }

    @Test
    public void testCreateTransferMethod_noJsonCacheTokenStringUserToken() {
        {

            Hyperwallet client = new Hyperwallet("test-username", "test-password");
            try {
                client.createTransferMethod("", "test-user-token");
                fail("Expect HyperwalletException");
            } catch (HyperwalletException e) {
                assertThat(e.getErrorCode(), is(nullValue()));
                assertThat(e.getResponse(), is(nullValue()));
                assertThat(e.getErrorMessage(), is(equalTo("JSON token is required")));
                assertThat(e.getMessage(), is(equalTo("JSON token is required")));
                assertThat(e.getHyperwalletErrors(), is(nullValue()));
                assertThat(e.getRelatedResources(), is(nullValue()));
            }
        }
    }

    @Test
    public void testCreateTransferMethod_noUserokenInTransferMethod() {
        HyperwalletTransferMethod transferMethod = new HyperwalletTransferMethod();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        String jsonCacheToken = "token123-123-123";
        try {
            client.createTransferMethod(jsonCacheToken, transferMethod);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateTransferMethod_noUserToken() {

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        String jsonCacheToken = "token123-123-123";

        try {
            client.createTransferMethod(jsonCacheToken, "");
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateTransferMethod_CompletedUserToken() throws Exception{

        HyperwalletTransferMethod transferMethodResponse = new HyperwalletTransferMethod();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class), Mockito.any(HashMap.class))).thenReturn(transferMethodResponse);

        String jsonCacheToken = "token123-123-123";
        String userToken = "test-user-token";
        HyperwalletTransferMethod resp = client.createTransferMethod(jsonCacheToken, userToken);
        assertThat(resp, is(equalTo(transferMethodResponse)));

        ArgumentCaptor<HyperwalletTransferMethod> argument = ArgumentCaptor.forClass(HyperwalletTransferMethod.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/transfer-methods"), argument.capture(),
                        Mockito.eq(resp.getClass()), (HashMap<String, String>) Mockito.anyMapOf(String.class, String.class));

        HyperwalletTransferMethod apiClientTransferMethod = argument.getValue();

        assertThat(apiClientTransferMethod, is(notNullValue()));
        assertThat(apiClientTransferMethod.getUserToken(), is(notNullValue()));

        assertThat(apiClientTransferMethod.getToken(), is(nullValue()));
        assertThat(apiClientTransferMethod.getStatus(), is(nullValue()));
        assertThat(apiClientTransferMethod.getCreatedOn(), is(nullValue()));
        assertThat(apiClientTransferMethod.getCountry(), is(nullValue()));
        assertThat(apiClientTransferMethod.getType(), is(nullValue()));
        assertThat(apiClientTransferMethod.getTransferMethodCountry(), is(nullValue()));
        assertThat(apiClientTransferMethod.getTransferMethodCurrency(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBankName(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBankId(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBranchName(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBranchId(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBankAccountId(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBankAccountPurpose(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBranchAddressLine1(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBranchAddressLine2(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBranchCity(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBranchStateProvince(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBranchCountry(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBranchPostalCode(), is(nullValue()));
        assertThat(apiClientTransferMethod.getWireInstructions(), is(nullValue()));
        assertThat(apiClientTransferMethod.getIntermediaryBankId(), is(nullValue()));
        assertThat(apiClientTransferMethod.getIntermediaryBankName(), is(nullValue()));
        assertThat(apiClientTransferMethod.getIntermediaryBankAccountId(), is(nullValue()));
        assertThat(apiClientTransferMethod.getIntermediaryBankAddressLine1(), is(nullValue()));
        assertThat(apiClientTransferMethod.getIntermediaryBankAddressLine2(), is(nullValue()));
        assertThat(apiClientTransferMethod.getIntermediaryBankCity(), is(nullValue()));
        assertThat(apiClientTransferMethod.getIntermediaryBankStateProvince(), is(nullValue()));
        assertThat(apiClientTransferMethod.getIntermediaryBankCountry(), is(nullValue()));
        assertThat(apiClientTransferMethod.getIntermediaryBankPostalCode(), is(nullValue()));
        assertThat(apiClientTransferMethod.getCardType(), is(nullValue()));
        assertThat(apiClientTransferMethod.getCardPackage(), is(nullValue()));
        assertThat(apiClientTransferMethod.getCardNumber(), is(nullValue()));
        assertThat(apiClientTransferMethod.getCardBrand(), is(nullValue()));
        assertThat(apiClientTransferMethod.getProfileType(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBusinessName(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBusinessOperatingName(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBusinessRegistrationId(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBusinessRegistrationStateProvince(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBusinessRegistrationCountry(), is(nullValue()));
        assertThat(apiClientTransferMethod.getBusinessContactRole(), is(nullValue()));
        assertThat(apiClientTransferMethod.getFirstName(), is(nullValue()));
        assertThat(apiClientTransferMethod.getMiddleName(), is(nullValue()));
        assertThat(apiClientTransferMethod.getLastName(), is(nullValue()));
        assertThat(apiClientTransferMethod.getCountryOfBirth(), is(nullValue()));
        assertThat(apiClientTransferMethod.getCountryOfNationality(), is(nullValue()));
        assertThat(apiClientTransferMethod.getGender(), is(nullValue()));
        assertThat(apiClientTransferMethod.getMobileNumber(), is(nullValue()));
        assertThat(apiClientTransferMethod.getEmail(), is(nullValue()));
        assertThat(apiClientTransferMethod.getGovernmentId(), is(nullValue()));
        assertThat(apiClientTransferMethod.getPassportId(), is(nullValue()));
        assertThat(apiClientTransferMethod.getDriversLicenseId(), is(nullValue()));
        assertThat(apiClientTransferMethod.getAddressLine1(), is(nullValue()));
        assertThat(apiClientTransferMethod.getAddressLine2(), is(nullValue()));
        assertThat(apiClientTransferMethod.getCity(), is(nullValue()));
        assertThat(apiClientTransferMethod.getStateProvince(), is(nullValue()));
        assertThat(apiClientTransferMethod.getPostalCode(), is(nullValue()));
        assertThat(apiClientTransferMethod.getCountry(), is(nullValue()));
    }

    @Test
    public void testCreateTransferMethod_Completed() throws Exception{
        HyperwalletTransferMethod transferMethod = createPrePopulatedTransferMethod();

        HyperwalletTransferMethod transferMethodResponse = new HyperwalletTransferMethod();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class), Mockito.any(HashMap.class))).thenReturn(transferMethodResponse);

        String jsonCacheToken = "token123-123-123";
        HyperwalletTransferMethod resp = client.createTransferMethod(jsonCacheToken, transferMethod);
        assertThat(resp, is(equalTo(transferMethodResponse)));

        ArgumentCaptor<HyperwalletTransferMethod> argument = ArgumentCaptor.forClass(HyperwalletTransferMethod.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/transfer-methods"), argument.capture(),
                        Mockito.eq(transferMethod.getClass()), (HashMap<String, String>) Mockito.anyMapOf(String.class, String.class));

        HyperwalletTransferMethod apiClientTransferMethod = argument.getValue();

        assertThat(apiClientTransferMethod, is(notNullValue()));
        assertThat(apiClientTransferMethod.getToken(), is(nullValue()));
        assertThat(apiClientTransferMethod.getStatus(), is(nullValue()));
        assertThat(apiClientTransferMethod.getCreatedOn(), is(nullValue()));

        assertThat(apiClientTransferMethod.getCountry(), is(equalTo("test-country")));
        assertThat(apiClientTransferMethod.getType(), is(equalTo(HyperwalletTransferMethod.Type.BANK_ACCOUNT)));
        assertThat(apiClientTransferMethod.getTransferMethodCountry(), is(equalTo("test-transfer-method-country")));
        assertThat(apiClientTransferMethod.getTransferMethodCurrency(), is(equalTo("test-transfer-method-currency")));
        assertThat(apiClientTransferMethod.getBankName(), is(equalTo("test-bank-name")));
        assertThat(apiClientTransferMethod.getBankId(), is(equalTo("test-bank-id")));
        assertThat(apiClientTransferMethod.getBranchName(), is(equalTo("test-branch-name")));
        assertThat(apiClientTransferMethod.getBranchId(), is(equalTo("test-branch-id")));
        assertThat(apiClientTransferMethod.getBankAccountId(), is(equalTo("test-bank-account-id")));
        assertThat(apiClientTransferMethod.getBankAccountPurpose(), is(equalTo("test-bank-account-purpose")));
        assertThat(apiClientTransferMethod.getBranchAddressLine1(), is(equalTo("test-branch-address-line1")));
        assertThat(apiClientTransferMethod.getBranchAddressLine2(), is(equalTo("test-branch-address-line2")));
        assertThat(apiClientTransferMethod.getBranchCity(), is(equalTo("test-branch-city")));
        assertThat(apiClientTransferMethod.getBranchStateProvince(), is(equalTo("test-branch-state-province")));
        assertThat(apiClientTransferMethod.getBranchCountry(), is(equalTo("test-branch-country")));
        assertThat(apiClientTransferMethod.getBranchPostalCode(), is(equalTo("test-branch-postal-code")));
        assertThat(apiClientTransferMethod.getWireInstructions(), is(equalTo("test-wire-instructions")));
        assertThat(apiClientTransferMethod.getIntermediaryBankId(), is(equalTo("test-intermediary-bank-id")));
        assertThat(apiClientTransferMethod.getIntermediaryBankName(), is(equalTo("test-intermediary-bank-name")));
        assertThat(apiClientTransferMethod.getIntermediaryBankAccountId(), is(equalTo("test-intermediary-bank-account-id")));
        assertThat(apiClientTransferMethod.getIntermediaryBankAddressLine1(), is(equalTo("test-intermediary-bank-address-line1")));
        assertThat(apiClientTransferMethod.getIntermediaryBankAddressLine2(), is(equalTo("test-intermediary-bank-address-line2")));
        assertThat(apiClientTransferMethod.getIntermediaryBankCity(), is(equalTo("test-intermediary-bank-city")));
        assertThat(apiClientTransferMethod.getIntermediaryBankStateProvince(), is(equalTo("test-intermediary-bank-state-province")));
        assertThat(apiClientTransferMethod.getIntermediaryBankCountry(), is(equalTo("test-intermediary-bank-country")));
        assertThat(apiClientTransferMethod.getIntermediaryBankPostalCode(), is(equalTo("test-intermediary-bank-postal-code")));
        assertThat(apiClientTransferMethod.getCardType(), is(equalTo(HyperwalletTransferMethod.CardType.VIRTUAL)));
        assertThat(apiClientTransferMethod.getCardPackage(), is(equalTo("test-card-package")));
        assertThat(apiClientTransferMethod.getCardNumber(), is(equalTo("test-card-number")));
        assertThat(apiClientTransferMethod.getCardBrand(), is(equalTo(HyperwalletPrepaidCard.Brand.VISA)));
        assertThat(apiClientTransferMethod.getUserToken(), is(equalTo("test-user-token")));
        assertThat(apiClientTransferMethod.getProfileType(), is(equalTo(HyperwalletUser.ProfileType.INDIVIDUAL)));
        assertThat(apiClientTransferMethod.getBusinessName(), is(equalTo("test-business-name")));
        assertThat(apiClientTransferMethod.getBusinessOperatingName(), is(equalTo("test-business-operating-name")));
        assertThat(apiClientTransferMethod.getBusinessRegistrationId(), is(equalTo("test-business-registration-id")));
        assertThat(apiClientTransferMethod.getBusinessRegistrationStateProvince(), is(equalTo("test-business-registration-state-province")));
        assertThat(apiClientTransferMethod.getBusinessRegistrationCountry(), is(equalTo("test-business-registration-country")));
        assertThat(apiClientTransferMethod.getBusinessContactRole(), is(equalTo(HyperwalletUser.BusinessContactRole.OWNER)));
        assertThat(apiClientTransferMethod.getFirstName(), is(equalTo("test-first-name")));
        assertThat(apiClientTransferMethod.getMiddleName(), is(equalTo("test-middle-name")));
        assertThat(apiClientTransferMethod.getLastName(), is(equalTo("test-last-name")));
        assertThat(apiClientTransferMethod.getCountryOfBirth(), is(equalTo("test-country-of-birth")));
        assertThat(apiClientTransferMethod.getCountryOfNationality(), is(equalTo("test-country-of-nationality")));
        assertThat(apiClientTransferMethod.getGender(), is(equalTo(HyperwalletUser.Gender.MALE)));
        assertThat(apiClientTransferMethod.getMobileNumber(), is(equalTo("test-mobile-number")));
        assertThat(apiClientTransferMethod.getEmail(), is(equalTo("test-email")));
        assertThat(apiClientTransferMethod.getGovernmentId(), is(equalTo("test-government-id")));
        assertThat(apiClientTransferMethod.getPassportId(), is(equalTo("test-passport-id")));
        assertThat(apiClientTransferMethod.getDriversLicenseId(), is(equalTo("test-drivers-license-id")));
        assertThat(apiClientTransferMethod.getAddressLine1(), is(equalTo("test-address-line1")));
        assertThat(apiClientTransferMethod.getAddressLine2(), is(equalTo("test-address-line2")));
        assertThat(apiClientTransferMethod.getCity(), is(equalTo("test-city")));
        assertThat(apiClientTransferMethod.getStateProvince(), is(equalTo("test-state-province")));
        assertThat(apiClientTransferMethod.getPostalCode(), is(equalTo("test-postal-code")));
        assertThat(apiClientTransferMethod.getCountry(), is(equalTo("test-country")));
    }

    private HyperwalletTransferMethod createPrePopulatedTransferMethod() {
        HyperwalletTransferMethod transferMethod = new HyperwalletTransferMethod();
        transferMethod
                .token("test-token")
                .type(HyperwalletTransferMethod.Type.BANK_ACCOUNT)
                .status(HyperwalletTransferMethod.Status.ACTIVATED)
                .createdOn(new Date())
                .transferMethodCountry("test-transfer-method-country")
                .transferMethodCurrency("test-transfer-method-currency")
                .bankName("test-bank-name")
                .bankId("test-bank-id")
                .branchName("test-branch-name")
                .branchId("test-branch-id")
                .bankAccountId("test-bank-account-id")
                .bankAccountPurpose("test-bank-account-purpose")
                .branchAddressLine1("test-branch-address-line1")
                .branchAddressLine2("test-branch-address-line2")
                .branchCity("test-branch-city")
                .branchStateProvince("test-branch-state-province")
                .branchCountry("test-branch-country")
                .branchPostalCode("test-branch-postal-code")
                .wireInstructions("test-wire-instructions")
                .intermediaryBankId("test-intermediary-bank-id")
                .intermediaryBankName("test-intermediary-bank-name")
                .intermediaryBankAccountId("test-intermediary-bank-account-id")
                .intermediaryBankAddressLine1("test-intermediary-bank-address-line1")
                .intermediaryBankAddressLine2("test-intermediary-bank-address-line2")
                .intermediaryBankCity("test-intermediary-bank-city")
                .intermediaryBankStateProvince("test-intermediary-bank-state-province")
                .intermediaryBankCountry("test-intermediary-bank-country")
                .intermediaryBankPostalCode("test-intermediary-bank-postal-code")
                .cardType(HyperwalletTransferMethod.CardType.VIRTUAL)
                .cardPackage("test-card-package")
                .cardNumber("test-card-number")
                .cardBrand(HyperwalletPrepaidCard.Brand.VISA)
                .dateOfExpiry(new Date())
                .userToken("test-user-token")
                .profileType(HyperwalletUser.ProfileType.INDIVIDUAL)
                .businessName("test-business-name")
                .businessOperatingName("test-business-operating-name")
                .businessRegistrationId("test-business-registration-id")
                .businessRegistrationStateProvince("test-business-registration-state-province")
                .businessRegistrationCountry("test-business-registration-country")
                .businessContactRole(HyperwalletUser.BusinessContactRole.OWNER)
                .firstName("test-first-name")
                .middleName("test-middle-name")
                .lastName("test-last-name")
                .dateOfBirth(new Date())
                .countryOfBirth("test-country-of-birth")
                .countryOfNationality("test-country-of-nationality")
                .gender(HyperwalletUser.Gender.MALE)
                .phoneNumber("test-phone-number")
                .mobileNumber("test-mobile-number")
                .email("test-email")
                .governmentId("test-government-id")
                .passportId("test-passport-id")
                .driversLicenseId("test-drivers-license-id")
                .addressLine1("test-address-line1")
                .addressLine2("test-address-line2")
                .city("test-city")
                .stateProvince("test-state-province")
                .postalCode("test-postal-code")
                .country("test-country");
        return transferMethod;
    }

    //--------------------------------------
    // Business Stakeholders
    //--------------------------------------

    private HyperwalletBusinessStakeholder createStakeholderObj() {
        HyperwalletBusinessStakeholder stakeholder = new HyperwalletBusinessStakeholder();
        Map<String,String> params = new HashMap<String,String>();
        params.put("rel","self");
        List<HyperwalletLink> links = new ArrayList<HyperwalletLink>();
        HyperwalletLink link = new HyperwalletLink();
        link.setHref("https://api.sandbox.hyperwallet.com/rest/v4/users/business-stakeholders");
        links.add(link);

        stakeholder.setIsBusinessContact(true);
        stakeholder.setIsDirector(false);
        stakeholder.setIsUltimateBeneficialOwner(false);
        stakeholder.setIsSeniorManagingOfficial(false);
        stakeholder.setProfileType(HyperwalletBusinessStakeholder.ProfileType.INDIVIDUAL);
        stakeholder.setFirstName("TestKK");
        stakeholder.setMiddleName("KK");
        stakeholder.setLastName("TestKK");
        stakeholder.setDateOfBirth(new Date());
        stakeholder.setPhoneNumber("3103322333");
        stakeholder.setMobileNumber(("31052341241"));
        stakeholder.setEmail("swerw@sefrew.com");
        stakeholder.setGovernmentId("333333333");
        stakeholder.setGovernmentIdType(HyperwalletBusinessStakeholder.GovernmentIdType.PASSPORT);
        stakeholder.setGender(HyperwalletBusinessStakeholder.Gender.FEMALE);
        stakeholder.setAddressLine1("456 Main St");
        stakeholder.setAddressLine2("45zd Maine");
        stakeholder.setCity("San Jose");
        stakeholder.setStateProvince("CA");
        stakeholder.setCountry("US");
        stakeholder.setPostalCode("22222");
        stakeholder.setCountryOfBirth("US");
        stakeholder.setCountryOfNationality("US");
        stakeholder.setLinks(links);
        return stakeholder;
    }


    @Test
    public void testCreateBusinessStakeholder_noUserToken() {
        HyperwalletBusinessStakeholder stakeholder = createStakeholderObj();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBusinessStakeholder(null, stakeholder);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token may not be present")));
            assertThat(e.getMessage(), is(equalTo("User token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }

    }

    @Test
    public void testCreateBusinessStakeholder_noStakeholder() {
        String token = "test-token";
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBusinessStakeholder(token, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Stakeholder is required")));
            assertThat(e.getMessage(), is(equalTo("Stakeholder is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBusinessStakeholder_noParams() {

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createBusinessStakeholder(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Stakeholder is required")));
            assertThat(e.getMessage(), is(equalTo("Stakeholder is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateBusinessStakeholder() {

        String token = "test-token";
        HyperwalletBusinessStakeholder stakeholder = createStakeholderObj();
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            HyperwalletBusinessStakeholder response = client.createBusinessStakeholder(token, stakeholder);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorMessage(), is(equalTo("The information entered does not match our records. Please try again.")));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateBusinessStakeholder_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletBusinessStakeholder stakeholder = new HyperwalletBusinessStakeholder();
        try {
            client.updateBusinessStakeholder(null, stakeholder);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token may not be present")));
            assertThat(e.getMessage(), is(equalTo("User token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateBusinessStakeholder_noStakeholder() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        String userToken = "test-token";
        try {
            client.updateBusinessStakeholder(userToken, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Stakeholder is required")));
            assertThat(e.getMessage(), is(equalTo("Stakeholder is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateBusinessStakeholder_noStakeholderToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletBusinessStakeholder stakeholder = new HyperwalletBusinessStakeholder();
        String userToken = "test-token";
        try {
            client.updateBusinessStakeholder(userToken, stakeholder);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Stakeholder token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Stakeholder token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateBusinessStakeholder_blankStakeholderToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletBusinessStakeholder stakeholder = createStakeholderObj();
        stakeholder.setToken("");
        String userToken = "test-token";
        try {
            client.updateBusinessStakeholder(userToken, stakeholder);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Stakeholder token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Stakeholder token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListBusinessStakeholders_noUserToken() throws Exception {

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBusinessStakeholders(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token may not be present")));
            assertThat(e.getMessage(), is(equalTo("User token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }

    }

    @Test
    public void testUpdateBusinessStakeholder() {

        String token = "test-token";
        HyperwalletBusinessStakeholder stakeholder = createStakeholderObj();
        stakeholder.setToken("stk-token");
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            HyperwalletBusinessStakeholder response = client.updateBusinessStakeholder(token, stakeholder);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorMessage(), is(equalTo("The information entered does not match our records. Please try again.")));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListBusinessStakeholders_noUserTokennoOptions() throws Exception {

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listBusinessStakeholders(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token may not be present")));
            assertThat(e.getMessage(), is(equalTo("User token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }

    }

    @Test
    public void testListBusinessStakeholders_withUserTokenNoOptions() throws Exception {

        String token = "test-token";
        HyperwalletList<HyperwalletBusinessStakeholder> response = new HyperwalletList<HyperwalletBusinessStakeholder>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBusinessStakeholder> resp = client.listBusinessStakeholders(token);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/" + token + "/business-stakeholders"),
                Mockito.any(TypeReference.class));

    }

    @Test
    public void testListBusinessStakeholders_withUserTokenAndOptions() throws Exception {

        String token = "test-token";
        HyperwalletList<HyperwalletBusinessStakeholder> response = new HyperwalletList<HyperwalletBusinessStakeholder>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);
        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("createdOn")
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBusinessStakeholder> resp = client.listBusinessStakeholders(token, options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/" + token
                        + "/business-stakeholders?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=createdOn&limit=10"),
                Mockito.any(TypeReference.class));

    }

    @Test
    public void testListBusinessStakeholders_withUserTokenAndSomeOptions() throws Exception {

        String token = "test-token";
        HyperwalletList<HyperwalletBusinessStakeholder> response = new HyperwalletList<HyperwalletBusinessStakeholder>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);
        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .limit(10)
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletBusinessStakeholder> resp = client.listBusinessStakeholders(token, options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/" + token
                + "/business-stakeholders?createdBefore=2016-06-29T17:58:26Z&limit=10"), Mockito.any(TypeReference.class));

    }

    //--------------------------------------
    // Venmo Accounts
    //--------------------------------------

    @Test
    public void testCreateVenmoAccount_noVenmoAccount() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createVenmoAccount(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Venmo Account is required")));
            assertThat(e.getMessage(), is(equalTo("Venmo Account is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }


    @Test
    public void testCreateVenmoAccount_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();
        try {
            client.createVenmoAccount(venmoAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateVenmoAccount_noTransferMethodCountry() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();
        venmoAccount.setUserToken("test-user-token");
        try {
            client.createVenmoAccount(venmoAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transfer Method Country is required")));
            assertThat(e.getMessage(), is(equalTo("Transfer Method Country is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateVenmoAccount_noTransferMethodCurrency() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();
        venmoAccount.setUserToken("test-user-token");
        venmoAccount.setTransferMethodCountry("test-transfer-method-country");
        try {
            client.createVenmoAccount(venmoAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transfer Method Currency is required")));
            assertThat(e.getMessage(), is(equalTo("Transfer Method Currency is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateVenmoAccount_noAccountId() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();
        venmoAccount.setUserToken("test-user-token");
        venmoAccount.setTransferMethodCountry("test-transfer-method-country");
        venmoAccount.setTransferMethodCurrency("test-transfer-method-currency");
        try {
            client.createVenmoAccount(venmoAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Account is required")));
            assertThat(e.getMessage(), is(equalTo("Account is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateVenmoAccount_withVenmoAccountToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();
        venmoAccount.setUserToken("test-user-token");
        venmoAccount.setTransferMethodCountry("test-transfer-method-country");
        venmoAccount.setTransferMethodCurrency("test-transfer-method-currency");
        venmoAccount.setAccountId("test-account");
        venmoAccount.setToken("test-token");
        try {
            client.createVenmoAccount(venmoAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Venmo Account token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Venmo Account token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateVenmoAccount_noType() throws Exception {
        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();
        venmoAccount.setUserToken("test-user-token");
        venmoAccount.setTransferMethodCountry("test-transfer-method-country");
        venmoAccount.setTransferMethodCurrency("test-transfer-method-currency");
        venmoAccount.setAccountId("test-account");
        venmoAccount.setStatus(HyperwalletTransferMethod.Status.ACTIVATED);
        venmoAccount.setCreatedOn(new Date());

        HyperwalletVenmoAccount venmoAccountResponse = new HyperwalletVenmoAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(venmoAccountResponse);

        HyperwalletVenmoAccount resp = client.createVenmoAccount(venmoAccount);
        assertThat(resp, is(equalTo(venmoAccountResponse)));

        ArgumentCaptor<HyperwalletVenmoAccount> argument = ArgumentCaptor.forClass(HyperwalletVenmoAccount.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/venmo-accounts"), argument.capture(),
                        Mockito.eq(venmoAccount.getClass()));

        HyperwalletVenmoAccount apiVenmoAccount = argument.getValue();
        assertThat(apiVenmoAccount, is(notNullValue()));
        assertThat(apiVenmoAccount.getUserToken(), is(equalTo("test-user-token")));
        assertThat(apiVenmoAccount.getTransferMethodCountry(), is(equalTo("test-transfer-method-country")));
        assertThat(apiVenmoAccount.getTransferMethodCurrency(), is(equalTo("test-transfer-method-currency")));
        assertThat(apiVenmoAccount.getStatus(), is(nullValue()));
        assertThat(apiVenmoAccount.getCreatedOn(), is(nullValue()));
        assertThat(apiVenmoAccount.getType(), is(HyperwalletTransferMethod.Type.VENMO_ACCOUNT));
    }

    @Test
    public void testCreateVenmoAccount_withType() throws Exception {
        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();
        venmoAccount.setUserToken("test-user-token");
        venmoAccount.setTransferMethodCountry("test-transfer-method-country");
        venmoAccount.setTransferMethodCurrency("test-transfer-method-currency");
        venmoAccount.setStatus(HyperwalletTransferMethod.Status.ACTIVATED);
        venmoAccount.setType(HyperwalletTransferMethod.Type.VENMO_ACCOUNT);
        venmoAccount.setAccountId("test-account");
        venmoAccount.setCreatedOn(new Date());

        HyperwalletVenmoAccount venmoAccountResponse = new HyperwalletVenmoAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(venmoAccountResponse);

        HyperwalletVenmoAccount resp = client.createVenmoAccount(venmoAccount);
        assertThat(resp, is(equalTo(venmoAccountResponse)));

        ArgumentCaptor<HyperwalletVenmoAccount> argument = ArgumentCaptor.forClass(HyperwalletVenmoAccount.class);
        Mockito.verify(mockApiClient)
                .post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/venmo-accounts"), argument.capture(),
                        Mockito.eq(venmoAccount.getClass()));

        HyperwalletVenmoAccount apiVenmoAccount = argument.getValue();
        assertThat(apiVenmoAccount, is(notNullValue()));
        assertThat(apiVenmoAccount.getUserToken(), is(equalTo("test-user-token")));
        assertThat(apiVenmoAccount.getTransferMethodCountry(), is(equalTo("test-transfer-method-country")));
        assertThat(apiVenmoAccount.getTransferMethodCurrency(), is(equalTo("test-transfer-method-currency")));
        assertThat(apiVenmoAccount.getStatus(), is(nullValue()));
        assertThat(apiVenmoAccount.getCreatedOn(), is(nullValue()));
        assertThat(apiVenmoAccount.getType(), is(HyperwalletTransferMethod.Type.VENMO_ACCOUNT));
    }

    @Test
    public void testGetVemoAccount_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getVenmoAccount(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetVenmoAccount_noVenmoAccountToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getVenmoAccount("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("venmo Account token is required")));
            assertThat(e.getMessage(), is(equalTo("venmo Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetVenmoAccount_successful() throws Exception {
        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(venmoAccount);

        HyperwalletVenmoAccount resp = client.getVenmoAccount("test-user-token", "test-venmo-account-token");
        assertThat(resp, is(equalTo(venmoAccount)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/venmo-accounts/test-venmo-account-token",
                venmoAccount.getClass());
    }

    @Test
    public void testListVenmoAccount_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listVenmoAccounts(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListVenmoAccount_noListOptions() throws Exception {
        HyperwalletList<HyperwalletVenmoAccount> response = new HyperwalletList<>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletVenmoAccount> resp = client.listVenmoAccounts("test-user-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/venmo-accounts"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testVenmoAccountListTransfer_withListOptions() throws Exception {
        HyperwalletList<HyperwalletVenmoAccount> response = new HyperwalletList<>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2020-08-20T21:58:26Z"))
                .createdBefore(convertStringToDate("2020-08-20T21:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletVenmoAccount> resp = client.listVenmoAccounts("test-user-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/venmo-accounts?createdAfter=2020-08-20T21:58:26Z"
                                + "&createdBefore=2020-08-20T21:58:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testDeactivateVenmoAccountStatusTransition_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.deactivateVenmoAccount(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testDeactivateVenmoAccountStatusTransition_noVenmoAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.deactivateVenmoAccount("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Venmo account token is required")));
            assertThat(e.getMessage(), is(equalTo("Venmo account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testDeactivateVenmoAccountStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition statusTransitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(statusTransitionResponse);

        HyperwalletStatusTransition resp = client.deactivateVenmoAccount("test-user-token", "test-venmo-account-token");
        assertThat(resp, is(equalTo(statusTransitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/venmo-accounts/test-venmo-account-token/status"
                                + "-transitions"),
                argument.capture(), Mockito.eq(statusTransitionResponse.getClass()));

        HyperwalletStatusTransition apiClientStatusTransition = argument.getValue();
        assertThat(apiClientStatusTransition, is(notNullValue()));
        assertThat(apiClientStatusTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
    }

    @Test
    public void testDeactivateVenmoAccountStatusTransition_withNotes_successful() throws Exception {
        HyperwalletStatusTransition statusTransitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(statusTransitionResponse);

        HyperwalletStatusTransition resp = client.deactivateVenmoAccount("test-user-token", "test-venmo-account-token", "test-notes");
        assertThat(resp, is(equalTo(statusTransitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/venmo-accounts/test-venmo-account-token/status"
                                + "-transitions"),
                argument.capture(), Mockito.eq(statusTransitionResponse.getClass()));

        HyperwalletStatusTransition apiClientStatusTransition = argument.getValue();
        assertThat(apiClientStatusTransition, is(notNullValue()));
        assertThat(apiClientStatusTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
        assertThat(apiClientStatusTransition.getNotes(), is(equalTo("test-notes")));
    }

    @Test
    public void testCreateVenmoAccountStatusTransition_noTransition() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createVenmoAccountStatusTransition(null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition is required")));
            assertThat(e.getMessage(), is(equalTo("Transition is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateVenmoAccountStatusTransition_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createVenmoAccountStatusTransition(null, null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateVenmoAccountStatusTransition_noVenmoAccountToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createVenmoAccountStatusTransition("test-user-token", null, new HyperwalletStatusTransition());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Venmo account token is required")));
            assertThat(e.getMessage(), is(equalTo("Venmo account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateVenmoAccountStatusTransition_transitionTokenSpecified() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setToken("test-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createVenmoAccountStatusTransition("test-user-token", "test-venmo-account-token", transition);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getMessage(), is(equalTo("Status Transition token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testCreateVenmoAccountStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setFromStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setCreatedOn(new Date());
        transition.setTransition(HyperwalletStatusTransition.Status.DE_ACTIVATED);

        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp = client.createVenmoAccountStatusTransition("test-user-token", "test-venmo-account-token", transition);
        assertThat(resp, is(equalTo(transitionResponse)));

        ArgumentCaptor<HyperwalletStatusTransition> argument = ArgumentCaptor.forClass(HyperwalletStatusTransition.class);
        Mockito.verify(mockApiClient).post(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/venmo-accounts/test-venmo-account-token/status"
                                + "-transitions"),
                argument.capture(), Mockito.eq(transition.getClass()));

        HyperwalletStatusTransition apiClientTransition = argument.getValue();
        assertThat(apiClientTransition, is(notNullValue()));
        assertThat(apiClientTransition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.DE_ACTIVATED)));
        assertThat(apiClientTransition.getFromStatus(), is(nullValue()));
        assertThat(apiClientTransition.getToStatus(), is(nullValue()));
        assertThat(apiClientTransition.getCreatedOn(), is(nullValue()));
    }

    @Test
    public void testGetVenmoAccountStatusTransition_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getVenmoAccountStatusTransition(null, null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetVenmoAccountStatusTransition_noVenmoAccountToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getVenmoAccountStatusTransition("test-user-token", null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Venmo account token is required")));
            assertThat(e.getMessage(), is(equalTo("Venmo account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetVenmoAccountStatusTransition_noTransitionToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getVenmoAccountStatusTransition("test-user-token", "test-venmo-account-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getMessage(), is(equalTo("Transition token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testGetVenmoAccountStatusTransition_successful() throws Exception {
        HyperwalletStatusTransition transitionResponse = new HyperwalletStatusTransition();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(transitionResponse);

        HyperwalletStatusTransition resp =
                client.getVenmoAccountStatusTransition("test-user-token", "test-venmo-account-token", "test-status-transition-token");
        assertThat(resp, is(equalTo(transitionResponse)));

        Mockito.verify(mockApiClient)
                .get("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/venmo-accounts/test-venmo-account-token/status-transitions"
                                + "/test-status-transition-token",
                        transitionResponse.getClass());
    }

    @Test
    public void testListVenmoAccountStatusTransitions_noParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listVenmoAccountStatusTransitions(null, null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListVenmoAccountStatusTransitions_noParameters_noVenmoAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listVenmoAccountStatusTransitions("test-user-token", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Venmo account token is required")));
            assertThat(e.getMessage(), is(equalTo("Venmo account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListVenmoAccountStatusTransitions_noParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp = client.listVenmoAccountStatusTransitions("test-user-token", "test-venmo-account-token");
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/venmo-accounts/test-venmo-account-token/status"
                                + "-transitions"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListVenmoAccountStatusTransitions_withParameters_noUserToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listVenmoAccountStatusTransitions(null, null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListVenmoAccountStatusTransitions_withParameters_noVenmoAccountToken() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listVenmoAccountStatusTransitions("test-user-token", null, new HyperwalletPaginationOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Venmo account token is required")));
            assertThat(e.getMessage(), is(equalTo("Venmo account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testListVenmoAccountStatusTransitions_withParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2020-08-20T23:14:26Z"))
                .createdBefore(convertStringToDate("2020-08-20T23:14:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp =
                client.listVenmoAccountStatusTransitions("test-user-token", "test-venmo-account-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/venmo-accounts/test-venmo-account-token/status"
                                + "-transitions?createdAfter=2020-08-20T23:14:26Z&createdBefore=2020-08-20T23:14:26Z&sortBy=test-sort-by&limit=10"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListVenmoAccountStatusTransitions_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletStatusTransition> response = new HyperwalletList<HyperwalletStatusTransition>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2020-08-20T23:20:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        HyperwalletList<HyperwalletStatusTransition> resp =
                client.listVenmoAccountStatusTransitions("test-user-token", "test-venmo-account-token", options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito
                        .eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/venmo-accounts/test-venmo-account-token/status"
                                + "-transitions?createdBefore=2020-08-20T23:20:26Z&sortBy=test-sort-by"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testUpdateVenmoAccount_noVenmoAccount() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updateVenmoAccount(null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Venmo Account is required")));
            assertThat(e.getMessage(), is(equalTo("Venmo Account is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateVenmoAccount_noUserToken() {
        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updateVenmoAccount(venmoAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is required")));
            assertThat(e.getMessage(), is(equalTo("User token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateVenmoAccount_noVenmoAccountToken() {
        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();
        venmoAccount.setUserToken("test-user-token");

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.updateVenmoAccount(venmoAccount);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("Venmo Account token is required")));
            assertThat(e.getMessage(), is(equalTo("Venmo Account token is required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUpdateVenmoAccount_successful() throws Exception {
        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount();
        venmoAccount.setToken("test-venmo-account-token");
        venmoAccount.setUserToken("test-user-token");

        HyperwalletVenmoAccount venmoAccountResponse = new HyperwalletVenmoAccount();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.put(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(venmoAccountResponse);

        HyperwalletVenmoAccount resp = client.updateVenmoAccount(venmoAccount);
        assertThat(resp, is(equalTo(venmoAccountResponse)));

        ArgumentCaptor<HyperwalletVenmoAccount> argument = ArgumentCaptor.forClass(HyperwalletVenmoAccount.class);
        Mockito.verify(mockApiClient)
                .put(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/users/test-user-token/venmo-accounts/test-venmo-account-token"),
                        argument.capture(), Mockito.eq(venmoAccount.getClass()));

        HyperwalletVenmoAccount apiClientVenmoAccount = argument.getValue();
        assertThat(apiClientVenmoAccount, is(notNullValue()));
    }

    @Test
    public void testDocumentUpload_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.documentUpload(null, new FormDataMultiPart());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token is not present")));
            assertThat(e.getMessage(), is(equalTo("User token is not present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testDocumentUpload_successful() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");

        HyperwalletUser hyperwalletUser = new HyperwalletUser();
        HyperwalletDocument hyperwalletDocument =
                new HyperwalletDocument();
        hyperwalletDocument.category(EDocumentCategory.AUTHORIZATION)
                .type(EIdentityVerificationType.LETTER_OF_AUTHORIZATION)
                .country(ECountryCode.CA).status(EKycDocumentVerificationStatus.NEW);
        List<HyperwalletDocument> hyperwalletDocumentList = new ArrayList<>();
        hyperwalletDocumentList.add(hyperwalletDocument);
        hyperwalletUser.setDocuments(hyperwalletDocumentList);

        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);
        Mockito.when(mockApiClient.put(Mockito.anyString(), Mockito.any(FormDataMultiPart.class), Mockito.any(Class.class)))
                .thenReturn(hyperwalletUser);

        HyperwalletUser hyperwalletUserresponse = client.documentUpload("test-token", new FormDataMultiPart());
        assertTrue(hyperwalletUserresponse.getDocuments().get(0).getCategory().equals(EDocumentCategory.AUTHORIZATION));
        assertTrue(hyperwalletUserresponse.getDocuments().get(0).getType().equals(EIdentityVerificationType.LETTER_OF_AUTHORIZATION));
        assertTrue(hyperwalletUserresponse.getDocuments().get(0).getCountry().equals(ECountryCode.CA));
        assertTrue(hyperwalletUserresponse.getDocuments().get(0).getStatus().equals(EKycDocumentVerificationStatus.NEW));
    }

    @Test
    public void testUploadDocumentBusinessStakeholder_noUserToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        String businessStakeholderToken = "business-token";
        try {
            HyperwalletBusinessStakeholder hyperwalletBusinessStakeholderResponse =
                    client.uploadDocumentBusinessStakeholder(null, businessStakeholderToken, new FormDataMultiPart());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("User token may not be present")));
            assertThat(e.getMessage(), is(equalTo("User token may not be present")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUploadDocumentBusinessStakeholder_noBusinessStakeholderToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        String userToken = "user-token";
        try {
            HyperwalletBusinessStakeholder hyperwalletBusinessStakeholderResponse =
                    client.uploadDocumentBusinessStakeholder(userToken, null, new FormDataMultiPart());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(nullValue()));
            assertThat(e.getResponse(), is(nullValue()));
            assertThat(e.getErrorMessage(), is(equalTo("BusinessStakeholderToken may not be required")));
            assertThat(e.getMessage(), is(equalTo("BusinessStakeholderToken may not be required")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getRelatedResources(), is(nullValue()));
        }
    }

    @Test
    public void testUploadDocumentBusinessStakeholder_Successful() throws Exception {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");

        String userToken = "user-token";
        String businessStakeholderToken = "business-token";
        HyperwalletBusinessStakeholder hyperwalletBusinessStakeholder = new HyperwalletBusinessStakeholder();
        List<HyperwalletVerificationDocument> hyperwalletVerificationDocumentList = new ArrayList<>();
        HyperwalletVerificationDocument hyperWalletVerificationDocument = new HyperwalletVerificationDocument();
        hyperWalletVerificationDocument.category("IDENTIFICATION").type("DRIVERS_LICENSE").status("NEW").country("AL");
        hyperwalletVerificationDocumentList.add(hyperWalletVerificationDocument);
        hyperwalletBusinessStakeholder.setDocuments(hyperwalletVerificationDocumentList);

        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);
        Mockito.when(mockApiClient.put(Mockito.anyString(), Mockito.any(FormDataMultiPart.class), Mockito.any(Class.class)))
                .thenReturn(hyperwalletBusinessStakeholder);

        HyperwalletBusinessStakeholder hyperwalletBusinessStakeholderResponse =
                client.uploadDocumentBusinessStakeholder(userToken, businessStakeholderToken, new FormDataMultiPart());
        HyperwalletVerificationDocument hyperwalletVerificationDocument = hyperwalletBusinessStakeholderResponse.getDocuments().get(0);
        assertTrue(hyperwalletVerificationDocument.getCategory().equals("IDENTIFICATION"));
        assertTrue(hyperwalletVerificationDocument.getType().equals("DRIVERS_LICENSE"));
        assertTrue(hyperwalletVerificationDocument.getCountry().equals("AL"));
        assertTrue(hyperwalletVerificationDocument.getStatus().equals("NEW"));
    }

    //--------------------------------------
    // Transfer Refund
    //--------------------------------------

    private void checkHyperwalletException(HyperwalletException actual, HyperwalletException expected) {
        assertThat(actual.getErrorCode(), is(expected.getErrorCode()));
        assertThat(actual.getResponse(), is(expected.getResponse()));
        assertThat(actual.getErrorMessage(), is(expected.getErrorMessage()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
        assertThat(actual.getHyperwalletErrors(), is(expected.getHyperwalletErrors()));
        assertThat(actual.getRelatedResources(), is(expected.getRelatedResources()));
    }

    @Test
    public void testCreateTransferRefund_noTransferRefund() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createTransferRefund("transferToken", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            HyperwalletException expectedException = new HyperwalletException("Transfer Refund is required");
            checkHyperwalletException(e, expectedException);
        }
    }

    @Test
    public void testCreateTransferRefund_noTransferToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createTransferRefund(null, new HyperwalletTransferRefund());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            HyperwalletException expectedException = new HyperwalletException("Transfer token is required");
            checkHyperwalletException(e, expectedException);
        }
    }

    @Test
    public void testCreateTransferRefund_noClientRefundId() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.createTransferRefund("transferToken", new HyperwalletTransferRefund());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            HyperwalletException expectedException = new HyperwalletException("ClientRefundId is required");
            checkHyperwalletException(e, expectedException);
        }
    }

    @Test
    public void testCreateTransferRefund_successful() throws Exception {
        String clientRefundId = "clientRefundId";
        double sourceAmount = 20.0;
        String notes = "notes";
        String memo = "memo";

        HyperwalletTransferRefund transferRefund = new HyperwalletTransferRefund();
        transferRefund.setClientRefundId(clientRefundId);
        transferRefund.setSourceAmount(sourceAmount);
        transferRefund.setNotes(notes);
        transferRefund.setMemo(memo);

        HyperwalletTransferRefund transferRefundResponse = new HyperwalletTransferRefund();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.post(Mockito.anyString(), Mockito.anyObject(), Mockito.any(Class.class))).thenReturn(transferRefundResponse);

        String transferToken = "transferToken";
        HyperwalletTransferRefund resp = client.createTransferRefund(transferToken, transferRefund);
        assertThat(resp, is(equalTo(transferRefundResponse)));

        ArgumentCaptor<HyperwalletTransferRefund> argument = ArgumentCaptor.forClass(HyperwalletTransferRefund.class);
        Mockito.verify(mockApiClient).post(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/transfers/" + transferToken + "/refunds"),
                argument.capture(), Mockito.eq(transferRefund.getClass()));

        HyperwalletTransferRefund apiTransfer = argument.getValue();
        assertThat(apiTransfer, is(notNullValue()));
        assertThat(apiTransfer.getClientRefundId(), is(equalTo(clientRefundId)));
        assertThat(apiTransfer.getSourceAmount(), is(equalTo(sourceAmount)));
        assertThat(apiTransfer.getNotes(), is(equalTo(notes)));
        assertThat(apiTransfer.getMemo(), is(equalTo(memo)));
        assertThat(apiTransfer.getCreatedOn(), is(nullValue()));
        assertThat(apiTransfer.getStatus(), is(nullValue()));
    }

    @Test
    public void testGetTransferRefund_noTransferToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getTransferRefund(null, "transferRefundToken");
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            HyperwalletException expectedException = new HyperwalletException("Transfer token is required");
            checkHyperwalletException(e, expectedException);
        }
    }

    @Test
    public void testGetTransferRefund_noTransferRefundToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.getTransferRefund("transferToken", null);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            HyperwalletException expectedException = new HyperwalletException("Transfer Refund token is required");
            checkHyperwalletException(e, expectedException);
        }
    }

    @Test
    public void testGetTransferRefund_successful() throws Exception {
        HyperwalletTransferRefund transferRefund = new HyperwalletTransferRefund();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(Class.class))).thenReturn(transferRefund);

        HyperwalletTransferRefund resp = client.getTransferRefund("transferToken", "transferRefundToken");
        assertThat(resp, is(equalTo(transferRefund)));

        Mockito.verify(mockApiClient).get("https://api.sandbox.hyperwallet.com/rest/v4/transfers/transferToken/refunds/transferRefundToken",
                transferRefund.getClass());
    }

    @Test
    public void testListTransferRefunds_noParameters() throws Exception {
        HyperwalletList<HyperwalletTransferRefund> response = new HyperwalletList<>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        String transferToken = "transferToken";
        HyperwalletList<HyperwalletTransferRefund> resp = client.listTransferRefunds(transferToken, null);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/transfers/" + transferToken + "/refunds"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListTransferRefunds_withAllParameters() throws Exception {
        HyperwalletList<HyperwalletTransferRefund> response = new HyperwalletList<>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletTransferRefundListOptions options = new HyperwalletTransferRefundListOptions();
        options
                .clientRefundId("clientRefundId")
                .sourceToken("sourceToken")
                .status("COMPLETED")
                .sortBy("sortByField")
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        String transferToken = "transferToken";
        HyperwalletList<HyperwalletTransferRefund> resp = client.listTransferRefunds(transferToken, options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/transfers/" + transferToken
                + "/refunds?createdAfter=2016-06-29T17:58:26Z&createdBefore=2016-06-29T17:58:26Z&sortBy=sortByField&limit=10"
                + "&clientRefundId=clientRefundId&sourceToken=sourceToken&status=COMPLETED"), Mockito.any(TypeReference.class));
    }

    @Test
    public void testListTransferRefunds_withSomeParameters() throws Exception {
        HyperwalletList<HyperwalletTransferRefund> response = new HyperwalletList<>();

        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        HyperwalletApiClient mockApiClient = createAndInjectHyperwalletApiClientMock(client);

        HyperwalletTransferRefundListOptions options = new HyperwalletTransferRefundListOptions();
        options
                .sourceToken("test-source-token")
                .sortBy("test-sort-by")
                .createdBefore(convertStringToDate("2016-06-29T17:58:26Z"));

        Mockito.when(mockApiClient.get(Mockito.anyString(), Mockito.any(TypeReference.class))).thenReturn(response);

        String transferToken = "transferToken";
        HyperwalletList<HyperwalletTransferRefund> resp = client.listTransferRefunds(transferToken, options);
        assertThat(resp, is(equalTo(response)));

        Mockito.verify(mockApiClient).get(Mockito.eq("https://api.sandbox.hyperwallet.com/rest/v4/transfers/" + transferToken + "/refunds"
                        + "?createdBefore=2016-06-29T17:58:26Z&sortBy=test-sort-by&sourceToken=test-source-token"),
                Mockito.any(TypeReference.class));
    }

    @Test
    public void testListTransferRefunds_noTransferToken() {
        Hyperwallet client = new Hyperwallet("test-username", "test-password");
        try {
            client.listTransferRefunds(null, new HyperwalletTransferRefundListOptions());
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            HyperwalletException expectedException = new HyperwalletException("Transfer token is required");
            checkHyperwalletException(e, expectedException);
        }
    }

}
