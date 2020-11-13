package com.hyperwallet.clientsdk.util;

import com.hyperwallet.clientsdk.HyperwalletException;
import com.hyperwallet.clientsdk.model.HyperwalletVerificationDocument;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HyperwalletMultipartUtils {

    public static Multipart convert(List<HyperwalletVerificationDocument> uploadList) throws IOException {

        final String crlf = "\r\n";
        Multipart multipartList = new Multipart();
        JSONObject document = new JSONObject();
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
            Map<String, String> multiPartUploadData = new HashMap<>();
            multiPartUploadData.put("data", data.toString());

            multipart.setContentType("Content-Type: application/json" + crlf);
            multipart.setContentDisposition("Content-Disposition: form-data; name=\"" + "data" + "\"" + crlf);
            multipart.setEntity(multiPartUploadData);
            multipartList.add(multipart);

            for (Map.Entry<String, String> entry : uploadData.getUploadFiles().entrySet()) {
                Multipart.MultipartData multipart1 = new Multipart.MultipartData();
                Path path = Paths.get(entry.getValue());

                String fileName = path.getFileName().toString();
                String extension = "";
                int i = fileName.lastIndexOf('.');
                if (i >= 0) {
                    extension = fileName.substring(i + 1);
                }
                multipart1.setContentType("Content-Type: image/" + extension + crlf);
                multipart1.setContentDisposition("Content-Disposition: form-data; name=\"" +
                    entry.getKey() + "\"; filename=\"" +
                    fileName + "\" " + crlf);
                Map<String, String> multiPartUploadData1 = new HashMap<>();
                multiPartUploadData1.put(entry.getKey(), entry.getValue());
                multipart1.setEntity(multiPartUploadData1);
                multipartList.add(multipart1);
            }
        }

        return multipartList;
    }
}
