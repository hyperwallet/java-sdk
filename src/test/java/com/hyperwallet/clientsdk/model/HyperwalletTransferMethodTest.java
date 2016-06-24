package com.hyperwallet.clientsdk.model;

import java.util.Date;

/**
 * @author fkrauthan
 */
public class HyperwalletTransferMethodTest extends BaseModelTest<HyperwalletTransferMethod> {
    protected HyperwalletTransferMethod createBaseModel() {
        HyperwalletTransferMethod transferMethod = new HyperwalletTransferMethod();
        transferMethod
                .token("test-token")

                .type(HyperwalletTransferMethod.Type.BANK_ACCOUNT)
                .status(HyperwalletTransferMethod.Status.ACTIVATED)
                .createdOn(new Date())
                .transferMethodCountry("test-transfer-method-country")
                .transferMethodCurrency("test-transfer-method-currency")

                .bankName("test-bank-name")
                .bankId("test-bank-id")
                .branchName("test-branch-name")
                .branchId("test-branch-id")
                .bankAccountId("test-bank-account-id")
                .bankAccountRelationship(HyperwalletTransferMethod.BankAccountRelationship.SELF)
                .bankAccountPurpose("test-bank-account-purpose")

                .branchAddressLine1("test-branch-address-line1")
                .branchAddressLine2("test-branch-address-line2")
                .branchCity("test-branch-city")
                .branchStateProvince("test-branch-state-province")
                .branchCountry("test-branch-country")
                .branchPostalCode("test-branch-postal-code")

                .wireInstructions("test-wire-instructions")
                .intermediaryBankId("test-intermediary-bank-id")
                .intermediaryBankName("test-intermediary-bank-name")
                .intermediaryBankAccountId("test-intermediary-bank-account-id")

                .intermediaryBankAddressLine1("test-intermediary-bank-address-line1")
                .intermediaryBankAddressLine2("test-intermediary-bank-address-line2")
                .intermediaryBankCity("test-intermediary-bank-city")
                .intermediaryBankStateProvince("test-intermediary-bank-state-province")
                .intermediaryBankCountry("test-intermediary-bank-country")
                .intermediaryBankPostalCode("test-intermediary-bank-postal-code")

                .cardType(HyperwalletTransferMethod.CardType.VIRTUAL)
                .cardPackage("test-card-package")
                .cardNumber("test-card-number")
                .cardBrand(HyperwalletPrepaidCard.Brand.VISA)
                .dateOfExpiry(new Date())

                .userToken("test-user-token")
                .profileType(HyperwalletUser.ProfileType.INDIVIDUAL)

                .businessName("test-business-name")
                .businessRegistrationId("test-business-registration-id")
                .businessRegistrationStateProvince("test-business-registration-state-province")
                .businessRegistrationCountry("test-business-registration-country")
                .businessContactRole(HyperwalletUser.BusinessContactRole.OWNER)

                .firstName("test-first-name")
                .middleName("test-middle-name")
                .lastName("test-last-name")
                .dateOfBirth(new Date())
                .countryOfBirth("test-country-of-birth")
                .countryOfNationality("test-country-of-nationality")
                .gender(HyperwalletUser.Gender.MALE)

                .phoneNumber("test-phone-number")
                .mobileNumber("test-mobile-number")
                .email("test-email")

                .governmentId("test-government-id")
                .passportId("test-passport-id")
                .driversLicenseId("test-drivers-license-id")

                .addressLine1("test-address-line1")
                .addressLine2("test-address-line2")
                .city("test-city")
                .stateProvince("test-state-province")
                .postalCode("test-postal-code")
                .country("test-country");

        return transferMethod;
    }

    protected Class<HyperwalletTransferMethod> createModelClass() {
        return HyperwalletTransferMethod.class;
    }
}
