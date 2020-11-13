package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.model.HyperwalletDocument.ECountryCode;
import com.hyperwallet.clientsdk.model.HyperwalletDocument.EDocumentCategory;
import com.hyperwallet.clientsdk.model.HyperwalletDocument.EIdentityVerificationType;
import com.hyperwallet.clientsdk.model.HyperwalletDocument.EKycDocumentVerificationStatus;
import com.hyperwallet.clientsdk.model.HyperwalletUser.VerificationStatus;


import java.util.*;

/**
 * @author fkrauthan
 */
public class HyperwalletUserTest extends BaseModelTest<HyperwalletUser> {

    protected HyperwalletUser createBaseModel() {
        HyperwalletUser user = new HyperwalletUser();
        HyperwalletVerificationDocument hyperwalletDocument = new HyperwalletVerificationDocument();
        hyperwalletDocument.category("IDENTIFICATION").type("DRIVERS_LICENSE")
                .country("US");
        List<HyperwalletVerificationDocument> hyperwalletDocumentList = new ArrayList<>();
        hyperwalletDocumentList.add(hyperwalletDocument);
        List<HyperwalletLink> hyperwalletUserLinks = new ArrayList<>();
        HyperwalletLink link = new HyperwalletLink();
        hyperwalletUserLinks.add(link);
        user
                .token("test-token")
                .status(HyperwalletUser.Status.ACTIVATED)
                .verificationStatus(VerificationStatus.VERIFIED)
                .businessStakeholderVerificationStatus(HyperwalletUser.BusinessStakeholderVerificationStatus.VERIFIED)
                .letterOfAuthorizationStatus(HyperwalletUser.LetterOfAuthorizationStatus.VERIFIED)
                .createdOn(new Date())
                .clientUserId("test-client-user-id")
                .profileType(HyperwalletUser.ProfileType.INDIVIDUAL)

                .businessType(HyperwalletUser.BusinessType.CORPORATION)
                .businessName("test-business-name")
                .businessOperatingName("test-business-operating-name")
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
                .governmentIdType(HyperwalletUser.GovernmentIdType.NATIONAL_ID_CARD)
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
                .programToken("test-program-token")
                .timeZone("GMT")
                .documents(hyperwalletDocumentList)
                .links(hyperwalletUserLinks);

        return user;
    }

    @Override
    protected Class<HyperwalletUser> createModelClass() {
        return HyperwalletUser.class;
    }
}
