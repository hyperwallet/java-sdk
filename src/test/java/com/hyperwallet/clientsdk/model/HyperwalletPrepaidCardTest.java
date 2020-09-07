package com.hyperwallet.clientsdk.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fkrauthan
 */
public class HyperwalletPrepaidCardTest extends BaseModelTest<HyperwalletPrepaidCard> {
    protected HyperwalletPrepaidCard createBaseModel() {
        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard();
        List<HyperwalletLink> hyperwalletLinkList = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLinkList.add(hyperwalletLink);
        prepaidCard
                .token("test-token")
                .type(HyperwalletTransferMethod.Type.PREPAID_CARD)

                .status(HyperwalletTransferMethod.Status.ACTIVATED)
                .createdOn(new Date())
                .transferMethodCountry("test-transfer-method-country")
                .transferMethodCurrency("test-transfer-method-currency")

                .cardType(HyperwalletPrepaidCard.CardType.VIRTUAL)
                .cardPackage("test-card-package")
                .cardNumber("test-card-number")
                .cardBrand(HyperwalletPrepaidCard.Brand.VISA)

                .dateOfExpiry(new Date())
                .userToken("test-user-token")
                .links(hyperwalletLinkList);
        return prepaidCard;
    }

    protected Class<HyperwalletPrepaidCard> createModelClass() {
        return HyperwalletPrepaidCard.class;
    }

}
