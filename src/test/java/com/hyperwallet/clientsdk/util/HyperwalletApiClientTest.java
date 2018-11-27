package com.hyperwallet.clientsdk.util;

import cc.protea.util.http.Response;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.HyperwalletException;
import com.hyperwallet.clientsdk.model.HyperwalletBaseMonitor;
import com.hyperwallet.clientsdk.model.HyperwalletError;
import com.hyperwallet.clientsdk.model.HyperwalletPayment;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.testng.Assert.fail;

/**
 * @author fkrauthan
 */
public class HyperwalletApiClientTest {

    private ClientAndServer mockServer;

    private String baseUrl;

    private HyperwalletApiClient hyperwalletApiClient;

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
        hyperwalletApiClient = new HyperwalletApiClient("test-username", "test-password", "1.0", null);
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
                        .withBody("{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", \"message\": \"test5\" }] }")
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
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", new TypeReference<TestBody>() {});
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

        TestBody body = hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", new TypeReference<TestBody>() {});
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
        );

        TestBody body = hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", new TypeReference<TestBody>() {});
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
        );

        TestBody body = hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", new TypeReference<TestBody>() {});
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
                        .withBody("{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", \"message\": \"test5\" }] }")
        );

        try {
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", new TypeReference<TestBody>() {});
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
            hyperwalletApiClient.get(baseUrl + "/test?test-query=test-value", new TypeReference<TestBody>() {});
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("500")));
            assertThat(e.getErrorMessage(), is(equalTo("Internal Server Error")));
            assertThat(e.getHyperwalletErrors(), is(nullValue()));
            assertThat(e.getResponse(), is(notNullValue()));
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
                        .withBody("{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", \"message\": \"test5\" }] }")
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
                        .withBody("{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", \"message\": \"test5\" }] }")
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
                        .withBody("{ \"errors\": [{ \"code\": \"test1\", \"fieldName\": \"test2\", \"message\": \"test3\" }, { \"code\": \"test4\", \"message\": \"test5\" }] }")
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
            hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class,headers);
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
                                        "        \"href\" : \"https://maelle.paylution.com/rest/v3/payments/pmt-81aad61a-03ff-4995-a2fc-6a6e2d8911af\"\n" +
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
                                        "        \"href\" : \"https://maelle.paylution.com/rest/v3/payments/pmt-81aad61a-03ff-4995-a2fc-6a6e2d8911af\"\n" +
                                        "      } ]\n" +
                                        "    }")
        );
        hyperwalletApiClient.post(baseUrl + "/test?test-query=test-value", requestBody, HyperwalletPayment.class);
    }

    @Test
    public void testPost_200Response_withEncryption() throws Exception {
        TestBody requestBody = new TestBody();
        requestBody.test1 = "value1";
        requestBody.getInclusions().add("test1");
        ClassLoader classLoader = getClass().getClassLoader();
        String hyperwalletKeysPath = new File(classLoader.getResource("encryption/public-jwkset").toURI()).getAbsolutePath();
        String clientPrivateKeysPath = new File(classLoader.getResource("encryption/private-jwkset").toURI()).getAbsolutePath();
        HyperwalletEncryption hyperwalletEncryption = new HyperwalletEncryption.HyperwalletEncryptionBuilder()
                .clientPrivateKeySetLocation(clientPrivateKeysPath).hyperwalletKeySetLocation(hyperwalletKeysPath).build();
        String testBody = "{\"test1\":\"value1\"}";
        String encryptedBody = hyperwalletEncryption.encrypt(testBody);

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
                        .withBody(encryptedBody)
        );


        HyperwalletApiClient hyperwalletApiClientEnc = new HyperwalletApiClient(
                "test-username", "test-password", "1.0", hyperwalletEncryption);
        TestBody body = hyperwalletApiClientEnc.post(baseUrl + "/test?test-query=test-value", requestBody, TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test1, is(equalTo("value1")));
        assertThat(body.test2, is(nullValue()));
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
}
