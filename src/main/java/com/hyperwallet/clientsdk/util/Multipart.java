package com.hyperwallet.clientsdk.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Multipart {

    List<MultipartData> multipartList;

    public List<MultipartData> getMultipartList() {
        return multipartList;
    }

    public void setMultipartList(List<MultipartData> multipartList) {
        this.multipartList = multipartList;
    }

    public void add(MultipartData multipartData) {
        if (multipartList == null) {
            multipartList = new ArrayList<MultipartData>();
        }
        multipartList.add(multipartData);
    }

    public static class MultipartData {
        private final String contentType; //json, img
        private final Map<String, String> entity; //name, content
        private final String contentDisposition;

        MultipartData(String contentType, String contentDisposition, Map<String, String> entity){
            this.contentType = contentType;
            this.contentDisposition = contentDisposition;
            this.entity = entity;
        }
        public String getContentType() {
            return contentType;
        }

        public String getContentDisposition() {
            return contentDisposition;
        }

        public Map<String, String> getEntity() {
            return entity;
        }

    }
}

