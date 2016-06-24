package com.hyperwallet.clientsdk.model;

import java.util.ArrayList;
import java.util.List;

public class HyperwalletList<T> {

    private int count;
    private int offset;
    private int limit;

    private List<T> data = new ArrayList<T>();

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
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
