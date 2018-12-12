package com.hyperwallet.clientsdk.util;

import cc.protea.util.http.Request;
import cc.protea.util.http.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.HyperwalletException;
import com.hyperwallet.clientsdk.model.HyperwalletErrorList;
import com.nimbusds.jose.JOSEException;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

public class HyperwalletApiClient {

    private final String username;
    private final String password;
    private final String version;
    private final HyperwalletEncryption hyperwalletEncryption;
    private final boolean isEncrypted;

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
        try {
            Request request = getService(url, true);
            Response response = request.getResource();
            return processResponse(request, response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    public <T> T get(final String url, final TypeReference<T> type) {
        try {
            Request request = getService(url, true);
            Response response = request.getResource();
            return processResponse(request, response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    public <T> T put(final String url, final Object bodyObject, final Class<T> type) {
        try {
            String body = encrypt(convert(bodyObject));
            Request request = getService(url, false).setBody(body);
            Response response = request.putResource();
            return processResponse(request, response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    public <T> T post(final String url, final Object bodyObject, final Class<T> type) {
        try {
            String body = bodyObject != null ? encrypt(convert(bodyObject)) : "";
            Request request = getService(url, false).setBody(body);
            Response response = request.postResource();
            return processResponse(request, response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    public <T> T post(final String url, final Object bodyObject, final Class<T> type, HashMap<String,String> header) {
        try {
            String body = encrypt(convert(bodyObject));
            Request request = getService(url, false).setBody(body);
            if (header != null) {
                for (String key : header.keySet()) {
                    request = request.addHeader(key, header.get(key));
                }
            }
            Response response = request.postResource();
            return processResponse(request, response, type);
        } catch (IOException | JOSEException | ParseException e) {
            throw new HyperwalletException(e);
        }
    }

    protected <T> T processResponse(final Request request, final Response response, final Class<T> type)
            throws ParseException, JOSEException, IOException {
        checkErrorResponse(response);
        checkContentType(request, response);
        if (response.getResponseCode() == 204) {
            return convert("{}", type);
        } else {
            return convert(response.getBody(), type);
        }
    }

    protected <T> T processResponse(final Request request, final Response response, final TypeReference<T> type)
            throws ParseException, JOSEException, IOException {
        checkErrorResponse(response);
        checkContentType(request, response);
        if (response.getResponseCode() == 204) {
            return convert("{}", type);
        } else {
            return convert(response.getBody(), type);
        }
    }

    protected void checkErrorResponse(final Response response) throws ParseException, JOSEException, IOException {
        HyperwalletErrorList errorList = null;
        if (response.getResponseCode() >= 400) {
            errorList = convert(response.getBody(), HyperwalletErrorList.class);
            if (errorList != null) {
                throw new HyperwalletException(response, errorList);
            } else {//unmapped errors
                throw new HyperwalletException(response, response.getResponseCode(), response.getResponseMessage());
            }
        }
    }

    protected void checkContentType(final Request request, final Response response) {
        String requestAccept = request.getHeader("Accept");
        String responseContentType = response.getHeader("Content-Type");
        String json = "application/json";
        String jose = "application/jose+json";

        if ((StringUtils.isEmpty(requestAccept) && (StringUtils.isEmpty(responseContentType) || !responseContentType.equals(json))) ||
                (!StringUtils.isEmpty(requestAccept) && requestAccept.equals(json) && (StringUtils.isEmpty(responseContentType)
                        || !responseContentType.equals(json))) ||
                (!StringUtils.isEmpty(requestAccept) && requestAccept.equals(jose) && (StringUtils.isEmpty(responseContentType) || !(
                        responseContentType.equals(json) || responseContentType.equals(jose))))) {
            throw new HyperwalletException("Invalid Content-Type specified in Response Header");
        }
    }

    private String getAuthorizationHeader() {
        final String pair = this.username + ":" + this.password;
        final String base64 = DatatypeConverter.printBase64Binary(pair.getBytes());
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

    private <T> T convert(final String responseBody, final Class<T> type)
            throws ParseException, JOSEException, IOException {
        if (responseBody == null) {
            return null;
        }
        return HyperwalletJsonUtil.fromJson(decryptResponse(responseBody), type);
    }

    private <T> T convert(final String responseBody, final TypeReference<T> type)
            throws ParseException, JOSEException, IOException {
        return HyperwalletJsonUtil.fromJson(decryptResponse(responseBody), type);
    }

    private String convert(final Object object) {
        return HyperwalletJsonUtil.toJson(object);
    }

    private String encrypt(String body) throws JOSEException, IOException, ParseException {
        return isEncrypted ? hyperwalletEncryption.encrypt(body) : body;
    }

    private String decryptResponse(String responseBody) throws ParseException, IOException, JOSEException {
        return isEncrypted ? hyperwalletEncryption.decrypt(responseBody) : responseBody;
    }

}
