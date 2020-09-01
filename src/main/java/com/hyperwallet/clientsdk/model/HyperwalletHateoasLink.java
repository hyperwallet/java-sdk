package com.hyperwallet.clientsdk.model;

import java.util.List;
import java.util.Map;

public class HyperwalletHateoasLink{

    public Map<String, String> params;
    public String href;

    public HyperwalletHateoasLink() {
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}

