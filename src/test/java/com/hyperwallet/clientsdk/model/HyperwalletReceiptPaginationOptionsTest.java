package com.hyperwallet.clientsdk.model;

import java.util.Date;

/**
 * @author fkrauthan
 */
public class HyperwalletReceiptPaginationOptionsTest extends BaseModelTest<HyperwalletReceiptPaginationOptions> {

    @Override
    protected HyperwalletReceiptPaginationOptions createBaseModel() {
        HyperwalletReceiptPaginationOptions options = new HyperwalletReceiptPaginationOptions();
        options
                .type(HyperwalletReceipt.Type.ADJUSTMENT)
                .createdAfter(new Date())
                .createdBefore(new Date())
                .limit(10)
                .sortBy("test-sort-by");
        return options;
    }

    @Override
    protected Class<HyperwalletReceiptPaginationOptions> createModelClass() {
        return HyperwalletReceiptPaginationOptions.class;
    }
}
