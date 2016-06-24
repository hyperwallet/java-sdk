package com.hyperwallet.clientsdk.model;

/**
 * @author fkrauthan
 */
public class HyperwalletBalanceListOptionsTest extends BaseModelTest<HyperwalletBalanceListOptions> {
    protected HyperwalletBalanceListOptions createBaseModel() {
        HyperwalletBalanceListOptions options = new HyperwalletBalanceListOptions();
        options
                .currency("test-currency")
                .limit(10)
                .offset(20)
                .sortBy("test-sort-by");
        return options;
    }

    protected Class<HyperwalletBalanceListOptions> createModelClass() {
        return HyperwalletBalanceListOptions.class;
    }
}
