package com.hyperwallet.clientsdk.util;

import cc.protea.util.http.Response;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Sets;
import com.hyperwallet.clientsdk.Hyperwallet;
import com.hyperwallet.clientsdk.HyperwalletException;
import com.hyperwallet.clientsdk.model.HyperwalletBaseMonitor;
import com.hyperwallet.clientsdk.model.HyperwalletError;
import com.hyperwallet.clientsdk.model.HyperwalletPayment;
import com.hyperwallet.clientsdk.model.HyperwalletUser;
import com.hyperwallet.clientsdk.util.HyperwalletEncryption.HyperwalletEncryptionBuilder;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mockito;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.StringBody;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.Header.header;
import static org.testng.Assert.fail;

/**
 * @author fkrauthan
 */
public class HyperwalletApiClientTest {

    private static final String EXP_HEADER = "exp";

    private ClientAndServer mockServer;

    private String baseUrl;

    private HyperwalletApiClient hyperwalletApiClient;
    private HyperwalletApiClient hyperwalletEncryptedApiClient;
    private HyperwalletEncryption hyperwalletEncryption;

    @JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestBody extends HyperwalletBaseMonitor {

        public String test1;
        public String test2;
    }

    @BeforeClass
    public void startServer() {
        mockServer = startClientAndServer(Integer.parseInt(System.getProperty("MOCK_SERVER_PORT", "2016")));
    }

    @AfterClass
    public void stopServer() {
        mockServer.stop();
    }

    @BeforeMethod
    public void setUp() {
        if (mockServer.isRunning()) {
            mockServer.reset();
        } else {
            mockServer = startClientAndServer(Integer.parseInt(System.getProperty("MOCK_SERVER_PORT", "2016")));
        }

        baseUrl = "http://localhost:" + mockServer.getPort();

        hyperwalletApiClient = new HyperwalletApiClient("test-username", "test-password", "1.0", null, 200, 300);

        hyperwalletEncryption = new HyperwalletEncryptionBuilder()
                .encryptionAlgorithm(JWEAlgorithm.RSA_OAEP_256)
                .encryptionMethod(EncryptionMethod.A256CBC_HS512)
                .signAlgorithm(JWSAlgorithm.RS256)
                .clientPrivateKeySetLocation("src/test/resources/encryption/private-jwkset")
                .hyperwalletKeySetLocation("src/test/resources/encryption/server-public-jwkset")
                .build();
        hyperwalletEncryptedApiClient = new HyperwalletApiClient("test-username", "test-password", "1.0", hyperwalletEncryption, 200, 300);
    }

    @Test
    public void testGet_withType_noConnection() {
        mockServer.stop();
        if (mockServer.isRunning()) {
            fail("Mockserver still running");
        }

        try {
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.ConnectException: Connection refused")));
        }
    }

    @Test
    public void testGet_withType_200Response() throws Exception {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"test1\": \"value1\" }")
        );

        TestBody body = hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testGet_withType_202ResponseWithNoBody() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(202)
                        .withHeader("Content-Type", "application/json")
        );

        TestBody body = hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", TestBody.class);
        assertThat(body, is(nullValue()));
    }

    @Test
    public void testGet_withType_204Response() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(204)
                        .withHeader("Content-Type", "application/json")
        );

        TestBody body = hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(nullValue()));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testGet_withType_400Response() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withBody(
                                "{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", "
                                        + "\"message\": \"test5\" }] }")
        );

        try {
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("test1")));
            assertThat(e.getErrorMessage(), is(equalTo("test3")));
            assertThat(e.getResponse(), is(notNullValue()));

            assertThat(e.getHyperwalletErrors(), hasSize(2));
            HyperwalletError error1 = e.getHyperwalletErrors().get(0);
            HyperwalletError error2 = e.getHyperwalletErrors().get(1);

            assertThat(error1.getCode(), is(equalTo("test1")));
            assertThat(error1.getFieldName(), is(equalTo("test2")));
            assertThat(error1.getMessage(), is(equalTo("test3")));

            assertThat(error2.getCode(), is(equalTo("test4")));
            assertThat(error2.getMessage(), is(equalTo("test5")));
        }
    }

    @Test
    public void testGet_withType_500Response() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(500)
        );

        try {
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("500")));
            assertThat(e.getErrorMessage(), is(equalTo("Internal Server Error")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getResponse(), is(notNullValue()));
        }
    }

    @Test
    public void testGet_withTypeReference_noConnection() {
        mockServer.stop();
        if (mockServer.isRunning()) {
            fail("Mockserver still running");
        }

        try {
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", new TypeReference<TestBody>() {
            });
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.ConnectException: Connection refused")));
        }
    }

    @Test
    public void testGet_withTypeReference_200Response() throws Exception {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"test1\": \"value1\" }")
        );

        TestBody body = hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", new TypeReference<TestBody>() {
        });
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testGet_withTypeReference_202ResponseWithNoBody() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(202)
                        .withHeader("Content-Type", "application/json")
        );

        TestBody body = hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", new TypeReference<TestBody>() {
        });
    }

    @Test
    public void testGet_withTypeReference_204Response() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(204)
                        .withHeader("Content-Type", "application/json")
        );

        TestBody body = hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", new TypeReference<TestBody>() {
        });
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(nullValue()));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testGet_withTypeReference_400Response() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withBody(
                                "{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", "
                                        + "\"message\": \"test5\" }] }")
        );

        try {
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", new TypeReference<TestBody>() {
            });
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("test1")));
            assertThat(e.getErrorMessage(), is(equalTo("test3")));
            assertThat(e.getResponse(), is(notNullValue()));

            assertThat(e.getHyperwalletErrors(), hasSize(2));
            HyperwalletError error1 = e.getHyperwalletErrors().get(0);
            HyperwalletError error2 = e.getHyperwalletErrors().get(1);

            assertThat(error1.getCode(), is(equalTo("test1")));
            assertThat(error1.getFieldName(), is(equalTo("test2")));
            assertThat(error1.getMessage(), is(equalTo("test3")));

            assertThat(error2.getCode(), is(equalTo("test4")));
            assertThat(error2.getMessage(), is(equalTo("test5")));
        }
    }

    @Test
    public void testGet_withTypeReference_500Response() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(500)
        );

        try {
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", new TypeReference<TestBody>() {
            });
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("500")));
            assertThat(e.getErrorMessage(), is(equalTo("Internal Server Error")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getResponse(), is(notNullValue()));
        }
    }

    @Test
    public void testGet_withUnrecognizedBody_429Response() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(429)
                        .withHeader("text/html")
                        .withBody(
                                "<html><head><title>Request Rejected</title></head><body>The requested URL was rejected. Please consult with your "
                                        + "administrator.</body></html>")
        );

        try {
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("429")));
            assertThat(e.getErrorMessage(), is(equalTo("Too Many Requests")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getResponse(), is(notNullValue()));
            assertThat(e.getResponse().getBody(), containsString("The requested URL was rejected. Please consult with your administrator."));
            assertThat(e.getCause(), is(notNullValue()));
        }
    }

    @Test
    public void testGet_withEncryption_200Response() throws Exception {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/jose+json")
                        .withBody(encryptResponse("{ \"test1\": \"value1\" }"))
        );

        HyperwalletApiClientTest.TestBody body =
                hyperwalletEncryptedApiClient.get(baseUrl + "/test?test-query=test-value", HyperwalletApiClientTest.TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testGet_withEncryption_202ResponseWithNoBody() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(202)
                        .withHeader("Content-Type", "application/jose+json")
        );

        HyperwalletApiClientTest.TestBody body =
                hyperwalletEncryptedApiClient.get(baseUrl + "/test?test-query=test-value", HyperwalletApiClientTest.TestBody.class);
        assertThat(body, is(nullValue()));
    }

    @Test
    public void testGet_withEncryption_204Response() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(204)
                        .withHeader("Content-Type", "application/jose+json")
        );

        HyperwalletApiClientTest.TestBody body =
                hyperwalletEncryptedApiClient.get(baseUrl + "/test?test-query=test-value", HyperwalletApiClientTest.TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(nullValue()));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testGet_withEncryption_400Response() throws Exception {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withHeader("Content-Type", "application/jose+json")
                        .withBody(encryptResponse(
                                "{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", "
                                        + "\"message\": \"test5\" }] }"))
        );

        try {
            hyperwalletEncryptedApiClient.get(baseUrl + "/test?test-query=test-value", HyperwalletApiClientTest.TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("test1")));
            assertThat(e.getErrorMessage(), is(equalTo("test3")));
            assertThat(e.getResponse(), is(notNullValue()));

            assertThat(e.getHyperwalletErrors(), hasSize(2));
            HyperwalletError error1 = e.getHyperwalletErrors().get(0);
            HyperwalletError error2 = e.getHyperwalletErrors().get(1);

            assertThat(error1.getCode(), is(equalTo("test1")));
            assertThat(error1.getFieldName(), is(equalTo("test2")));
            assertThat(error1.getMessage(), is(equalTo("test3")));

            assertThat(error2.getCode(), is(equalTo("test4")));
            assertThat(error2.getMessage(), is(equalTo("test5")));
        }
    }

    @Test
    public void testGet_withEncryptionAndJsonContent_400Response() throws Exception {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", "
                                        + "\"message\": \"test5\" }] }")
        );

        try {
            hyperwalletEncryptedApiClient.get(baseUrl + "/test?test-query=test-value", HyperwalletApiClientTest.TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("test1")));
            assertThat(e.getErrorMessage(), is(equalTo("test3")));
            assertThat(e.getResponse(), is(notNullValue()));

            assertThat(e.getHyperwalletErrors(), hasSize(2));
            HyperwalletError error1 = e.getHyperwalletErrors().get(0);
            HyperwalletError error2 = e.getHyperwalletErrors().get(1);

            assertThat(error1.getCode(), is(equalTo("test1")));
            assertThat(error1.getFieldName(), is(equalTo("test2")));
            assertThat(error1.getMessage(), is(equalTo("test3")));

            assertThat(error2.getCode(), is(equalTo("test4")));
            assertThat(error2.getMessage(), is(equalTo("test5")));
        }
    }

    @Test
    public void testGet_withEncryptionAndUnrecognizedBody_429Response() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(429)
                        .withHeader("text/html")
                        .withBody(
                                "<html><head><title>Request Rejected</title></head><body>The requested URL was rejected. Please consult with your "
                                        + "administrator.</body></html>")
        );

        try {
            hyperwalletEncryptedApiClient.get(baseUrl + "/test?test-query=test-value", HyperwalletApiClientTest.TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("429")));
            assertThat(e.getErrorMessage(), is(equalTo("Too Many Requests")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getResponse(), is(notNullValue()));
            assertThat(e.getResponse().getBody(), containsString("The requested URL was rejected. Please consult with your administrator."));
            assertThat(e.getCause(), is(notNullValue()));
        }
    }

    @Test
    public void testPut_noConnection() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.stop();
        if (mockServer.isRunning()) {
            fail("Mockserver still running");
        }

        try {
            hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.ConnectException: Connection refused")));
        }
    }

    @Test
    public void testPut_200Response() throws Exception {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"test1\": \"value1\" }")
        );

        TestBody body = hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPut_204Response() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(204)
                        .withHeader("Content-Type", "application/json")
        );

        TestBody body = hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(nullValue()));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPut_400Response() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withBody(
                                "{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", "
                                        + "\"message\": \"test5\" }] }")
        );

        try {
            hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("test1")));
            assertThat(e.getErrorMessage(), is(equalTo("test3")));
            assertThat(e.getResponse(), is(notNullValue()));

            assertThat(e.getHyperwalletErrors(), hasSize(2));
            HyperwalletError error1 = e.getHyperwalletErrors().get(0);
            HyperwalletError error2 = e.getHyperwalletErrors().get(1);

            assertThat(error1.getCode(), is(equalTo("test1")));
            assertThat(error1.getFieldName(), is(equalTo("test2")));
            assertThat(error1.getMessage(), is(equalTo("test3")));

            assertThat(error2.getCode(), is(equalTo("test4")));
            assertThat(error2.getMessage(), is(equalTo("test5")));
        }
    }

    @Test
    public void testPut_500Response() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(500)
        );

        try {
            hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("500")));
            assertThat(e.getErrorMessage(), is(equalTo("Internal Server Error")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getResponse(), is(notNullValue()));
        }
    }

    @Test
    public void testPut_withUnrecognizedBody_429Response() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(429)
                        .withHeader("text/html")
                        .withBody(
                                "<html><head><title>Request Rejected</title></head><body>The requested URL was rejected. Please consult with your "
                                        + "administrator.</body></html>")
        );

        try {
            hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("429")));
            assertThat(e.getErrorMessage(), is(equalTo("Too Many Requests")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getResponse(), is(notNullValue()));
            assertThat(e.getResponse().getBody(), containsString("The requested URL was rejected. Please consult with your administrator."));
            assertThat(e.getCause(), is(notNullValue()));
        }
    }

    @Test
    public void testPutMultipart_noConnection() {
        Multipart requestBody = new Multipart();
        List<Multipart.MultipartData> multipartList = new ArrayList<Multipart.MultipartData>();
        requestBody.setMultipartList(multipartList);

        mockServer.stop();
        if (mockServer.isRunning()) {
            fail("Mockserver still running");
        }

        try {
            hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.ConnectException: Connection refused")));
        }
    }

    @Test
    public void testPutMultipart_withError_invalidContent() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/test-user-token")
                        .withHeaders(
                                header("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk"),
                                header("Accept", "application/json"),
                                header("Content-Type", "multipart/form-data; boundary=--0011010110123111"),
                                header("User-Agent", "Hyperwallet Java SDK v1.0")
                        )
                        .withBody(StringBody.exact("----0011010110123111\r\n"
                                + "Content-Disposition: form-dataContent-type: json"
                                + "\r\n{\"documents\":[{\"country\":\"US\",\"type\":\"DRIVERS_LICENSE\",\"category\":\"IDENTIFICATION\"}]}\r\n\r\n"
                                + "----0011010110123111--\r\n"))
                ,
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withHeader("Content-Type", "application/jose+json")
                        .withBody("{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", "
                                        + "\"message\": \"test5\" }] }")
        );

        try {
            Multipart multipart = new Multipart();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "DRIVERS_LICENSE");
            jsonObject.put("country", "US");
            jsonObject.put("category", "IDENTIFICATION");
            List<JSONObject> documents = new ArrayList<>();
            documents.add(jsonObject);
            JSONObject data = new JSONObject();
            data.put("documents", documents);
            Map<String, String> fields = new HashMap<String, String>();
            fields.put("data", data.toString());
            Multipart.MultipartData formFields = new Multipart.MultipartData("Content-type: json", "Content-Disposition: form-data", fields);
            multipart.add(formFields);

            hyperwalletApiClient.put(baseUrl +"/test-user-token", multipart, HyperwalletUser.class);
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("test1")));
            assertThat(e.getErrorMessage(), is(equalTo("test3")));
            assertThat(e.getResponse(), is(notNullValue()));
        }
    }

    @Test
    public void testPutMultipart_WithSucess() {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/test-user-token")
                        .withHeaders(
                                header("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk"),
                                header("Accept", "application/json"),
                                header("Content-Type", "multipart/form-data; boundary=--0011010110123111"),
                                header("User-Agent", "Hyperwallet Java SDK v1.0")
                        )
                        .withBody(StringBody.exact("----0011010110123111\r\n"
                                + "Content-Disposition: form-dataContent-type: json"
                                + "\r\n{\"documents\":[{\"country\":\"US\",\"type\":\"DRIVERS_LICENSE\",\"category\":\"IDENTIFICATION\"}]}\r\n\r\n"
                                + "----0011010110123111--\r\n"))
                ,
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"test1\": \"value1\" }")
        );

        Multipart multipart = new Multipart();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "DRIVERS_LICENSE");
        jsonObject.put("country", "US");
        jsonObject.put("category", "IDENTIFICATION");
        List<JSONObject> documents = new ArrayList<>();
        documents.add(jsonObject);
        JSONObject data = new JSONObject();
        data.put("documents", documents);
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("data", data.toString());
        Multipart.MultipartData formFields = new Multipart.MultipartData("Content-type: json", "Content-Disposition: form-data", fields);
        multipart.add(formFields);

        TestBody body = hyperwalletApiClient.put(baseUrl + "/test-user-token", multipart, TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPut_withEncryption_200Response() throws Exception {
        HyperwalletApiClientTest.TestBody requestBody = new HyperwalletApiClientTest.TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/jose+json")
                        .withBody(encryptResponse("{ \"test1\": \"value1\" }"))
        );

        HyperwalletApiClientTest.TestBody
                body =
                hyperwalletEncryptedApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletApiClientTest.TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPut_withEncryption_204Response() {
        HyperwalletApiClientTest.TestBody requestBody = new HyperwalletApiClientTest.TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(204)
                        .withHeader("Content-Type", "application/json")
        );

        HyperwalletApiClientTest.TestBody
                body =
                hyperwalletEncryptedApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletApiClientTest.TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(nullValue()));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPut_withEncryption_400Response() throws Exception {
        HyperwalletApiClientTest.TestBody requestBody = new HyperwalletApiClientTest.TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withHeader("Content-Type", "application/jose+json")
                        .withBody(encryptResponse(
                                "{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", "
                                        + "\"message\": \"test5\" }] }"))
        );

        try {
            hyperwalletEncryptedApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletApiClientTest.TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("test1")));
            assertThat(e.getErrorMessage(), is(equalTo("test3")));
            assertThat(e.getResponse(), is(notNullValue()));

            assertThat(e.getHyperwalletErrors(), hasSize(2));
            HyperwalletError error1 = e.getHyperwalletErrors().get(0);
            HyperwalletError error2 = e.getHyperwalletErrors().get(1);

            assertThat(error1.getCode(), is(equalTo("test1")));
            assertThat(error1.getFieldName(), is(equalTo("test2")));
            assertThat(error1.getMessage(), is(equalTo("test3")));

            assertThat(error2.getCode(), is(equalTo("test4")));
            assertThat(error2.getMessage(), is(equalTo("test5")));
        }
    }

    @Test
    public void testPut_withEncryptionAndJsonContent_400Response() throws Exception {
        HyperwalletApiClientTest.TestBody requestBody = new HyperwalletApiClientTest.TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", "
                                        + "\"message\": \"test5\" }] }")
        );

        try {
            hyperwalletEncryptedApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletApiClientTest.TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("test1")));
            assertThat(e.getErrorMessage(), is(equalTo("test3")));
            assertThat(e.getResponse(), is(notNullValue()));

            assertThat(e.getHyperwalletErrors(), hasSize(2));
            HyperwalletError error1 = e.getHyperwalletErrors().get(0);
            HyperwalletError error2 = e.getHyperwalletErrors().get(1);

            assertThat(error1.getCode(), is(equalTo("test1")));
            assertThat(error1.getFieldName(), is(equalTo("test2")));
            assertThat(error1.getMessage(), is(equalTo("test3")));

            assertThat(error2.getCode(), is(equalTo("test4")));
            assertThat(error2.getMessage(), is(equalTo("test5")));
        }
    }

    @Test
    public void testPut_withEncryptionAndUnrecognizedBody_429Response() {
        HyperwalletApiClientTest.TestBody requestBody = new HyperwalletApiClientTest.TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(429)
                        .withHeader("text/html")
                        .withBody(
                                "<html><head><title>Request Rejected</title></head><body>The requested URL was rejected. Please consult with your "
                                        + "administrator.</body></html>")
        );

        try {
            hyperwalletEncryptedApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletApiClientTest.TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("429")));
            assertThat(e.getErrorMessage(), is(equalTo("Too Many Requests")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getResponse(), is(notNullValue()));
            assertThat(e.getResponse().getBody(), containsString("The requested URL was rejected. Please consult with your administrator."));
            assertThat(e.getCause(), is(notNullValue()));
        }
    }

    @Test
    public void testPost_noConnection() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.stop();
        if (mockServer.isRunning()) {
            fail("Mockserver still running");
        }

        try {
            hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.ConnectException: Connection refused")));
        }
    }

    @Test
    public void testPost_200Response() throws Exception {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"test1\": \"value1\" }")
        );

        TestBody body = hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPost_200Response_withEmptyContentTypeAndBody_statusCode204() throws Exception {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(204)
        );

        TestBody body = hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(nullValue()));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPost_204Response() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(204)
                        .withHeader("Content-Type", "application/json")
        );

        TestBody body = hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(nullValue()));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPost_400Response() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withBody(
                                "{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", "
                                        + "\"message\": \"test5\" }] }")
        );

        try {
            hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("test1")));
            assertThat(e.getErrorMessage(), is(equalTo("test3")));
            assertThat(e.getResponse(), is(notNullValue()));

            assertThat(e.getHyperwalletErrors(), hasSize(2));
            HyperwalletError error1 = e.getHyperwalletErrors().get(0);
            HyperwalletError error2 = e.getHyperwalletErrors().get(1);

            assertThat(error1.getCode(), is(equalTo("test1")));
            assertThat(error1.getFieldName(), is(equalTo("test2")));
            assertThat(error1.getMessage(), is(equalTo("test3")));

            assertThat(error2.getCode(), is(equalTo("test4")));
            assertThat(error2.getMessage(), is(equalTo("test5")));
        }
    }

    @Test
    public void testPost_500Response() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(500)
        );

        try {
            hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("500")));
            assertThat(e.getErrorMessage(), is(equalTo("Internal Server Error")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getResponse(), is(notNullValue()));
        }
    }

    @Test
    public void testPost_withUnrecognizedBody_429Response() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(429)
                        .withHeader("text/html")
                        .withBody(
                                "<html><head><title>Request Rejected</title></head><body>The requested URL was rejected. Please consult with your "
                                        + "administrator.</body></html>")
        );

        try {
            hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("429")));
            assertThat(e.getErrorMessage(), is(equalTo("Too Many Requests")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getResponse(), is(notNullValue()));
            assertThat(e.getResponse().getBody(), containsString("The requested URL was rejected. Please consult with your administrator."));
            assertThat(e.getCause(), is(notNullValue()));
        }
    }

    @Test
    public void testPost_WithHeaders() throws Exception {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"test1\": \"value1\" }")
        );
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Json-Cache-Token", "token123-123-123");

        TestBody body = hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class, headers);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }


    @Test
    public void testPost_OverwriteHeaders() throws Exception {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic wrong_password")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withBody(
                                "{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", "
                                        + "\"message\": \"test5\" }] }")
        );

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Basic wrong_password");

        try {
            hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class, headers);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("test1")));
            assertThat(e.getErrorMessage(), is(equalTo("test3")));
            assertThat(e.getResponse(), is(notNullValue()));
        }
    }

    @Test
    public void testPost_NullHeaders() throws Exception {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{ \"test1\": \"value1\" }")
        );

        TestBody body = hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class, null);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }


    @Test
    public void testPost_noConnectionWithHeaders() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.stop();
        if (mockServer.isRunning()) {
            fail("Mockserver still running");
        }

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Basic wrong_password");

        try {
            hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class, headers);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.ConnectException: Connection refused")));
        }
    }

    @Test(expectedExceptions = HyperwalletException.class)
    public void testInvalidJsonResponse() throws Exception {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "    {\n" +
                                        "      \"token\" : \"pmt-81aad61a-03ff-4995-a2fc-6a6e2d8911af\",\n" +
                                        "      \"createdOn\" : \"2016-11-11T21:57:40\",\n" +
                                        "      \"amount\" : \"1,707.58\",\n" +
                                        "      \"currency\" : \"GBP\",\n" +
                                        "      \"clientPaymentId\" : \"8729\",\n" +
                                        "      \"memo\" : \"Advance Payment for Oct 2016\",\n" +
                                        "      \"purpose\" : \"OTHER\",\n" +
                                        "      \"expiresOn\" : \"2017-05-10\",\n" +
                                        "      \"destinationToken\" : \"usr-332b1efe-3486-407f-b72b-f74f36723e6d\",\n" +
                                        "      \"programToken\" : \"prg-11f244b9-7ce4-4d8f-a367-614617529b11\",\n" +
                                        "      \"links\" : [ {\n" +
                                        "        \"params\" : {\n" +
                                        "          \"rel\" : \"self\"\n" +
                                        "        },\n" +
                                        "        \"href\" : \"https://maelle.paylution"
                                        + ".com/rest/v4/payments/pmt-81aad61a-03ff-4995-a2fc-6a6e2d8911af\"\n"
                                        +
                                        "      } ]\n" +
                                        "    }    {\n" +
                                        "      \"token\" : \"pmt-81aad61a-03ff-4995-a2fc-6a6e2d8911af\",\n" +
                                        "      \"createdOn\" : \"2016-11-11T21:57:40\",\n" +
                                        "      \"amount\" : \"1,707.58\",\n" +
                                        "      \"currency\" : \"GBP\",\n" +
                                        "      \"clientPaymentId\" : \"8729\",\n" +
                                        "      \"memo\" : \"Advance Payment for Oct 2016\",\n" +
                                        "      \"purpose\" : \"OTHER\",\n" +
                                        "      \"expiresOn\" : \"2017-05-10\",\n" +
                                        "      \"destinationToken\" : \"usr-332b1efe-3486-407f-b72b-f74f36723e6d\",\n" +
                                        "      \"programToken\" : \"prg-11f244b9-7ce4-4d8f-a367-614617529b11\",\n" +
                                        "      \"links\" : [ {\n" +
                                        "        \"params\" : {\n" +
                                        "          \"rel\" : \"self\"\n" +
                                        "        },\n" +
                                        "        \"href\" : \"https://maelle.paylution"
                                        + ".com/rest/v4/payments/pmt-81aad61a-03ff-4995-a2fc-6a6e2d8911af\"\n"
                                        +
                                        "      } ]\n" +
                                        "    }")
        );
        hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletPayment.class);
    }

    @Test
    public void testPost_withEncryption_200Response() throws Exception {
        HyperwalletApiClientTest.TestBody requestBody = new HyperwalletApiClientTest.TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/jose+json")
                        .withBody(encryptResponse("{ \"test1\": \"value1\" }"))
        );

        HyperwalletApiClientTest.TestBody
                body =
                hyperwalletEncryptedApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletApiClientTest.TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPost_200Response_withEncryptionAndEmptyContentTypeAndBody_statusCode204() throws Exception {
        HyperwalletApiClientTest.TestBody requestBody = new HyperwalletApiClientTest.TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(204)
        );

        HyperwalletApiClientTest.TestBody
                body =
                hyperwalletEncryptedApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletApiClientTest.TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(nullValue()));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPost_200Response_withEncryption_withWrongContentType() throws Exception {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "wrongContentType")
                        .withBody(encryptResponse("{\"test1\":\"value1\"}"))
        );

        try {
            hyperwalletEncryptedApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorMessage(), is("Invalid Content-Type specified in Response Header"));
            assertThat(e.getErrorCode(), is(nullValue()));
        }
    }

    @Test
    public void testPost_200Response_withEncryption_ResponseContentTypeHeaderWithCharset() throws Exception {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");
        String testBody = "{\"test1\":\"value1\"}";

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/jose+json;charset=utf-8")
                        .withBody(encryptResponse(testBody))
        );

        TestBody body = hyperwalletEncryptedApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPost_200Response_withEncryption_ResponseContentTypeHeaderWithLeadingCharset() throws Exception {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");
        String testBody = "{\"test1\":\"value1\"}";

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "charset=utf-8;application/jose+json")
                        .withBody(encryptResponse(testBody))
        );

        TestBody body = hyperwalletEncryptedApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPost_withEncryption_204Response() {
        HyperwalletApiClientTest.TestBody requestBody = new HyperwalletApiClientTest.TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(204)
                        .withHeader("Content-Type", "application/jose+json")
        );

        HyperwalletApiClientTest.TestBody
                body =
                hyperwalletEncryptedApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletApiClientTest.TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(nullValue()));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPost_withEncryption_400Response() throws Exception {
        HyperwalletApiClientTest.TestBody requestBody = new HyperwalletApiClientTest.TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withHeader("Content-Type", "application/jose+json")
                        .withBody(encryptResponse(
                                "{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", "
                                        + "\"message\": \"test5\" }] }"))
        );

        try {
            hyperwalletEncryptedApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletApiClientTest.TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("test1")));
            assertThat(e.getErrorMessage(), is(equalTo("test3")));
            assertThat(e.getResponse(), is(notNullValue()));

            assertThat(e.getHyperwalletErrors(), hasSize(2));
            HyperwalletError error1 = e.getHyperwalletErrors().get(0);
            HyperwalletError error2 = e.getHyperwalletErrors().get(1);

            assertThat(error1.getCode(), is(equalTo("test1")));
            assertThat(error1.getFieldName(), is(equalTo("test2")));
            assertThat(error1.getMessage(), is(equalTo("test3")));

            assertThat(error2.getCode(), is(equalTo("test4")));
            assertThat(error2.getMessage(), is(equalTo("test5")));
        }
    }

    @Test
    public void testPost_withEncryptionWithCharsetContentHeader_400Response() throws Exception {
        HyperwalletApiClientTest.TestBody requestBody = new HyperwalletApiClientTest.TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withHeader("Content-Type", "application/jose+json;charset=utf-8")
                        .withBody(encryptResponse(
                                "{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", "
                                        + "\"message\": \"test5\" }] }"))
        );

        try {
            hyperwalletEncryptedApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletApiClientTest.TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("test1")));
            assertThat(e.getErrorMessage(), is(equalTo("test3")));
            assertThat(e.getResponse(), is(notNullValue()));

            assertThat(e.getHyperwalletErrors(), hasSize(2));
            HyperwalletError error1 = e.getHyperwalletErrors().get(0);
            HyperwalletError error2 = e.getHyperwalletErrors().get(1);

            assertThat(error1.getCode(), is(equalTo("test1")));
            assertThat(error1.getFieldName(), is(equalTo("test2")));
            assertThat(error1.getMessage(), is(equalTo("test3")));

            assertThat(error2.getCode(), is(equalTo("test4")));
            assertThat(error2.getMessage(), is(equalTo("test5")));
        }
    }

    @Test
    public void testPost_withEncryption_500Response() {
        HyperwalletApiClientTest.TestBody requestBody = new HyperwalletApiClientTest.TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(500)
        );

        try {
            hyperwalletEncryptedApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletApiClientTest.TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("500")));
            assertThat(e.getErrorMessage(), is(equalTo("Internal Server Error")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getResponse(), is(notNullValue()));
        }
    }

    @Test
    public void testPost_withEncryptionAndJsonContent_400Response() throws Exception {
        HyperwalletApiClientTest.TestBody requestBody = new HyperwalletApiClientTest.TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody(
                                "{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", "
                                        + "\"message\": \"test5\" }] }")
        );

        try {
            hyperwalletEncryptedApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletApiClientTest.TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("test1")));
            assertThat(e.getErrorMessage(), is(equalTo("test3")));
            assertThat(e.getResponse(), is(notNullValue()));

            assertThat(e.getHyperwalletErrors(), hasSize(2));
            HyperwalletError error1 = e.getHyperwalletErrors().get(0);
            HyperwalletError error2 = e.getHyperwalletErrors().get(1);

            assertThat(error1.getCode(), is(equalTo("test1")));
            assertThat(error1.getFieldName(), is(equalTo("test2")));
            assertThat(error1.getMessage(), is(equalTo("test3")));

            assertThat(error2.getCode(), is(equalTo("test4")));
            assertThat(error2.getMessage(), is(equalTo("test5")));
        }
    }

    @Test
    public void testPost_withEncryptionAndUnrecognizedBody_429Response() {
        HyperwalletApiClientTest.TestBody requestBody = new HyperwalletApiClientTest.TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/jose+json")
                        .withHeader("Content-Type", "application/jose+json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(429)
                        .withHeader("text/html")
                        .withBody(
                                "<html><head><title>Request Rejected</title></head><body>The requested URL was rejected. Please consult with your "
                                        + "administrator.</body></html>")
        );

        try {
            hyperwalletEncryptedApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletApiClientTest.TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("429")));
            assertThat(e.getErrorMessage(), is(equalTo("Too Many Requests")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getResponse(), is(notNullValue()));
            assertThat(e.getResponse().getBody(), containsString("The requested URL was rejected. Please consult with your administrator."));
            assertThat(e.getCause(), is(notNullValue()));
        }
    }

    @Test
    public void checkErrorResponse() throws Exception {
        Response errorResponse = new Response();
        int responseCode = 500;
        errorResponse.setResponseCode(responseCode);
        String responseMessage = "something went wrong";
        errorResponse.setResponseMessage(responseMessage);
        errorResponse.setBody(getErrorResponseBodyString());
        try {
            hyperwalletApiClient.checkErrorResponse(errorResponse);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), not(String.valueOf(responseCode)));
            assertThat(e.getErrorCode(), is("ERROR_CODE"));
            assertThat(e.getErrorMessage(), not(equalTo("responseMessage")));
            assertThat(e.getErrorMessage(), is(equalTo("ERROR_MESSAGE")));
            assertThat(e.getHyperwalletErrors(), is(notNullValue()));
            assertThat(e.getHyperwalletErrors().get(0).getRelatedResources(), is(notNullValue()));
            assertThat(e.getHyperwalletErrors().get(0).getRelatedResources().get(0), is("relatedResource1"));
            assertThat(e.getHyperwalletErrors().get(0).getRelatedResources().get(1), is("relatedResource2"));
        }
    }

    private String getErrorResponseBodyString() throws JSONException {
        JSONObject outer = new JSONObject();
        JSONObject inner = new JSONObject();
        inner.put("message", "ERROR_MESSAGE");
        inner.put("code", "ERROR_CODE");
        List<String> relatedResources = new ArrayList<>();
        relatedResources.add("relatedResource1");
        relatedResources.add("relatedResource2");
        inner.put("relatedResources", relatedResources);
        List<JSONObject> errors = new ArrayList<>();
        errors.add(inner);
        outer.put("errors", errors);
        return outer.toString();
    }

    @Test
    public void testPost_500Response_WithRelatedResources() throws JSONException {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(500)
                        .withBody(getErrorResponseBodyString())
        );

        try {
            hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), not(String.valueOf(500)));
            assertThat(e.getErrorCode(), is(equalTo("ERROR_CODE")));
            assertThat(e.getErrorMessage(), not(equalTo("Internal Server Error")));
            assertThat(e.getErrorMessage(), is(equalTo("ERROR_MESSAGE")));
            assertThat(e.getHyperwalletErrors(), is(notNullValue()));
            assertThat(e.getHyperwalletErrors().get(0).getRelatedResources(), is(notNullValue()));
            assertThat(e.getHyperwalletErrors().get(0).getRelatedResources().get(0), is("relatedResource1"));
            assertThat(e.getHyperwalletErrors().get(0).getRelatedResources().get(1), is("relatedResource2"));
            assertThat(e.getResponse(), is(notNullValue()));
        }
    }

    @Test
    public void testPost_200Response_WithWrongContentType() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "wrongContentType")
                        .withBody("{ \"test1\": \"value1\" }")
        );

        try {
            hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorMessage(), is("Invalid Content-Type specified in Response Header"));
            assertThat(e.getErrorCode(), is(nullValue()));
        }
    }

    @Test
    public void testPost_200Response_ResponseContentTypeHeaderWithCharset() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json;charset=utf-8")
                        .withBody("{\"test1\": \"value1\"}")
        );

        TestBody body = hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testPost_200Response_ResponseContentTypeHeaderWithLeadingCharset() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");

        mockServer.when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("Content-Type", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0")
                        .withBody(StringBody.exact("{\"test1\":\"value1\"}")),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "charset=utf-8;application/json")
                        .withBody("{ \"test1\": \"value1\" }")
        );

        TestBody body = hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
    }

    @Test
    public void testGetProxy_withIncorrectPort() {
        try {
            hyperwalletApiClient.setProxy("localhost", 1000);
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.ConnectException: Connection refused")));
        }
    }

    @Test
    public void testGetProxy_withIncorrectHost() {
        try {
            hyperwalletApiClient.setProxy("localhostFail", 9090);
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.UnknownHostException: localhost")));
        }
    }

    @Test
    public void testGetProxy_withIncorrectURL() {
        try {
            hyperwalletApiClient.setProxy("localhost:", 9090);
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.UnknownHostException: localhost")));
        }
    }

    @Test
    public void testPutProxy_withIncorrectPort() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");
        try {
            hyperwalletApiClient.setProxy("localhost", 1000);
            hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.ConnectException: Connection refused")));
        }
    }

    @Test
    public void testPutProxy_withIncorrectHost() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");
        try {
            hyperwalletApiClient.setProxy("localhostFail", 9090);
            hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.UnknownHostException: localhost")));
        }
    }

    @Test
    public void testPutProxy_withIncorrectURL() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");
        try {
            hyperwalletApiClient.setProxy("localhost:", 9090);
            hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.UnknownHostException: localhost")));
        }
    }

    @Test
    public void testPutMultipartProxy_withIncorrectPort() {
        Multipart requestBody = new Multipart();
        List<Multipart.MultipartData> multipartList = new ArrayList<Multipart.MultipartData>();
        requestBody.setMultipartList(multipartList);

        try {
            hyperwalletApiClient.setProxy("localhost", 1000);
            hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.ConnectException: Connection refused")));
        }
    }

    @Test
    public void testPutMultipartProxy_withIncorrectHost() {
        Multipart requestBody = new Multipart();
        List<Multipart.MultipartData> multipartList = new ArrayList<Multipart.MultipartData>();
        requestBody.setMultipartList(multipartList);

        try {
            hyperwalletApiClient.setProxy("localhostFail", 9090);
            hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.UnknownHostException: localhostFail")));
        }
    }

    @Test
    public void testPutMultipartProxy_withIncorrectURL() {
        Multipart requestBody = new Multipart();
        List<Multipart.MultipartData> multipartList = new ArrayList<Multipart.MultipartData>();
        requestBody.setMultipartList(multipartList);

        try {
            hyperwalletApiClient.setProxy("localhost:", 9090);
            hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.UnknownHostException: localhost:")));
        }
    }

    @Test
    public void testPostProxy_withIncorrectPort() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");
        try {
            hyperwalletApiClient.setProxy("localhost", 1000);
            hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.ConnectException: Connection refused")));
        }
    }

    @Test
    public void testPostProxy_withIncorrectHost() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");
        try {
            hyperwalletApiClient.setProxy("localhostFail", 9090);
            hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.UnknownHostException: localhost")));
        }
    }

    @Test
    public void testPostProxy_withIncorrectURL() {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");
        try {
            hyperwalletApiClient.setProxy("localhost:", 9090);
            hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("java.net.UnknownHostException: localhost")));
        }
    }

    @Test
    public void testGet_exceedReadTimeoutIn10Milliseconds() throws InterruptedException {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withBody("{}")
                        .withDelay(TimeUnit.MILLISECONDS, 200)
        );

        hyperwalletApiClient = new HyperwalletApiClient("test-username", "test-password", "1.0",
                null, 10, 10);

        try {
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(equalTo("java.net.SocketTimeoutException: Read timed out")));
        }
    }


    @Test
    public void testGet_ignoreTimeoutWhenSetZeroValue() throws InterruptedException {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-notimeout")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withBody("{ \"test1\": \"notimeout\" }")
                        .withDelay(TimeUnit.MILLISECONDS, 50)
        );

        hyperwalletApiClient = new HyperwalletApiClient("test-username", "test-password", "1.0",
                null, 0, 0);


        try {
            TestBody body = hyperwalletApiClient.get(baseUrl + "/test?test-query=test-notimeout", TestBody.class);
            assertThat(body, is(notNullValue()));
            assertThat(body.test1, is(equalTo("notimeout")));
            assertThat(body.test2, is(nullValue()));

        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(equalTo("java.net.SocketTimeoutException: Read timed out")));
            fail("Not Expected HyperwalletException");
        }
    }

    @Test
    public void testPutMultipart_exceedReadTimeoutIn10Milliseconds() throws InterruptedException {
        mockServer.when(
                HttpRequest.request()
                        .withMethod("PUT")
                        .withPath("/test")
                        .withQueryStringParameter("test-query", "test-value")
                        .withHeader("Authorization", "Basic dGVzdC11c2VybmFtZTp0ZXN0LXBhc3N3b3Jk")
                        .withHeader("Accept", "application/json")
                        .withHeader("User-Agent", "Hyperwallet Java SDK v1.0"),
                Times.exactly(1)
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withBody("{}")
                        .withDelay(TimeUnit.MILLISECONDS, 50)
        );

        hyperwalletApiClient = new HyperwalletApiClient("test-username", "test-password", "1.0",
                null, 10, 10);

        try {
            Multipart requestBody = new Multipart();
            List<Multipart.MultipartData> multipartList = new ArrayList<Multipart.MultipartData>();
            requestBody.setMultipartList(multipartList);
            hyperwalletApiClient.put(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(equalTo("java.net.SocketTimeoutException: Read timed out")));
        }
    }

    private HyperwalletApiClient createAndInjectHyperwalletApiClientMock(Hyperwallet client) throws Exception {
        HyperwalletApiClient mock = Mockito.mock(HyperwalletApiClient.class);

        Field apiClientField = client.getClass().getDeclaredField("apiClient");
        apiClientField.setAccessible(true);
        apiClientField.set(client, mock);

        return mock;
    }

    private String encryptResponse(String responseBody) throws IOException, ParseException, JOSEException {
        ClassLoader classLoader = getClass().getClassLoader();
        String serverPrivateJwksetString =
                IOUtils.toString(classLoader.getResourceAsStream("encryption/server-private-jwkset"), Charset.forName("UTF-8"));
        String clientPublicJwksetString = IOUtils.toString(classLoader.getResourceAsStream("encryption/public-jwkset"), Charset.forName("UTF-8"));

        JWK signingKey = JWKSet.parse(serverPrivateJwksetString).getKeyByKeyId("hwtest");
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(signingKey.getKeyID())
                .criticalParams(Sets.newHashSet(EXP_HEADER))
                .customParam(EXP_HEADER, (System.currentTimeMillis() / 1000) + 300)
                .build();
        JWSObject jwsObject = new JWSObject(header, new Payload(responseBody));
        JWSSigner signer = new RSASSASigner((RSAKey) signingKey);
        jwsObject.sign(signer);

        JWK encryptionKey = JWKSet.parse(clientPublicJwksetString).getKeyByKeyId("2018_enc_rsa_RSA-OAEP-256");
        JWEHeader jweHeader = new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A256CBC_HS512)
                .keyID(encryptionKey.getKeyID())
                .contentType("JWT")
                .build();
        JWEObject jwt = new JWEObject(jweHeader, new Payload(jwsObject));
        RSAEncrypter rsaEncrypter = new RSAEncrypter((RSAKey) encryptionKey);
        jwt.encrypt(rsaEncrypter);

        return jwt.serialize();
    }
}
