package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletTransferRefundListOptionsTest extends BaseModelTest<HyperwalletTransferRefundListOptions> {

    protected HyperwalletTransferRefundListOptions createBaseModel() {
        HyperwalletTransferRefundListOptions options = new HyperwalletTransferRefundListOptions();
        options
                .sourceToken("source-token")
                .clientRefundId("clientRefundId")
                .status("COMPLETED")
                .createdAfter(new Date())
                .createdBefore(new Date())
                .limit(10)
                .sortBy("test-sort-by");
        return options;
    }

    protected Class<HyperwalletTransferRefundListOptions> createModelClass() {
        return HyperwalletTransferRefundListOptions.class;
    }
}