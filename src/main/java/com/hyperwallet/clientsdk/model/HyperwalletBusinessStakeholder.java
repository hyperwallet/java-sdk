package com.hyperwallet.clientsdk.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.hyperwallet.clientsdk.util.HyperwalletJsonConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

@JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletBusinessStakeholder extends HyperwalletBaseMonitor {

    public static enum Gender {MALE, FEMALE}

    public static enum ProfileType {INDIVIDUAL}

    public static enum GovernmentIdType {PASSPORT, NATIONAL_ID_CARD}

    public static enum Status {ACTIVATED, DE_ACTIVATED}

    public static enum VerificationStatus {UNDER_REVIEW, VERIFIED, REQUIRED, NOT_REQUIRED, READY_FOR_REVIEW}

    private String token;
    private Boolean isBusinessContact;
    private Boolean isDirector;
    private Boolean isUltimateBeneficialOwner;
    private Boolean isSeniorManagingOfficial;
    private Status status;
    private VerificationStatus verificationStatus;
    private Date createdOn;
    private ProfileType profileType;
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
    private GovernmentIdType governmentIdType;
    private String driversLicenseId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String stateProvince;
    private String country;
    private String postalCode;
    private List<HyperwalletLink> links;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        addField("token", token);
        this.token = token;
    }

    public HyperwalletBusinessStakeholder token(String token) {
        addField("token", token);
        this.token = token;
        return this;
    }

    public HyperwalletBusinessStakeholder clearToken() {
        clearField("token");
        token = null;
        return this;
    }

    public Boolean getIsBusinessContact() {
        return isBusinessContact;
    }

    public void setIsBusinessContact(Boolean isBusinessContact) {
        addField("isBusinessContact", isBusinessContact);
        this.isBusinessContact = isBusinessContact;
    }

    public HyperwalletBusinessStakeholder isBusinessContact(Boolean isBusinessContact) {
        addField("isBusinessContact", isBusinessContact);
        this.isBusinessContact = isBusinessContact;
        return this;
    }

    public HyperwalletBusinessStakeholder clearIsBusinessContact() {
        clearField("isBusinessContact");
        isBusinessContact = null;
        return this;
    }

    public Boolean getIsDirector() {
        return isDirector;
    }

    public void setIsDirector(Boolean isDirector) {
        addField("isDirector", isDirector);
        this.isDirector = isDirector;
    }

    public HyperwalletBusinessStakeholder isDirector(Boolean isDirector) {
        addField("isDirector", isDirector);
        this.isDirector = isDirector;
        return this;
    }

    public HyperwalletBusinessStakeholder clearIsDirector() {
        clearField("isDirector");
        isDirector = null;
        return this;
    }

    public Boolean getIsUltimateBeneficialOwner() {
        return isUltimateBeneficialOwner;
    }

    public void setIsUltimateBeneficialOwner(Boolean isUltimateBeneficialOwner) {
        addField("isUltimateBeneficialOwner", isUltimateBeneficialOwner);
        this.isUltimateBeneficialOwner = isUltimateBeneficialOwner;
    }

    public HyperwalletBusinessStakeholder isUltimateBeneficialOwner(Boolean isUltimateBeneficialOwner) {
        addField("isUltimateBeneficialOwner", isUltimateBeneficialOwner);
        this.isUltimateBeneficialOwner = isUltimateBeneficialOwner;
        return this;
    }

    public HyperwalletBusinessStakeholder clearIsUltimateBeneficialOwner() {
        clearField("isUltimateBeneficialOwner");
        isUltimateBeneficialOwner = null;
        return this;
    }

    public Boolean getIsSeniorManagingOfficial() {
        return isSeniorManagingOfficial;
    }

    public void setIsSeniorManagingOfficial(Boolean isSeniorManagingOfficial) {
        addField("isSeniorManagingOfficial", isSeniorManagingOfficial);
        this.isSeniorManagingOfficial = isSeniorManagingOfficial;
    }

    public HyperwalletBusinessStakeholder isSeniorManagingOfficial(Boolean isSeniorManagingOfficial) {
        addField("isSeniorManagingOfficial", isSeniorManagingOfficial);
        this.isSeniorManagingOfficial = isSeniorManagingOfficial;
        return this;
    }

    public HyperwalletBusinessStakeholder clearIsSeniorManagingOfficial() {
        clearField("isSeniorManagingOfficial");
        isSeniorManagingOfficial = null;
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        addField("status", status);
        this.status = status;
    }

    public HyperwalletBusinessStakeholder status(Status status) {
        addField("status", status);
        this.status = status;
        return this;
    }

    public HyperwalletBusinessStakeholder clearStatus() {
        clearField("status");
        status = null;
        return this;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        addField("verificationStatus", verificationStatus);
        this.verificationStatus = verificationStatus;
    }

    public HyperwalletBusinessStakeholder verificationStatus(VerificationStatus verificationStatus) {
        addField("verificationStatus", verificationStatus);
        this.verificationStatus = verificationStatus;
        return this;
    }

    public HyperwalletBusinessStakeholder clearVerificationStatus() {
        clearField("verificationStatus");
        verificationStatus = null;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
    }

    public HyperwalletBusinessStakeholder createdOn(Date createdOn) {
        addField("createdOn", createdOn);
        this.createdOn = createdOn;
        return this;
    }

    public HyperwalletBusinessStakeholder clearCreatedOn() {
        clearField("createdOn");
        createdOn = null;
        return this;
    }

    public ProfileType getProfileType() {
        return profileType;
    }

    public void setProfileType(ProfileType profileType) {
        addField("profileType", profileType);
        this.profileType = profileType;
    }

    public HyperwalletBusinessStakeholder profileType(ProfileType profileType) {
        addField("profileType", profileType);
        this.profileType = profileType;
        return this;
    }

    public HyperwalletBusinessStakeholder clearProfileType() {
        clearField("profileType");
        profileType = null;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        addField("firstName", firstName);
        this.firstName = firstName;
    }

    public HyperwalletBusinessStakeholder firstName(String firstName) {
        addField("firstName", firstName);
        this.firstName = firstName;
        return this;
    }

    public HyperwalletBusinessStakeholder clearFirstName() {
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

    public HyperwalletBusinessStakeholder middleName(String middleName) {
        addField("middleName", middleName);
        this.middleName = middleName;
        return this;
    }

    public HyperwalletBusinessStakeholder clearMiddleName() {
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

    public HyperwalletBusinessStakeholder lastName(String lastName) {
        addField("lastName", lastName);
        this.lastName = lastName;
        return this;
    }

    public HyperwalletBusinessStakeholder clearLastName() {
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

    public HyperwalletBusinessStakeholder dateOfBirth(Date dateOfBirth) {
        addField("dateOfBirth", dateOfBirth);
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public HyperwalletBusinessStakeholder clearDateOfBirth() {
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

    public HyperwalletBusinessStakeholder countryOfBirth(String countryOfBirth) {
        addField("countryOfBirth", countryOfBirth);
        this.countryOfBirth = countryOfBirth;
        return this;
    }

    public HyperwalletBusinessStakeholder clearCountryOfBirth() {
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

    public HyperwalletBusinessStakeholder countryOfNationality(String countryOfNationality) {
        addField("countryOfNationality", countryOfNationality);
        this.countryOfNationality = countryOfNationality;
        return this;
    }

    public HyperwalletBusinessStakeholder clearCountryOfNationality() {
        clearField("countryOfNationality");
        countryOfNationality = null;
        return this;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        addField("gender", gender);
        this.gender = gender;
    }

    public HyperwalletBusinessStakeholder gender(Gender gender) {
        addField("gender", gender);
        this.gender = gender;
        return this;
    }

    public HyperwalletBusinessStakeholder clearGender() {
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

    public HyperwalletBusinessStakeholder phoneNumber(String phoneNumber) {
        addField("phoneNumber", phoneNumber);
        this.phoneNumber = phoneNumber;
        return this;
    }

    public HyperwalletBusinessStakeholder clearPhoneNumber() {
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

    public HyperwalletBusinessStakeholder mobileNumber(String mobileNumber) {
        addField("mobileNumber", mobileNumber);
        this.mobileNumber = mobileNumber;
        return this;
    }

    public HyperwalletBusinessStakeholder clearMobileNumber() {
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

    public HyperwalletBusinessStakeholder email(String email) {
        addField("email", email);
        this.email = email;
        return this;
    }

    public HyperwalletBusinessStakeholder clearEmail() {
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

    public HyperwalletBusinessStakeholder governmentId(String governmentId) {
        addField("governmentId", governmentId);
        this.governmentId = governmentId;
        return this;
    }

    public HyperwalletBusinessStakeholder clearGovernmentId() {
        clearField("governmentId");
        governmentId = null;
        return this;
    }


    public GovernmentIdType getGovernmentIdType() {
        return governmentIdType;
    }

    public void setGovernmentIdType(GovernmentIdType governmentIdType) {
        addField("governmentIdType", governmentIdType);
        this.governmentIdType = governmentIdType;
    }

    public HyperwalletBusinessStakeholder governmentIdType(GovernmentIdType governmentIdType) {
        addField("governmentIdType", governmentIdType);
        this.governmentIdType = governmentIdType;
        return this;
    }

    public HyperwalletBusinessStakeholder clearGovernmentIdType() {
        clearField("governmentIdType");
        governmentIdType = null;
        return this;
    }

    public String getDriversLicenseId() {
        return driversLicenseId;
    }

    public void setDriversLicenseId(String driversLicenseId) {
        addField("driversLicenseId", driversLicenseId);
        this.driversLicenseId = driversLicenseId;
    }

    public HyperwalletBusinessStakeholder driversLicenseId(String driversLicenseId) {
        addField("driversLicenseId", driversLicenseId);
        this.driversLicenseId = driversLicenseId;
        return this;
    }

    public HyperwalletBusinessStakeholder clearDriversLicenseId() {
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

    public HyperwalletBusinessStakeholder addressLine1(String addressLine1) {
        addField("addressLine1", addressLine1);
        this.addressLine1 = addressLine1;
        return this;
    }

    public HyperwalletBusinessStakeholder clearAddressLine1() {
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

    public HyperwalletBusinessStakeholder addressLine2(String addressLine2) {
        addField("addressLine2", addressLine2);
        this.addressLine2 = addressLine2;
        return this;
    }

    public HyperwalletBusinessStakeholder clearAddressLine2() {
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

    public HyperwalletBusinessStakeholder city(String city) {
        addField("city", city);
        this.city = city;
        return this;
    }

    public HyperwalletBusinessStakeholder clearCity() {
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

    public HyperwalletBusinessStakeholder stateProvince(String stateProvince) {
        addField("stateProvince", stateProvince);
        this.stateProvince = stateProvince;
        return this;
    }

    public HyperwalletBusinessStakeholder clearStateProvince() {
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

    public HyperwalletBusinessStakeholder postalCode(String postalCode) {
        addField("postalCode", postalCode);
        this.postalCode = postalCode;
        return this;
    }

    public HyperwalletBusinessStakeholder clearPostalCode() {
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

    public HyperwalletBusinessStakeholder country(String country) {
        addField("country", country);
        this.country = country;
        return this;
    }

    public HyperwalletBusinessStakeholder clearCountry() {
        clearField("country");
        country = null;
        return this;
    }

    public List<HyperwalletLink> getLinks() {
        return links;
    }

    public void setLinks(List<HyperwalletLink> links) {
        addField("links", links);
        this.links = links;
    }

    public HyperwalletBusinessStakeholder links(List<HyperwalletLink> links) {
        addField("links", links);
        this.links = links;
        return this;
    }

    public HyperwalletBusinessStakeholder clearLinks() {
        clearField("links");
        links = null;
        return this;
    }

}
