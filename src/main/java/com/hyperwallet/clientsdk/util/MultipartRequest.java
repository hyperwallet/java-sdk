package com.hyperwallet.clientsdk.util;


import cc.protea.util.http.Response;
import com.hyperwallet.clientsdk.util.Multipart.MultipartData;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * Represents the HTTP Multipart Request message.
 */
public class MultipartRequest extends Request{

    public static final String BOUNDARY = "--0011010110123111";
    public static final String CRLF = "\r\n";
    private static final String SEPARATOR = "--";

    /**
     * The Constructor takes the url as a String, a proxy as a Proxy, and proxy credentials as a String.
     *
     * @param url               The url parameter does not need the query string parameters if they are going to be supplied via calls to
     *                          {@link #addQueryParameter(String, String)}. You can, however, supply the query parameters in the URL if you wish.
     * @param connectionTimeout A specified timeout value, in milliseconds, to establish communications link to the resource by
     *                          {@link  java.net.URLConnection}.
     * @param readTimeout       A specified timeout, in milliseconds, for reading data from an established connection to the resource
     *                          by {@link  java.net.URLConnection}.
     * @param proxy             The Connection's Proxy value
     * @param proxyUsername     The Proxy username
     * @param proxyPassword     The Proxy password
     */
    public MultipartRequest(String url, int connectionTimeout, int readTimeout, Proxy proxy, String proxyUsername,
            String proxyPassword) {
        super(url, connectionTimeout, readTimeout, proxy, proxyUsername, proxyPassword);
    }

    /**
     * Issues a PUT to the server.
     *
     * @param multipart The {@link Multipart}
     * @return The {@link Response} from the server
     * @throws IOException a {@link IOException}
     */
    public Response putMultipartResource(final Multipart multipart) throws IOException {
        return writeMuiltipartResource("PUT", multipart);
    }

    private Response writeMuiltipartResource(final String method, final Multipart multipartList) throws IOException {
        buildQueryString();
        buildHeaders();

        connection.setDoOutput(true);
        connection.setRequestMethod(method);

        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        writeMultipartBody(dataOutputStream, multipartList.getMultipartList());
        dataOutputStream.flush();
        dataOutputStream.close();

        Response response = readResponse();
        connection.disconnect();
        return response;
    }

    private void writeMultipartBody(DataOutputStream dataOutputStream, List<MultipartData> multipartList) throws IOException {
        for (MultipartData multipartData : multipartList) {
            for (Map.Entry<String, String> entry : multipartData.getEntity().entrySet()) {
                dataOutputStream.writeBytes(SEPARATOR + BOUNDARY + CRLF);
                dataOutputStream.writeBytes(multipartData.getContentDisposition());
                dataOutputStream.writeBytes(multipartData.getContentType());
                dataOutputStream.writeBytes(CRLF);

                if (multipartData.getContentType().contains("image")) {
                    byte[] bytes = Files.readAllBytes(new File(entry.getValue().toString()).toPath());
                    dataOutputStream.write(bytes);
                } else {
                    dataOutputStream.writeBytes(entry.getValue());
                }
                dataOutputStream.writeBytes(CRLF);
                dataOutputStream.flush();
            }
        }
        dataOutputStream.writeBytes(CRLF);
        dataOutputStream.writeBytes(SEPARATOR + BOUNDARY + SEPARATOR + CRLF);
    }
}
