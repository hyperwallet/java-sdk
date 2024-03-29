package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
        USER_UPDATED_STATUS_PRE_ACTIVATED("USERS.UPDATED.STATUS.PRE_ACTIVATED"),
        PAYPAL_ACCOUNT_CREATED("USERS.PAYPAL_ACCOUNTS.CREATED"),
        PAYPAL_ACCOUNT_STATUS_ACTIVATED("USERS.PAYPAL_ACCOUNTS.UPDATED.STATUS.ACTIVATED"),
        PAYPAL_ACCOUNT_STATUS_VERIFIED("USERS.PAYPAL_ACCOUNTS.UPDATED.STATUS.VERIFIED"),
        PAYPAL_ACCOUNT_STATUS_INVALID("USERS.PAYPAL_ACCOUNTS.UPDATED.STATUS.INVALID"),
        PAYPAL_ACCOUNT_STATUS_DE_ACTIVATED("USERS.PAYPAL_ACCOUNTS.UPDATED.STATUS.DE_ACTIVATED"),
        VENMO_ACCOUNT_CREATED("USERS.VENMO_ACCOUNTS.CREATED"),
        VENMO_ACCOUNT_STATUS_ACTIVATED("USERS.VENMO_ACCOUNTS.UPDATED.STATUS.ACTIVATED"),
        VENMO_ACCOUNT_STATUS_VERIFIED("USERS.VENMO_ACCOUNTS.UPDATED.STATUS.VERIFIED"),
        VENMO_ACCOUNT_STATUS_INVALID("USERS.VENMO_ACCOUNTS.UPDATED.STATUS.INVALID"),
        VENMO_ACCOUNT_STATUS_DE_ACTIVATED("USERS.VENMO_ACCOUNTS.UPDATED.STATUS.DE_ACTIVATED"),
        USER_UPDATED_VERIFICATION_STATUS_REQUIRED("USERS.UPDATED.VERIFICATION_STATUS.REQUIRED"),
        USER_UPDATED_VERIFICATION_STATUS_UNDER_REVIEW("USERS.UPDATED.VERIFICATION_STATUS.UNDER_REVIEW"),
        USER_UPDATED_VERIFICATION_STATUS_VERIFIED("USERS.UPDATED.VERIFICATION_STATUS.VERIFIED"),
        USER_UPDATED_VERIFICATION_STATUS_NOT_REQUIRED("USERS.UPDATED.VERIFICATION_STATUS.NOT_REQUIRED"),
        USER_UPDATED_TAX_VERIFICATION_STATUS_NOT_REQUIRED("USERS.UPDATED.TAX_VERIFICATION_STATUS.NOT_REQUIRED"),
        USER_UPDATED_TAX_VERIFICATION_STATUS_REQUIRED("USERS.UPDATED.TAX_VERIFICATION_STATUS.REQUIRED"),
        USER_UPDATED_TAX_VERIFICATION_STATUS_UNDER_REVIEW("USERS.UPDATED.TAX_VERIFICATION_STATUS.UNDER_REVIEW"),
        USER_UPDATED_TAX_VERIFICATION_STATUS_VERIFIED("USERS.UPDATED.TAX_VERIFICATION_STATUS.VERIFIED"),
        USER_BUSINESS_STAKEHOLDER_UPDATED_VERIFICATION_STATUS_REQUIRED("USERS.BUSINESS_STAKEHOLDERS.UPDATED.VERIFICATION_STATUS.REQUIRED"),
        USER_BUSINESS_STAKEHOLDER_UPDATED_VERIFICATION_STATUS_UNDER_REVIEW("USERS.BUSINESS_STAKEHOLDERS.UPDATED.VERIFICATION_STATUS.UNDER_REVIEW"),
        USER_BUSINESS_STAKEHOLDER_UPDATED_VERIFICATION_STATUS_VERIFIED("USERS.BUSINESS_STAKEHOLDERS.UPDATED.VERIFICATION_STATUS.VERIFIED"),
        USER_BUSINESS_STAKEHOLDER_UPDATED_VERIFICATION_STATUS_NOT_REQUIRED("USERS.BUSINESS_STAKEHOLDERS.UPDATED.VERIFICATION_STATUS.NOT_REQUIRED"),
        USER_BUSINESS_STAKEHOLDER_CREATED("USERS.BUSINESS_STAKEHOLDERS.CREATED"),
        USER_BUSINESS_STAKEHOLDER_UPDATED_STATUS_ACTIVATED("USERS.BUSINESS_STAKEHOLDERS.UPDATED.STATUS.ACTIVATED"),
        USER_BUSINESS_STAKEHOLDER_UPDATED_STATUS_DE_ACTIVATED("USERS.BUSINESS_STAKEHOLDERS.UPDATED.STATUS.DE_ACTIVATED"),
        BANK_ACCOUNTS_UPDATED_EXTERNAL_VERIFICATION_STATUS_REQUIRED("USERS.BANK_ACCOUNTS.UPDATED.EXTERNAL_VERIFICATION_STATUS.REQUIRED"),
        BANK_ACCOUNTS_UPDATED_EXTERNAL_VERIFICATION_STATUS_UNDER_REVIEW("USERS.BANK_ACCOUNTS.UPDATED.EXTERNAL_VERIFICATION_STATUS.UNDER_REVIEW"),
        BANK_ACCOUNTS_UPDATED_EXTERNAL_VERIFICATION_STATUS_VERIFIED("USERS.BANK_ACCOUNTS.UPDATED.EXTERNAL_VERIFICATION_STATUS.VERIFIED"),
        BANK_ACCOUNTS_UPDATED_EXTERNAL_VERIFICATION_STATUS_FAILED("USERS.BANK_ACCOUNTS.UPDATED.EXTERNAL_VERIFICATION_STATUS.FAILED"),
        BANK_CARD_CREATED("USERS.BANK_CARDS.CREATED"),
        BANK_CARD_STATUS_ACTIVATED("USERS.BANK_CARDS.UPDATED.STATUS.ACTIVATED"),
        BANK_CARD_STATUS_VERIFIED("USERS.BANK_CARDS.UPDATED.STATUS.VERIFIED"),
        BANK_CARD_STATUS_INVALID("USERS.BANK_CARDS.UPDATED.STATUS.INVALID"),
        BANK_CARD_STATUS_DE_ACTIVATED("USERS.BANK_CARDS.UPDATED.STATUS.DE_ACTIVATED"),
        BANK_ACCOUNT_DDA_CREATED("USERS.BANK_ACCOUNTS.DIRECT_DEBIT_AUTHORIZATIONS.CREATED"),
        BANK_ACCOUNT_DDA_STATUS_ACTIVATED("USERS.BANK_ACCOUNTS.DIRECT_DEBIT_AUTHORIZATIONS.UPDATED.STATUS.ACTIVATED"),
        BANK_ACCOUNT_DDA_STATUS_FAILED("USERS.BANK_ACCOUNTS.DIRECT_DEBIT_AUTHORIZATIONS.UPDATED.STATUS.FAILED"),
        BANK_ACCOUNT_DDA_STATUS_CANCELLED("USERS.BANK_ACCOUNTS.DIRECT_DEBIT_AUTHORIZATIONS.UPDATED.STATUS.CANCELLED"),
        BANK_ACCOUNT_DDA_STATUS_EXPIRED("USERS.BANK_ACCOUNTS.DIRECT_DEBIT_AUTHORIZATIONS.UPDATED.STATUS.EXPIRED"),
        PREPAID_CARD_UPDATED_VERIFICATION_STATUS_REQUIRED("USERS.PREPAID_CARDS.UPDATED.VERIFICATION_STATUS.REQUIRED"),
        PREPAID_CARD_UPDATED_VERIFICATION_STATUS_UNDER_REVIEW("USERS.PREPAID_CARDS.UPDATED.VERIFICATION_STATUS.UNDER_REVIEW"),
        PREPAID_CARD_UPDATED_VERIFICATION_STATUS_VERIFIED("USERS.PREPAID_CARDS.UPDATED.VERIFICATION_STATUS.VERIFIED"),
        PAYMENTS_CREATED("PAYMENTS.CREATED"),
        PAYMENTS_UPDATED_STATUS_SCHEDULED("PAYMENTS.UPDATED.STATUS.SCHEDULED"),
        PAYMENTS_UPDATED_STATUS_PENDING_ACCOUNT_ACTIVATION("PAYMENTS.UPDATED.STATUS.PENDING_ACCOUNT_ACTIVATION"),
        PAYMENTS_UPDATED_STATUS_PENDING_ID_VERIFICATION("PAYMENTS.UPDATED.STATUS.PENDING_ID_VERIFICATION"),
        PAYMENTS_UPDATED_STATUS_PENDING_TAX_VERIFICATION("PAYMENTS.UPDATED.STATUS.PENDING_TAX_VERIFICATION"),
        PAYMENTS_UPDATED_STATUS_PENDING_TRANSFER_METHOD_ACTION("PAYMENTS.UPDATED.STATUS.PENDING_TRANSFER_METHOD_ACTION"),
        PAYMENTS_UPDATED_STATUS_EXPIRED("PAYMENTS.UPDATED.STATUS.EXPIRED"),
        PAYMENTS_UPDATED_STATUS_PENDING_TRANSACTION_VERIFICATION("PAYMENTS.UPDATED.STATUS.PENDING_TRANSACTION_VERIFICATION"),
        PAYMENTS_UPDATED_STATUS_IN_PROGRESS("PAYMENTS.UPDATED.STATUS.IN_PROGRESS"),
        PAYMENTS_UPDATED_STATUS_UNCLAIMED("PAYMENTS.UPDATED.STATUS.UNCLAIMED"),
        PAYMENTS_UPDATED_STATUS_COMPLETED("PAYMENTS.UPDATED.STATUS.COMPLETED"),
        PAYMENTS_UPDATED_STATUS_CANCELLED("PAYMENTS.UPDATED.STATUS.CANCELLED"),
        PAYMENTS_UPDATED_STATUS_FAILED("PAYMENTS.UPDATED.STATUS.FAILED"),
        PAYMENTS_UPDATED_STATUS_RECALLED("PAYMENTS.UPDATED.STATUS.RECALLED"),
        PAYMENTS_UPDATED_STATUS_RETURNED("PAYMENTS.UPDATED.STATUS.RETURNED"),
        PAYMENTS_UPDATED_STATUS_WAITING_FOR_SUPPLEMENTAL_DATA("PAYMENTS.UPDATED.STATUS.WAITING_FOR_SUPPLEMENTAL_DATA"),
        PAYMENTS_UPDATED_STATUS_WAITING_FOR_RELEASE("PAYMENTS.UPDATED.STATUS.WAITING_FOR_RELEASE"),
        TRANSFERS_UPDATED_STATUS_SCHEDULED("TRANSFERS.UPDATED.STATUS.SCHEDULED"),
        TRANSFERS_UPDATED_STATUS_IN_PROGRESS("TRANSFERS.UPDATED.STATUS.IN_PROGRESS"),
        TRANSFERS_UPDATED_STATUS_COMPLETED("TRANSFERS.UPDATED.STATUS.COMPLETED"),
        TRANSFERS_UPDATED_STATUS_FAILED("TRANSFERS.UPDATED.STATUS.FAILED"),
        TRANSFERS_UPDATED_STATUS_CANCELLED("TRANSFERS.UPDATED.STATUS.CANCELLED"),
        TRANSFERS_UPDATED_STATUS_RETURNED("TRANSFERS.UPDATED.STATUS.RETURNED"),
        TRANSFERS_UPDATED_STATUS_VERIFICATION_REQUIRED("TRANSFERS.UPDATED.STATUS.VERIFICATION_REQUIRED"),
        TRANSFERS_REFUND_CREATED("TRANSFERS.REFUND.CREATED"),
        TRANSFERS_REFUND_UPDATED("TRANSFERS.REFUND.UPDATED"),
        PAYMENT_ROUTING_ACCOUNTS_CREATED("PAYMENT_ROUTING_ACCOUNTS.CREATED");


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
    private List<HyperwalletLink> links;

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

    public List<HyperwalletLink> getLinks() {
        return links;
    }

    public void setLinks(List<HyperwalletLink> links) {
        this.links = links;
    }

    public HyperwalletWebhookNotification links(List<HyperwalletLink> links) {
        setLinks(links);
        return this;
    }
}
