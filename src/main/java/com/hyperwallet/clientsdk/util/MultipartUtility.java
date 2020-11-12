package com.hyperwallet.clientsdk.util;

import com.hyperwallet.clientsdk.model.HyperwalletVerificationDocument;
import net.minidev.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MultipartUtility {

    private Map<String, String> formFields;
    private Map<String, String> files;

    public Map<String, String> getFormFields() {
        return formFields;
    }

    public void setFormFields(Map<String, String> formFields) {
        this.formFields = formFields;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }

    public MultipartUtility convert(HyperwalletVerificationDocument uploadData){

        JSONObject document = new JSONObject();
        document.put("type", uploadData.getType());
        document.put("country", uploadData.getCountry());
        document.put("category", uploadData.getCategory());
        List<JSONObject> documents = new ArrayList<>();
        documents.add(document);
        JSONObject data = new JSONObject();
        data.put("documents", documents);
        Map<String,String> multiPartUploadData = new HashMap<String,String>();
        multiPartUploadData.put("data",data.toString());

        formFields = new HashMap<String, String>();
        this.setFormFields(multiPartUploadData);

        this.setFiles(uploadData.getUploadFiles());
        return this;
    }

}
