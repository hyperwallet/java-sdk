package com.hyperwallet.clientsdk.model;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.hyperwallet.clientsdk.util.HyperwalletJsonConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletProgram extends HyperwalletBaseMonitor{

    private String token;
    private Date createdOn;
    private String name;
    private String parentToken;
    private List<HyperwalletLink> links;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        addField("token", token);
        this.token = token;
    }

    public HyperwalletProgram token(String token) {
        addField("token", token);
        this.token = token;
        return this;
    }

    public HyperwalletProgram clearToken() {
        clearField("token");
        this.token = null;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
    }

    public HyperwalletProgram createdOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
        return this;
    }

    public HyperwalletProgram clearCreatedOn() {
        clearField("createdOn");
        this.createdOn = null;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        addField("name", name);
        this.name = name;
    }

    public HyperwalletProgram name(String name) {
        addField("name", name);
        this.name = name;
        return this;
    }

    public HyperwalletProgram clearName() {
        clearField("name");
        this.name = null;
        return this;
    }

    public String getParentToken() {
        return parentToken;
    }

    public void setParentToken(String parentToken) {
        addField("parentToken", parentToken);
        this.parentToken = parentToken;
    }

    public HyperwalletProgram parentToken(String parentToken) {
        addField("parentToken", parentToken);
        this.parentToken = parentToken;
        return this;
    }

    public HyperwalletProgram clearParentToken() {
        clearField("parentToken");
        this.parentToken = null;
        return this;
    }

    public List<HyperwalletLink> getLinks() {
        return links;
    }

    public void setLinks(List<HyperwalletLink> links) {
        addField("links", links);
        this.links = links;
    }

    public HyperwalletProgram links(List<HyperwalletLink> links) {
        addField("links", links);
        this.links = links;
        return this;
    }

    public HyperwalletProgram clearLinks() {
        clearField("links");
        this.links = null;
        return this;
    }
}

