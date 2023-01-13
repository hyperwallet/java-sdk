package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletBankCardTest extends BaseModelTest<HyperwalletBankCard> {
    protected HyperwalletBankCard createBaseModel() {
        HyperwalletBankCard bankCard = new HyperwalletBankCard();
        bankCard
                .token("test-token")
                .type(HyperwalletBankCard.Type.BANK_CARD)
                .status(HyperwalletBankCard.Status.ACTIVATED)
                .transition(HyperwalletBankCard.Status.ACTIVATED)
                .createdOn(new Date())
                .transferMethodCountry("test-transfer-method-country")
                .transferMethodCurrency("test-transfer-method-currency")
                .cardType(HyperwalletBankCard.CardType.DEBIT)
                .cardNumber("test-card-number")
                .cardBrand(HyperwalletBankCard.Brand.VISA)
                .processingTime("30 mins")
                .dateOfExpiry(new Date())
                .cvv("cvv")
                .userToken("test-user-token")
                .isDefaultTransferMethod(Boolean.TRUE);
        return bankCard;
    }

    protected Class<HyperwalletBankCard> createModelClass() {
        return HyperwalletBankCard.class;
    }

}
