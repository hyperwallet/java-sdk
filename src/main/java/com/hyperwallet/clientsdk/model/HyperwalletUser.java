package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletUser {

	public static enum Gender { MALE, FEMALE }
	public static enum ProfileType { INDIVIDUAL, BUSINESS }
	public static enum BusinessType { CORPORATION, PARTNERSHIP }
	public static enum BusinessContactRole { DIRECTOR, OWNER, OTHER }
	public static enum Status { PRE_ACTIVATED, ACTIVATED, LOCKED, FROZEN, DE_ACTIVATED}

	public String token;
	public Status status;
	public Date createdOn;
	public String clientUserId;
	public ProfileType profileType;

	public BusinessType businessType;
	public String businessName;
	public String businessRegistrationId;
	public String businessRegistrationStateProvince;
	public String businessRegistrationCountry;
	public BusinessContactRole businessContactRole;

	public String firstName;
	public String middleName;
	public String lastName;
	public Date dateOfBirth;
	public String countryOfBirth;
	public String countryOfNationality;
	public Gender gender;
	public String phoneNumber;
	public String mobileNmber;
	public String email;
	public String governmentId;
	public String passportId;
	public String driversLicenseId;
	public String addressLine1;
	public String addressLine2;
	public String city;
	public String stateProvince;
	public String postalCode;
	public String country;
	public String language;
	public String programToken;

	public String getToken() {
		return token;
	}

	public HyperwalletUser setToken(String token) {
		this.token = token;
		return this;
	}

	public Status getStatus() {
		return status;
	}

	public HyperwalletUser setStatus(Status status) {
		this.status = status;
		return this;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public HyperwalletUser setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
		return this;
	}

	public String getClientUserId() {
		return clientUserId;
	}

	public HyperwalletUser setClientUserId(String clientUserId) {
		this.clientUserId = clientUserId;
		return this;
	}

	public ProfileType getProfileType() {
		return profileType;
	}

	public HyperwalletUser setProfileType(ProfileType profileType) {
		this.profileType = profileType;
		return this;
	}

	public BusinessType getBusinessType() {
		return businessType;
	}

	public HyperwalletUser setBusinessType(BusinessType businessType) {
		this.businessType = businessType;
		return this;
	}

	public String getBusinessName() {
		return businessName;
	}

	public HyperwalletUser setBusinessName(String businessName) {
		this.businessName = businessName;
		return this;
	}

	public String getBusinessRegistrationId() {
		return businessRegistrationId;
	}

	public HyperwalletUser setBusinessRegistrationId(String businessRegistrationId) {
		this.businessRegistrationId = businessRegistrationId;
		return this;
	}

	public String getBusinessRegistrationStateProvince() {
		return businessRegistrationStateProvince;
	}

	public HyperwalletUser setBusinessRegistrationStateProvince(String businessRegistrationStateProvince) {
		this.businessRegistrationStateProvince = businessRegistrationStateProvince;
		return this;
	}

	public String getBusinessRegistrationCountry() {
		return businessRegistrationCountry;
	}

	public HyperwalletUser setBusinessRegistrationCountry(String businessRegistrationCountry) {
		this.businessRegistrationCountry = businessRegistrationCountry;
		return this;
	}

	public BusinessContactRole getBusinessContactRole() {
		return businessContactRole;
	}

	public HyperwalletUser setBusinessContactRole(BusinessContactRole businessContactRole) {
		this.businessContactRole = businessContactRole;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public HyperwalletUser setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getMiddleName() {
		return middleName;
	}

	public HyperwalletUser setMiddleName(String middleName) {
		this.middleName = middleName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public HyperwalletUser setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public HyperwalletUser setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		return this;
	}

	public String getCountryOfBirth() {
		return countryOfBirth;
	}

	public HyperwalletUser setCountryOfBirth(String countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
		return this;
	}

	public String getCountryOfNationality() {
		return countryOfNationality;
	}

	public HyperwalletUser setCountryOfNationality(String countryOfNationality) {
		this.countryOfNationality = countryOfNationality;
		return this;
	}

	public Gender getGender() {
		return gender;
	}

	public HyperwalletUser setGender(Gender gender) {
		this.gender = gender;
		return this;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public HyperwalletUser setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public String getMobileNmber() {
		return mobileNmber;
	}

	public HyperwalletUser setMobileNmber(String mobileNmber) {
		this.mobileNmber = mobileNmber;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public HyperwalletUser setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getGovernmentId() {
		return governmentId;
	}

	public HyperwalletUser setGovernmentId(String governmentId) {
		this.governmentId = governmentId;
		return this;
	}

	public String getPassportId() {
		return passportId;
	}

	public HyperwalletUser setPassportId(String passportId) {
		this.passportId = passportId;
		return this;
	}

	public String getDriversLicenseId() {
		return driversLicenseId;
	}

	public HyperwalletUser setDriversLicenseId(String driversLicenseId) {
		this.driversLicenseId = driversLicenseId;
		return this;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public HyperwalletUser setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
		return this;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public HyperwalletUser setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
		return this;
	}

	public String getCity() {
		return city;
	}

	public HyperwalletUser setCity(String city) {
		this.city = city;
		return this;
	}

	public String getStateProvince() {
		return stateProvince;
	}

	public HyperwalletUser setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
		return this;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public HyperwalletUser setPostalCode(String postalCode) {
		this.postalCode = postalCode;
		return this;
	}

	public String getCountry() {
		return country;
	}

	public HyperwalletUser setCountry(String country) {
		this.country = country;
		return this;
	}

	public String getLanguage() {
		return language;
	}

	public HyperwalletUser setLanguage(String language) {
		this.language = language;
		return this;
	}

	public String getProgramToken() {
		return programToken;
	}

	public HyperwalletUser setProgramToken(String programToken) {
		this.programToken = programToken;
		return this;
	}
}
