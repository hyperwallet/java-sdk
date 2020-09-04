package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.model.HyperwalletTransferRefund.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HyperwalletTransferRefundTest extends BaseModelTest<HyperwalletTransferRefund> {

    @Override
    protected HyperwalletTransferRefund createBaseModel() {
        List<HyperwalletLink> hyperwalletLinkList = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLinkList.add(hyperwalletLink);
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
                .memo("memo")
                .links(hyperwalletLinkList);

        return transferRefund;
    }

    @Override
    protected Class<HyperwalletTransferRefund> createModelClass() {
        return HyperwalletTransferRefund.class;
    }
}
