package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.hyperwallet.clientsdk.HyperwalletJsonConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletUser extends HyperwalletBaseModel {

    public static enum Gender {MALE, FEMALE}

    public static enum ProfileType {INDIVIDUAL, BUSINESS}

    public static enum BusinessType {CORPORATION, PARTNERSHIP}

    public static enum BusinessContactRole {DIRECTOR, OWNER, OTHER}

    public static enum Status {PRE_ACTIVATED, ACTIVATED, LOCKED, FROZEN, DE_ACTIVATED}

    private String token;
    private Status status;
    private Date createdOn;
    private String clientUserId;
    private ProfileType profileType;

    private BusinessType businessType;
    private String businessName;
    private String businessRegistrationId;
    private String businessRegistrationStateProvince;
    private String businessRegistrationCountry;
    private BusinessContactRole businessContactRole;

    private String firstName;
    private String middleName;
    private String lastName;
    private Date dateOfBirth;
    private String countryOfBirth;
    private String countryOfNationality;
    private Gender gender;
    private String phoneNumber;
    private String mobileNumber;
    private String email;
    private String governmentId;
    private String passportId;
    private String driversLicenseId;
    private String employerId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String language;
    private String programToken;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        addField("token", token);
        this.token = token;
    }

    public HyperwalletUser token(String token) {
        addField("token", token);
        this.token = token;
        return this;
    }

    public void clearToken() {
        clearField("token");
        token = null;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        addField("status", status);
        this.status = status;
    }

    public HyperwalletUser status(Status status) {
        addField("status", status);
        this.status = status;
        return this;
    }

    public void clearStatus() {
        clearField("status");
        status = null;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
    }

    public HyperwalletUser createdOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
        return this;
    }

    public void clearCreatedOn() {
        clearField("createdOn");
        createdOn = null;
    }

    public String getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(String clientUserId) {
        addField("clientUserId", clientUserId);
        this.clientUserId = clientUserId;
    }

    public HyperwalletUser clientUserId(String clientUserId) {
        addField("clientUserId", clientUserId);
        this.clientUserId = clientUserId;
        return this;
    }

    public void clearClientUserId() {
        clearField("clientUserId");
        clientUserId = null;
    }

    public ProfileType getProfileType() {
        return profileType;
    }

    public void setProfileType(ProfileType profileType) {
        addField("profileType", profileType);
        this.profileType = profileType;
    }

    public HyperwalletUser profileType(ProfileType profileType) {
        this.profileType = profileType;
        return this;
    }

    public void clearProfileType() {
        clearField("profileType");
        profileType = null;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        addField("businessType", businessType);
        this.businessType = businessType;
    }

    public HyperwalletUser businessType(BusinessType businessType) {
        addField("businessType", businessType);
        this.businessType = businessType;
        return this;
    }

    public void clearBusinessType() {
        clearField("businessType");
        businessType = null;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        addField("businessName", businessName);
        this.businessName = businessName;
    }

    public HyperwalletUser businessName(String businessName) {
        addField("businessName", businessName);
        this.businessName = businessName;
        return this;
    }

    public void clearBusinessName() {
        clearField("businessName");
        businessName = null;
    }

    public String getBusinessRegistrationId() {
        return businessRegistrationId;
    }

    public void setBusinessRegistrationId(String businessRegistrationId) {
        addField("businessRegistrationId", businessRegistrationId);
        this.businessRegistrationId = businessRegistrationId;
    }

    public HyperwalletUser businessRegistrationId(String businessRegistrationId) {
        addField("businessRegistrationId", businessRegistrationId);
        this.businessRegistrationId = businessRegistrationId;
        return this;
    }

    public void clearBusinessRegistration() {
        clearField("businessRegistrationId");
        businessRegistrationId = null;
    }

    public String getBusinessRegistrationStateProvince() {
        return businessRegistrationStateProvince;
    }

    public void setBusinessRegistrationStateProvince(String businessRegistrationStateProvince) {
        addField("businessRegistrationStateProvince", businessRegistrationStateProvince);
        this.businessRegistrationStateProvince = businessRegistrationStateProvince;
    }

    public HyperwalletUser businessRegistrationStateProvince(String businessRegistrationStateProvince) {
        addField("businessRegistrationStateProvince", businessRegistrationStateProvince);
        this.businessRegistrationStateProvince = businessRegistrationStateProvince;
        return this;
    }

    public void clearRegistrationStateProvince() {
        clearField("businessRegistrationStateProvince");
        businessRegistrationStateProvince = null;
    }

    public String getBusinessRegistrationCountry() {
        return businessRegistrationCountry;
    }

    public void setBusinessRegistrationCountry(String businessRegistrationCountry) {
        addField("businessRegistrationCountry", businessRegistrationCountry);
        this.businessRegistrationCountry = businessRegistrationCountry;
    }

    public HyperwalletUser businessRegistrationCountry(String businessRegistrationCountry) {
        this.businessRegistrationCountry = businessRegistrationCountry;
        return this;
    }

    public void clearBusinessRegistrationCountry() {
        clearField("businessRegistrationCountry");
        businessRegistrationCountry = null;
    }

    public BusinessContactRole getBusinessContactRole() {
        return businessContactRole;
    }

    public void setBusinessContactRole(BusinessContactRole businessContactRole) {
        addField("businessContactRole", businessContactRole);
        this.businessContactRole = businessContactRole;
    }

    public HyperwalletUser businessContactRole(BusinessContactRole businessContactRole) {
        addField("businessContactRole", businessContactRole);
        this.businessContactRole = businessContactRole;
        return this;
    }

    public void clearBusinessContractRole() {
        clearField("businessContactRole");
        businessContactRole = null;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        addField("firstName" , firstName);
        this.firstName = firstName;
    }

    public HyperwalletUser firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void clearFirstName() {
        clearField("firstName");
        firstName = null;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        addField("middleName", middleName);
        this.middleName = middleName;
    }

    public HyperwalletUser middleName(String middleName) {
        addField("middleName", middleName);
        this.middleName = middleName;
        return this;
    }

    public void clearMiddleName() {
        clearField("middleName");
        middleName = null;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        addField("lastName", lastName);
        this.lastName = lastName;
    }

    public HyperwalletUser lastName(String lastName) {
        addField("lastName", lastName);
        this.lastName = lastName;
        return this;
    }

    public void clearLastName() {
        clearField("lastName");
        lastName = null;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        addField("dateOfBirth", dateOfBirth);
        this.dateOfBirth = dateOfBirth;
    }

    public HyperwalletUser dateOfBirth(Date dateOfBirth) {
        addField("dateOfBirth", dateOfBirth);
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void clearDateOfBirth() {
        clearField("dateOfBirth");
        dateOfBirth = null;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        addField("countryOfBirth", countryOfBirth);
        this.countryOfBirth = countryOfBirth;
    }

    public HyperwalletUser countryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
        return this;
    }

    public void clearCountryOfBirth() {
        clearField("countryOfBirth");
        countryOfBirth = null;
    }

    public String getCountryOfNationality() {
        return countryOfNationality;
    }

    public void setCountryOfNationality(String countryOfNationality) {
        addField("countryOfNationality", countryOfNationality);
        this.countryOfNationality = countryOfNationality;
    }

    public HyperwalletUser countryOfNationality(String countryOfNationality) {
        addField("countryOfNationality", countryOfNationality);
        this.countryOfNationality = countryOfNationality;
        return this;
    }

    public void clearCountryOfNationality() {
        clearField("countryOfNationality");
        countryOfNationality = null;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        addField("gender", gender);
        this.gender = gender;
    }

    public HyperwalletUser gender(Gender gender) {
        addField("gender", gender);
        this.gender = gender;
        return this;
    }

    public void clearGender() {
        clearField("gender");
        gender = null;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        addField("phoneNumber", phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public HyperwalletUser phoneNumber(String phoneNumber) {
        addField("phoneNumber", phoneNumber);
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void clearPhoneNumber() {
        clearField("phoneNumber");
        phoneNumber = null;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        addField("mobileNumber", mobileNumber);
        this.mobileNumber = mobileNumber;
    }

    public HyperwalletUser mobileNumber(String mobileNumber) {
        addField("mobileNumber", mobileNumber);
        this.mobileNumber = mobileNumber;
        return this;
    }

    public void clearMobileNumber() {
        clearField("mobileNumber");
        mobileNumber = null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        addField("email", email);
        this.email = email;
    }

    public HyperwalletUser email(String email) {
        this.email = email;
        return this;
    }

    public void clearEmail() {
        clearField("email");
        email = null;
    }

    public String getGovernmentId() {
        return governmentId;
    }

    public void setGovernmentId(String governmentId) {
        addField("governmentId", governmentId);
        this.governmentId = governmentId;
    }

    public HyperwalletUser governmentId(String governmentId) {
        this.governmentId = governmentId;
        return this;
    }

    public void clearGovernmentId() {
        clearField("governmentId");
        governmentId = null;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        addField("passportId", passportId);
        this.passportId = passportId;
    }

    public HyperwalletUser passportId(String passportId) {
        addField("passportId", passportId);
        this.passportId = passportId;
        return this;
    }

    public void clearPassportId() {
        clearField("passportId");
        passportId = null;
    }

    public String getDriversLicenseId() {
        return driversLicenseId;
    }

    public void setDriversLicenseId(String driversLicenseId) {
        addField("driversLicenseId", driversLicenseId);
        this.driversLicenseId = driversLicenseId;
    }

    public HyperwalletUser driversLicenseId(String driversLicenseId) {
        addField("driversLicenseId", driversLicenseId);
        this.driversLicenseId = driversLicenseId;
        return this;
    }

    public void clearDriversLicenseId() {
        clearField("driversLicenseId");
        driversLicenseId = null;
    }

    public String getEmployerId() {
        return employerId;
    }

    public void setEmployerId(String employerId) {
        addField("employerId", employerId);
        this.employerId = employerId;
    }

    public HyperwalletUser employerId(String employerId) {
        addField("employerId", employerId);
        this.employerId = employerId;
        return this;
    }

    public void clearEmployerId() {
        clearField("employerId");
        employerId = null;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        addField("addressLine1", addressLine1);
        this.addressLine1 = addressLine1;
    }

    public HyperwalletUser addressLine1(String addressLine1) {
        addField("addressLine1", addressLine1);
        this.addressLine1 = addressLine1;
        return this;
    }

    public void clearAddressLine1() {
        clearField("addressLine1");
        addressLine1 = null;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        addField("addressLine2", addressLine2);
        this.addressLine2 = addressLine2;
    }

    public HyperwalletUser addressLine2(String addressLine2) {
        addField("addressLine2", addressLine2);
        this.addressLine2 = addressLine2;
        return this;
    }

    public void clearAddressLine2() {
        clearField("addressLine2");
        addressLine2 = null;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        addField("city", city);
        this.city = city;
    }

    public HyperwalletUser city(String city) {
        addField("city", city);
        this.city = city;
        return this;
    }

    public void clearCity() {
        clearField("city");
        city = null;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        addField("stateProvince", stateProvince);
        this.stateProvince = stateProvince;
    }

    public HyperwalletUser stateProvince(String stateProvince) {
        addField("stateProvince", stateProvince);
        this.stateProvince = stateProvince;
        return this;
    }

    public void clearStateProvince() {
        clearField("stateProvince");
        stateProvince = null;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        addField("postalCode", postalCode);
        this.postalCode = postalCode;
    }

    public HyperwalletUser postalCode(String postalCode) {
        addField("postalCode", postalCode);
        this.postalCode = postalCode;
        return this;
    }

    public void clearPostalCode() {
        clearField("postalCode");
        postalCode = null;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        addField("country", country);
        this.country = country;
    }

    public HyperwalletUser country(String country) {
        addField("country", country);
        this.country = country;
        return this;
    }

    public void clearCountry() {
        clearField("country");
        country = null;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        addField("language", language);
        this.language = language;
    }

    public HyperwalletUser language(String language) {
        addField("language", language);
        this.language = language;
        return this;
    }

    public void clearLanguage() {
        clearField("language");
        language = null;
    }

    public String getProgramToken() {
        return programToken;
    }

    public void setProgramToken(String programToken) {
        addField("programToken", programToken);
        this.programToken = programToken;
    }

    public HyperwalletUser programToken(String programToken) {
        addField("programToken", programToken);
        this.programToken = programToken;
        return this;
    }
}
