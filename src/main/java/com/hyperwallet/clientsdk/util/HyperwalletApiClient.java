package com.hyperwallet.clientsdk.util;

import cc.protea.util.http.Request;
import cc.protea.util.http.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.HyperwalletException;
import com.hyperwallet.clientsdk.model.HyperwalletErrorList;
import com.hyperwallet.clientsdk.model.HyperwalletVerificationDocument;
import com.nimbusds.jose.JOSEException;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.io.File;
import java.util.List;

public class HyperwalletApiClient {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String VALID_JSON_CONTENT_TYPE = "application/json";
    private static final String VALID_JSON_JOSE_CONTENT_TYPE = "application/jose+json";
    private HttpURLConnection httpConn;
    private final String username;
    private final String password;
    private final String version;
    private final HyperwalletEncryption hyperwalletEncryption;
    private final boolean isEncrypted;
    private final String BOUNDARY = "--001103040245431341";

    public HyperwalletApiClient(final String username, final String password, final String version) {
        this(username, password, version, null);
    }

    public HyperwalletApiClient(final String username, final String password, final String version,
            HyperwalletEncryption hyperwalletEncryption) {
        this.username = username;
        this.password = password;
        this.version = version;
        this.hyperwalletEncryption = hyperwalletEncryption;
        this.isEncrypted = hyperwalletEncryption != null;

        // TLS fix
        if (System.getProperty("java.version").startsWith("1.7.")) {
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        }
    }

    public <T> T get(final String url, final Class<T> type) {
        Response response = null;
        try {
            response = getService(url, true).getResource();
            return processResponse(response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    public <T> T get(final String url, final TypeReference<T> type) {
        Response response = null;
        try {
            response = getService(url, true).getResource();
            return processResponse(response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    public <T> T put(final String url, Multipart uploadData, final Class<T> type) {
        Response response = null;
        try {
            response = getMultipartService(url, uploadData).putResource();
            return processResponse(response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    public <T> T put(final String url, final Object bodyObject, final Class<T> type) {
        Response response = null;
        try {
            String body = convert(bodyObject);
            response = getService(url, false).setBody(encrypt(body)).putResource();
            return processResponse(response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    public <T> T post(final String url, final Object bodyObject, final Class<T> type) {
        Response response = null;
        try {
            Request request = getService(url, false);
            String body = bodyObject != null ? encrypt(convert(bodyObject)) : "";
            request.setBody(body);
            response = request.postResource();
            return processResponse(response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    public <T> T post(final String url, final Object bodyObject, final Class<T> type, HashMap<String,String> header) {
        Response response = null;
        try {
            String body = convert(bodyObject);
            Request request = getService(url, false).setBody(encrypt(body));
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
            errorList = convert(decryptResponse(response.getBody()), HyperwalletErrorList.class);
            if (errorList != null) {
                throw new HyperwalletException(response, errorList);
            } else {//unmapped errors
                throw new HyperwalletException(response, response.getResponseCode(), response.getResponseMessage());
            }
        }
    }

    private void checkResponseHeader(Response response) {
        String contentTypeHeader = response.getHeader(CONTENT_TYPE_HEADER);
        String expectedContentType = isEncrypted ? VALID_JSON_JOSE_CONTENT_TYPE : VALID_JSON_CONTENT_TYPE;
        boolean invalidContentType = response.getResponseCode() != 204 && contentTypeHeader != null
                && !contentTypeHeader.contains(expectedContentType);
        if (invalidContentType) {
            throw new HyperwalletException("Invalid Content-Type specified in Response Header");
        }
    }

    private String getAuthorizationHeader() {
         String pair = this.username + ":" + this.password;
         String base64 = DatatypeConverter.printBase64Binary(pair.getBytes());
        return "Basic " + base64;
    }

    private Request getService(final String url, boolean isHttpGet) {
        String contentType = "application/" + ((isEncrypted) ? "jose+json" : "json");
        if (isHttpGet) {
            return new Request(url)
                    .addHeader("Authorization", getAuthorizationHeader())
                    .addHeader("Accept", contentType)
                    .addHeader("User-Agent", "Hyperwallet Java SDK v" + version);
        } else {
            return new Request(url)
                    .addHeader("Authorization", getAuthorizationHeader())
                    .addHeader("Accept", contentType)
                    .addHeader("Content-Type", contentType)
                    .addHeader("User-Agent", "Hyperwallet Java SDK v" + version);
        }
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
        if (responseBody == null) {
            return null;
        }
        return isEncrypted ? hyperwalletEncryption.decrypt(responseBody) : responseBody;
    }

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @throws IOException
     */

    private MultipartRequest getMultipartService(String requestURL, Multipart multipartData)
            throws IOException {
        // creates a unique boundary based on time stamp
        return (MultipartRequest) new MultipartRequest(requestURL, multipartData)
            .addHeader("Authorization", getAuthorizationHeader())
            .addHeader("Accept", VALID_JSON_CONTENT_TYPE)
            .addHeader("Content-Type", "multipart/form-data; boundary=" + this.BOUNDARY)
            .addHeader("User-Agent", "Hyperwallet Java SDK v" + version);
    }
}
