package com.hyperwallet.clientsdk;

import com.hyperwallet.clientsdk.model.HyperwalletList;
import com.hyperwallet.clientsdk.model.HyperwalletPrepaidCard;
import com.hyperwallet.clientsdk.model.HyperwalletTransferMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HyperwalletPrepaidCardTest {

    @Test
    public void testCreatePrepaidCard() {

        HyperwalletPrepaidCard ppc = new HyperwalletPrepaidCard();
        ppc.setType(HyperwalletTransferMethod.Type.PREPAID_CARD);
        ppc.setUserToken("usr-38cfc293-8ea1-4df1-b97b-7436ec59551d");
        ppc.setToken("trm-***");
        ppc.setType(HyperwalletTransferMethod.Type.PREPAID_CARD);
        ppc.setStatus(HyperwalletTransferMethod.Status.PRE_ACTIVATED);
        ppc.setCreatedOn(new Date());
        ppc.setTransferMethodCurrency("USD");
        ppc.setTransferMethodCountry("US");
        ppc.setCardType(HyperwalletPrepaidCard.CardType.INSTANT_ISSUE);
        ppc.setCardPackage("ppc-pack");
        ppc.setCardNumber("11111-2222-3333");
        ppc.setCardBrand(HyperwalletPrepaidCard.Brand.MASTERCARD);
        ppc.setDateOfExpiry(new Date());

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.createPrepaidCard(ppc)).thenReturn(ppc);
        HyperwalletPrepaidCard prepaidCard = hw.createPrepaidCard(ppc);

        assertEquals(ppc.getToken(), prepaidCard.getToken());
        assertEquals(ppc.getType(), prepaidCard.getType());
        assertEquals(ppc.getStatus(), prepaidCard.getStatus());
        assertEquals(ppc.getCreatedOn(), prepaidCard.getCreatedOn());
        assertEquals(ppc.getTransferMethodCurrency(), prepaidCard.getTransferMethodCurrency());
        assertEquals(ppc.getTransferMethodCountry(), prepaidCard.getTransferMethodCountry());
        assertEquals(ppc.getCardType(), prepaidCard.getCardType());
        assertEquals(ppc.getCardPackage(), prepaidCard.getCardPackage());
        assertEquals(ppc.getCardNumber(), prepaidCard.getCardNumber());
        assertEquals(ppc.getCardBrand(), prepaidCard.getCardBrand());
        assertEquals(ppc.getDateOfExpiry(), prepaidCard.getDateOfExpiry());
    }

    @Test
    public void testGetPrepaidCard() {
        HyperwalletPrepaidCard ppc = new HyperwalletPrepaidCard();
        ppc.setType(HyperwalletTransferMethod.Type.PREPAID_CARD);
        ppc.setUserToken("usr-38cfc293-8ea1-4df1-b97b-7436ec59551d");
        ppc.setToken("trm-***");
        ppc.setType(HyperwalletTransferMethod.Type.PREPAID_CARD);
        ppc.setStatus(HyperwalletTransferMethod.Status.PRE_ACTIVATED);
        ppc.setCreatedOn(new Date());
        ppc.setTransferMethodCurrency("USD");
        ppc.setTransferMethodCountry("US");
        ppc.setCardType(HyperwalletPrepaidCard.CardType.INSTANT_ISSUE);
        ppc.setCardPackage("ppc-pack");
        ppc.setCardNumber("11111-2222-3333");
        ppc.setCardBrand(HyperwalletPrepaidCard.Brand.MASTERCARD);
        ppc.setDateOfExpiry(new Date());

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.getPrepaidCard("usr-token", "trm-token")).thenReturn(ppc);
        HyperwalletPrepaidCard prepaidCard = hw.getPrepaidCard("usr-token", "trm-token");

        assertEquals(ppc.getToken(), prepaidCard.getToken());
        assertEquals(ppc.getType(), prepaidCard.getType());
        assertEquals(ppc.getStatus(), prepaidCard.getStatus());
        assertEquals(ppc.getCreatedOn(), prepaidCard.getCreatedOn());
        assertEquals(ppc.getTransferMethodCurrency(), prepaidCard.getTransferMethodCurrency());
        assertEquals(ppc.getTransferMethodCountry(), prepaidCard.getTransferMethodCountry());
        assertEquals(ppc.getCardType(), prepaidCard.getCardType());
        assertEquals(ppc.getCardPackage(), prepaidCard.getCardPackage());
        assertEquals(ppc.getCardNumber(), prepaidCard.getCardNumber());
        assertEquals(ppc.getCardBrand(), prepaidCard.getCardBrand());
        assertEquals(ppc.getDateOfExpiry(), prepaidCard.getDateOfExpiry());
    }

    @Test
    public void testGetPrepaidCardList() {

        HyperwalletPrepaidCard ppc = new HyperwalletPrepaidCard();
        ppc.setType(HyperwalletTransferMethod.Type.PREPAID_CARD);
        ppc.setUserToken("usr-38cfc293-8ea1-4df1-b97b-7436ec59551d");
        ppc.setToken("trm-***");
        ppc.setType(HyperwalletTransferMethod.Type.PREPAID_CARD);
        ppc.setStatus(HyperwalletTransferMethod.Status.PRE_ACTIVATED);
        ppc.setCreatedOn(new Date());
        ppc.setTransferMethodCurrency("USD");
        ppc.setTransferMethodCountry("US");
        ppc.setCardType(HyperwalletPrepaidCard.CardType.INSTANT_ISSUE);
        ppc.setCardPackage("ppc-pack");
        ppc.setCardNumber("11111-2222-3333");
        ppc.setCardBrand(HyperwalletPrepaidCard.Brand.MASTERCARD);
        ppc.setDateOfExpiry(new Date());

        HyperwalletList<HyperwalletPrepaidCard> list = new HyperwalletList<HyperwalletPrepaidCard>();
        list.data = new ArrayList<HyperwalletPrepaidCard>();
        list.data.add(ppc);

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.listPrepaidCards("usr-token")).thenReturn(list);

        HyperwalletList<HyperwalletPrepaidCard> resultList = hw.listPrepaidCards("usr-token");
        assertNotNull(resultList);
        assertNotNull(resultList.data);
        assertFalse(resultList.data.isEmpty());

        HyperwalletPrepaidCard prepaidCard = resultList.data.get(0);
        assertEquals(ppc.getToken(), prepaidCard.getToken());
        assertEquals(ppc.getType(), prepaidCard.getType());
        assertEquals(ppc.getStatus(), prepaidCard.getStatus());
        assertEquals(ppc.getCreatedOn(), prepaidCard.getCreatedOn());
        assertEquals(ppc.getTransferMethodCurrency(), prepaidCard.getTransferMethodCurrency());
        assertEquals(ppc.getTransferMethodCountry(), prepaidCard.getTransferMethodCountry());
        assertEquals(ppc.getCardType(), prepaidCard.getCardType());
        assertEquals(ppc.getCardPackage(), prepaidCard.getCardPackage());
        assertEquals(ppc.getCardNumber(), prepaidCard.getCardNumber());
        assertEquals(ppc.getCardBrand(), prepaidCard.getCardBrand());
        assertEquals(ppc.getDateOfExpiry(), prepaidCard.getDateOfExpiry());
    }

}
