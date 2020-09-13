package com.hyperwallet.clientsdk.model;

/**
 * @author fkrauthan
 */
public class HyperwalletListPaginationOptionsTest extends BaseModelTest<HyperwalletListPaginationOptions> {
    protected HyperwalletListPaginationOptions createBaseModel() {
        HyperwalletListPaginationOptions options = new HyperwalletListPaginationOptions();
        options
                .status(HyperwalletTransferMethod.Status.ACTIVATED);

        return options;
    }

    protected Class<HyperwalletListPaginationOptions> createModelClass() {
        return HyperwalletListPaginationOptions.class;
    }
}

