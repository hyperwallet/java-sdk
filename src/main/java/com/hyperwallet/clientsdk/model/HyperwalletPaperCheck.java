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
public class HyperwalletPaperCheck extends HyperwalletBaseMonitor {

    public enum ShippingMethod {STANDARD,EXPEDITED}
    public enum GovernmentIdType {PASSPORT,NATIONAL_ID_CARD}

    private HyperwalletTransferMethod.Type type;
    private String token;
    private HyperwalletTransferMethod.Status status;
    private Date createdOn;

    private String transferMethodCountry;
    private String transferMethodCurrency;
    private Boolean isDefaultTransferMethod;
    private String addressLine1;
    private String addressLine2;
    private HyperwalletTransferMethod.BankAccountRelationship bankAccountRelationship;
    private HyperwalletUser.BusinessContactRole businessContactRole;
    private String businessName;
    private String businessRegistrationCountry;
    private String businessRegistrationId;
    private String businessRegistrationStateProvince;
    private HyperwalletUser.BusinessType businessType;
    private String city;
    private String country;
    private String countryOfBirth;
    private String countryOfNationality;
    private Date dateOfBirth;
    private String driversLicenseId;
    private String employerId;
    private String firstName;
    private HyperwalletUser.Gender gender;
    private String governmentId;
    private GovernmentIdType governmentIdType;
    private String lastName;
    private String middleName;
    private String mobileNumber;
    private String passportId;
    private String phoneNumber;
    private String postalCode;
    private HyperwalletUser.ProfileType profileType;
    private ShippingMethod shippingMethod;
    private String stateProvince;

    private String userToken;

    public HyperwalletTransferMethod.Type getType() {
        return type;
    }

    public void setType(HyperwalletTransferMethod.Type type) {
        addField("type", type);
        this.type = type;
    }

    public HyperwalletPaperCheck type(HyperwalletTransferMethod.Type type) {
        addField("type", type);
        this.type = type;
        return this;
    }

    public HyperwalletPaperCheck clearType() {
        clearField("type");
        this.type = null;
        return this;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        addField("token", token);
        this.token = token;
    }

    public HyperwalletPaperCheck token(String token) {
        addField("token", token);
        this.token = token;
        return this;
    }

    public HyperwalletPaperCheck clearToken() {
        clearField("token");
        this.token = null;
        return this;
    }

    public HyperwalletTransferMethod.Status getStatus() {
        return status;
    }

    public void setStatus(HyperwalletTransferMethod.Status status) {
        addField("status", status);
        this.status = status;
    }

    public HyperwalletPaperCheck status(HyperwalletTransferMethod.Status status) {
        addField("status", status);
        this.status = status;
        return this;
    }

    public HyperwalletPaperCheck clearStatus() {
        clearField("status");
        this.status = null;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
    }

    public HyperwalletPaperCheck createdOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
        return this;
    }

    public HyperwalletPaperCheck clearCreatedOn() {
        clearField("createdOn");
        this.createdOn = null;
        return this;
    }

    public String getTransferMethodCountry() {
        return transferMethodCountry;
    }

    public void setTransferMethodCountry(String transferMethodCountry) {
        addField("transferMethodCountry", transferMethodCountry);
        this.transferMethodCountry = transferMethodCountry;
    }

    public HyperwalletPaperCheck transferMethodCountry(String transferMethodCountry) {
        addField("transferMethodCountry", transferMethodCountry);
        this.transferMethodCountry = transferMethodCountry;
        return this;
    }

    public HyperwalletPaperCheck clearTransferMethodCountry() {
        clearField("transferMethodCountry");
        this.transferMethodCountry = null;
        return this;
    }

    public String getTransferMethodCurrency() {
        return transferMethodCurrency;
    }

    public void setTransferMethodCurrency(String transferMethodCurrency) {
        addField("transferMethodCurrency", transferMethodCurrency);
        this.transferMethodCurrency = transferMethodCurrency;
    }

    public HyperwalletPaperCheck transferMethodCurrency(String transferMethodCurrency) {
        addField("transferMethodCurrency", transferMethodCurrency);
        this.transferMethodCurrency = transferMethodCurrency;
        return this;
    }

    public HyperwalletPaperCheck clearTransferMethodCurrency() {
        clearField("transferMethodCurrency");
        this.transferMethodCurrency = null;
        return this;
    }

    public Boolean getIsDefaultTransferMethod() {
        return isDefaultTransferMethod;
    }

    public void setIsDefaultTransferMethod(Boolean isDefaultTransferMethod) {
        addField("isDefaultTransferMethod", isDefaultTransferMethod);
        this.isDefaultTransferMethod = isDefaultTransferMethod;
    }

    public HyperwalletPaperCheck isDefaultTransferMethod(Boolean isDefaultTransferMethod) {
        addField("isDefaultTransferMethod", isDefaultTransferMethod);
        this.isDefaultTransferMethod = isDefaultTransferMethod;
        return this;
    }

    public HyperwalletPaperCheck clearIsDefaultTransferMethod() {
        clearField("isDefaultTransferMethod");
        this.isDefaultTransferMethod = null;
        return this;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        addField("addressLine1", addressLine1);
        this.addressLine1 = addressLine1;
    }

    public HyperwalletPaperCheck addressLine1(String addressLine1) {
        addField("addressLine1", addressLine1);
        this.addressLine1 = addressLine1;
        return this;
    }

    public HyperwalletPaperCheck clearAddressLine1() {
        clearField("addressLine1");
        this.addressLine1 = null;
        return this;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        addField("addressLine2", addressLine2);
        this.addressLine2 = addressLine2;
    }

    public HyperwalletPaperCheck addressLine2(String addressLine2) {
        addField("addressLine2", addressLine2);
        this.addressLine2 = addressLine2;
        return this;
    }

    public HyperwalletPaperCheck clearAddressLine2() {
        clearField("addressLine2");
        this.addressLine2 = null;
        return this;
    }

    public HyperwalletTransferMethod.BankAccountRelationship getBankAccountRelationship() {
        return bankAccountRelationship;
    }

    public void setBankAccountRelationship(HyperwalletTransferMethod.BankAccountRelationship bankAccountRelationship) {
        addField("bankAccountRelationship", bankAccountRelationship);
        this.bankAccountRelationship = bankAccountRelationship;
    }

    public HyperwalletPaperCheck bankAccountRelationship(HyperwalletTransferMethod.BankAccountRelationship bankAccountRelationship) {
        addField("bankAccountRelationship", bankAccountRelationship);
        this.bankAccountRelationship = bankAccountRelationship;
        return this;
    }

    public HyperwalletPaperCheck clearBankAccountRelationship() {
        clearField("bankAccountRelationship");
        this.bankAccountRelationship = null;
        return this;
    }

    public HyperwalletUser.BusinessContactRole getBusinessContactRole() {
        return businessContactRole;
    }

    public void setBusinessContactRole(HyperwalletUser.BusinessContactRole businessContactRole) {
        addField("businessContactRole", businessContactRole);
        this.businessContactRole = businessContactRole;
    }

    public HyperwalletPaperCheck businessContactRole(HyperwalletUser.BusinessContactRole businessContactRole) {
        addField("businessContactRole", businessContactRole);
        this.businessContactRole = businessContactRole;
        return this;
    }

    public HyperwalletPaperCheck clearBusinessContactRole() {
        clearField("businessContactRole");
        this.businessContactRole = null;
        return this;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        addField("businessName", businessName);
        this.businessName = businessName;
    }

    public HyperwalletPaperCheck businessName(String businessName) {
        addField("businessName", businessName);
        this.businessName = businessName;
        return this;
    }

    public HyperwalletPaperCheck clearBusinessName() {
        clearField("businessName");
        this.businessName = null;
        return this;
    }

    public String getBusinessRegistrationCountry() {
        return businessRegistrationCountry;
    }

    public void setBusinessRegistrationCountry(String businessRegistrationCountry) {
        addField("businessRegistrationCountry", businessRegistrationCountry);
        this.businessRegistrationCountry = businessRegistrationCountry;
    }

    public HyperwalletPaperCheck businessRegistrationCountry(String businessRegistrationCountry) {
        addField("businessRegistrationCountry", businessRegistrationCountry);
        this.businessRegistrationCountry = businessRegistrationCountry;
        return this;
    }

    public HyperwalletPaperCheck clearBusinessRegistrationCountry() {
        clearField("businessRegistrationCountry");
        this.businessRegistrationCountry = null;
        return this;
    }

    public String getBusinessRegistrationId() {
        return businessRegistrationId;
    }

    public void setBusinessRegistrationId(String businessRegistrationId) {
        addField("businessRegistrationId", businessRegistrationId);
        this.businessRegistrationId = businessRegistrationId;
    }

    public HyperwalletPaperCheck businessRegistrationId(String businessRegistrationId) {
        addField("businessRegistrationId", businessRegistrationId);
        this.businessRegistrationId = businessRegistrationId;
        return this;
    }

    public HyperwalletPaperCheck clearBusinessRegistrationId() {
        clearField("businessRegistrationId");
        this.businessRegistrationId = null;
        return this;
    }

    public String getBusinessRegistrationStateProvince() {
        return businessRegistrationStateProvince;
    }

    public void setBusinessRegistrationStateProvince(String businessRegistrationStateProvince) {
        addField("businessRegistrationStateProvince", businessRegistrationStateProvince);
        this.businessRegistrationStateProvince = businessRegistrationStateProvince;
    }

    public HyperwalletPaperCheck businessRegistrationStateProvince(String businessRegistrationStateProvince) {
        addField("businessRegistrationStateProvince", businessRegistrationStateProvince);
        this.businessRegistrationStateProvince = businessRegistrationStateProvince;
        return this;
    }

    public HyperwalletPaperCheck clearBusinessRegistrationStateProvince() {
        clearField("businessRegistrationStateProvince");
        this.businessRegistrationStateProvince = null;
        return this;
    }

    public HyperwalletUser.BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(HyperwalletUser.BusinessType businessType) {
        addField("businessType", businessType);
        this.businessType = businessType;
    }

    public HyperwalletPaperCheck businessType(HyperwalletUser.BusinessType businessType) {
        addField("businessType", businessType);
        this.businessType = businessType;
        return this;
    }

    public HyperwalletPaperCheck clearBusinessType() {
        clearField("businessType");
        this.businessType = null;
        return this;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        addField("city", city);
        this.city = city;
    }

    public HyperwalletPaperCheck city(String city) {
        addField("city", city);
        this.city = city;
        return this;
    }

    public HyperwalletPaperCheck clearCity() {
        clearField("city");
        this.city = null;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        addField("country", country);
        this.country = country;
    }

    public HyperwalletPaperCheck country(String country) {
        addField("country", country);
        this.country = country;
        return this;
    }

    public HyperwalletPaperCheck clearCountry() {
        clearField("country");
        this.country = null;
        return this;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        addField("countryOfBirth", countryOfBirth);
        this.countryOfBirth = countryOfBirth;
    }

    public HyperwalletPaperCheck countryOfBirth(String countryOfBirth) {
        addField("countryOfBirth", countryOfBirth);
        this.countryOfBirth = countryOfBirth;
        return this;
    }

    public HyperwalletPaperCheck clearCountryOfBirth() {
        clearField("countryOfBirth");
        this.countryOfBirth = null;
        return this;
    }

    public String getCountryOfNationality() {
        return countryOfNationality;
    }

    public void setCountryOfNationality(String countryOfNationality) {
        addField("countryOfNationality", countryOfNationality);
        this.countryOfNationality = countryOfNationality;
    }

    public HyperwalletPaperCheck countryOfNationality(String countryOfNationality) {
        addField("countryOfNationality", countryOfNationality);
        this.countryOfNationality = countryOfNationality;
        return this;
    }

    public HyperwalletPaperCheck clearCountryOfNationality() {
        clearField("countryOfNationality");
        this.countryOfNationality = null;
        return this;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        addField("dateOfBirth", dateOfBirth);
        this.dateOfBirth = dateOfBirth;
    }

    public HyperwalletPaperCheck dateOfBirth(Date dateOfBirth) {
        addField("dateOfBirth", dateOfBirth);
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public HyperwalletPaperCheck clearDateOfBirth() {
        clearField("dateOfBirth");
        this.dateOfBirth = null;
        return this;
    }

    public String getDriversLicenseId() {
        return driversLicenseId;
    }

    public void setDriversLicenseId(String driversLicenseId) {
        addField("driversLicenseId", driversLicenseId);
        this.driversLicenseId = driversLicenseId;
    }

    public HyperwalletPaperCheck driversLicenseId(String driversLicenseId) {
        addField("driversLicenseId", driversLicenseId);
        this.driversLicenseId = driversLicenseId;
        return this;
    }

    public HyperwalletPaperCheck clearDriversLicenseId() {
        clearField("driversLicenseId");
        this.driversLicenseId = null;
        return this;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        addField("employerId", employerId);
        this.employerId = employerId;
    }

    public HyperwalletPaperCheck employerId(String employerId) {
        addField("employerId", employerId);
        this.employerId = employerId;
        return this;
    }

    public HyperwalletPaperCheck clearEmployerId() {
        clearField("employerId");
        this.employerId = null;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        addField("firstName", firstName);
        this.firstName = firstName;
    }

    public HyperwalletPaperCheck firstName(String firstName) {
        addField("firstName", firstName);
        this.firstName = firstName;
        return this;
    }

    public HyperwalletPaperCheck clearFirstName() {
        clearField("firstName");
        this.firstName = null;
        return this;
    }

    public HyperwalletUser.Gender getGender() {
        return gender;
    }

    public void setGender(HyperwalletUser.Gender gender) {
        addField("gender", gender);
        this.gender = gender;
    }

    public HyperwalletPaperCheck gender(HyperwalletUser.Gender gender) {
        addField("gender", gender);
        this.gender = gender;
        return this;
    }

    public HyperwalletPaperCheck clearGender() {
        clearField("gender");
        this.gender = null;
        return this;
    }

    public String getGovernmentId() {
        return governmentId;
    }

    public void setGovernmentId(String governmentId) {
        addField("governmentId", governmentId);
        this.governmentId = governmentId;
    }

    public HyperwalletPaperCheck governmentId(String governmentId) {
        addField("governmentId", governmentId);
        this.governmentId = governmentId;
        return this;
    }

    public HyperwalletPaperCheck clearGovernmentId() {
        clearField("governmentId");
        this.governmentId = null;
        return this;
    }

    public GovernmentIdType getGovernmentIdType() {
        return governmentIdType;
    }

    public void setGovernmentIdType(GovernmentIdType governmentIdType) {
        addField("governmentIdType", governmentIdType);
        this.governmentIdType = governmentIdType;
    }

    public HyperwalletPaperCheck governmentIdType(GovernmentIdType governmentIdType) {
        addField("governmentIdType", governmentIdType);
        this.governmentIdType = governmentIdType;
        return this;
    }

    public HyperwalletPaperCheck clearGovernmentIdType() {
        clearField("governmentIdType");
        this.governmentIdType = null;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        addField("lastName", lastName);
        this.lastName = lastName;
    }

    public HyperwalletPaperCheck lastName(String lastName) {
        addField("lastName", lastName);
        this.lastName = lastName;
        return this;
    }

    public HyperwalletPaperCheck clearLastName() {
        clearField("lastName");
        this.lastName = null;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        addField("middleName", middleName);
        this.middleName = middleName;
    }

    public HyperwalletPaperCheck middleName(String middleName) {
        addField("middleName", middleName);
        this.middleName = middleName;
        return this;
    }

    public HyperwalletPaperCheck clearMiddleName() {
        clearField("middleName");
        this.middleName = null;
        return this;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        addField("mobileNumber", mobileNumber);
        this.mobileNumber = mobileNumber;
    }

    public HyperwalletPaperCheck mobileNumber(String mobileNumber) {
        addField("mobileNumber", mobileNumber);
        this.mobileNumber = mobileNumber;
        return this;
    }

    public HyperwalletPaperCheck clearMobileNumber() {
        clearField("mobileNumber");
        this.mobileNumber = null;
        return this;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        addField("passportId", passportId);
        this.passportId = passportId;
    }

    public HyperwalletPaperCheck passportId(String passportId) {
        addField("passportId", passportId);
        this.passportId = passportId;
        return this;
    }

    public HyperwalletPaperCheck clearPassportId() {
        clearField("passportId");
        this.passportId = null;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        addField("phoneNumber", phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public HyperwalletPaperCheck phoneNumber(String phoneNumber) {
        addField("phoneNumber", phoneNumber);
        this.phoneNumber = phoneNumber;
        return this;
    }

    public HyperwalletPaperCheck clearPhoneNumber() {
        clearField("phoneNumber");
        this.phoneNumber = null;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        addField("postalCode", postalCode);
        this.postalCode = postalCode;
    }

    public HyperwalletPaperCheck postalCode(String postalCode) {
        addField("postalCode", postalCode);
        this.postalCode = postalCode;
        return this;
    }

    public HyperwalletPaperCheck clearPostalCode() {
        clearField("postalCode");
        this.postalCode = null;
        return this;
    }

    public HyperwalletUser.ProfileType getProfileType() {
        return profileType;
    }

    public void setProfileType(HyperwalletUser.ProfileType profileType) {
        addField("profileType", profileType);
        this.profileType = profileType;
    }

    public HyperwalletPaperCheck profileType(HyperwalletUser.ProfileType profileType) {
        addField("profileType", profileType);
        this.profileType = profileType;
        return this;
    }

    public HyperwalletPaperCheck clearProfileType() {
        clearField("profileType");
        this.profileType = null;
        return this;
    }

    public ShippingMethod getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(ShippingMethod shippingMethod) {
        addField("shippingMethod", shippingMethod);
        this.shippingMethod = shippingMethod;
    }

    public HyperwalletPaperCheck shippingMethod(ShippingMethod shippingMethod) {
        addField("shippingMethod", shippingMethod);
        this.shippingMethod = shippingMethod;
        return this;
    }

    public HyperwalletPaperCheck clearShippingMethod() {
        clearField("shippingMethod");
        this.shippingMethod = null;
        return this;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        addField("stateProvince", stateProvince);
        this.stateProvince = stateProvince;
    }

    public HyperwalletPaperCheck stateProvince(String stateProvince) {
        addField("stateProvince", stateProvince);
        this.stateProvince = stateProvince;
        return this;
    }

    public HyperwalletPaperCheck clearStateProvince() {
        clearField("stateProvince");
        this.stateProvince = null;
        return this;
    }


    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        addField("userToken", userToken);
        this.userToken = userToken;
    }

    public HyperwalletPaperCheck userToken(String userToken) {
        addField("userToken", userToken);
        this.userToken = userToken;
        return this;
    }

    public HyperwalletPaperCheck clearUserToken() {
        clearField("userToken");
        this.userToken = null;
        return this;
    }
}
