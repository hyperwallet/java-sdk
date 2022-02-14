package com.hyperwallet.clientsdk.util;

import cc.protea.util.http.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*

 Modified with modifications copyright Richard Stanford

 -- Original portions by Joe Ernst --

 Copyright 2012 Joe J. Ernst

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 */

/**
 * This class represents an HTTP Message, which could either be a Request or a Response. Message is an abstract class that contains fields and methods that are common to both types of Messages.
 *
 * @param <T>
 *            A Type that extends Message (either {@link Request} or {@link Response})
 */
public abstract class Message<T extends Message<T>> {
    Map<String, List<String>> headers = new HashMap<String, List<String>>();
    String body;

    /**
     * The default constructor is a no-op constructor.
     */
    public Message() {
        // No-arg, No-op constructor
    }

    /**
     * Returns the Message body (also known as the Entity body).
     *
     * @return The body of the HTTP Message. It will typically be HTML, JSON, or XML.
     */
    public String getBody() {
        return this.body;
    }

    public Map<String, List<String>> getHeaders() {
        return this.headers;
    }

    public List<String> getHeaders(final String label) {
        if (label == null) {
            return null;
        }
        for (String key : headers.keySet()) {
            if (label.equalsIgnoreCase(key)) {
                return headers.get(key);
            }
        }
        return null;
    }

    public String getHeader(final String label) {
        List<String> list = getHeaders(label);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Sets the body of the Message.
     *
     * @param body
     *            This is typically the JSON, XML, or Form Parameters being sent to the server.
     * @return this Message, to support chained method calls
     */
    @SuppressWarnings("unchecked")
    public T setBody(final String body) {
        this.body = body;
        return (T) this;
    }

    /**
     * Adds a single header value to the Message.
     *
     * @param name
     *            The header name.
     * @param value
     *            The header value
     * @return this Message, to support chained method calls
     */
    @SuppressWarnings("unchecked")
    public T addHeader(final String name, final String value) {
        List<String> values = new ArrayList<String>();
        values.add(value);

        this.headers.put(name, values);
        return (T) this;
    }

    /**
     * Removes the specified header.
     *
     * @param name
     *            The name of the header to remove.
     * @return this Message, to support chained method calls
     */
    @SuppressWarnings("unchecked")
    public T removeHeader(final String name) {
        this.headers.remove(name);
        return (T) this;
    }

    /**
     * Sets all of the headers in one call.
     *
     * @param headers
     *            A Map of headers, where the header name is a String, and the value is a List of one or more values.
     * @return this Message, to support chained method calls
     */
    @SuppressWarnings("unchecked")
    public T setHeaders(final Map<String, List<String>> headers) {
        this.headers = headers;
        return (T) this;
    }

    String encode(final String in) {
        try {
            return URLEncoder.encode(in, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return in;
        }
    }

}