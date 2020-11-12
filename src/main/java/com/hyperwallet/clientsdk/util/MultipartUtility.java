package com.hyperwallet.clientsdk.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MultipartUtility {
    private DataOutputStream request;
    private final String boundary = "--001103040245431341";
    private final String crlf = "\r\n";
    private final String twoHyphens = "--";

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

    public DataOutputStream getDataOutputStream() {
        return request;
    }

    public void setDataOutputStream(DataOutputStream request) {
        this.request = request;
    }

}
