package com.hyperwallet.clientsdk.util;

import com.hyperwallet.clientsdk.HyperwalletException;
import com.nimbusds.jose.JOSEException;
import org.apache.commons.lang3.StringUtils;
import com.hyperwallet.clientsdk.model.HyperwalletVerificationDocument;
import net.minidev.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HyperwalletMultipartUtils {

    public static Multipart convert(List<HyperwalletVerificationDocument> uploadList) throws IOException {

        final String crlf = "\r\n";
        Multipart multipartList = new Multipart();
        JSONObject document = new JSONObject();
        try {
            for (HyperwalletVerificationDocument uploadData : uploadList) {
                Multipart.MultipartData multipart = new Multipart.MultipartData();
                if (!StringUtils.isEmpty(uploadData.getType())) document.put("type", uploadData.getType());
                if (!StringUtils.isEmpty(uploadData.getCountry())) document.put("country", uploadData.getCountry());
                if (!StringUtils.isEmpty(uploadData.getCategory())) document.put("category", uploadData.getCategory());
                if (!StringUtils.isEmpty(uploadData.getStatus())) document.put("status", uploadData.getStatus());
                List<JSONObject> documents = new ArrayList<>();
                documents.add(document);
                JSONObject data = new JSONObject();
                data.put("documents", documents);
                Map<String, Object> multiPartUploadData = new HashMap<String, Object>();
                multiPartUploadData.put("data", data.toString());

                multipart.setContentType("Content-Type: application/json" + crlf);
                multipart.setContentDisposition("Content-Disposition: form-data; name=\"" + "data" + "\"" + crlf);
                multipart.setEntity(multiPartUploadData);
                multipartList.add(multipart);

                for (Map.Entry<String, String> entry : uploadData.getUploadFiles().entrySet()) {
                    String fileName = entry.getValue();
                    String extension = "";
                    int i = fileName.lastIndexOf('.');
                    if (i >= 0) {
                        extension = fileName.substring(i + 1);
                    }
                    multipart.setContentType("Content-Type: image/" + extension + crlf);
                    multipart.setContentDisposition("Content-Disposition: form-data; name=\"" +
                        entry.getKey() + "\"; filename=\"" +
                        fileName + "\" " + crlf);
                    multiPartUploadData.put(entry.getKey(), Files.readAllBytes(new File(entry.getValue()).toPath()));
                    multipart.setEntity(multiPartUploadData);
                    multipartList.add(multipart);
                }
            }
        } catch (IOException e) {
            throw new HyperwalletException(e);
        }

        return multipartList;
    }
}
