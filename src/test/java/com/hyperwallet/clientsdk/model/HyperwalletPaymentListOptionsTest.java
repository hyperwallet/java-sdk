package com.hyperwallet.clientsdk.model;

import java.util.Date;

/**
 * @author fkrauthan
 */
public class HyperwalletPaymentListOptionsTest extends BaseModelTest<HyperwalletPaymentListOptions> {
    protected HyperwalletPaymentListOptions createBaseModel() {
        HyperwalletPaymentListOptions options = new HyperwalletPaymentListOptions();
        options.clientPaymentId("gv47LDuf")
                .currency("test-currency")
                .memo("test-memo")
                .releaseDate(new Date());
        return options;
    }

    protected Class<HyperwalletPaymentListOptions> createModelClass() {
        return HyperwalletPaymentListOptions.class;
    }
}
