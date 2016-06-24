package com.hyperwallet.clientsdk.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletProgram {

    private String token;
    private Date createdOn;
    private String name;
    private String parentToken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public HyperwalletProgram token(String token) {
        this.token = token;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public HyperwalletProgram createdOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HyperwalletProgram name(String name) {
        this.name = name;
        return this;
    }

    public String getParentToken() {
        return parentToken;
    }

    public void setParentToken(String parentToken) {
        this.parentToken = parentToken;
    }

    public HyperwalletProgram parentToken(String parentToken) {
        this.parentToken = parentToken;
        return this;
    }
}

