package com.hyperwallet.clientsdk.util;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

public class MultipartUtility {
    private HttpURLConnection httpConn;
    private DataOutputStream request;
    private final String boundary;
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
        boundary = "--X-INSOMNIA-BOUNDARY";
        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
//        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
//        httpConn.setDoInput(true);
        httpConn.setRequestProperty("authorization", "Basic " + base64);
        httpConn.setRequestMethod("PUT");
        httpConn.setRequestProperty("accept", "application/json");
        httpConn.setRequestProperty(
                "Content-Type", "multipart/form-data; boundary="+this.boundary);
        request =  new DataOutputStream(httpConn.getOutputStream());
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) throws IOException {
        request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
  //      request.writeBytes("Content-Type: application/json" + this.crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" + name + "\""+ this.crlf);
        request.writeBytes(this.crlf);
        request.writeBytes(value+ this.crlf);

        request.flush();
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
        String modification = "Tue, 22 Sep 2020 13:15:19 GMT";
        int size = 138995;
        request.writeBytes(this.crlf + this.twoHyphens + this.boundary + this.crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                fieldName + "\"; filename=\"" +
                fileName + "\" "+ this.crlf);
        request.writeBytes("Content-Type: image/png" + this.crlf);
        request.writeBytes(this.crlf);
        byte[] bytes = Files.readAllBytes(uploadFile.toPath());
        request.write(bytes);
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public String finish() throws IOException {
        String response ="";

        request.writeBytes(this.crlf);
        request.writeBytes(this.twoHyphens + this.boundary +
                this.twoHyphens + this.crlf);

        request.flush();
        request.close();

        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            InputStream responseStream = new
                    BufferedInputStream(httpConn.getInputStream());

            BufferedReader responseStreamReader =
                    new BufferedReader(new InputStreamReader(responseStream));

            String line = "";
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            responseStreamReader.close();

            response = stringBuilder.toString();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }

        return response;
    }
}
