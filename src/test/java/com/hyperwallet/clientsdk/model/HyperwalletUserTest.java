package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.model.DocumentVerificationDocumentsRepresentation.ECountryCode;
import com.hyperwallet.clientsdk.model.DocumentVerificationDocumentsRepresentation.EDocumentCategory;
import com.hyperwallet.clientsdk.model.DocumentVerificationDocumentsRepresentation.EIdentityVerificationType;
import com.hyperwallet.clientsdk.model.DocumentVerificationDocumentsRepresentation.EKycDocumentVerificationStatus;
import com.hyperwallet.clientsdk.model.HyperwalletUser.VerificationStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fkrauthan
 */
public class HyperwalletUserTest extends BaseModelTest<HyperwalletUser> {

    protected HyperwalletUser createBaseModel() {
        HyperwalletUser user = new HyperwalletUser();
        DocumentVerificationDocumentsRepresentation documentVerificationDocumentsRepresentation = new DocumentVerificationDocumentsRepresentation();
        documentVerificationDocumentsRepresentation.category(EDocumentCategory.AUTHORIZATION).type(EIdentityVerificationType.LETTER_OF_AUTHORIZATION)
                .country(ECountryCode.CA).status(EKycDocumentVerificationStatus.NEW);
        List<DocumentVerificationDocumentsRepresentation> documentVerificationDocumentsRepresentationList = new ArrayList<>();
        documentVerificationDocumentsRepresentationList.add(documentVerificationDocumentsRepresentation);
        user
                .token("test-token")
                .status(HyperwalletUser.Status.ACTIVATED)
                .verificationStatus(VerificationStatus.VERIFIED)
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
                .documents(documentVerificationDocumentsRepresentationList);

        return user;
    }

    @Override
    protected Class<HyperwalletUser> createModelClass() {
        return HyperwalletUser.class;
    }
}
