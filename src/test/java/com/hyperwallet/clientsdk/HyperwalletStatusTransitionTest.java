package com.hyperwallet.clientsdk;

import com.hyperwallet.clientsdk.model.HyperwalletBankAccount;
import com.hyperwallet.clientsdk.model.HyperwalletList;
import com.hyperwallet.clientsdk.model.HyperwalletStatusTransition;
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
public class HyperwalletStatusTransitionTest {

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void testFaildCreatePrepaidCardStatusTransitionUserToken() {
        thrown.expect(HyperwalletException.class);
        new Hyperwallet("d", "d").createPrepaidCardStatusTransition("", "d",
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNLLOCKED));
    }

    @Test
    public void testFaildCreatePrepaidCardStatusTransitionUserPrepaidCardToken() {
        thrown.expect(HyperwalletException.class);
        new Hyperwallet("d", "d").createPrepaidCardStatusTransition("d", "",
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNLLOCKED));
    }

    @Test
    public void testFaildCreatePrepaidCardStatusTransitionUserTransition() {
        thrown.expect(HyperwalletException.class);
        new Hyperwallet("d", "d").createPrepaidCardStatusTransition("d", "d", null);
    }

    @Test
    public void testCreatePrepaidCardStatusTransition() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNLLOCKED);
        transition.setNotes("Activated prepaid card");
        transition.setFromStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.ACTIVATED);

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.createPrepaidCardStatusTransition("user-token", "trm-token", transition)).thenReturn(transition);

        HyperwalletStatusTransition newTrans = hw.createPrepaidCardStatusTransition("user-token", "trm-token", transition);

        assertNotNull(newTrans);
        assertEquals(transition.getNotes(), newTrans.getNotes());
        assertEquals(transition.getFromStatus(), newTrans.getFromStatus());
        assertEquals(transition.getToStatus(), newTrans.getToStatus());
        assertEquals(transition.getTransition(), newTrans.getTransition());
    }

    @Test
    public void testGetPrepaidCardStatusTransition() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNLLOCKED);
        transition.setNotes("Activated prepaid card");
        transition.setFromStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToken("sts-token");

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.getPrepaidCardStatusTransition("user-token", "trm-token", "sts-token")).thenReturn(transition);

        HyperwalletStatusTransition newTrans = hw.getPrepaidCardStatusTransition("user-token", "trm-token", "sts-token");

        assertNotNull(newTrans);
        assertEquals(transition.getNotes(), newTrans.getNotes());
        assertEquals(transition.getFromStatus(), newTrans.getFromStatus());
        assertEquals(transition.getToStatus(), newTrans.getToStatus());
        assertEquals(transition.getTransition(), newTrans.getTransition());
        assertEquals(transition.getToken(), newTrans.getToken());
    }

    @Test
    public void testListPrepaidCardStatusTransition() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNLLOCKED);
        transition.setNotes("Activated prepaid card");
        transition.setFromStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToken("sts-token");

        HyperwalletList<HyperwalletStatusTransition> list = new HyperwalletList<HyperwalletStatusTransition>();
        list.data = new ArrayList<HyperwalletStatusTransition>();
        list.data.add(transition);

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.listPrepaidCardStatusTransitions("user-token", "trm-token")).thenReturn(list);

        HyperwalletList<HyperwalletStatusTransition> retList = hw.listPrepaidCardStatusTransitions("user-token", "trm-token");

        assertNotNull(retList);
        assertTrue(retList.data.size() > 0);

        HyperwalletStatusTransition newTrans = retList.data.get(0);

        assertEquals(transition.getNotes(), newTrans.getNotes());
        assertEquals(transition.getFromStatus(), newTrans.getFromStatus());
        assertEquals(transition.getToStatus(), newTrans.getToStatus());
        assertEquals(transition.getTransition(), newTrans.getTransition());
        assertEquals(transition.getToken(), newTrans.getToken());
    }

    public void testFaildCreateBankAccountStatusTransitionUserToken() {
        thrown.expect(HyperwalletException.class);
        new Hyperwallet("d", "d").createBankAccountStatusTransition("", "d",
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNLLOCKED));
    }

    @Test
    public void testFaildCreateBankAccountStatusTransitionUserBankAccountToken() {
        thrown.expect(HyperwalletException.class);
        new Hyperwallet("d", "d").createBankAccountStatusTransition("d", "",
                new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNLLOCKED));
    }

    @Test
    public void testFaildCreateBankAccountStatusTransitionUserTransition() {
        thrown.expect(HyperwalletException.class);
        new Hyperwallet("d", "d").createBankAccountStatusTransition("d", "d", null);
    }

    @Test
    public void testCreateBankAccountStatusTransition() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNLLOCKED);
        transition.setNotes("Activated bank account card");
        transition.setFromStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToken("trm-token");

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.createBankAccountStatusTransition("usr-token", "trm-token", transition)).thenReturn(transition);
        HyperwalletStatusTransition newTrans = hw.createBankAccountStatusTransition("usr-token", "trm-token", transition);

        assertEquals(transition.getNotes(), newTrans.getNotes());
        assertEquals(transition.getFromStatus(), newTrans.getFromStatus());
        assertEquals(transition.getToStatus(), newTrans.getToStatus());
        assertEquals(transition.getTransition(), newTrans.getTransition());
        assertEquals(transition.getToken(), newTrans.getToken());
    }

    @Test
    public void testCreateBankAccountStatusObjectTransition() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNLLOCKED);
        transition.setNotes("Activated bank account card");
        transition.setFromStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToken("trm-token");

        HyperwalletBankAccount bank = new HyperwalletBankAccount();
        bank.setToken("trm-token");

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.createBankAccountStatusTransition("usr-token", bank, transition)).thenReturn(transition);
        HyperwalletStatusTransition newTrans = hw.createBankAccountStatusTransition("usr-token", bank, transition);

        assertEquals(transition.getNotes(), newTrans.getNotes());
        assertEquals(transition.getFromStatus(), newTrans.getFromStatus());
        assertEquals(transition.getToStatus(), newTrans.getToStatus());
        assertEquals(transition.getTransition(), newTrans.getTransition());
        assertEquals(transition.getToken(), newTrans.getToken());
    }

    @Test
    public void testGetBankAccountStatusTransition() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNLLOCKED);
        transition.setNotes("Activated bank account card");
        transition.setFromStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToken("trm-token");

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.getBankAccountStatusTransition("usr-token", "trm-token", "sts-token")).thenReturn(transition);
        HyperwalletStatusTransition newTrans = hw.getBankAccountStatusTransition("usr-token", "trm-token", "sts-token");

        assertEquals(transition.getNotes(), newTrans.getNotes());
        assertEquals(transition.getFromStatus(), newTrans.getFromStatus());
        assertEquals(transition.getToStatus(), newTrans.getToStatus());
        assertEquals(transition.getTransition(), newTrans.getTransition());
        assertEquals(transition.getToken(), newTrans.getToken());
    }

    @Test
    public void testListBankAccountStatusTransition() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.UNLLOCKED);
        transition.setNotes("Activated prepaid card");
        transition.setFromStatus(HyperwalletStatusTransition.Status.DE_ACTIVATED);
        transition.setToStatus(HyperwalletStatusTransition.Status.ACTIVATED);
        transition.setToken("sts-token");

        HyperwalletList<HyperwalletStatusTransition> list = new HyperwalletList<HyperwalletStatusTransition>();
        list.data = new ArrayList<HyperwalletStatusTransition>();
        list.data.add(transition);

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.listBankAccountStatusTransitions("user-token", "trm-token")).thenReturn(list);

        HyperwalletList<HyperwalletStatusTransition> retList = hw.listBankAccountStatusTransitions("user-token", "trm-token");

        assertNotNull(retList);
        assertTrue(retList.data.size() > 0);

        HyperwalletStatusTransition newTrans = retList.data.get(0);

        assertEquals(transition.getNotes(), newTrans.getNotes());
        assertEquals(transition.getFromStatus(), newTrans.getFromStatus());
        assertEquals(transition.getToStatus(), newTrans.getToStatus());
        assertEquals(transition.getTransition(), newTrans.getTransition());
        assertEquals(transition.getToken(), newTrans.getToken());
    }
}
