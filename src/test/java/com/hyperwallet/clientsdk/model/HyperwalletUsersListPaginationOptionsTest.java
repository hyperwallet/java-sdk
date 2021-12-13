package com.hyperwallet.clientsdk.model;
import com.hyperwallet.clientsdk.model.HyperwalletUser.TaxVerificationStatus;

public class HyperwalletUsersListPaginationOptionsTest extends BaseModelTest<HyperwalletUsersListPaginationOptions> {
    protected HyperwalletUsersListPaginationOptions createBaseModel() {
        HyperwalletUsersListPaginationOptions options = new HyperwalletUsersListPaginationOptions();
        options
                .clientUserId("test-client-id")
                .email("test@test.com")
                .programToken("test-prg-token")
                .status(HyperwalletUser.Status.ACTIVATED)
                .transition(HyperwalletUser.Status.ACTIVATED)
                .verificationStatus(HyperwalletUser.VerificationStatus.REQUIRED)
                .taxVerificationStatus(TaxVerificationStatus.REQUIRED);

        return options;
    }

    protected Class<HyperwalletUsersListPaginationOptions> createModelClass() {
        return HyperwalletUsersListPaginationOptions.class;
    }
}

