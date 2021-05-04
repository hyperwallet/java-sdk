package com.hyperwallet.clientsdk.model;

public class HyperwalletUsersListPaginationOptionsTest extends BaseModelTest<HyperwalletUsersListPaginationOptions> {
    protected HyperwalletUsersListPaginationOptions createBaseModel() {
        HyperwalletUsersListPaginationOptions options = new HyperwalletUsersListPaginationOptions();
        options
                .clientUserId("test-client-id")
                .email("test@test.com")
                .programToken("test-prg-token")
                .status(HyperwalletUser.Status.ACTIVATED)
                .verificationStatus(HyperwalletUser.VerificationStatus.REQUIRED);

        return options;
    }

    protected Class<HyperwalletUsersListPaginationOptions> createModelClass() {
        return HyperwalletUsersListPaginationOptions.class;
    }
}

