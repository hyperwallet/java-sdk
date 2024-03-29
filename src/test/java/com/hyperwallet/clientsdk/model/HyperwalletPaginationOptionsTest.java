package com.hyperwallet.clientsdk.model;

import java.util.Date;

/**
 * @author fkrauthan
 */
public class HyperwalletPaginationOptionsTest extends BaseModelTest<HyperwalletPaginationOptions> {

    @Override
    protected HyperwalletPaginationOptions createBaseModel() {
        HyperwalletPaginationOptions options = new HyperwalletPaginationOptions();
        options
                .createdAfter(new Date())
                .createdBefore(new Date())
                .limit(10)
                .sortBy("test-sort-by");
        return options;
    }

    @Override
    protected Class<HyperwalletPaginationOptions> createModelClass() {
        return HyperwalletPaginationOptions.class;
    }
}
