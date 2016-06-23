package com.hyperwallet.clientsdk;

import cc.protea.util.http.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.model.HyperwalletList;
import com.hyperwallet.clientsdk.model.HyperwalletStatusTransition;
import com.hyperwallet.clientsdk.model.HyperwalletUser;
import org.junit.Test;

import static org.junit.Assert.*;

public class HyperwalletApiClientErrorTest {

    @Test
    public void testCheckErrorResponseWithHyperwalletErrorResponse () {
        HyperwalletApiClient hyperwalletApiClient = new HyperwalletApiClient("username", "password", "programtoken", "version");
        Response response = new Response();
        response.setResponseCode(400);
        response.setBody(getError());
        HyperwalletException exception = null;

        try {
            hyperwalletApiClient.checkErrorResponse(response);
        } catch (HyperwalletException e) {
            exception = e;
        }
        assertNotNull(exception.getHyperwalletErrors());
        assertTrue(exception.getHyperwalletErrors().size() > 0);
        assertEquals(exception.getErrorMessage(), "Test Error Message");
        assertEquals(exception.getErrorCode(), "TEST_ERROR_CODE");
    }

    @Test
    public void testCheckErrorResponseNoHyperwalletErrorMapping () {
        HyperwalletApiClient hyperwalletApiClient = new HyperwalletApiClient("username", "password", "programtoken", "version");
        Response response = new Response();
        response.setResponseCode(400);
        response.setBody(null);
        response.setResponseMessage("This is a test message");

        HyperwalletException exception = null;
        try {
            hyperwalletApiClient.checkErrorResponse(response);
        } catch (HyperwalletException e) {
            exception = e;
        }
        assertEquals(exception.getErrorMessage(), "This is a test message");
        assertEquals(exception.getErrorCode(), "400");
        assertNotNull(exception.response);
    }

    @Test
    public void testProcessResponseClassMappedError () {
        HyperwalletApiClient hyperwalletApiClient = new HyperwalletApiClient("username", "password", "programtoken", "version");
        Response response = new Response();
        response.setResponseCode(400);
        response.setBody(getError());
        HyperwalletException exception = null;
        try {
            hyperwalletApiClient.processResponse(response, HyperwalletUser.class);
        } catch (HyperwalletException e) {
            exception = e;
        }

        assertNotNull(exception.getHyperwalletErrors());
        assertTrue(exception.getHyperwalletErrors().size() > 0);
        assertEquals(exception.getErrorMessage(), "Test Error Message");
        assertEquals(exception.getErrorCode(), "TEST_ERROR_CODE");
    }

    @Test
    public void testProcessResponseTypeReferenceMappedError () {
        HyperwalletApiClient hyperwalletApiClient = new HyperwalletApiClient("username", "password", "programtoken", "version");
        Response response = new Response();
        response.setResponseCode(400);
        response.setBody(getError());
        HyperwalletException exception = null;
        try {
            hyperwalletApiClient.processResponse(response, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {});
        } catch (HyperwalletException e) {
            exception = e;
        }

        assertNotNull(exception.getHyperwalletErrors());
        assertTrue(exception.getHyperwalletErrors().size() > 0);
        assertEquals(exception.getErrorMessage(), "Test Error Message");
        assertEquals(exception.getErrorCode(), "TEST_ERROR_CODE");
    }


    @Test
    public void testProcessResponseClassUnMappedError () {
        HyperwalletApiClient hyperwalletApiClient = new HyperwalletApiClient("username", "password", "programtoken", "version");
        Response response = new Response();
        response.setResponseCode(400);
        response.setBody(null);
        response.setResponseMessage("This is a test message");
        HyperwalletException exception = null;
        try {
            hyperwalletApiClient.processResponse(response, HyperwalletUser.class);
        } catch (HyperwalletException e) {
            exception = e;
        }

        assertEquals(exception.getErrorMessage(), "This is a test message");
        assertEquals(exception.getErrorCode(), "400");
        assertNotNull(exception.response);
    }

    @Test
    public void testProcessResponseTypeReferenceUnMappedError () {
        HyperwalletApiClient hyperwalletApiClient = new HyperwalletApiClient("username", "password", "programtoken", "version");
        Response response = new Response();
        response.setResponseCode(400);
        response.setBody(null);
        response.setResponseMessage("This is a test message");
        HyperwalletException exception = null;
        try {
            hyperwalletApiClient.processResponse(response, new TypeReference<HyperwalletList<HyperwalletStatusTransition>>() {});
        } catch (HyperwalletException e) {
            exception = e;
        }

        assertEquals(exception.getErrorMessage(), "This is a test message");
        assertEquals(exception.getErrorCode(), "400");
        assertNotNull(exception.response);
    }

    private String getError() {
        String error =
                "{\n" +
                "  \"errors\": [\n" +
                "    {\n" +
                "      \"fieldName\": \"errorField\",\n" +
                "      \"message\": \"Test Error Message\",\n" +
                "      \"code\": \"TEST_ERROR_CODE\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return error;
    }
}