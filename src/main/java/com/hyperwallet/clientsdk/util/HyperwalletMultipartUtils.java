package com.hyperwallet.clientsdk.util;

import com.hyperwallet.clientsdk.model.HyperwalletVerificationDocument;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HyperwalletMultipartUtils {

    public static Multipart convert(List<HyperwalletVerificationDocument> uploadList) throws IOException {

        Multipart multipartList = new Multipart();
        List<JSONObject> documents = new ArrayList<>();
        for (HyperwalletVerificationDocument uploadData : uploadList) {

            JSONObject document = new JSONObject();
            addDocumentValue(document, "type", uploadData.getType());
            addDocumentValue(document, "country", uploadData.getCountry());
            addDocumentValue(document, "category", uploadData.getCategory());
            addDocumentValue(document, "status", uploadData.getStatus());

            documents.add(document);

            for (Map.Entry<String, String> entry : uploadData.getUploadFiles().entrySet()) {

                Path path = Paths.get(entry.getValue());

                String fileName = path.getFileName().toString();
                String extension = "";
                int i = fileName.lastIndexOf('.');
                if (i >= 0) {
                    extension = fileName.substring(i + 1);
                }
                Map<String, String> entity = new HashMap<>();
                entity.put(entry.getKey(), entry.getValue());
                Multipart.MultipartData multipart1 = new Multipart.MultipartData("Content-Type: image/" + extension + Request.CRLF,
                        "Content-Disposition: form-data; name=\"" +
                                entry.getKey() + "\"; filename=\"" +
                                fileName + "\" " + Request.CRLF,
                        entity );
                multipartList.add(multipart1);
            }
        }
        JSONObject data = new JSONObject();
        data.put("documents", documents);
        Map<String, String> multiPartUploadData = new HashMap<>();
        multiPartUploadData.put("data", data.toString());

        Multipart.MultipartData multipart = new Multipart.MultipartData("Content-Type: application/json" + Request.CRLF,
                "Content-Disposition: form-data; name=\"" + "data" + "\"" + Request.CRLF,
                multiPartUploadData);
        multipartList.add(multipart);

        return multipartList;
    }

    private static void addDocumentValue(JSONObject document, String field, String value) {
        if (!StringUtils.isEmpty(value)) {
            document.put(field, value);
        }
    }
}
