package com.hyperwallet.clientsdk.util;

import com.hyperwallet.clientsdk.model.HyperwalletVerificationDocument;
import net.minidev.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Multipart {

    List<MultipartData> multipartList;

    public List<MultipartData> getMultipartList() {
        return multipartList;
    }

    public void setMultipartList(List<MultipartData> multipartList) {
        this.multipartList = multipartList;
    }

    public void add(MultipartData multipartData){
        if (multipartList == null) {
            multipartList = new ArrayList<MultipartData>();
        }
        multipartList.add(multipartData);
    }

    public static class MultipartData {
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
    }
}
