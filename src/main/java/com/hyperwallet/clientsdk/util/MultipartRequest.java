package com.hyperwallet.clientsdk.util;

import cc.protea.util.http.Response;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MultipartRequest {
    private final String BOUNDARY = "--0011010110123111";
    private final String SEPARATOR = "--";

    private HttpURLConnection connection;
    private Multipart multipartList = new Multipart();
    private DataOutputStream outStream;
    private Map<String, List<String>> headers = new HashMap<String, List<String>>();
    private String requestURL;

    private final String username;
    private final String password;
    public static final String CRLF = "\r\n";

    public Multipart getMultipartList() {
        return multipartList;
    }

    public void setMultipartList(Multipart multipartList) {
        this.multipartList = multipartList;
    }

    MultipartRequest(String url, Multipart multipartList, String username, String password) throws IOException {
        requestURL = url;
        this.username  = username;
        this.password = password;
        this.multipartList = multipartList;
    }

    public Response putResource(boolean usesProxy, Proxy proxy, final String proxyUsername, final String proxyPassword) throws IOException {
        Response response = new Response() ;
        URL url = new URL(requestURL);
        final String pair = username + ":" + password;
        final String base64 = DatatypeConverter.printBase64Binary(pair.getBytes());
        // If there is proxy authentication provided, it will be set here
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
        connection = usesProxy ? (HttpURLConnection) url.openConnection(proxy) : (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        connection.setRequestProperty("authorization", "Basic " + base64);
        connection.setRequestProperty("accept", "application/json");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        outStream = new DataOutputStream(this.connection.getOutputStream());
        writeMultipartBody();
        outStream.flush();
        outStream.close();
        // checks server's status code first
        int status = this.connection.getResponseCode();
        InputStream responseStream;
        if (status == HttpURLConnection.HTTP_OK || status == HttpURLConnection.HTTP_CREATED) {
            responseStream = new BufferedInputStream(connection.getInputStream());
        } else {
            responseStream = new BufferedInputStream(connection.getErrorStream());
        }
        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = responseStreamReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        responseStreamReader.close();
        response.setResponseCode(status);
        response.setBody(stringBuilder.toString());
        response.setHeaders(this.connection.getHeaderFields());
        this.connection.disconnect();
        return response;
    }

    private void writeMultipartBody() throws IOException {
        for (Multipart.MultipartData multipartData : multipartList.getMultipartList()) {
            for (Map.Entry<String, String> entry : multipartData.getEntity().entrySet()) {
                outStream.writeBytes(this.SEPARATOR + this.BOUNDARY + this.CRLF);
                outStream.writeBytes(multipartData.getContentDisposition());
                outStream.writeBytes(multipartData.getContentType());
                outStream.writeBytes(this.CRLF);

                if (multipartData.getContentType().contains("image")) {
                    byte[] bytes = Files.readAllBytes(new File(entry.getValue().toString()).toPath());
                    outStream.write(bytes);
                    outStream.writeBytes(this.CRLF);
                } else {
                    outStream.writeBytes(entry.getValue() + this.CRLF);
                }
                outStream.flush();
            }
        }
        outStream.writeBytes(this.CRLF);
        outStream.writeBytes(this.SEPARATOR + this.BOUNDARY + this.SEPARATOR + this.CRLF);

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
