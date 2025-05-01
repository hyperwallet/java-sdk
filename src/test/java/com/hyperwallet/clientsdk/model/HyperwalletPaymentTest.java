package com.hyperwallet.clientsdk.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fkrauthan
 */
public class HyperwalletPaymentTest extends BaseModelTest<HyperwalletPayment> {

    protected HyperwalletPayment createBaseModel() {
        List<HyperwalletLink> hyperwalletLinkList = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLinkList.add(hyperwalletLink);
        HyperwalletPayment payment = new HyperwalletPayment();
        payment
                .status("COMPLETED")
                .token("test-token")
                .reasonCode("PAYEE_ACCOUNT_LIMITATION")
                .createdOn(new Date())
                .amount("15.99")
                .currency("test-currency")
                .memo("test-memo")
                .notes("test-note")
                .purpose("test-purpose")
                .releaseOn(new Date())
                .destinationToken("test-destination-token")
                .programToken("test-program-token")
                .clientPaymentId("test-client-payment-id")
                .links(hyperwalletLinkList)
                .expiresOn(new Date());
        return payment;
    }

    protected Class<HyperwalletPayment> createModelClass() {
        return HyperwalletPayment.class;
    }
}
