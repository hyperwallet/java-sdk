package com.hyperwallet.clientsdk.model;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

public class HyperwalletList<T> {

    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private int limit;
    private List<T> data;
    private List<HyperwalletHateoasLink> links;

    public List<HyperwalletHateoasLink> getLinks() {
        return links;
    }

    public void setLinks(List<HyperwalletHateoasLink> links) {
        this.links = links;
    }

    public HyperwalletList() {
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
