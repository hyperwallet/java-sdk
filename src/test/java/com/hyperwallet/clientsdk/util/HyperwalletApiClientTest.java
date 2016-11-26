package com.hyperwallet.clientsdk.util;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.HyperwalletException;
import com.hyperwallet.clientsdk.model.HyperwalletBaseMonitor;
import com.hyperwallet.clientsdk.model.HyperwalletError;
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

import java.util.HashMap;

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
        hyperwalletApiClient = new HyperwalletApiClient("test-username", "test-password", "1.0");
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
            assertThat(e.getMessage(), is(equalTo("java.net.ConnectException: Connection refused")));
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
            assertThat(e.getMessage(), is(equalTo("java.net.ConnectException: Connection refused")));
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
        assertThat(body, is(nullValue()));
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
            assertThat(e.getMessage(), is(equalTo("java.net.ConnectException: Connection refused")));
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
            assertThat(e.getMessage(), is(equalTo("java.net.ConnectException: Connection refused")));
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
            assertThat(e.getMessage(), is(equalTo("java.net.ConnectException: Connection refused")));
        }
    }

}
