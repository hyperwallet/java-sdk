
package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.model.HyperwalletTransfer.ForeignExchange;
import com.hyperwallet.clientsdk.model.HyperwalletTransferRefund.Status;

import java.util.Collections;
import java.util.Date;

public class HyperwalletTransferRefundTest extends BaseModelTest<HyperwalletTransferRefund> {

    @Override
    protected HyperwalletTransferRefund createBaseModel() {
        ForeignExchange foreignExchange = new ForeignExchange();
        foreignExchange.setSourceAmount("200.0");
        foreignExchange.setSourceCurrency("USD");
        foreignExchange.setDestinationAmount("100.0");
        foreignExchange.setDestinationCurrency("CAD");
        foreignExchange.setRate("2.3");
        HyperwalletTransferRefund transferRefund = new HyperwalletTransferRefund()
                .token("token")
                .status(Status.COMPLETED)
                .clientRefundId("clientRefundId")
                .sourceToken("sourceToken")
                .sourceAmount("20.00")
                .sourceCurrency("USD")
                .destinationToken("destinationToken")
                .destinationAmount("20.00")
                .destinationCurrency("USD")
                .createdOn(new Date())
                .notes("notes")
                .memo("memo")
                .foreignExchanges(Collections.singletonList(foreignExchange));

        return transferRefund;
    }

    @Override
    protected Class<HyperwalletTransferRefund> createModelClass() {
        return HyperwalletTransferRefund.class;
    }
}
