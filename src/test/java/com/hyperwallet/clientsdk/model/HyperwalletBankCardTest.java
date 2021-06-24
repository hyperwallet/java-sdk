package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.model.HyperwalletBankCard.Status;
import com.hyperwallet.clientsdk.model.HyperwalletBankCard.Type;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HyperwalletBankCardTest extends BaseModelTest<HyperwalletBankCard> {
    protected HyperwalletBankCard createBaseModel() {
        List<HyperwalletLink> hyperwalletLinkList = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLinkList.add(hyperwalletLink);
        HyperwalletBankCard bankCard = new HyperwalletBankCard();
        bankCard
                .token("test-token")
                .type(Type.BANK_CARD)
                .status(Status.ACTIVATED)
                .createdOn(new Date())
                .transferMethodCountry("test-transfer-method-country")
                .transferMethodCurrency("test-transfer-method-currency")

                .cardType(HyperwalletBankCard.CardType.DEBIT)
                .cardNumber("test-card-number")
                .cardBrand(HyperwalletBankCard.Brand.VISA)

                .dateOfExpiry(new Date())
                .cvv("cvv")
                .processingTime("processing-time")
                .links(hyperwalletLinkList)
                .userToken("test-user-token");
        return bankCard;
    }

    protected Class<HyperwalletBankCard> createModelClass() {
        return HyperwalletBankCard.class;
    }

}
