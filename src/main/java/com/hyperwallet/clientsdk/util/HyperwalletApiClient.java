package com.hyperwallet.clientsdk.util;

import cc.protea.util.http.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.HyperwalletException;
import com.hyperwallet.clientsdk.model.HyperwalletErrorList;
import com.nimbusds.jose.JOSEException;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.ParseException;
import java.util.HashMap;
import java.util.UUID;

/**
 * The Hyperwallet API Client
 */
public class HyperwalletApiClient {

    private static final String ACCEPT = "Accept";
    private static final String APPLICATION_JOSE_JSON = "application/jose+json";
    private static final String APPLICATION_JSON = "application/json";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String MULTIPART_FORM_DATA_BOUNDARY = "multipart/form-data; boundary=";
    private static final String SDK_TYPE = "java";
    private final HyperwalletEncryption hyperwalletEncryption;
    private Proxy proxy;
    private String proxyPassword;
    private String proxyUsername;
    private final String contextId;
    private final String password;
    private final String username;
    private final String version;
    private final boolean isEncrypted;
    private final int connectionTimeout;
    private final int readTimeout;

    /**
     * Create a instance of the API client
     *
     * @param username              the API username
     * @param password              the API password
     * @param version               the SDK version
     * @param hyperwalletEncryption the {@link HyperwalletEncryption}
     * @param connectionTimeout     the timeout value that will be used for making new connections to the Hyperwallet API (in milliseconds).
     * @param readTimeout           the timeout value that will be used when reading data from an established connection to
     *                              the Hyperwallet API (in milliseconds).
     */
    public HyperwalletApiClient(final String username, final String password, final String version,
            final HyperwalletEncryption hyperwalletEncryption, final int connectionTimeout, final int readTimeout) {
        this.username = username;
        this.password = password;
        this.version = version;
        this.hyperwalletEncryption = hyperwalletEncryption;
        this.isEncrypted = hyperwalletEncryption != null;
        this.contextId = String.valueOf(UUID.randomUUID());

        this.connectionTimeout = connectionTimeout;
        this.readTimeout = readTimeout;

        // TLS fix
        if (System.getProperty("java.version").startsWith("1.7.")) {
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        }
    }

    /**
     * Perform a GET call to the Hyperwallet API server
     *
     * @param <T>  Response class type
     * @param url  The api endpoint to call
     * @param type The response class type
     * @return an instance of response class type
     */
    public <T> T get(final String url, final Class<T> type) {
        Response response = null;
        try {
            response = buildGetRequest(url)
                    .getResource();
            return processResponse(response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    /**
     * Perform a GET call to the Hyperwallet API server
     *
     * @param <T>  Response class type
     * @param url  The api endpoint to call
     * @param type The response {@link TypeReference} type
     * @return an instance of {@link TypeReference}
     */
    public <T> T get(final String url, final TypeReference<T> type) {
        Response response = null;
        try {
            response = buildGetRequest(url)
                    .getResource();
            return processResponse(response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    /**
     * Perform a PUT call to the Hyperwallet API server to upload {@link Multipart}
     *
     * @param <T>        Response class type
     * @param url        The api endpoint to call
     * @param uploadData The {@link Multipart}
     * @param type       The response class type
     * @return an instance of response class type
     */
    public <T> T put(final String url, final Multipart uploadData, final Class<T> type) {
        try {
            Response response = buildMultipartRequest(url)
                    .putMultipartResource(uploadData);
            return processResponse(response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    /**
     * Perform a PUT call to the Hyperwallet API server to upload {@link Object}
     *
     * @param <T>        Response class type
     * @param url        The api endpoint to call
     * @param bodyObject The {@link Object} body
     * @param type       The response class type
     * @return an instance of response class type
     */
    public <T> T put(final String url, final Object bodyObject, final Class<T> type) {
        Response response = null;
        try {
            String body = convert(bodyObject);
            response = buildRequest(url)
                    .setBody(encrypt(body))
                    .putResource();
            return processResponse(response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    /**
     * Perform a POST call to the Hyperwallet API server to upload {@link Object}
     *
     * @param <T>        Response class type
     * @param url        The api endpoint to call
     * @param bodyObject The {@link Object} body
     * @param type       The response class type
     * @return an instance of response class type
     */
    public <T> T post(final String url, final Object bodyObject, final Class<T> type) {
        Response response = null;
        try {
            Request request = buildRequest(url);
            String body = bodyObject != null ? encrypt(convert(bodyObject)) : "";
            request.setBody(body);
            response = request.postResource();
            return processResponse(response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    /**
     * Perform a POST call to the Hyperwallet API server to upload {@link Object}
     *
     * @param <T>        Response class type
     * @param url        The api endpoint to call
     * @param bodyObject The {@link Object} body
     * @param type       The response class type
     * @param header     HTTP header properties
     * @return an instance of response class type
     */
    public <T> T post(final String url, final Object bodyObject, final Class<T> type, HashMap<String, String> header) {
        Response response = null;
        try {
            String body = convert(bodyObject);
            Request request = buildRequest(url)
                    .setBody(encrypt(body));
            if (header != null) {
                for (String key : header.keySet()) {
                    request = request.addHeader(key, header.get(key));
                }
            }

            response = request.postResource();
            return processResponse(response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    /**
     * Checks if current API Client instance uses a proxy
     *
     * @return Boolean if client has a proxy config
     */
    public Boolean usesProxy() {
        return proxy != null;
    }

    /**
     * Create Proxy setting for API Client instance
     *
     * @param url  url of Proxy
     * @param port port of Proxy
     */
    public void setProxy(String url, Integer port) {
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(url, port));
    }

    /**
     * Returns Proxy setting for Authentication
     *
     * @return Proxy current Proxy config of client
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * Create Proxy setting for API Client instance
     *
     * @param proxy value of Proxy
     */
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Returns Proxy Username for Authentication
     *
     * @return current ProxyUsername
     */
    public String getProxyUsername() {
        return proxyUsername;
    }

    /**
     * Create Proxy Username for Authentication
     *
     * @param proxyUsername username of Proxy
     */
    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    /**
     * Returns Proxy Password for Authentication
     *
     * @return current ProxyUsername
     */
    public String getProxyPassword() {
        return proxyPassword;
    }

    /**
     * Create Proxy Password setting for Authentication
     *
     * @param proxyPassword username of Proxy
     */
    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    /**
     * Process the {@link  Response} to return success <T> class
     *
     * @param response the {@link  Response}
     * @param type     The response class type
     * @return an instance of <T>
     */
    private <T> T processResponse(final Response response, final Class<T> type)
            throws ParseException, JOSEException, IOException {
        checkErrorResponse(response);
        checkResponseHeader(response);
        if (response.getResponseCode() == 204) {
            return convert("{}", type);
        } else {
            return convert(decryptResponse(response.getBody()), type);
        }
    }

    /**
     * Process the {@link  Response} to return success {@link TypeReference<T>} class
     *
     * @param response the {@link  Response}
     * @param type     The response {@link TypeReference<T>} type
     * @return an instance of {@link TypeReference<T>}
     */
    private <T> T processResponse(final Response response, final TypeReference<T> type)
            throws ParseException, JOSEException, IOException {
        checkErrorResponse(response);
        checkResponseHeader(response);
        if (response.getResponseCode() == 204) {
            return convert("{}", type);
        } else {
            return convert(decryptResponse(response.getBody()), type);
        }
    }

    /**
     * Evaluates the {@link Response} contains Api Server error when HTTP status from 4.x.x and raise {@link HyperwalletException}
     *
     * @param response the {@link  Response}
     * @throws HyperwalletException a {@link HyperwalletException}
     */
    protected void checkErrorResponse(final Response response) {
        HyperwalletErrorList errorList = null;
        if (response.getResponseCode() >= 400) {
            try {
                errorList = convert(decryptErrorResponse(response), HyperwalletErrorList.class);
            } catch (Exception e) {
                //Failed to convert the response
                throw new HyperwalletException(response, response.getResponseCode(), response.getResponseMessage(), e);
            }

            if (errorList != null && !errorList.getErrors().isEmpty()) {
                throw new HyperwalletException(response, errorList);
            } else {//unmapped errors
                throw new HyperwalletException(response, response.getResponseCode(), response.getResponseMessage());
            }
        }
    }

    private void checkResponseHeader(Response response) {
        String contentTypeHeader = response.getHeader(CONTENT_TYPE);
        String expectedContentType = getContentType();
        boolean invalidContentType = response.getResponseCode() != 204 && contentTypeHeader != null
                && !contentTypeHeader.contains(expectedContentType);
        if (invalidContentType) {
            throw new HyperwalletException("Invalid Content-Type specified in Response Header");
        }
    }

    private String getAuthorizationHeader() {
        final String pair = this.username + ":" + this.password;
        final String base64 = DatatypeConverter.printBase64Binary(pair.getBytes());
        return "Basic " + base64;
    }

    private Request buidBaseRequest(final String url) {
        Request request = new Request(url, connectionTimeout, readTimeout, proxy, proxyUsername, proxyPassword);
        request.addHeader("Authorization", getAuthorizationHeader())
                .addHeader("User-Agent", "Hyperwallet Java SDK v" + version)
                .addHeader("x-sdk-version", version)
                .addHeader("x-sdk-type", SDK_TYPE)
                .addHeader("x-sdk-contextId", contextId);
        return request;
    }

    private Request buildGetRequest(final String url) {
        Request request = buidBaseRequest(url);
        request.addHeader(ACCEPT, getContentType());
        return request;
    }

    private Request buildRequest(final String url) {
        Request request = buildGetRequest(url);
        String contentType = getContentType();

        request.addHeader(ACCEPT, contentType)
                .addHeader(CONTENT_TYPE, contentType);
        return request;
    }

    private Request buildMultipartRequest(final String url) {
        Request request = buildGetRequest(url);
        String contentType = buildMultipartContentType();
        request.addHeader(ACCEPT, APPLICATION_JSON)
                .addHeader(CONTENT_TYPE, contentType);
        return request;
    }

    private String getContentType() {
        return isEncrypted ? APPLICATION_JOSE_JSON : APPLICATION_JSON;
    }

    private String buildMultipartContentType() {
        return MULTIPART_FORM_DATA_BOUNDARY + Request.BOUNDARY;
    }

    private <T> T convert(final String responseBody, final Class<T> type) {
        if (responseBody == null) {
            return null;
        }
        return HyperwalletJsonUtil.fromJson(responseBody, type);
    }

    private <T> T convert(final String responseBody, final TypeReference<T> type) {
        return HyperwalletJsonUtil.fromJson(responseBody, type);
    }

    private String convert(final Object object) {
        return HyperwalletJsonUtil.toJson(object);
    }

    private String encrypt(String body) throws JOSEException, IOException, ParseException {
        return isEncrypted ? hyperwalletEncryption.encrypt(body) : body;
    }

    private String decryptResponse(String responseBody) throws ParseException, IOException, JOSEException {
        if (responseBody == null || responseBody.length() == 0) {
            return null;
        }
        return isEncrypted ? hyperwalletEncryption.decrypt(responseBody) : responseBody;
    }

    /**
     * Method to handle error responses based on the content type header.  Although the HyperWallet encryption object is set, it is still possible to
     * receive an error response with content type=application/json.  Please see the following
     * <a href="https://docs.hyperwallet.com/content/api/v4/overview/payload-encryption">documentation</a>.
     *
     * @param response The response received from the server
     * @return The decrypted error response
     */
    private String decryptErrorResponse(Response response) throws ParseException, IOException, JOSEException {
        String responseBody = response.getBody();
        if (responseBody == null || responseBody.length() == 0) {
            return null;
        }
        return isEncrypted && isJoseContentType(response) ? hyperwalletEncryption.decrypt(responseBody) : responseBody;
    }

    private Boolean isJoseContentType(Response response) {
        String contentTypeHeader = response.getHeader(CONTENT_TYPE);
        return contentTypeHeader != null && contentTypeHeader.contains(APPLICATION_JOSE_JSON);
    }
}
