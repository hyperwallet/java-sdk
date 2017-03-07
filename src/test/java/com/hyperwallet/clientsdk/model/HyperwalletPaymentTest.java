package com.hyperwallet.clientsdk.model;

import java.util.Date;

/**
 * @author fkrauthan
 */
public class HyperwalletPaymentTest extends BaseModelTest<HyperwalletPayment> {
    protected HyperwalletPayment createBaseModel() {
        HyperwalletPayment payment = new HyperwalletPayment();
        payment
                .token("test-token")
                .createdOn(new Date())
                .amount(15.99)
                .currency("test-currency")

                .memo("test-memo")
                .notes("test-note")

                .purpose("test-purpose")
                .releaseOn(new Date())

                .destinationToken("test-destination-token")
                .programToken("test-program-token")
                .clientPaymentId("test-client-payment-id");
        return payment;
    }

    protected Class<HyperwalletPayment> createModelClass() {
        return HyperwalletPayment.class;
    }
}
