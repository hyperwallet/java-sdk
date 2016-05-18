package com.hyperwallet.clientsdk.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletProgram {

    public String token;
    public Date createdOn;
    public String name;
    public String parentToken;

    public String getToken() {
        return token;
    }

    public HyperwalletProgram setToken(String token) {
        this.token = token;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public HyperwalletProgram setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public String getName() {
        return name;
    }

    public HyperwalletProgram setName(String name) {
        this.name = name;
        return this;
    }

    public String getParentToken() {
        return parentToken;
    }

    public HyperwalletProgram setParentToken(String parentToken) {
        this.parentToken = parentToken;
        return this;
    }
}

