package com.hyperwallet.clientsdk.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HyperwalletTransferTest extends BaseModelTest<HyperwalletTransfer> {
    protected HyperwalletTransfer createBaseModel() {
        HyperwalletTransfer transfer = new HyperwalletTransfer();
        HyperwalletTransfer.ForeignExchange foreignExchange = new HyperwalletTransfer.ForeignExchange();
        foreignExchange.setSourceAmount("200.00");
        foreignExchange.setSourceCurrency("USD");
        foreignExchange.setDestinationAmount("100.00");
        foreignExchange.setDestinationCurrency("USD");
        foreignExchange.setRate("2.3");
        List<HyperwalletLink> hyperwalletLinkList = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLinkList.add(hyperwalletLink);
        transfer
                .token("test-token")
                .status(HyperwalletTransfer.Status.QUOTED)
                .createdOn(new Date())
                .clientTransferId("client-transfer-id")
                .sourceToken("source-token")
                .sourceAmount("200.0")
                .sourceFeeAmount("1.4")
                .sourceCurrency("source-currency")
                .destinationToken("destination-token")
                .destinationAmount("100.0")
                .destinationFeeAmount("2.3")
                .destinationCurrency("USD")
                .notes("notes")
                .memo("memo")
                .expiresOn(new Date())
                .links(hyperwalletLinkList);
        transfer.setForeignExchanges(Collections.singletonList(foreignExchange));
        return transfer;
    }

    protected Class<HyperwalletTransfer> createModelClass() {
        return HyperwalletTransfer.class;
    }
}