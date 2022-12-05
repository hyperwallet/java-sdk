package com.hyperwallet.clientsdk.model;

import java.util.Date;

/**
 * @author fkrauthan
 */
public class HyperwalletPrepaidCardTest extends BaseModelTest<HyperwalletPrepaidCard> {
    protected HyperwalletPrepaidCard createBaseModel() {
        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard();
        prepaidCard
                .token("test-token")
                .type(HyperwalletPrepaidCard.Type.PREPAID_CARD)
                .status(HyperwalletPrepaidCard.Status.ACTIVATED)
                .transition(HyperwalletPrepaidCard.Status.ACTIVATED)
                .createdOn(new Date())
                .transferMethodCountry("test-transfer-method-country")
                .transferMethodCurrency("test-transfer-method-currency")
                .cardType(HyperwalletPrepaidCard.CardType.VIRTUAL)
                .cardPackage("test-card-package")
                .cardNumber("test-card-number")
                .cardBrand(HyperwalletPrepaidCard.Brand.VISA)
                .isDefaultTransferMethod(Boolean.TRUE)
                .dateOfExpiry(new Date())
                .userToken("test-user-token");
        return prepaidCard;
    }

    protected Class<HyperwalletPrepaidCard> createModelClass() {
        return HyperwalletPrepaidCard.class;
    }

}
