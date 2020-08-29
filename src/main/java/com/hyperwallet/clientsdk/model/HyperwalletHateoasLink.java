package com.hyperwallet.clientsdk.model;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class HyperwalletHateoasLink{

    public Params params;
    public String href;

    public HyperwalletHateoasLink() {
    }


    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
    public class Params{

        private String rels;

        public Params() {
            this.rels = rels;
        }

        public String getRels() {
            return rels;
        }

        public void setRels(String rels) {
            this.rels = rels;
        }

    }
}
