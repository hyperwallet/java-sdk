package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletAuthenticationToken {

    private String value;

    /**
     * Method to set value in the Json response
     * @return String of authentication token
     */
    public String getValue() {
        return value;
    }
}
