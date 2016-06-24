package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

public class HyperwalletBaseMonitor {

    @JsonIgnore
    private Set<String> inclusions = new HashSet<String>();

    protected HyperwalletBaseMonitor() {
    }

    public Set<String> getInclusions() {
        return inclusions;
    }

    protected void addField(String field, Object o) {
        if (o == null && inclusions.contains(field)) {
            inclusions.remove(field);
        } else if (o != null) {
            inclusions.add(field);
        }
    }

    protected void clearField(String field) {
        inclusions.add(field);
    }
}
