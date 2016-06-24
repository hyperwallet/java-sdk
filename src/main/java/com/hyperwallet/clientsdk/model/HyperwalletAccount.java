package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by rbao on 5/20/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletAccount {

    public enum EType {FUNDING, MERCHANT, REVENUE, COLLECTIONS, VIRTUAL_INCENTIVES, POST_FUNDING}

    private String token;
    private EType type;
    private Date createdOn;
    private String email;

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public EType getType() {
        return type;
    }

    public void setType(EType type) {
        this.type = type;
    }

    public HyperwalletAccount token(String token) {
        setToken(token);
        return this;
    }

    public HyperwalletAccount type(EType type) {
        setType(type);
        return this;
    }

    public HyperwalletAccount createdOn(Date createdOn) {
        setCreatedOn(createdOn);
        return this;
    }

    public HyperwalletAccount email(String email) {
        setEmail(email);
        return this;
    }

}
