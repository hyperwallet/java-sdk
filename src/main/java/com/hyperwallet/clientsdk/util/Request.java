package com.hyperwallet.clientsdk.util;

import cc.protea.util.http.Response;
import cc.protea.util.http.BinaryResponse;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*

 Modified with modifications copyright Richard Stanford

 -- Original portions by Joe Ernst --

 * Copyright 2012 Joe J. Ernst
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This class represents an HTTP Request message.
 */
public class Request extends Message<Request> {

    HttpURLConnection connection;
    OutputStreamWriter writer;

    URL url;
    Map<String, String> query = new HashMap<String, String>();

    /**
     * The Constructor takes the url as a String.
     *
     * @param url
     *            The url parameter does not need the query string parameters if they are going to be supplied via calls to
     *            {@link #addQueryParameter(String, String)}. You can, however, supply the query parameters in the URL if you wish.
     */
    public Request(final String url) {
        try {
            this.url = new URL(url);
            this.connection = (HttpURLConnection) this.url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The Constructor takes the url as a String, a proxy as a Proxy, and proxy credentials as a String.
     *
     * @param url
     *            The url parameter does not need the query string parameters if they are going to be supplied via calls to
     *            {@link #addQueryParameter(String, String)}. You can, however, supply the query parameters in the URL if you wish.
     * @param proxy
     *			  The Connection's Proxy value
     *
     * @param proxyUsername
     *            The Proxy username
     *
     *@param proxyPassword
     *			  The Proxy password
     *
     */
    public Request(final String url, final Proxy proxy, final String proxyUsername, final String proxyPassword) {
        try {
            this.url = new URL(url);
            if (proxyUsername != null && proxyPassword != null) {
                // NOTE: Removing Basic Auth from tunneling disabledSchemas is required in order
                // for Proxy Authorization to work. To prevent overriding client System Settings,
                // the client should set this System property themselves inside their JVM Options (1)
                // or their own code (2). Approaches listed below:
                // 1. jdk.http.auth.tunneling.disabledSchemes=
                // 2. System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "false");

                Authenticator authenticator = new DefaultPasswordAuthenticator(
                        proxyUsername, proxyPassword);
                Authenticator.setDefault(authenticator);
            }
            this.connection = (HttpURLConnection) this.url.openConnection(proxy);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a Query Parameter to a list. The list is converted to a String and appended to the URL when the Request is submitted.
     *
     * @param name
     *            The Query Parameter's name
     * @param value
     *            The Query Parameter's value
     * @return this Request, to support chained method calls
     */
    public Request addQueryParameter(final String name, final String value) {
        this.query.put(name, value);
        return this;
    }

    /**
     * Removes the specified Query Parameter.
     *
     * @param name
     *            The name of the Query Parameter to remove
     * @return this Request, to support chained method calls
     */
    public Request removeQueryParameter(final String name) {
        this.query.remove(name);
        return this;
    }

    /**
     * Issues a GET to the server.
     *
     * @return The {@link Response} from the server
     * @throws IOException
     */
    public Response getResource() throws IOException {
        buildQueryString();
        buildHeaders();

        connection.setDoOutput(true);
        connection.setRequestMethod("GET");

        return readResponse();
    }

    /**
     * Issues a GET to the server.
     *
     * @return The {@link Response} from the server
     * @throws IOException
     */
    public BinaryResponse getBinaryResource() throws IOException {
        buildQueryString();
        buildHeaders();

        connection.setDoOutput(true);
        connection.setRequestMethod("GET");

        return readBinaryResponse();
    }

    /**
     * Issues a PUT to the server.
     *
     * @return The {@link Response} from the server
     * @throws IOException
     */
    public Response putResource() throws IOException {
        return writeResource("PUT", this.body);
    }

    public Response headResource() throws IOException {
        buildQueryString();
        buildHeaders();

        connection.setDoOutput(true);
        connection.setRequestMethod("HEAD");

        return readResponse();
    }

    public Response optionsResource() throws IOException {
        buildQueryString();
        buildHeaders();

        connection.setDoOutput(true);
        connection.setRequestMethod("OPTIONS");

        return readResponse();
    }

    public Response traceResource() throws IOException {
        buildQueryString();
        buildHeaders();

        connection.setDoOutput(true);
        connection.setRequestMethod("TRACE");

        return readResponse();
    }

    /**
     * Issues a POST to the server.
     *
     * @return The {@link Response} from the server
     * @throws IOException
     */
    public Response postResource() throws IOException {
        return writeResource("POST", this.body);
    }



    /**
     * Issues a DELETE to the server.
     *
     * @return The {@link Response} from the server
     * @throws IOException
     */
    public Response deleteResource() throws IOException {
        buildQueryString();
        buildHeaders();

        connection.setDoOutput(true);
        connection.setRequestMethod("DELETE");

        return readResponse();
    }

    /**
     * Issues a PURGE to the server.
     *
     * @return The {@link Response} from the server
     * @throws IOException
     */
    public Response purgeResource() throws IOException {
        buildQueryString();
        buildHeaders();

        connection.setDoOutput(true);
        connection.setRequestMethod("PURGE");

        return readResponse();
    }

    /**
     * A private method that handles issuing POST and PUT requests
     *
     * @param method
     *            POST or PUT
     * @param body
     *            The body of the Message
     * @return the {@link Response} from the server
     * @throws IOException
     */
    private Response writeResource(final String method, final String body) throws IOException {
        buildQueryString();
        buildHeaders();

        connection.setDoOutput(true);
        connection.setRequestMethod(method);

        writer = new OutputStreamWriter(connection.getOutputStream());
        writer.write(body);
        writer.close();

        return readResponse();
    }

    /**
     * A private method that handles reading the Responses from the server.
     *
     * @return a {@link Response} from the server.
     * @throws IOException
     */
    private Response readResponse() throws IOException {
        Response response = new Response();
        response.setResponseCode(connection.getResponseCode());
        response.setResponseMessage(connection.getResponseMessage());
        response.setHeaders(connection.getHeaderFields());
        try {
            response.setBody(getStringFromStream(connection.getInputStream()));
        } catch (IOException e) {
            response.setBody(getStringFromStream(connection.getErrorStream()));
        }
        return response;
    }

    /**
     * A private method that handles reading the Responses from the server.
     *
     * @return a {@link Response} from the server.
     * @throws IOException
     */
    private BinaryResponse readBinaryResponse() throws IOException {
        BinaryResponse response = new BinaryResponse();
        response.setResponseCode(connection.getResponseCode());
        response.setResponseMessage(connection.getResponseMessage());
        response.setHeaders(connection.getHeaderFields());
        try {
            response.setBinaryBody(getBinaryFromStream(connection.getInputStream()));
        } catch (IOException e) {
            response.setBinaryBody(getBinaryFromStream(connection.getErrorStream()));
        }
        return response;
    }

    private byte[] getBinaryFromStream(final InputStream is) {
        if (is == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[0xFFFF];
            for (int len = is.read(buffer); len != -1; len = is.read(buffer)) {
                os.write(buffer, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            return null;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // no-op
                }
            }
        }
    }

    private String getStringFromStream(final InputStream is) {
        if (is == null) {
            return null;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            return builder.toString();
        } catch (IOException e) {
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // no-op
                }
            }
        }
    }

    /**
     * A private method that loops through the query parameter Map, building a String to be appended to the URL.
     *
     * @throws MalformedURLException
     */
    private void buildQueryString() throws MalformedURLException {
        StringBuilder builder = new StringBuilder();

        // Put the query parameters on the URL before issuing the request
        if (!query.isEmpty()) {
            for (Map.Entry<String, String> param : query.entrySet()) {
                builder.append(param.getKey());
                builder.append("=");
                builder.append(param.getValue());
                builder.append("&");
            }
            builder.deleteCharAt(builder.lastIndexOf("&")); // Remove the trailing ampersand
        }

        if (builder.length() > 0) {
            // If there was any query string at all, begin it with the question mark
            builder.insert(0, "?");
        }

        url = new URL(url.toString() + builder.toString());
    }

    /**
     * A private method that loops through the headers Map, putting them on the Request or Response object.
     */
    private void buildHeaders() {
        if (!headers.isEmpty()) {
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                List<String> values = entry.getValue();

                for (String value : values) {
                    connection.addRequestProperty(entry.getKey(), value);
                }
            }
        }
    }

    public Request setBodyUrlEncoded(final Map<String, String> map) {

        if (map == null) {
            this.body = null;
            return this;
        }

        StringBuilder sb = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(first ? "" : "&").append(encode(entry.getKey())).append("=").append(encode(entry.getValue()));
            first = false;
        }

        setBody(sb.toString());
        addHeader("Content-type", "application/x-www-form-urlencoded");
        addHeader("Content-length", "" + (body.length()));

        return this;
    }

    static {
        allowMethods("PURGE");
    }

    private static void allowMethods(String... methods) {
        try {
            Field methodsField = HttpURLConnection.class.getDeclaredField("methods");

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(methodsField, methodsField.getModifiers() & ~Modifier.FINAL);

            methodsField.setAccessible(true);

            String[] oldMethods = (String[]) methodsField.get(null);
            Set<String> methodsSet = new LinkedHashSet<String>(Arrays.asList(oldMethods));
            methodsSet.addAll(Arrays.asList(methods));
            String[] newMethods = methodsSet.toArray(new String[0]);

            methodsField.set(null/*static field*/, newMethods);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    private static class DefaultPasswordAuthenticator extends Authenticator {

        /**
         * Username
         */
        private String userName;

        /**
         * Password
         */
        private String password;

        public DefaultPasswordAuthenticator(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        public PasswordAuthentication getPasswordAuthentication() {
            return (new PasswordAuthentication(userName, password.toCharArray()));
        }
    }

}
