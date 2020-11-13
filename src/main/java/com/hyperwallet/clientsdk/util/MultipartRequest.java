package com.hyperwallet.clientsdk.util;

import cc.protea.util.http.Request;
import cc.protea.util.http.Response;
import com.hyperwallet.clientsdk.HyperwalletException;
import com.hyperwallet.clientsdk.model.HyperwalletVerificationDocument;
import net.minidev.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URL;


public class MultipartRequest extends Request{
    final String boundary = "--0011010110123111";
    final String crlf = "\r\n";
    final String twoHyphens = "--";
    final String DATA = "data";

    HttpURLConnection connection;
    List<Multipart> multipartList;
    DataOutputStream outStream;
    Map<String, List<String>> headers = new HashMap<String, List<String>>();

    public List<Multipart> getMultipartList() {
        return multipartList;
    }

    public void setMultipartList(List<Multipart> multipartList) {
        this.multipartList = multipartList;
    }

    MultipartRequest(String url, List<Multipart> multipartList) throws IOException {
        super(url);
        this.multipartList = multipartList;
    }

    public Response putResource() throws IOException {
        Response response = new Response() ;
        buildHeaders();
        connection.setDoOutput(true); // indicates POST method
        connection.setRequestMethod("PUT");

        outStream = new DataOutputStream(this.connection.getOutputStream());
        writeMultipartBody();
        outStream.flush();
        outStream.close();
        // checks server's status code first
        int status = this.connection.getResponseCode();
        if (status == HttpURLConnection.HTTP_CREATED) {
            InputStream responseStream = new
                BufferedInputStream(connection.getInputStream());
            BufferedReader responseStreamReader =
                new BufferedReader(new InputStreamReader(responseStream));
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
        } else {
            throw new HyperwalletException("Server returned non-OK status: " + status);
        }
        return response;
    }

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

    private void writeMultipartBody() throws IOException {

        for (Multipart multipartData : multipartList ){
            for (Map.Entry<String, Object> entry : multipartData.getEntity().entrySet()) {
                outStream.writeBytes(this.twoHyphens + this.boundary + this.crlf);
                outStream.writeBytes(multipartData.getContentDisposition());
                outStream.writeBytes(multipartData.getContentType());
                outStream.writeBytes(this.crlf);
                outStream.writeBytes(entry.getValue() + this.crlf);
                outStream.flush();
            }
        }
        outStream.writeBytes(this.crlf);
        outStream.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);

    }
}

