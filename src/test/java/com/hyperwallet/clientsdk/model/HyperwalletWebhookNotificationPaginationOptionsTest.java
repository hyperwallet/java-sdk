package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletWebhookNotificationPaginationOptionsTest extends BaseModelTest<HyperwalletWebhookNotificationPaginationOptions> {
    protected HyperwalletWebhookNotificationPaginationOptions createBaseModel() {
        HyperwalletWebhookNotificationPaginationOptions options = new HyperwalletWebhookNotificationPaginationOptions();
        options
                .type(HyperwalletWebhookNotification.Type.PAPER_CHECK_CREATED.toString())
                .programToken("program-token")
                .createdAfter(new Date())
                .createdBefore(new Date())
                .limit(10)
                .sortBy("test-sort-by");
        return options;
    }

    protected Class<HyperwalletWebhookNotificationPaginationOptions> createModelClass() {
        return HyperwalletWebhookNotificationPaginationOptions.class;
    }
}
