package com.hyperwallet.clientsdk.model;

import java.util.Date;

/**
 * @author fkrauthan
 */
public class HyperwalletReceiptPaginationOptionsTest extends BaseModelTest<HyperwalletReceiptPaginationOptions> {
    protected HyperwalletReceiptPaginationOptions createBaseModel() {
        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .type(HyperwalletReceipt.Type.ADJUSTMENT)
                .createdAfter(new Date())
                .createdBefore(new Date())
                .limit(10)
                .offset(20)
                .sortBy("test-sort-by");
        return options;
    }

    protected Class<HyperwalletReceiptPaginationOptions> createModelClass() {
        return HyperwalletReceiptPaginationOptions.class;
    }
}
