package com.hyperwallet.clientsdk.util;

import cc.protea.util.http.Request;
import cc.protea.util.http.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.HyperwalletException;
import com.hyperwallet.clientsdk.model.*;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

public class HyperwalletApiClient {

    private final String username;
    private final String password;
    private final String version;

    public HyperwalletApiClient(final String username, final String password, final String version) {
        this.username = username;
        this.password = password;
        this.version = version;

        // TLS fix
        if (System.getProperty("java.version").startsWith("1.7.")) {
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        } else if (System.getProperty("java.version").startsWith("1.6.")) {
            System.setProperty("https.protocols", "TLSv1,TLSv1.1");
        }
    }

    public <T> T get(final String url, final Class<T> type) {
        Response response = null;
        try {
            response = getService(url, true).getResource();
            return processResponse(response, type);
        } catch (IOException e) {
            throw new HyperwalletException(e);
        }
    }

    public <T> T get(final String url, final TypeReference<T> type) {
        Response response = null;
        try {
            response = getService(url, true).getResource();
            return processResponse(response, type);
        } catch (IOException e) {
            throw new HyperwalletException(e);
        }
    }

    public <T> T put(final String url, final Object bodyObject, final Class<T> type) {
        Response response = null;
        try {
            String body = convert(bodyObject);
            response = getService(url, false).setBody(body).putResource();
            return processResponse(response, type);
        } catch (IOException e) {
            throw new HyperwalletException(e);
        }
    }

    public <T> T post(final String url, final Object bodyObject, final Class<T> type) {
        Response response = null;
        try {
            String body = convert(bodyObject);
            response = getService(url, false).setBody(body).postResource();
            return processResponse(response, type);
        } catch (IOException e) {
            throw new HyperwalletException(e);
        }
    }

    protected <T> T processResponse(final Response response, final Class<T> type) {
        checkErrorResponse(response);
        if (response.getResponseCode() == 204) {
            return convert("{}", type);
        } else {
            return convert(response.getBody(), type);
        }
    }

    protected <T> T processResponse(final Response response, final TypeReference<T> type) {
        checkErrorResponse(response);
        if (response.getResponseCode() == 204) {
            return convert("{}", type);
        } else {
            return convert(response.getBody(), type);
        }
    }

    protected void checkErrorResponse(final Response response) {
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

    private String getAuthorizationHeader() {
        final String pair = this.username + ":" + this.password;
        final String base64 = DatatypeConverter.printBase64Binary(pair.getBytes());
        return "Basic " + base64;
    }

    private Request getService(final String url, boolean isHttpGet) {
        if (isHttpGet) {
            return new Request(url)
                    .addHeader("Authorization", getAuthorizationHeader())
                    .addHeader("Accept", "application/json")
                    .addHeader("User-Agent", "Hyperwallet Java SDK v" + version);
        } else {
            return new Request(url)
                    .addHeader("Authorization", getAuthorizationHeader())
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", "Hyperwallet Java SDK v" + version);
        }
    }

    private <T> T convert(final String json, final Class<T> type) {
        if (json == null) {
            return null;
        }
        return HyperwalletJsonUtil.fromJson(json, type);
    }

    private <T> T convert(final String json, final TypeReference<T> type) {
        return HyperwalletJsonUtil.fromJson(json, type);
    }

    private String convert(final Object object) {
        return HyperwalletJsonUtil.toJson(object);
    }

}
