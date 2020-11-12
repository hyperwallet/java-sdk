package com.hyperwallet.clientsdk.util;

import javax.xml.bind.DatatypeConverter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class MultipartUtility {
    private HttpURLConnection httpConn;
    private DataOutputStream request;
    private final String boundary = "--001103040245431341";
    private final String crlf = "\r\n";
    private final String twoHyphens = "--";

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String username, String password)
            throws IOException {
        final String pair = username + ":" + password;
        final String base64 = DatatypeConverter.printBase64Binary(pair.getBytes());
        // creates a unique boundary based on time stamp
        URL url = new URL(requestURL);
        this.httpConn = (HttpURLConnection) url.openConnection();
        this.httpConn.setDoOutput(true); // indicates POST method
        this.httpConn.setRequestProperty("authorization", "Basic " + base64);
        this.httpConn.setRequestMethod("PUT");
        this.httpConn.setRequestProperty("accept", "application/json");
        this.httpConn.setRequestProperty(
                "Content-Type", "multipart/form-data; boundary="+this.boundary);
        this.request =  new DataOutputStream(this.httpConn.getOutputStream());
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) throws IOException {
        this.request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
        this.request.writeBytes("Content-Disposition: form-data; name=\"" + name + "\""+ this.crlf);
        this.request.writeBytes(this.crlf);
        this.request.writeBytes(value+ this.crlf);
        this.request.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        String extension = "";
        int i = fileName.lastIndexOf('.');
        if (i >= 0) {
            extension = fileName.substring(i+1);
        }
        this.request.writeBytes(this.crlf + this.twoHyphens + this.boundary + this.crlf);
        this.request.writeBytes("Content-Disposition: form-data; name=\"" +
                fieldName + "\"; filename=\"" +
                fileName + "\" "+ this.crlf);
        this.request.writeBytes("Content-Type: image/"+extension + this.crlf);
        this.request.writeBytes(this.crlf);
        byte[] bytes = Files.readAllBytes(uploadFile.toPath());
        this.request.write(bytes);
    }

    public HttpURLConnection getHttpURLConnection(){
        return this.httpConn;
    }
    public DataOutputStream getDataOutputStream(){
        return this.request;
    }

}
