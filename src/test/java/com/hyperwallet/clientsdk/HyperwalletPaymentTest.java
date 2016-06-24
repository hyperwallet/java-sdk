package com.hyperwallet.clientsdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.model.HyperwalletList;
import com.hyperwallet.clientsdk.model.HyperwalletPayment;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HyperwalletPaymentTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testModelBuilder() {
        HyperwalletPayment payment = new HyperwalletPayment();
        payment.token("").createdOn(new Date()).amount(10.0).currency("CAD").description("")
                .memo("").purpose("").releaseOn(new Date()).destinationToken("").programToken("")
                .clientPaymentId("");

        Field[] fields = HyperwalletPayment.class.getDeclaredFields();
        Set<String> inclusions = payment.getInclusions();

        assertEquals(fields.length, inclusions.size());

        for (Field f : fields) {
            assertTrue(inclusions.contains(f.getName()));
        }

        payment.setDescription(null);
        assertFalse(inclusions.contains("description"));
        assertTrue(inclusions.size() == fields.length - 1);
    }

    @Test
    public void testModelBean() {
        HyperwalletPayment payment = new HyperwalletPayment();
        payment.setToken("");
        payment.setCreatedOn(new Date());
        payment.setAmount(10.5);
        payment.setCurrency("USD");
        payment.setDescription("");
        payment.setMemo("");
        payment.setPurpose("");
        payment.setReleaseOn(new Date());
        payment.setDestinationToken("");
        payment.setProgramToken("");
        payment.setClientPaymentId("");

        Field[] fields = HyperwalletPayment.class.getDeclaredFields();
        Set<String> inclusions = payment.getInclusions();

        assertEquals(fields.length, inclusions.size());

        for (Field f : fields) {
            assertTrue(inclusions.contains(f.getName()));
        }

        payment.setDescription(null);
        assertFalse(inclusions.contains("description"));
        assertTrue(inclusions.size() == fields.length - 1);
    }

    @Test
    public void testFailedCreatePaymentNull() {
        thrown.expect(HyperwalletException.class);
        new Hyperwallet("d", "d").createPayment(null);
    }

    @Test
    public void testFailedCreatePaymentNoToken() {
        thrown.expect(HyperwalletException.class);
        HyperwalletPayment payment = new HyperwalletPayment();
        new Hyperwallet("d", "d").createPayment(payment);
    }

    @Test
    public void testFailedGetPaymentTokenNull() {
        thrown.expect(HyperwalletException.class);
        new Hyperwallet("d", "d").getPayment(null);
    }

    @Test
    public void testFailedGetPaymentTokenEmpty() {
        thrown.expect(HyperwalletException.class);
        new Hyperwallet("d", "d").getPayment("");
    }

    @Test
    public void testCreatePayment() {
        HyperwalletPayment payment = new HyperwalletPayment();
        payment.clientPaymentId("clientId")
                .amount(10.5).currency("USD")
                .description("Earnings for 2016 Q1")
                .memo("PmtBatch-20160501")
                .purpose("OTHER")
                .destinationToken("trm-token")
                .programToken("prg-token").setCreatedOn(new Date());

        HyperwalletApiClient client = mock(HyperwalletApiClient.class);
        Hyperwallet hw = new Hyperwallet("username", "password");
        hw.setClientService(client);

        when(client.post(anyString(), anyObject(), any(Class.class))).thenReturn(payment);
        when(client.copy(payment)).thenReturn(payment);
        HyperwalletPayment processed = hw.createPayment(payment);

        assertNotNull(processed);
        assertEquals(payment.getClientPaymentId(), processed.getClientPaymentId());
        assertEquals(payment.getAmount(), processed.getAmount());
        assertEquals(payment.getDescription(), processed.getDescription());
        assertEquals(payment.getMemo(), processed.getMemo());
        assertEquals(payment.getPurpose(), processed.getPurpose());
        assertEquals(payment.getDestinationToken(), processed.getDestinationToken());
        assertEquals(payment.getProgramToken(), processed.getProgramToken());
    }

    @Test
    public void testGetPayment() {
        HyperwalletPayment payment = new HyperwalletPayment();
        payment.clientPaymentId("clientId")
                .amount(10.5).currency("USD")
                .description("Earnings for 2016 Q1")
                .memo("PmtBatch-20160501")
                .purpose("OTHER")
                .destinationToken("trm-token")
                .programToken("prg-token");

        HyperwalletApiClient client = mock(HyperwalletApiClient.class);
        Hyperwallet hw = new Hyperwallet("username", "password");
        hw.setClientService(client);
        when(client.get(anyString(), any(Class.class))).thenReturn(payment);
        HyperwalletPayment processed = hw.getPayment("pmt-token");

        assertNotNull(processed);
        assertEquals(payment.getClientPaymentId(), processed.getClientPaymentId());
        assertEquals(payment.getAmount(), processed.getAmount());
        assertEquals(payment.getDescription(), processed.getDescription());
        assertEquals(payment.getMemo(), processed.getMemo());
        assertEquals(payment.getPurpose(), processed.getPurpose());
        assertEquals(payment.getDestinationToken(), processed.getDestinationToken());
        assertEquals(payment.getProgramToken(), processed.getProgramToken());
    }

    @Test
    public void testListPayment() {
        HyperwalletPayment payment = new HyperwalletPayment();
        payment.clientPaymentId("clientId")
                .amount(10.5).currency("USD")
                .description("Earnings for 2016 Q1")
                .memo("PmtBatch-20160501")
                .purpose("OTHER")
                .destinationToken("trm-token")
                .programToken("prg-token");

        HyperwalletList<HyperwalletPayment> paymentList = new HyperwalletList<HyperwalletPayment>();
        paymentList.data = new ArrayList<HyperwalletPayment>();
        paymentList.data.add(payment);

        HyperwalletApiClient client = mock(HyperwalletApiClient.class);
        Hyperwallet hw = new Hyperwallet("username", "password");
        hw.setClientService(client);

        when(client.get(anyString(), any(TypeReference.class))).thenReturn(paymentList);
        HyperwalletList<HyperwalletPayment> list = hw.listPayments();

        assertNotNull(list);
        assertTrue(list.data.size() > 0);

        HyperwalletPayment processed = list.data.get(0);

        assertEquals(payment.getClientPaymentId(), processed.getClientPaymentId());
        assertEquals(payment.getAmount(), processed.getAmount());
        assertEquals(payment.getDescription(), processed.getDescription());
        assertEquals(payment.getMemo(), processed.getMemo());
        assertEquals(payment.getPurpose(), processed.getPurpose());
        assertEquals(payment.getDestinationToken(), processed.getDestinationToken());
        assertEquals(payment.getProgramToken(), processed.getProgramToken());
    }
}
