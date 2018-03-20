package com.hyperwallet.clientsdk.model;

import java.util.Date;

public class HyperwalletBankCardTest extends BaseModelTest<HyperwalletBankCard> {
    protected HyperwalletBankCard createBaseModel() {
        HyperwalletBankCard bankCard = new HyperwalletBankCard();
        bankCard
                .token("test-token")
                .type(HyperwalletTransferMethod.Type.PREPAID_CARD)

                .status(HyperwalletTransferMethod.Status.ACTIVATED)
                .createdOn(new Date())
                .transferMethodCountry("test-transfer-method-country")
                .transferMethodCurrency("test-transfer-method-currency")

                .cardType(HyperwalletBankCard.CardType.DEBIT)
                .cardNumber("test-card-number")
                .cardBrand(HyperwalletBankCard.Brand.VISA)

                .dateOfExpiry(new Date())
                .userToken("test-user-token");
        return bankCard;
    }

    protected Class<HyperwalletBankCard> createModelClass() {
        return HyperwalletBankCard.class;
    }

}
