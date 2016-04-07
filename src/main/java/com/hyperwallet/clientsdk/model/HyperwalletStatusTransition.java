package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletStatusTransition {

	public static enum Status { ACTIVATED, DE_ACTIVATED, SUSPENDED, UNSUSPENDED, LOST_OR_STOLEN }

	public String token;

	public Status transition;
	public Status fromStatus;
	public Status toStatus;

	public String notes;

	public String getToken() {
		return token;
	}

	public HyperwalletStatusTransition() {
	}

	public HyperwalletStatusTransition(Status status) {
		this.transition = status;
	}

	public HyperwalletStatusTransition(Status status, String notes) {
		this(status);
		this.notes = notes;
	}

	public HyperwalletStatusTransition setToken(String token) {
		this.token = token;
		return this;
	}

	public Status getTransition() {
		return transition;
	}

	public HyperwalletStatusTransition setTransition(Status transition) {
		this.transition = transition;
		return this;
	}

	public Status getFromStatus() {
		return fromStatus;
	}

	public HyperwalletStatusTransition setFromStatus(Status fromStatus) {
		this.fromStatus = fromStatus;
		return this;
	}

	public Status getToStatus() {
		return toStatus;
	}

	public HyperwalletStatusTransition setToStatus(Status toStatus) {
		this.toStatus = toStatus;
		return this;
	}

	public String getNotes() {
		return notes;
	}

	public HyperwalletStatusTransition setNotes(String notes) {
		this.notes = notes;
		return this;
	}

}
