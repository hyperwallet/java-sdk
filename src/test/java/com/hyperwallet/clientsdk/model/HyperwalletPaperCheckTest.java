package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletPaperCheckTest extends BaseModelTest<HyperwalletPaperCheck> {
    protected HyperwalletPaperCheck createBaseModel() {
        HyperwalletPaperCheck paperCheck = new HyperwalletPaperCheck();
        paperCheck
                .token("test-token")
                .type(HyperwalletTransferMethod.Type.PREPAID_CARD)
                .status(HyperwalletTransferMethod.Status.ACTIVATED)
                .createdOn(new Date())
                .transferMethodCountry("test-transfer-method-country")
                .transferMethodCurrency("test-transfer-method-currency")
                .isDefaultTransferMethod(Boolean.FALSE)

                .addressLine1("test-address-line1")
                .addressLine2("test-address-line2")
                .bankAccountRelationship(HyperwalletTransferMethod.BankAccountRelationship.SELF)
                .businessContactRole(HyperwalletUser.BusinessContactRole.OWNER)
                .businessName("test-business-name")
                .businessRegistrationCountry("test-business-registration-country")
                .businessRegistrationId("test-business-registration-id")
                .businessRegistrationStateProvince("test-business-registration-state-province")
                .businessType(HyperwalletUser.BusinessType.PARTNERSHIP)
                .city("test-city")
                .country("test-country")
                .countryOfBirth("test-country-of-birth")
                .countryOfNationality("test-country-of-nationality")
                .dateOfBirth(new Date())
                .driversLicenseId("test-drivers-license-id")
                .employerId("test-employer-id")
                .firstName("test-first-name")
                .gender(HyperwalletUser.Gender.FEMALE)
                .governmentId("test-government-id")
                .governmentIdType(HyperwalletPaperCheck.GovernmentIdType.NATIONAL_ID_CARD)
                .lastName("test-last-name")
                .middleName("test-middle-name")
                .mobileNumber("test-mobile-number")
                .passportId("test-passport-id")
                .phoneNumber("test-phone-number")
                .postalCode("test-postal-code")
                .profileType(HyperwalletUser.ProfileType.BUSINESS)
                .shippingMethod(HyperwalletPaperCheck.ShippingMethod.STANDARD)
                .stateProvince("test-state-province")

                .userToken("test-user-token");
        return paperCheck;
    }

    protected Class<HyperwalletPaperCheck> createModelClass() {
        return HyperwalletPaperCheck.class;
    }

}
