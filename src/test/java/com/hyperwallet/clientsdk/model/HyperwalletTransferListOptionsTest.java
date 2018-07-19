package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletTransferListOptionsTest extends BaseModelTest<HyperwalletTransferListOptions> {
    protected HyperwalletTransferListOptions createBaseModel() {
        HyperwalletTransferListOptions options = new HyperwalletTransferListOptions();
        options
                .sourceToken("source-token")
                .destinationToken("destination-token")
                .createdAfter(new Date())
                .createdBefore(new Date())
                .limit(10)
                .offset(20)
                .sortBy("test-sort-by");
        return options;
    }

    protected Class<HyperwalletTransferListOptions> createModelClass() {
        return HyperwalletTransferListOptions.class;
    }
}