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

public class HyperwalletApiClient {

    private static final String SDK_TYPE = "java";
    private static final String ACCEPT = "Accept";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JOSE_JSON = "application/jose+json";
    private static final String APPLICATION_JSON = "application/json";
    private static final String MULTIPART_FORM_DATA_BOUNDARY = "multipart/form-data; boundary=";
    private final String username;
    private final String password;
    private final String version;
    private final HyperwalletEncryption hyperwalletEncryption;
    private final boolean isEncrypted;
    private final String contextId;
    private Proxy proxy;
    private String proxyUsername;
    private String proxyPassword;
    /**
     * A specified timeout value, in milliseconds, to establish communications link to Hyperwallet API resource.
     */
    private final int connectionTimeout;
    /**
     * A specified timeout, in milliseconds, for reading data from an established connection to Hyperwallet API resource.
     */
    private final int readTimeout;

    public HyperwalletApiClient(final String username, final String password, final String version,
            HyperwalletEncryption hyperwalletEncryption, int connectionTimeout, int readTimeout) {
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

    public <T> T put(final String url, Multipart uploadData, final Class<T> type) {
        Response response = null;
        try {
            response = buildMultipartRequest(url)
                    .putMultipartResource(uploadData);
            return processResponse(response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

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

    protected <T> T processResponse(final Response response, final Class<T> type)
            throws ParseException, JOSEException, IOException {
        checkErrorResponse(response);
        checkResponseHeader(response);
        if (response.getResponseCode() == 204) {
            return convert("{}", type);
        } else {
            return convert(decryptResponse(response.getBody()), type);
        }
    }

    protected <T> T processResponse(final Response response, final TypeReference<T> type)
            throws ParseException, JOSEException, IOException {
        checkErrorResponse(response);
        checkResponseHeader(response);
        if (response.getResponseCode() == 204) {
            return convert("{}", type);
        } else {
            return convert(decryptResponse(response.getBody()), type);
        }
    }

    protected void checkErrorResponse(final Response response) throws ParseException, JOSEException, IOException {
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
     * @throws ParseException
     * @throws IOException
     * @throws JOSEException
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

    public Boolean usesProxy() {
        return proxy != null;
    }

    public void setProxy(String url, Integer port) {
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(url, port));
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }
}
