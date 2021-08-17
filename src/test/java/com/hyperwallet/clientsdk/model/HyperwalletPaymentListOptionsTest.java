package com.hyperwallet.clientsdk.model;

import java.util.Date;

/**
 * @author fkrauthan
 */
public class HyperwalletPaymentListOptionsTest extends BaseModelTest<HyperwalletPaymentListOptions> {

    @Override
    protected HyperwalletPaymentListOptions createBaseModel() {
        HyperwalletPaymentListOptions options = new HyperwalletPaymentListOptions();
        options
                .clientPaymentId("test-clientPaymentId")
                .releaseDate(new Date());
        return options;
    }

    @Override
    protected Class<HyperwalletPaymentListOptions> createModelClass() {
        return HyperwalletPaymentListOptions.class;
    }
}
