package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class HyperwalletBankAccount {

	public static enum Type { BANK_ACCOUNT, WIRE_ACCOUNT }
	public static enum Status { ACTIVATED, INVALID, DE_ACTIVATED }
	public static enum Relationship { SELF, JOINT_ACCOUNT, SPOUSE, RELATIVE, BUSINESS_PARTNER, UPLINE, DOWNLINE, OTHER, OWN_COMPANY, BILL_PAYMENT }

	public String token;

	public Type type;
	public Status status;
	public Date createdOn;
	public String transferMethodCountry;
	public String transferMethodCurrency;

	public String bankName;
	public String bankId;
	public String branchName;
	public String branchId;
	public String bankAccountId;
	public Relationship bankAccountRelationship;
	public String bankAccountPurpose;

	public String branchAddressLine1;
	public String branchAddressLine2;
	public String branchCity;
	public String branchStateProvince;
	public String branchCountry;
	public String branchPostalCode;

	public String wireInstructions;
	public String intermediaryBankId;
	public String intermediaryBankName;
	public String intermediaryBankAccountId;
	public String intermediaryBankAddressLine1;
	public String intermediaryBankAddressLine2;
	public String intermediaryBankCity;
	public String intermediaryBankStateProvince;
	public String intermediaryBankCountry;
	public String intermediaryBankPostalCode;

	public String userToken;

	public HyperwalletUser.ProfileType profileType;

	public String businessName;
	public String businessRegistrationId;
	public String businessRegistrationStateProvince;
	public String businessRegistrationCountry;
	public HyperwalletUser.BusinessContactRole businessContactRole;
	public String firstName;
	public String middleName;
	public String lastName;
	public Date dateOfBirth;
	public String countryOfBirth;
	public String countryOfNationality;
	public HyperwalletUser.Gender gender;
	public String phoneNumber;
	public String mobileNumber;
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

	public String getToken() {
		return token;
	}

	public HyperwalletBankAccount setToken(String token) {
		this.token = token;
		return this;
	}

	public Type getType() {
		return type;
	}

	public HyperwalletBankAccount setType(Type type) {
		this.type = type;
		return this;
	}

	public Status getStatus() {
		return status;
	}

	public HyperwalletBankAccount setStatus(Status status) {
		this.status = status;
		return this;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public HyperwalletBankAccount setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
		return this;
	}

	public String getTransferMethodCountry() {
		return transferMethodCountry;
	}

	public HyperwalletBankAccount setTransferMethodCountry(String transferMethodCountry) {
		this.transferMethodCountry = transferMethodCountry;
		return this;
	}

	public String getTransferMethodCurrency() {
		return transferMethodCurrency;
	}

	public HyperwalletBankAccount setTransferMethodCurrency(String transferMethodCurrency) {
		this.transferMethodCurrency = transferMethodCurrency;
		return this;
	}

	public String getBankName() {
		return bankName;
	}

	public HyperwalletBankAccount setBankName(String bankName) {
		this.bankName = bankName;
		return this;
	}

	public String getBankId() {
		return bankId;
	}

	public HyperwalletBankAccount setBankId(String bankId) {
		this.bankId = bankId;
		return this;
	}

	public String getBranchName() {
		return branchName;
	}

	public HyperwalletBankAccount setBranchName(String branchName) {
		this.branchName = branchName;
		return this;
	}

	public String getBranchId() {
		return branchId;
	}

	public HyperwalletBankAccount setBranchId(String branchId) {
		this.branchId = branchId;
		return this;
	}

	public String getBankAccountId() {
		return bankAccountId;
	}

	public HyperwalletBankAccount setBankAccountId(String bankAccountId) {
		this.bankAccountId = bankAccountId;
		return this;
	}

	public Relationship getRelationship() {
		return bankAccountRelationship;
	}

	public HyperwalletBankAccount setRelationship(Relationship bankAccountRelationship) {
		this.bankAccountRelationship = bankAccountRelationship;
		return this;
	}

	public String getBankAccountPurpose() {
		return bankAccountPurpose;
	}

	public HyperwalletBankAccount setBankAccountPurpose(String bankAccountPurpose) {
		this.bankAccountPurpose = bankAccountPurpose;
		return this;
	}

	public String getBranchAddressLine1() {
		return branchAddressLine1;
	}

	public HyperwalletBankAccount setBranchAddressLine1(String branchAddressLine1) {
		this.branchAddressLine1 = branchAddressLine1;
		return this;
	}

	public String getBranchAddressLine2() {
		return branchAddressLine2;
	}

	public HyperwalletBankAccount setBranchAddressLine2(String branchAddressLine2) {
		this.branchAddressLine2 = branchAddressLine2;
		return this;
	}

	public String getBranchCity() {
		return branchCity;
	}

	public HyperwalletBankAccount setBranchCity(String branchCity) {
		this.branchCity = branchCity;
		return this;
	}

	public String getBranchStateProvince() {
		return branchStateProvince;
	}

	public HyperwalletBankAccount setBranchStateProvince(String branchStateProvince) {
		this.branchStateProvince = branchStateProvince;
		return this;
	}

	public String getBranchCountry() {
		return branchCountry;
	}

	public HyperwalletBankAccount setBranchCountry(String branchCountry) {
		this.branchCountry = branchCountry;
		return this;
	}

	public String getBranchPostalCode() {
		return branchPostalCode;
	}

	public HyperwalletBankAccount setBranchPostalCode(String branchPostalCode) {
		this.branchPostalCode = branchPostalCode;
		return this;
	}

	public String getWireInstructions() {
		return wireInstructions;
	}

	public HyperwalletBankAccount setWireInstructions(String wireInstructions) {
		this.wireInstructions = wireInstructions;
		return this;
	}

	public String getIntermediaryBankId() {
		return intermediaryBankId;
	}

	public HyperwalletBankAccount setIntermediaryBankId(String intermediaryBankId) {
		this.intermediaryBankId = intermediaryBankId;
		return this;
	}

	public String getIntermediaryBankName() {
		return intermediaryBankName;
	}

	public HyperwalletBankAccount setIntermediaryBankName(String intermediaryBankName) {
		this.intermediaryBankName = intermediaryBankName;
		return this;
	}

	public String getIntermediaryBankAccountId() {
		return intermediaryBankAccountId;
	}

	public HyperwalletBankAccount setIntermediaryBankAccountId(String intermediaryBankAccountId) {
		this.intermediaryBankAccountId = intermediaryBankAccountId;
		return this;
	}

	public String getIntermediaryBankAddressLine1() {
		return intermediaryBankAddressLine1;
	}

	public HyperwalletBankAccount setIntermediaryBankAddressLine1(String intermediaryBankAddressLine1) {
		this.intermediaryBankAddressLine1 = intermediaryBankAddressLine1;
		return this;
	}

	public String getIntermediaryBankAddressLine2() {
		return intermediaryBankAddressLine2;
	}

	public HyperwalletBankAccount setIntermediaryBankAddressLine2(String intermediaryBankAddressLine2) {
		this.intermediaryBankAddressLine2 = intermediaryBankAddressLine2;
		return this;
	}

	public String getIntermediaryBankCity() {
		return intermediaryBankCity;
	}

	public HyperwalletBankAccount setIntermediaryBankCity(String intermediaryBankCity) {
		this.intermediaryBankCity = intermediaryBankCity;
		return this;
	}

	public String getIntermediaryBankStateProvince() {
		return intermediaryBankStateProvince;
	}

	public HyperwalletBankAccount setIntermediaryBankStateProvince(String intermediaryBankStateProvince) {
		this.intermediaryBankStateProvince = intermediaryBankStateProvince;
		return this;
	}

	public String getIntermediaryBankCountry() {
		return intermediaryBankCountry;
	}

	public HyperwalletBankAccount setIntermediaryBankCountry(String intermediaryBankCountry) {
		this.intermediaryBankCountry = intermediaryBankCountry;
		return this;
	}

	public String getIntermediaryBankPostalCode() {
		return intermediaryBankPostalCode;
	}

	public HyperwalletBankAccount setIntermediaryBankPostalCode(String intermediaryBankPostalCode) {
		this.intermediaryBankPostalCode = intermediaryBankPostalCode;
		return this;
	}

	public String getUserToken() {
		return userToken;
	}

	public HyperwalletBankAccount setUserToken(String userToken) {
		this.userToken = userToken;
		return this;
	}

	public HyperwalletUser.ProfileType getProfileType() {
		return profileType;
	}

	public HyperwalletBankAccount setProfileType(HyperwalletUser.ProfileType profileType) {
		this.profileType = profileType;
		return this;
	}

	public String getBusinessName() {
		return businessName;
	}

	public HyperwalletBankAccount setBusinessName(String businessName) {
		this.businessName = businessName;
		return this;
	}

	public String getBusinessRegistrationId() {
		return businessRegistrationId;
	}

	public HyperwalletBankAccount setBusinessRegistrationId(String businessRegistrationId) {
		this.businessRegistrationId = businessRegistrationId;
		return this;
	}

	public String getBusinessRegistrationStateProvince() {
		return businessRegistrationStateProvince;
	}

	public HyperwalletBankAccount setBusinessRegistrationStateProvince(String businessRegistrationStateProvince) {
		this.businessRegistrationStateProvince = businessRegistrationStateProvince;
		return this;
	}

	public String getBusinessRegistrationCountry() {
		return businessRegistrationCountry;
	}

	public HyperwalletBankAccount setBusinessRegistrationCountry(String businessRegistrationCountry) {
		this.businessRegistrationCountry = businessRegistrationCountry;
		return this;
	}

	public HyperwalletUser.BusinessContactRole getBusinessContactRole() {
		return businessContactRole;
	}

	public HyperwalletBankAccount setBusinessContactRole(HyperwalletUser.BusinessContactRole businessContactRole) {
		this.businessContactRole = businessContactRole;
		return this;
	}

	public String getFirstName() {
		return firstName;
	}

	public HyperwalletBankAccount setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public String getMiddleName() {
		return middleName;
	}

	public HyperwalletBankAccount setMiddleName(String middleName) {
		this.middleName = middleName;
		return this;
	}

	public String getLastName() {
		return lastName;
	}

	public HyperwalletBankAccount setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public HyperwalletBankAccount setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
		return this;
	}

	public String getCountryOfBirth() {
		return countryOfBirth;
	}

	public HyperwalletBankAccount setCountryOfBirth(String countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
		return this;
	}

	public String getCountryOfNationality() {
		return countryOfNationality;
	}

	public HyperwalletBankAccount setCountryOfNationality(String countryOfNationality) {
		this.countryOfNationality = countryOfNationality;
		return this;
	}

	public HyperwalletUser.Gender getGender() {
		return gender;
	}

	public HyperwalletBankAccount setGender(HyperwalletUser.Gender gender) {
		this.gender = gender;
		return this;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public HyperwalletBankAccount setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public HyperwalletBankAccount setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public HyperwalletBankAccount setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getGovernmentId() {
		return governmentId;
	}

	public HyperwalletBankAccount setGovernmentId(String governmentId) {
		this.governmentId = governmentId;
		return this;
	}

	public String getPassportId() {
		return passportId;
	}

	public HyperwalletBankAccount setPassportId(String passportId) {
		this.passportId = passportId;
		return this;
	}

	public String getDriversLicenseId() {
		return driversLicenseId;
	}

	public HyperwalletBankAccount setDriversLicenseId(String driversLicenseId) {
		this.driversLicenseId = driversLicenseId;
		return this;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public HyperwalletBankAccount setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
		return this;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public HyperwalletBankAccount setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
		return this;
	}

	public String getCity() {
		return city;
	}

	public HyperwalletBankAccount setCity(String city) {
		this.city = city;
		return this;
	}

	public String getStateProvince() {
		return stateProvince;
	}

	public HyperwalletBankAccount setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
		return this;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public HyperwalletBankAccount setPostalCode(String postalCode) {
		this.postalCode = postalCode;
		return this;
	}

	public String getCountry() {
		return country;
	}

	public HyperwalletBankAccount setCountry(String country) {
		this.country = country;
		return this;
	}
}
