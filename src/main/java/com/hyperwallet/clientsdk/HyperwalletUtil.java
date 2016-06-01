package com.hyperwallet.clientsdk;

import cc.protea.util.http.Request;
import cc.protea.util.http.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.model.*;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

class HyperwalletUtil {

    private final String username;
    private final String password;
    private final String version;
    private final String programToken;

    HyperwalletUtil(final String username, final String password, final String programToken, final String version) {
        this.username = username;
        this.password = password;
        this.version = version;
        this.programToken = programToken;
    }

    <T> T options(final String url, final Class<T> type) {
        Response response = null;
        try {
            response = getService(url).optionsResource();
            return processResponse(response, type);
        } catch (IOException e) {
            throw new HyperwalletException(e);
        }
    }

    <T> T get(final String url, final Class<T> type) {
        Response response = null;
        try {
            response = getService(url).getResource();
            return processResponse(response, type);
        } catch (IOException e) {
            throw new HyperwalletException(e);
        }
    }

    <T> T get(final String url, final TypeReference<T> type) {
        Response response = null;
        try {
            response = getService(url).getResource();
            return processResponse(response, type);
        } catch (IOException e) {
            throw new HyperwalletException(e);
        }
    }

    <T> T delete(final String url, final Object bodyObject, final Class<T> type) {
        Response response = null;
        try {
            String body = convert(bodyObject);
            response = getService(url).setBody(body).deleteResource();
            return processResponse(response, type);
        } catch (IOException e) {
            throw new HyperwalletException(e);
        }
    }

    void delete(final String url, final Object bodyObject) {
        Response response = null;
        try {
            String body = convert(bodyObject);
            response = getService(url).setBody(body).deleteResource();
        } catch (IOException e) {
            throw new HyperwalletException(e);
        }
    }

    <T> T put(final String url, final Object bodyObject, final Class<T> type) {
        Response response = null;
        try {
            String body = convert(bodyObject);
            response = getService(url).setBody(body).putResource();
            return processResponse(response, type);
        } catch (IOException e) {
            throw new HyperwalletException(e);
        }
    }

    <T> T post(final String url, final Object bodyObject, final Class<T> type) {
        Response response = null;
        try {
            String body = convert(bodyObject);
            response = getService(url).setBody(body).postResource();
            return processResponse(response, type);
        } catch (IOException e) {
            throw new HyperwalletException(e);
        }
    }

    protected <T> T processResponse(final Response response, final Class<T> type) {
        checkErrorResponse(response);
        return convert(response.getBody(), type);
    }

    protected <T> T processResponse(final Response response, final TypeReference<T> type) {
        checkErrorResponse(response);
        return convert(response.getBody(), type);
    }

    protected void checkErrorResponse(final Response response) {
        HyperwalletErrorList errorList = null;
        if (response.getResponseCode() >= 400) {
            errorList = convert(response.getBody(), HyperwalletErrorList.class);
            if (errorList != null) {
                throw new HyperwalletException(errorList);
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

    private Request getService(final String url) {
        return new Request(url)
                .addHeader("Authorization", getAuthorizationHeader())
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "Hyperwallet Java SDK v" + version);
    }

    <T> T convert(final String json, final Class<T> type) {
        return convert(json, type, true);
    }

    <T> T convert(final String json, final TypeReference<T> type) {
        return convert(json, type, true);
    }


    <T> T convert(final String json, final Class<T> type, final boolean handleErrors) {
        if (json == null) {
            return null;
        }
        if (String.class.equals(type)) {
            return (T) json;
        }
        return HyperwalletJsonUtil.fromJson(json, type);
    }

    <T> T convert(final String json, final TypeReference<T> type, final boolean handleErrors) {
        if (json == null) {
            return null;
        }
        if (String.class.equals(type)) {
            return (T) json;
        }
        return HyperwalletJsonUtil.fromJson(json, type);
    }

    private String convert(final Object object) {
        return HyperwalletJsonUtil.toJson(object);
    }

    void setProgramToken(HyperwalletUser user) {
        if (user != null && user.getProgramToken() == null) {
            user.setProgramToken(this.programToken);
        }
    }

    void setProgramToken(HyperwalletPayment payment) {
        if (payment != null && payment.getProgramToken() == null) {
            payment.setProgramToken(this.programToken);
        }
    }

    HyperwalletUser clean(HyperwalletUser user) {
        user = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(user), HyperwalletUser.class);
        setProgramToken(user);
        return user;
    }

    HyperwalletPayment clean(HyperwalletPayment payment) {
        payment = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(payment), HyperwalletPayment.class);
        setProgramToken(payment);
        return payment;
    }

    HyperwalletPrepaidCard clean(HyperwalletPrepaidCard method) {
        method = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(method), HyperwalletPrepaidCard.class);
        return method;
    }

    HyperwalletBankAccount clean(HyperwalletBankAccount method) {
        method = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(method), HyperwalletBankAccount.class);
        return method;
    }

    HyperwalletStatusTransition clean(HyperwalletStatusTransition statusTransition) {
        statusTransition = HyperwalletJsonUtil.fromJson(HyperwalletJsonUtil.toJson(statusTransition), HyperwalletStatusTransition.class);
        return statusTransition;
    }
}
