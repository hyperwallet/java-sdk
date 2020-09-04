package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.model.HyperwalletBusinessStakeholder.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HyperwalletBusinessStakeholderTest extends BaseModelTest<HyperwalletBusinessStakeholder> {

    protected HyperwalletBusinessStakeholder createBaseModel() {

        HyperwalletBusinessStakeholder stakeholder = new HyperwalletBusinessStakeholder();
        HyperwalletVerificationDocument hyperWalletVerificationDocument = new HyperwalletVerificationDocument();
        hyperWalletVerificationDocument.category("IDENTIFICATION").type("DRIVERS_LICENSE").status("NEW").country("AL");
        List<HyperwalletVerificationDocument> hyperwalletVerificationDocumentList = new ArrayList<>();
        hyperwalletVerificationDocumentList.add(hyperWalletVerificationDocument);
        stakeholder
                .token("test-token")
                .isBusinessContact(true)
                .isDirector(true)
                .isUltimateBeneficialOwner(true)
                .isSeniorManagingOfficial(true)
                .status(Status.ACTIVATED)
                .verificationStatus(VerificationStatus.REQUIRED)
                .createdOn(new Date())
                .profileType(ProfileType.INDIVIDUAL)
                .firstName("TestEE")
                .middleName("EE")
                .lastName("TestEE")
                .dateOfBirth(new Date())
                .countryOfBirth("US")
                .countryOfNationality("US")
                .gender(Gender.FEMALE)
                .phoneNumber("3103322333")
                .mobileNumber("31052341241")
                .email("swerw@sefrew.com")
                .governmentId("345232")
                .governmentIdType(GovernmentIdType.PASSPORT)
                .driversLicenseId("12345")
                .addressLine1("456 Main St")
                .addressLine2("45 Maine")
                .city("San Jose")
                .stateProvince("CA")
                .country("US")
                .postalCode("22222")
                .documents(hyperwalletVerificationDocumentList);
        ;

        return stakeholder;
    }

    protected Class<HyperwalletBusinessStakeholder> createModelClass() {
        return HyperwalletBusinessStakeholder.class;
    }
}

