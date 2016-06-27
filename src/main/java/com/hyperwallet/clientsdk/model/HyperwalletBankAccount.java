package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.hyperwallet.clientsdk.util.HyperwalletJsonConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletBankAccount extends HyperwalletBaseMonitor {

    public static enum Type {BANK_ACCOUNT, WIRE_ACCOUNT}

    public static enum Status {ACTIVATED, INVALID, DE_ACTIVATED}

    private String token;

    private Type type;
    private Status status;
    private Date createdOn;
    private String transferMethodCountry;
    private String transferMethodCurrency;

    private String bankName;
    private String bankId;
    private String branchName;
    private String branchId;
    private String bankAccountId;
    private HyperwalletTransferMethod.BankAccountRelationship bankAccountRelationship;
    private String bankAccountPurpose;

    private String branchAddressLine1;
    private String branchAddressLine2;
    private String branchCity;
    private String branchStateProvince;
    private String branchCountry;
    private String branchPostalCode;

    private String wireInstructions;
    private String intermediaryBankId;
    private String intermediaryBankName;
    private String intermediaryBankAccountId;
    private String intermediaryBankAddressLine1;
    private String intermediaryBankAddressLine2;
    private String intermediaryBankCity;
    private String intermediaryBankStateProvince;
    private String intermediaryBankCountry;
    private String intermediaryBankPostalCode;

    private String userToken;

    private HyperwalletUser.ProfileType profileType;

    private String businessName;
    private String businessRegistrationId;
    private String businessRegistrationStateProvince;
    private String businessRegistrationCountry;
    private HyperwalletUser.BusinessContactRole businessContactRole;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date dateOfBirth;
    private String countryOfBirth;
    private String countryOfNationality;
    private HyperwalletUser.Gender gender;
    private String phoneNumber;
    private String mobileNumber;
    private String email;
    private String governmentId;
    private String passportId;
    private String driversLicenseId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        addField("token", token);
        this.token = token;
    }

    public HyperwalletBankAccount token(String token) {
        addField("token", token);
        this.token = token;
        return this;
    }

    public HyperwalletBankAccount clearToken() {
        clearField("token");
        token = null;
        return this;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        addField("type", type);
        this.type = type;
    }

    public HyperwalletBankAccount type(Type type) {
        addField("type", type);
        this.type = type;
        return this;
    }

    public HyperwalletBankAccount clearType() {
        clearField("type");
        type = null;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        addField("status", status);
        this.status = status;
    }

    public HyperwalletBankAccount status(Status status) {
        addField("status", status);
        this.status = status;
        return this;
    }

    public HyperwalletBankAccount clearStatus() {
        clearField("status");
        status = null;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
    }

    public HyperwalletBankAccount createdOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
        return this;
    }

    public HyperwalletBankAccount clearCreatedOn() {
        clearField("createdOn");
        createdOn = null;
        return this;
    }

    public String getTransferMethodCountry() {
        return transferMethodCountry;
    }

    public void setTransferMethodCountry(String transferMethodCountry) {
        addField("transferMethodCountry", transferMethodCountry);
        this.transferMethodCountry = transferMethodCountry;
    }

    public HyperwalletBankAccount transferMethodCountry(String transferMethodCountry) {
        addField("transferMethodCountry", transferMethodCountry);
        this.transferMethodCountry = transferMethodCountry;
        return this;
    }

    public HyperwalletBankAccount clearTransferMethodCountry() {
        clearField("transferMethodCountry");
        transferMethodCountry = null;
        return this;
    }

    public String getTransferMethodCurrency() {
        return transferMethodCurrency;
    }

    public void setTransferMethodCurrency(String transferMethodCurrency) {
        addField("transferMethodCurrency", transferMethodCurrency);
        this.transferMethodCurrency = transferMethodCurrency;
    }

    public HyperwalletBankAccount transferMethodCurrency(String transferMethodCurrency) {
        addField("transferMethodCurrency", transferMethodCurrency);
        this.transferMethodCurrency = transferMethodCurrency;
        return this;
    }

    public HyperwalletBankAccount clearTransferMethodCurrency() {
        clearField("transferMethodCurrency");
        transferMethodCurrency = null;
        return this;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        addField("bankName", bankName);
        this.bankName = bankName;
    }

    public HyperwalletBankAccount bankName(String bankName) {
        addField("bankName", bankName);
        this.bankName = bankName;
        return this;
    }

    public HyperwalletBankAccount clearBankName() {
        clearField("bankName");
        bankName = null;
        return this;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        addField("bankId", bankId);
        this.bankId = bankId;
    }

    public HyperwalletBankAccount bankId(String bankId) {
        addField("bankId", bankId);
        this.bankId = bankId;
        return this;
    }

    public HyperwalletBankAccount clearBankId() {
        clearField("bankId");
        bankId = null;
        return this;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        addField("branchName", branchName);
        this.branchName = branchName;
    }

    public HyperwalletBankAccount branchName(String branchName) {
        addField("branchName", branchName);
        this.branchName = branchName;
        return this;
    }

    public HyperwalletBankAccount clearBranchName() {
        clearField("branchName");
        branchName = null;
        return this;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        addField("branchId", branchId);
        this.branchId = branchId;
    }

    public HyperwalletBankAccount branchId(String branchId) {
        addField("branchId", branchId);
        this.branchId = branchId;
        return this;
    }

    public HyperwalletBankAccount clearBranchId() {
        clearField("branchId");
        branchId = null;
        return this;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        addField("bankAccountId", bankAccountId);
        this.bankAccountId = bankAccountId;
    }

    public HyperwalletBankAccount bankAccountId(String bankAccountId) {
        addField("bankAccountId", bankAccountId);
        this.bankAccountId = bankAccountId;
        return this;
    }

    public HyperwalletBankAccount clearBankAccountId() {
        clearField("bankAccountId");
        bankAccountId = null;
        return this;
    }

    public HyperwalletTransferMethod.BankAccountRelationship getBankAccountRelationship() {
        return bankAccountRelationship;
    }

    public void setBankAccountRelationship(HyperwalletTransferMethod.BankAccountRelationship bankAccountRelationship) {
        addField("bankAccountRelationship", bankAccountRelationship);
        this.bankAccountRelationship = bankAccountRelationship;
    }

    public HyperwalletBankAccount bankAccountRelationship(HyperwalletTransferMethod.BankAccountRelationship bankAccountRelationship) {
        addField("bankAccountRelationship", bankAccountRelationship);
        this.bankAccountRelationship = bankAccountRelationship;
        return this;
    }

    public HyperwalletBankAccount clearBankAccountRelationship() {
        clearField("bankAccountRelationship");
        bankAccountRelationship = null;
        return this;
    }

    public String getBankAccountPurpose() {
        return bankAccountPurpose;
    }

    public void setBankAccountPurpose(String bankAccountPurpose) {
        addField("bankAccountPurpose", bankAccountPurpose);
        this.bankAccountPurpose = bankAccountPurpose;
    }

    public HyperwalletBankAccount bankAccountPurpose(String bankAccountPurpose) {
        addField("bankAccountPurpose", bankAccountPurpose);
        this.bankAccountPurpose = bankAccountPurpose;
        return this;
    }

    public HyperwalletBankAccount clearBankAccountPurpose() {
        clearField("bankAccountPurpose");
        bankAccountPurpose = null;
        return this;
    }

    public String getBranchAddressLine1() {
        return branchAddressLine1;
    }

    public void setBranchAddressLine1(String branchAddressLine1) {
        addField("branchAddressLine1", branchAddressLine1);
        this.branchAddressLine1 = branchAddressLine1;
    }

    public HyperwalletBankAccount branchAddressLine1(String branchAddressLine1) {
        addField("branchAddressLine1", branchAddressLine1);
        this.branchAddressLine1 = branchAddressLine1;
        return this;
    }

    public HyperwalletBankAccount clearBranchAddressLine1() {
        clearField("branchAddressLine1");
        branchAddressLine1 = null;
        return this;
    }

    public String getBranchAddressLine2() {
        return branchAddressLine2;
    }

    public void setBranchAddressLine2(String branchAddressLine2) {
        addField("branchAddressLine2", branchAddressLine2);
        this.branchAddressLine2 = branchAddressLine2;
    }

    public HyperwalletBankAccount branchAddressLine2(String branchAddressLine2) {
        addField("branchAddressLine2", branchAddressLine2);
        this.branchAddressLine2 = branchAddressLine2;
        return this;
    }

    public HyperwalletBankAccount clearBranchAddressLine2() {
        clearField("branchAddressLine2");
        branchAddressLine2 = null;
        return this;
    }

    public String getBranchCity() {
        return branchCity;
    }

    public void setBranchCity(String branchCity) {
        addField("branchCity", branchCity);
        this.branchCity = branchCity;
    }

    public HyperwalletBankAccount branchCity(String branchCity) {
        addField("branchCity", branchCity);
        this.branchCity = branchCity;
        return this;
    }

    public HyperwalletBankAccount clearBranchCity() {
        clearField("branchCity");
        branchCity = null;
        return this;
    }

    public String getBranchStateProvince() {
        return branchStateProvince;
    }

    public void setBranchStateProvince(String branchStateProvince) {
        addField("branchStateProvince", branchStateProvince);
        this.branchStateProvince = branchStateProvince;
    }

    public HyperwalletBankAccount branchStateProvince(String branchStateProvince) {
        addField("branchStateProvince", branchStateProvince);
        this.branchStateProvince = branchStateProvince;
        return this;
    }

    public HyperwalletBankAccount clearBranchStateProvince() {
        clearField("branchStateProvince");
        branchStateProvince = null;
        return this;
    }

    public String getBranchCountry() {
        return branchCountry;
    }

    public void setBranchCountry(String branchCountry) {
        addField("branchCountry", branchCountry);
        this.branchCountry = branchCountry;
    }

    public HyperwalletBankAccount branchCountry(String branchCountry) {
        addField("branchCountry", branchCountry);
        this.branchCountry = branchCountry;
        return this;
    }

    public HyperwalletBankAccount clearBranchCountry() {
        clearField("branchCountry");
        branchCountry = null;
        return this;
    }

    public String getBranchPostalCode() {
        return branchPostalCode;
    }

    public void setBranchPostalCode(String branchPostalCode) {
        addField("branchPostalCode", branchPostalCode);
        this.branchPostalCode = branchPostalCode;
    }

    public HyperwalletBankAccount branchPostalCode(String branchPostalCode) {
        addField("branchPostalCode", branchPostalCode);
        this.branchPostalCode = branchPostalCode;
        return this;
    }

    public HyperwalletBankAccount clearBranchPostalCode() {
        clearField("branchPostalCode");
        branchPostalCode = null;
        return this;
    }

    public String getWireInstructions() {
        return wireInstructions;
    }

    public void setWireInstructions(String wireInstructions) {
        addField("wireInstructions", wireInstructions);
        this.wireInstructions = wireInstructions;
    }

    public HyperwalletBankAccount wireInstructions(String wireInstructions) {
        addField("wireInstructions", wireInstructions);
        this.wireInstructions = wireInstructions;
        return this;
    }

    public HyperwalletBankAccount clearWireInstructions() {
        clearField("wireInstructions");
        wireInstructions = null;
        return this;
    }

    public String getIntermediaryBankId() {
        return intermediaryBankId;
    }

    public void setIntermediaryBankId(String intermediaryBankId) {
        addField("intermediaryBankId", intermediaryBankId);
        this.intermediaryBankId = intermediaryBankId;
    }

    public HyperwalletBankAccount intermediaryBankId(String intermediaryBankId) {
        addField("intermediaryBankId", intermediaryBankId);
        this.intermediaryBankId = intermediaryBankId;
        return this;
    }

    public HyperwalletBankAccount clearIntermediaryBankId() {
        clearField("intermediaryBankId");
        intermediaryBankId = null;
        return this;
    }

    public String getIntermediaryBankName() {
        return intermediaryBankName;
    }

    public void setIntermediaryBankName(String intermediaryBankName) {
        addField("intermediaryBankName", intermediaryBankName);
        this.intermediaryBankName = intermediaryBankName;
    }

    public HyperwalletBankAccount intermediaryBankName(String intermediaryBankName) {
        addField("intermediaryBankName", intermediaryBankName);
        this.intermediaryBankName = intermediaryBankName;
        return this;
    }

    public HyperwalletBankAccount clearIntermediaryBankName() {
        clearField("intermediaryBankName");
        intermediaryBankName = null;
        return this;
    }

    public String getIntermediaryBankAccountId() {
        return intermediaryBankAccountId;
    }

    public void setIntermediaryBankAccountId(String intermediaryBankAccountId) {
        addField("intermediaryBankAccountId", intermediaryBankAccountId);
        this.intermediaryBankAccountId = intermediaryBankAccountId;
    }

    public HyperwalletBankAccount intermediaryBankAccountId(String intermediaryBankAccountId) {
        addField("intermediaryBankAccountId", intermediaryBankAccountId);
        this.intermediaryBankAccountId = intermediaryBankAccountId;
        return this;
    }

    public HyperwalletBankAccount clearIntermediaryBankAccountId() {
        clearField("intermediaryBankAccountId");
        intermediaryBankAccountId = null;
        return this;
    }

    public String getIntermediaryBankAddressLine1() {
        return intermediaryBankAddressLine1;
    }

    public void setIntermediaryBankAddressLine1(String intermediaryBankAddressLine1) {
        addField("intermediaryBankAddressLine1", intermediaryBankAddressLine1);
        this.intermediaryBankAddressLine1 = intermediaryBankAddressLine1;
    }

    public HyperwalletBankAccount intermediaryBankAddressLine1(String intermediaryBankAddressLine1) {
        addField("intermediaryBankAddressLine1", intermediaryBankAddressLine1);
        this.intermediaryBankAddressLine1 = intermediaryBankAddressLine1;
        return this;
    }

    public HyperwalletBankAccount clearIntermediaryBankAddressLine1() {
        clearField("intermediaryBankAddressLine1");
        intermediaryBankAddressLine1 = null;
        return this;
    }

    public String getIntermediaryBankAddressLine2() {
        return intermediaryBankAddressLine2;
    }

    public void setIntermediaryBankAddressLine2(String intermediaryBankAddressLine2) {
        addField("intermediaryBankAddressLine2", intermediaryBankAddressLine2);
        this.intermediaryBankAddressLine2 = intermediaryBankAddressLine2;
    }

    public HyperwalletBankAccount intermediaryBankAddressLine2(String intermediaryBankAddressLine2) {
        addField("intermediaryBankAddressLine2", intermediaryBankAddressLine2);
        this.intermediaryBankAddressLine2 = intermediaryBankAddressLine2;
        return this;
    }

    public HyperwalletBankAccount clearIntermediaryBankAddressLine2() {
        clearField("intermediaryBankAddressLine2");
        intermediaryBankAddressLine2 = null;
        return this;
    }

    public String getIntermediaryBankCity() {
        return intermediaryBankCity;
    }

    public void setIntermediaryBankCity(String intermediaryBankCity) {
        addField("intermediaryBankCity", intermediaryBankCity);
        this.intermediaryBankCity = intermediaryBankCity;
    }

    public HyperwalletBankAccount intermediaryBankCity(String intermediaryBankCity) {
        addField("intermediaryBankCity", intermediaryBankCity);
        this.intermediaryBankCity = intermediaryBankCity;
        return this;
    }

    public HyperwalletBankAccount clearIntermediaryBankCity() {
        clearField("intermediaryBankCity");
        intermediaryBankCity = null;
        return this;
    }

    public String getIntermediaryBankStateProvince() {
        return intermediaryBankStateProvince;
    }

    public void setIntermediaryBankStateProvince(String intermediaryBankStateProvince) {
        addField("intermediaryBankStateProvince", intermediaryBankStateProvince);
        this.intermediaryBankStateProvince = intermediaryBankStateProvince;
    }

    public HyperwalletBankAccount intermediaryBankStateProvince(String intermediaryBankStateProvince) {
        addField("intermediaryBankStateProvince", intermediaryBankStateProvince);
        this.intermediaryBankStateProvince = intermediaryBankStateProvince;
        return this;
    }

    public HyperwalletBankAccount clearIntermediaryBankStateProvince() {
        clearField("intermediaryBankStateProvince");
        intermediaryBankStateProvince = null;
        return this;
    }

    public String getIntermediaryBankCountry() {
        return intermediaryBankCountry;
    }

    public void setIntermediaryBankCountry(String intermediaryBankCountry) {
        addField("intermediaryBankCountry", intermediaryBankCountry);
        this.intermediaryBankCountry = intermediaryBankCountry;
    }

    public HyperwalletBankAccount intermediaryBankCountry(String intermediaryBankCountry) {
        addField("intermediaryBankCountry", intermediaryBankCountry);
        this.intermediaryBankCountry = intermediaryBankCountry;
        return this;
    }

    public HyperwalletBankAccount clearIntermediaryBankCountry() {
        clearField("intermediaryBankCountry");
        intermediaryBankCountry = null;
        return this;
    }

    public String getIntermediaryBankPostalCode() {
        return intermediaryBankPostalCode;
    }

    public void setIntermediaryBankPostalCode(String intermediaryBankPostalCode) {
        addField("intermediaryBankPostalCode", intermediaryBankPostalCode);
        this.intermediaryBankPostalCode = intermediaryBankPostalCode;
    }

    public HyperwalletBankAccount intermediaryBankPostalCode(String intermediaryBankPostalCode) {
        addField("intermediaryBankPostalCode", intermediaryBankPostalCode);
        this.intermediaryBankPostalCode = intermediaryBankPostalCode;
        return this;
    }

    public HyperwalletBankAccount clearIntermediaryBankPostalCode() {
        clearField("intermediaryBankPostalCode");
        intermediaryBankPostalCode = null;
        return this;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        addField("userToken", userToken);
        this.userToken = userToken;
    }

    public HyperwalletBankAccount userToken(String userToken) {
        addField("userToken", userToken);
        this.userToken = userToken;
        return this;
    }

    public HyperwalletBankAccount clearUserToken() {
        clearField("userToken");
        userToken = null;
        return this;
    }

    public HyperwalletUser.ProfileType getProfileType() {
        return profileType;
    }

    public void setProfileType(HyperwalletUser.ProfileType profileType) {
        addField("profileType", profileType);
        this.profileType = profileType;
    }

    public HyperwalletBankAccount profileType(HyperwalletUser.ProfileType profileType) {
        addField("profileType", profileType);
        this.profileType = profileType;
        return this;
    }

    public HyperwalletBankAccount clearProfileType() {
        clearField("profileType");
        profileType = null;
        return this;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        addField("businessName", businessName);
        this.businessName = businessName;
    }

    public HyperwalletBankAccount businessName(String businessName) {
        addField("businessName", businessName);
        this.businessName = businessName;
        return this;
    }

    public HyperwalletBankAccount clearBusinessName() {
        clearField("businessName");
        businessName = null;
        return this;
    }

    public String getBusinessRegistrationId() {
        return businessRegistrationId;
    }

    public void setBusinessRegistrationId(String businessRegistrationId) {
        addField("businessRegistrationId", businessRegistrationId);
        this.businessRegistrationId = businessRegistrationId;
    }

    public HyperwalletBankAccount businessRegistrationId(String businessRegistrationId) {
        addField("businessRegistrationId", businessRegistrationId);
        this.businessRegistrationId = businessRegistrationId;
        return this;
    }

    public HyperwalletBankAccount clearBusinessRegistrationId() {
        clearField("businessRegistrationId");
        businessRegistrationId = null;
        return this;
    }

    public String getBusinessRegistrationStateProvince() {
        return businessRegistrationStateProvince;
    }

    public void setBusinessRegistrationStateProvince(String businessRegistrationStateProvince) {
        addField("businessRegistrationStateProvince", businessRegistrationStateProvince);
        this.businessRegistrationStateProvince = businessRegistrationStateProvince;
    }

    public HyperwalletBankAccount businessRegistrationStateProvince(String businessRegistrationStateProvince) {
        addField("businessRegistrationStateProvince", businessRegistrationStateProvince);
        this.businessRegistrationStateProvince = businessRegistrationStateProvince;
        return this;
    }

    public HyperwalletBankAccount clearBusinessRegistrationStateProvince() {
        clearField("businessRegistrationStateProvince");
        businessRegistrationStateProvince = null;
        return this;
    }

    public String getBusinessRegistrationCountry() {
        return businessRegistrationCountry;
    }

    public void setBusinessRegistrationCountry(String businessRegistrationCountry) {
        addField("businessRegistrationCountry", businessRegistrationCountry);
        this.businessRegistrationCountry = businessRegistrationCountry;
    }

    public HyperwalletBankAccount businessRegistrationCountry(String businessRegistrationCountry) {
        addField("businessRegistrationCountry", businessRegistrationCountry);
        this.businessRegistrationCountry = businessRegistrationCountry;
        return this;
    }

    public HyperwalletBankAccount clearBusinessRegistrationCountry() {
        clearField("businessRegistrationCountry");
        businessRegistrationCountry = null;
        return this;
    }

    public HyperwalletUser.BusinessContactRole getBusinessContactRole() {
        return businessContactRole;
    }

    public void setBusinessContactRole(HyperwalletUser.BusinessContactRole businessContactRole) {
        addField("businessContactRole", businessContactRole);
        this.businessContactRole = businessContactRole;
    }

    public HyperwalletBankAccount businessContactRole(HyperwalletUser.BusinessContactRole businessContactRole) {
        addField("businessContactRole", businessContactRole);
        this.businessContactRole = businessContactRole;
        return this;
    }

    public HyperwalletBankAccount clearBusinessContactRole() {
        clearField("businessContactRole");
        businessContactRole = null;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        addField("firstName", firstName);
        this.firstName = firstName;
    }

    public HyperwalletBankAccount firstName(String firstName) {
        addField("firstName", firstName);
        this.firstName = firstName;
        return this;
    }

    public HyperwalletBankAccount clearFirstName() {
        clearField("firstName");
        firstName = null;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        addField("middleName", middleName);
        this.middleName = middleName;
    }

    public HyperwalletBankAccount middleName(String middleName) {
        addField("middleName", middleName);
        this.middleName = middleName;
        return this;
    }

    public HyperwalletBankAccount clearMiddleName() {
        clearField("middleName");
        middleName = null;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        addField("lastName", lastName);
        this.lastName = lastName;
    }

    public HyperwalletBankAccount lastName(String lastName) {
        addField("lastName", lastName);
        this.lastName = lastName;
        return this;
    }

    public HyperwalletBankAccount clearLastName() {
        clearField("lastName");
        lastName = null;
        return this;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        addField("dateOfBirth", dateOfBirth);
        this.dateOfBirth = dateOfBirth;
    }

    public HyperwalletBankAccount dateOfBirth(Date dateOfBirth) {
        addField("dateOfBirth", dateOfBirth);
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public HyperwalletBankAccount clearDateOfBirth() {
        clearField("dateOfBirth");
        dateOfBirth = null;
        return this;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        addField("countryOfBirth", countryOfBirth);
        this.countryOfBirth = countryOfBirth;
    }

    public HyperwalletBankAccount countryOfBirth(String countryOfBirth) {
        addField("countryOfBirth", countryOfBirth);
        this.countryOfBirth = countryOfBirth;
        return this;
    }

    public HyperwalletBankAccount clearCountryOfBirth() {
        clearField("countryOfBirth");
        countryOfBirth = null;
        return this;
    }

    public String getCountryOfNationality() {
        return countryOfNationality;
    }

    public void setCountryOfNationality(String countryOfNationality) {
        addField("countryOfNationality", countryOfNationality);
        this.countryOfNationality = countryOfNationality;
    }

    public HyperwalletBankAccount countryOfNationality(String countryOfNationality) {
        addField("countryOfNationality", countryOfNationality);
        this.countryOfNationality = countryOfNationality;
        return this;
    }

    public HyperwalletBankAccount clearCountryOfNationality() {
        clearField("countryOfNationality");
        countryOfNationality = null;
        return this;
    }

    public HyperwalletUser.Gender getGender() {
        return gender;
    }

    public void setGender(HyperwalletUser.Gender gender) {
        addField("gender", gender);
        this.gender = gender;
    }

    public HyperwalletBankAccount gender(HyperwalletUser.Gender gender) {
        addField("gender", gender);
        this.gender = gender;
        return this;
    }

    public HyperwalletBankAccount clearGender() {
        clearField("gender");
        gender = null;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        addField("phoneNumber", phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public HyperwalletBankAccount phoneNumber(String phoneNumber) {
        addField("phoneNumber", phoneNumber);
        this.phoneNumber = phoneNumber;
        return this;
    }

    public HyperwalletBankAccount clearPhoneNumber() {
        clearField("phoneNumber");
        phoneNumber = null;
        return this;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        addField("mobileNumber", mobileNumber);
        this.mobileNumber = mobileNumber;
    }

    public HyperwalletBankAccount mobileNumber(String mobileNumber) {
        addField("mobileNumber", mobileNumber);
        this.mobileNumber = mobileNumber;
        return this;
    }

    public HyperwalletBankAccount clearMobileNumber() {
        clearField("mobileNumber");
        mobileNumber = null;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        addField("email", email);
        this.email = email;
    }

    public HyperwalletBankAccount email(String email) {
        addField("email", email);
        this.email = email;
        return this;
    }

    public HyperwalletBankAccount clearEmail() {
        clearField("email");
        email = null;
        return this;
    }

    public String getGovernmentId() {
        return governmentId;
    }

    public void setGovernmentId(String governmentId) {
        addField("governmentId", governmentId);
        this.governmentId = governmentId;
    }

    public HyperwalletBankAccount governmentId(String governmentId) {
        addField("governmentId", governmentId);
        this.governmentId = governmentId;
        return this;
    }

    public HyperwalletBankAccount clearGovernmentId() {
        clearField("governmentId");
        governmentId = null;
        return this;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        addField("passportId", passportId);
        this.passportId = passportId;
    }

    public HyperwalletBankAccount passportId(String passportId) {
        addField("passportId", passportId);
        this.passportId = passportId;
        return this;
    }

    public HyperwalletBankAccount clearPassportId() {
        clearField("passportId");
        passportId = null;
        return this;
    }

    public String getDriversLicenseId() {
        return driversLicenseId;
    }

    public void setDriversLicenseId(String driversLicenseId) {
        addField("driversLicenseId", driversLicenseId);
        this.driversLicenseId = driversLicenseId;
    }

    public HyperwalletBankAccount driversLicenseId(String driversLicenseId) {
        addField("driversLicenseId", driversLicenseId);
        this.driversLicenseId = driversLicenseId;
        return this;
    }

    public HyperwalletBankAccount clearDriversLicenseId() {
        clearField("driversLicenseId");
        driversLicenseId = null;
        return this;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        addField("addressLine1", addressLine1);
        this.addressLine1 = addressLine1;
    }

    public HyperwalletBankAccount addressLine1(String addressLine1) {
        addField("addressLine1", addressLine1);
        this.addressLine1 = addressLine1;
        return this;
    }

    public HyperwalletBankAccount clearAddressLine1() {
        clearField("addressLine1");
        addressLine1 = null;
        return this;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        addField("addressLine2", addressLine2);
        this.addressLine2 = addressLine2;
    }

    public HyperwalletBankAccount addressLine2(String addressLine2) {
        addField("addressLine2", addressLine2);
        this.addressLine2 = addressLine2;
        return this;
    }

    public HyperwalletBankAccount clearAddressLine2() {
        clearField("addressLine2");
        addressLine2 = null;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        addField("city", city);
        this.city = city;
    }

    public HyperwalletBankAccount city(String city) {
        addField("city", city);
        this.city = city;
        return this;
    }

    public HyperwalletBankAccount clearCity() {
        clearField("city");
        city = null;
        return this;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        addField("stateProvince", stateProvince);
        this.stateProvince = stateProvince;
    }

    public HyperwalletBankAccount stateProvince(String stateProvince) {
        addField("stateProvince", stateProvince);
        this.stateProvince = stateProvince;
        return this;
    }

    public HyperwalletBankAccount clearStateProvince() {
        clearField("stateProvince");
        stateProvince = null;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        addField("postalCode", postalCode);
        this.postalCode = postalCode;
    }

    public HyperwalletBankAccount postalCode(String postalCode) {
        addField("postalCode", postalCode);
        this.postalCode = postalCode;
        return this;
    }

    public HyperwalletBankAccount clearPostalCode() {
        clearField("postalCode");
        postalCode = null;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        addField("country", country);
        this.country = country;
    }

    public HyperwalletBankAccount country(String country) {
        addField("country", country);
        this.country = country;
        return this;
    }

    public HyperwalletBankAccount clearCountry() {
        clearField("country");
        country = null;
        return this;
    }
}
