package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.model.HyperwalletTransferRefund.Status;

import java.util.Date;

public class HyperwalletTransferRefundTest extends BaseModelTest<HyperwalletTransferRefund> {

    @Override
    protected HyperwalletTransferRefund createBaseModel() {
        HyperwalletTransferRefund transferRefund = new HyperwalletTransferRefund()
                .token("token")
                .status(Status.COMPLETED)
                .clientRefundId("clientRefundId")
                .sourceToken("sourceToken")
                .sourceAmount(20.00)
                .sourceCurrency("USD")
                .destinationToken("destinationToken")
                .destinationAmount(20.00)
                .destinationCurrency("USD")
                .createdOn(new Date())
                .notes("notes")
                .memo("memo");

        return transferRefund;
    }

    @Override
    protected Class<HyperwalletTransferRefund> createModelClass() {
        return HyperwalletTransferRefund.class;
    }
}
