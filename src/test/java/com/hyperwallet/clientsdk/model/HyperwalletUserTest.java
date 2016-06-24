package com.hyperwallet.clientsdk.model;

import java.util.Date;

/**
 * @author fkrauthan
 */
public class HyperwalletUserTest extends BaseModelTest<HyperwalletUser> {

    protected HyperwalletUser createBaseModel() {
        HyperwalletUser user = new HyperwalletUser();
        user
                .token("test-token")
                .status(HyperwalletUser.Status.ACTIVATED)
                .createdOn(new Date())
                .clientUserId("test-client-user-id")
                .profileType(HyperwalletUser.ProfileType.INDIVIDUAL)

                .businessType(HyperwalletUser.BusinessType.CORPORATION)
                .businessName("test-business-name")
                .businessRegistrationId("test-business-registration-id")
                .businessRegistrationStateProvince("test-business-registration-state-province")
                .businessRegistrationCountry("test-business-registration-country")
                .businessContactRole(HyperwalletUser.BusinessContactRole.DIRECTOR)

                .firstName("test-first-name")
                .middleName("test-middle-name")
                .lastName("test-last-name")
                .dateOfBirth(new Date())
                .countryOfBirth("test-country-of-borth")
                .countryOfNationality("test-country-of-nationality")
                .gender(HyperwalletUser.Gender.MALE)

                .phoneNumber("test-phone-number")
                .mobileNumber("test-mobile-number")
                .email("test-email")

                .governmentId("test-government-id")
                .passportId("test-passport-id")
                .driversLicenseId("test-drivers-license-id")
                .employerId("test-employer-id")

                .addressLine1("test-address-line1")
                .addressLine2("test-address-line2")
                .city("test-city")
                .stateProvince("test-state-province")
                .postalCode("test-postal-code")
                .country("test-country")

                .language("test-language")
                .programToken("test-program-token");

        return user;
    }

    @Override
    protected Class<HyperwalletUser> createModelClass() {
        return HyperwalletUser.class;
    }
}
