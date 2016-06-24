package com.hyperwallet.clientsdk.model;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class HyperwalletErrorList {

    public List<HyperwalletError> errors = new ArrayList<HyperwalletError>();

    public HyperwalletErrorList() {
    }

    public List<HyperwalletError> getErrors() {
        return errors;
    }

    public void setErrors(List<HyperwalletError> errors) {
        this.errors = errors;
    }
}
