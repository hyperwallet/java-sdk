package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.model.HyperwalletTransferMethod.Status;
import com.hyperwallet.clientsdk.model.HyperwalletTransferMethod.Type;

import java.util.Date;

public class HyperwalletTransferMethodListOptionsTest extends BaseModelTest<HyperwalletTransferMethodListOptions> {
    protected HyperwalletTransferMethodListOptions createBaseModel() {
        HyperwalletTransferMethodListOptions options = new HyperwalletTransferMethodListOptions();
        options.status(Status.ACTIVATED)
                .type(Type.PREPAID_CARD)
                .createdAfter(new Date())
                .createdBefore(new Date())
                .limit(10)
                .sortBy("test-sort-by");
        return options;
    };


    protected Class<HyperwalletTransferMethodListOptions> createModelClass() { return HyperwalletTransferMethodListOptions.class ; };

}
