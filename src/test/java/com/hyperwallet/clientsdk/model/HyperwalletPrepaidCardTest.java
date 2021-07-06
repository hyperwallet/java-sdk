package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.model.HyperwalletPrepaidCard.EReplacePrepaidCardReason;

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
                .type(HyperwalletPrepaidCard.Type.PREPAID_CARD)
                .status(HyperwalletPrepaidCard.Status.ACTIVATED)
                .createdOn(new Date())
                .transferMethodCountry("test-transfer-method-country")
                .transferMethodCurrency("test-transfer-method-currency")
                .cardType(HyperwalletPrepaidCard.CardType.VIRTUAL)
                .cardPackage("test-card-package")
                .cardNumber("test-card-number")
                .cardBrand(HyperwalletPrepaidCard.Brand.VISA)
                .dateOfExpiry(new Date())
                .userToken("test-user-token")
                .replacementOf("replacementOf")
                .replacementReason(EReplacePrepaidCardReason.DAMAGED)
                .links(hyperwalletLinkList);
        return prepaidCard;
    }

    protected Class<HyperwalletPrepaidCard> createModelClass() {
        return HyperwalletPrepaidCard.class;
    }

}
