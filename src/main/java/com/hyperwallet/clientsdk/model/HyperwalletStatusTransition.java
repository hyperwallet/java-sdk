package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.hyperwallet.clientsdk.util.HyperwalletJsonConfiguration;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletStatusTransition extends HyperwalletBaseMonitor {

    public enum Status {
        PRE_ACTIVATED, ACTIVATED, DE_ACTIVATED, FROZEN, SUSPENDED, UNSUSPENDED, LOST_OR_STOLEN, LOCKED, UNLOCKED,
        CREATED, SCHEDULED, PENDING_ACCOUNT_ACTIVATION, PENDING_ID_VERIFICATION, PENDING_TAX_VERIFICATION,
        PENDING_TRANSFER_METHOD_ACTION, PENDING_TRANSACTION_VERIFICATION, IN_PROGRESS, COMPLETED, FAILED,
        RECALLED, RETURNED, EXPIRED, CANCELLED, VERIFIED, INVALID, QUOTED, VERIFICATION_REQUIRED

    }

    private String token;

    private Status transition;
    private Status fromStatus;
    private Status toStatus;

    private Date createdOn;

    private String notes;

    public String getToken() {
        return token;
    }

    public HyperwalletStatusTransition() {
    }

    public HyperwalletStatusTransition(Status transition) {
        addField("transition", transition);
        this.transition = transition;
    }

    public void setToken(String token) {
        addField("token", token);
        this.token = token;
    }

    public HyperwalletStatusTransition token(String token) {
        addField("token", token);
        this.token = token;
        return this;
    }

    public HyperwalletStatusTransition clearToken() {
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

    public HyperwalletStatusTransition clearCreatedOn() {
        clearField("createdOn");
        this.createdOn = null;
        return this;
    }

    public HyperwalletStatusTransition createdOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
        return this;
    }

    public Status getTransition() {
        return transition;
    }

    public void setTransition(Status transition) {
        addField("transition", transition);
        this.transition = transition;
    }

    public HyperwalletStatusTransition transition(Status transition) {
        addField("transition", transition);
        this.transition = transition;
        return this;
    }

    public HyperwalletStatusTransition clearTransition() {
        clearField("transition");
        this.transition = null;
        return this;
    }

    public Status getFromStatus() {
        return fromStatus;
    }

    public void setFromStatus(Status fromStatus) {
        addField("fromStatus", fromStatus);
        this.fromStatus = fromStatus;
    }

    public HyperwalletStatusTransition fromStatus(Status fromStatus) {
        addField("fromStatus", fromStatus);
        this.fromStatus = fromStatus;
        return this;
    }

    public HyperwalletStatusTransition clearFromStatus() {
        clearField("fromStatus");
        this.fromStatus = null;
        return this;
    }

    public Status getToStatus() {
        return toStatus;
    }

    public void setToStatus(Status toStatus) {
        addField("toStatus", toStatus);
        this.toStatus = toStatus;
    }

    public HyperwalletStatusTransition toStatus(Status toStatus) {
        addField("toStatus", toStatus);
        this.toStatus = toStatus;
        return this;
    }

    public HyperwalletStatusTransition clearToStatus() {
        clearField("toStatus");
        this.toStatus = null;
        return this;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        addField("notes", notes);
        this.notes = notes;
    }

    public HyperwalletStatusTransition notes(String notes) {
        addField("notes", notes);
        this.notes = notes;
        return this;
    }

    public HyperwalletStatusTransition clearNotes() {
        clearField("notes");
        this.notes = null;
        return this;
    }
}
