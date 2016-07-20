package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletReceipt {

    public static enum Entry { DEBIT, CREDIT };

    public static enum Type {
        // Generic Fees
        ANNUAL_FEE,
        ANNUAL_FEE_REFUND,
        CUSTOMER_SERVICE_FEE,
        CUSTOMER_SERVICE_FEE_REFUND,
        EXPEDITED_SHIPPING_FEE,
        GENERIC_FEE_REFUND,
        MONTHLY_FEE,
        MONTHLY_FEE_REFUND,
        PAYMENT_EXPIRY_FEE,
        PAYMENT_FEE,
        PROCESSING_FEE,
        STANDARD_SHIPPING_FEE,
        TRANSFER_FEE,

        // Generic Payment Types
        ADJUSTMENT,
        FOREIGN_EXCHANGE,
        DEPOSIT,
        MANUAL_ADJUSTMENT,
        PAYMENT_EXPIRATION,

        // Related to Bank Accounts
        BANK_ACCOUNT_TRANSFER_FEE,
        BANK_ACCOUNT_TRANSFER_RETURN,
        BANK_ACCOUNT_TRANSFER_RETURN_FEE,
        TRANSFER_TO_BANK_ACCOUNT,

        // Related to Cards
        CARD_ACTIVATION_FEE,
        CARD_ACTIVATION_FEE_WAIVER,
        CARD_FEE,
        MANUAL_TRANSFER_TO_PREPAID_CARD,
        PREPAID_CARD_ACCOUNT_DEPOSIT,
        PREPAID_CARD_ACCOUNT_FEE,
        PREPAID_CARD_ANNUAL_FEE_DISCOUNT,
        PREPAID_CARD_BALANCE_INQUIRY_FEE,
        PREPAID_CARD_BILL_REPRINT_FEE,
        PREPAID_CARD_CASH_ADVANCE,
        PREPAID_CARD_ATM_OR_CASH_ADVANCE_FEE,
        PREPAID_CARD_CASH_ADVANCE_CHARGEBACK,
        PREPAID_CARD_CASH_ADVANCE_CHARGEBACK_REVERSAL,
        PREPAID_CARD_CASH_ADVANCE_REPRESS,
        PREPAID_CARD_CASH_ADVANCE_REPRESS_REVERSAL,
        PREPAID_CARD_CHARGEBACK,
        PREPAID_CARD_CHARGEBACK_REFUND,
        PREPAID_CARD_CHARGEBACK_REFUND_REVERSAL,
        PREPAID_CARD_CHARGEBACK_REVERSAL,
        PREPAID_CARD_COMMISSION_OR_FEE,
        PREPAID_CARD_DEBIT_TRANSFER,
        PREPAID_CARD_DISPUTED_CHARGE_REFUND,
        PREPAID_CARD_DISPUTE_DEPOSIT,
        PREPAID_CARD_DOCUMENT_REQUEST_FEE,
        PREPAID_CARD_DOMESTIC_CASH_WITHDRAWAL_FEE,
        PREPAID_CARD_EMERGENCY_CASH,
        PREPAID_CARD_EMERGENCY_CARD,
        PREPAID_CARD_EXCHANGE_RATE_DIFFERENCE,
        PREPAID_CARD_INCOME,
        PREPAID_CARD_LOAD_FEE,
        PREPAID_CARD_MANUAL_UNLOAD,
        PREPAID_CARD_OVERDUE_PAYMENT_INTEREST,
        PREPAID_CARD_OVERSEAS_CASH_WITHDRAWAL_FEE,
        PREPAID_CARD_PAYMENT,
        PREPAID_CARD_PIN_CHANGE_FEE,
        PREPAID_CARD_PIN_REPRINT_FEE,
        PREPAID_CARD_PRIORITY_PASS_FEE,
        PREPAID_CARD_PRIORITY_PASS_RENEWAL,
        PREPAID_CARD_RECURRING_INTEREST,
        PREPAID_CARD_REFUND,
        PREPAID_CARD_REFUND_REPRESS,
        PREPAID_CARD_REFUND_REPRESS_REVERSAL,
        PREPAID_CARD_REPLACEMENT_FEE,
        PREPAID_CARD_SALE,
        PREPAID_CARD_SALE_REPRESS,
        PREPAID_CARD_SALE_REVERSAL,
        PREPAID_CARD_STATEMENT_FEE,
        PREPAID_CARD_TELEPHONE_SUPPORT_FEE,
        PREPAID_CARD_TRANSFER_FEE,
        PREPAID_CARD_TRANSFER_RETURN,
        PREPAID_CARD_UNLOAD,
        PREPAID_CARD_BANK_WITHDRAWAL_REVERSAL,
        PREPAID_CARD_BANK_WITHDRAWAL_CHARGEBACK,
        TRANSFER_TO_PREPAID_CARD,

        // Related to Donations
        DONATION,
        DONATION_FEE,
        DONATION_RETURN,

        // Related to Merchant Payments
        MERCHANT_PAYMENT,
        MERCHANT_PAYMENT_FEE,
        MERCHANT_PAYMENT_REFUND,
        MERCHANT_PAYMENT_RETURN,

        // Related to MoneyGram
        MONEYGRAM_TRANSFER_RETURN,
        TRANSFER_TO_MONEYGRAM,

        // Related to Paper Checks
        PAPER_CHECK_FEE,
        PAPER_CHECK_REFUND,
        TRANSFER_TO_PAPER_CHECK,

        // Related to PayNearMe
        PAYNEARME_CASH_DEPOSIT,

        // Related to Users or Program Accounts
        ACCOUNT_CLOSURE,
        ACCOUNT_CLOSURE_FEE,
        ACCOUNT_UNLOAD,
        DORMANT_USER_FEE,
        DORMANT_USER_FEE_REFUND,
        PAYMENT,
        PAYMENT_CANCELLATION,
        PAYMENT_REVERSAL,
        PAYMENT_REVERSAL_FEE,
        PAYMENT_RETURN,
        TRANSFER_TO_PROGRAM_ACCOUNT,
        TRANSFER_TO_USER,

        // Related to Virtual Incentives
        VIRTUAL_INCENTIVE_CANCELLATION,
        VIRTUAL_INCENTIVE_ISSUANCE,
        VIRTUAL_INCENTIVE_PURCHASE,
        VIRTUAL_INCENTIVE_REFUND,

        // Related to Western Union and WUBS
        TRANSFER_TO_WESTERN_UNION,
        TRANSFER_TO_WUBS_WIRE,
        WESTERN_UNION_TRANSFER_RETURN,
        WUBS_WIRE_TRANSFER_RETURN,

        // Related to Wire Transfers
        TRANSFER_TO_WIRE,
        WIRE_TRANSFER_FEE,
        WIRE_TRANSFER_RETURN
    }

    public static enum DetailFieldKey {
        CLIENT_PAYMENT_ID("clientPaymentId"),
        NOTES("notes"),
        MEMO("memo"),
        RETURN_OR_RECALL_REASON("returnOrRecallReason"),
        WEBSITE("website"),
        PAYER_NAME("payerName"),
        PAYEE_NAME("payeeName"),
        CHARITY_NAME("charityName"),
        CARD_HOLDER_NAME("cardHolderName"),
        BANK_NAME("bankName"),
        BANK_ID("bankId"),
        BRANCH_NAME("branchName"),
        BRANCH_ID("branchId"),
        BANK_ACCOUNT_ID("bankAccountId"),
        BANK_ACCOUNT_PURPOSE("bankAccountPurpose"),
        BRANCH_ADDRESS_LINE1("branchAddressLine1"),
        BRANCH_ADDRESS_LINE2("branchAddressLine2"),
        BRANCH_CITY("branchCity"),
        BRANCH_STATE_PROVINCE("branchStateProvince"),
        BRANCH_COUNTRY("branchCountry"),
        BRANCH_POSTAL_CODE("branchPostalCode"),
        CHECK_NUMBER("checkNumber"),
        CARD_NUMBER("cardNumber"),
        CARD_EXPIRY_DATE("cardExpiryDate"),
        PAYEE_EMAIL("payeeEmail"),
        PAYEE_ADDRESS_LINE1("payeeAddressLine1"),
        PAYEE_ADDRESS_LINE2("payeeAddressLine2"),
        PAYEE_CITY("payeeCity"),
        PAYEE_STATE_PROVINCE("payeeStateProvince"),
        PAYEE_COUNTRY("payeeCountry"),
        PAYEE_POSTAL_CODE("payeePostalCode"),
        PAYMENT_EXPIRY_DATE("paymentExpiryDate"),
        SECURITY_QUESTION("securityQuestion"),
        SECURITY_ANSWER("securityAnswer");

        private String key;
        DetailFieldKey(String key) {
            this.key = key;
        }

        public String key() {
            return key;
        }

    }

    private String journalId;

    private Type type;

    private Date createdOn;

    private Entry entry;

    private String sourceToken;

    private String destinationToken;

    private Double amount;

    private Double fee;

    private String currency;

    private Double foreignExchangeRate;

    private String foreignExchangeCurrency;

    private Map<String, String> details = new HashMap<String, String>();

    public String getJournalId() {
        return journalId;
    }

    public void setJournalId(String journalId) {
        this.journalId = journalId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public String getSourceToken() {
        return sourceToken;
    }

    public void setSourceToken(String sourceToken) {
        this.sourceToken = sourceToken;
    }

    public String getDestinationToken() {
        return destinationToken;
    }

    public void setDestinationToken(String destinationToken) {
        this.destinationToken = destinationToken;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getForeignExchangeRate() {
        return foreignExchangeRate;
    }

    public void setForeignExchangeRate(Double foreignExchangeRate) {
        this.foreignExchangeRate = foreignExchangeRate;
    }

    public String getForeignExchangeCurrency() {
        return foreignExchangeCurrency;
    }

    public void setForeignExchangeCurrency(String foreignExchangeCurrency) {
        this.foreignExchangeCurrency = foreignExchangeCurrency;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}
