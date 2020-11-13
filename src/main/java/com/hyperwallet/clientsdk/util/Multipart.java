package com.hyperwallet.clientsdk.util;

import com.hyperwallet.clientsdk.model.HyperwalletVerificationDocument;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Multipart {

    private String contentType; //json, img
    private Map<String, Object> entity; //name, object
    private String contentDisposition;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    public Map<String, Object> getEntity() {
        return entity;
    }

    public void setEntity(Map<String, Object> entity) {
        this.entity = entity;
    }


//    public Multipart convert(HyperwalletVerificationDocument uploadData){
//
//        JSONObject document = new JSONObject();
//        document.put("type", uploadData.getType());
//        document.put("country", uploadData.getCountry());
//        document.put("category", uploadData.getCategory());
//        List<JSONObject> documents = new ArrayList<>();
//        documents.add(document);
//        JSONObject data = new JSONObject();
//        data.put("documents", documents);
//        Map<String,String> multiPartUploadData = new HashMap<String,String>();
//        multiPartUploadData.put("data",data.toString());
//
//        formFields = new HashMap<String, String>();
//        this.setFormFields(multiPartUploadData);
//
//        this.setFiles(uploadData.getUploadFiles());
//        return this;
//    }

}
