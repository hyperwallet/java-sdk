package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletWebhookNotification {

    public static enum Type {

        USER_CREATED("USERS.CREATED"),
        USER_UPDATED("USERS.UPDATED"),
        USER_UPDATED_STATUS_ACTIVATED("USERS.UPDATED.STATUS.ACTIVATED"),
        USER_UPDATED_STATUS_LOCKED("USERS.UPDATED.STATUS.LOCKED"),
        USER_UPDATED_STATUS_FROZEN("USERS.UPDATED.STATUS.FROZEN"),
        USER_UPDATED_STATUS_DE_ACTIVATED("USERS.UPDATED.STATUS.DE_ACTIVATED"),
        BANK_ACCOUNT_CREATED("USERS.BANK_ACCOUNTS.CREATED"),
        BANK_ACCOUNT_UPDATED("USERS.BANK_ACCOUNTS.UPDATED"),
        BANK_ACCOUNT_STATUS_ACTIVATED("USERS.BANK_ACCOUNTS.UPDATED.STATUS.ACTIVATED"),
        BANK_ACCOUNT_STATUS_VERIFIED("USERS.BANK_ACCOUNTS.UPDATED.STATUS.VERIFIED"),
        BANK_ACCOUNT_STATUS_INVALID("USERS.BANK_ACCOUNTS.UPDATED.STATUS.INVALID"),
        BANK_ACCOUNT_STATUS_DE_ACTIVATED("USERS.BANK_ACCOUNTS.UPDATED.STATUS.DE_ACTIVATED"),
        PREPAID_CARD_CREATED("USERS.PREPAID_CARDS.CREATED"),
        PREPAID_CARD_UPDATED("USERS.PREPAID_CARDS.UPDATED"),
        PREPAID_CARD_STATUS_QUEUED("USERS.PREPAID_CARDS.UPDATED.STATUS.QUEUED"),
        PREPAID_CARD_STATUS_PRE_ACTIVATED("USERS.PREPAID_CARDS.UPDATED.STATUS.PRE_ACTIVATED"),
        PREPAID_CARD_STATUS_ACTIVATED("USERS.PREPAID_CARDS.UPDATED.STATUS.ACTIVATED"),
        PREPAID_CARD_STATUS_DECLINED("USERS.PREPAID_CARDS.UPDATED.STATUS.DECLINED"),
        PREPAID_CARD_STATUS_SUSPENDED("USERS.PREPAID_CARDS.UPDATED.STATUS.SUSPENDED"),
        PREPAID_CARD_STATUS_LOST_OR_STOLEN("USERS.PREPAID_CARDS.UPDATED.STATUS.LOST_OR_STOLEN"),
        PREPAID_CARD_STATUS_DE_ACTIVATED("USERS.PREPAID_CARDS.UPDATED.STATUS.DE_ACTIVATED"),
        PREPAID_CARD_STATUS_COMPLIANCE_HOLD("USERS.PREPAID_CARDS.UPDATED.STATUS.COMPLIANCE_HOLD"),
        PREPAID_CARD_STATUS_KYC_HOLD("USERS.PREPAID_CARDS.UPDATED.STATUS.KYC_HOLD"),
        PREPAID_CARD_STATUS_LOCKED("USERS.PREPAID_CARDS.UPDATED.STATUS.LOCKED"),
        PAPER_CHECK_CREATED("USERS.PAPER_CHECKS.CREATED"),
        PAPER_CHECK_UPDATED("USERS.PAPER_CHECKS.UPDATED"),
        PAPER_CHECK_STATUS_ACTIVATED("USERS.PAPER_CHECKS.UPDATED.STATUS.ACTIVATED"),
        PAPER_CHECK_STATUS_VERIFIED("USERS.PAPER_CHECKS.UPDATED.STATUS.VERIFIED"),
        PAPER_CHECK_STATUS_INVALID("USERS.PAPER_CHECKS.UPDATED.STATUS.INVALID"),
        PAPER_CHECK_STATUS_DE_ACTIVATED("USERS.PAPER_CHECKS.UPDATED.STATUS.DE_ACTIVATED"),
        PAYMENT_CREATED("PAYMENTS.CREATED");

        static Map<String, Type> eventTypeMap = new HashMap<String, Type>();
        static {
            for (Type eventType : Type.values()) {
                eventTypeMap.put(eventType.toString(), eventType);
            }
        }

        public static Type getType(String type) {
            return eventTypeMap.get(type);
        }

        private String type;
        Type(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    private String token;
    private String type;
    private Date createdOn;
    private Object object;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
