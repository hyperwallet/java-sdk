package com.hyperwallet.clientsdk.model;

/**
 * @author fkrauthan
 */
public class HyperwalletPaymentListOptionsTest extends BaseModelTest<HyperwalletPaymentListOptions> {

    @Override
    protected HyperwalletPaymentListOptions createBaseModel() {
        HyperwalletPaymentListOptions options = new HyperwalletPaymentListOptions();
        options
                .clientPaymentId("test-clientPaymentId");

        return options;
    }

    @Override
    protected Class<HyperwalletPaymentListOptions> createModelClass() {
        return HyperwalletPaymentListOptions.class;
    }
}
