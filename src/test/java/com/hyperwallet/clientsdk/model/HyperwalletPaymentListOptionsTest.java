package com.hyperwallet.clientsdk.model;

import java.util.Date;

/**
 * @author fkrauthan
 */
public class HyperwalletPaymentListOptionsTest extends BaseModelTest<HyperwalletPaymentListOptions> {
    protected HyperwalletPaymentListOptions createBaseModel() {
        HyperwalletPaymentListOptions options = new HyperwalletPaymentListOptions();
        options
                .currency("test-currency")
                .releasedOn(new Date());
        return options;
    }

    protected Class<HyperwalletPaymentListOptions> createModelClass() {
        return HyperwalletPaymentListOptions.class;
    }
}
