package com.hyperwallet.clientsdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.model.HyperwalletBalance;
import com.hyperwallet.clientsdk.model.HyperwalletList;
import org.apache.catalina.LifecycleException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HyperwalletBalancesTest {

    Hyperwallet hyperwallet;

    @Before
    public void initialize() throws LifecycleException {
        hyperwallet = new Hyperwallet("username", "password", "programToken", "url");
    }

    @Test(expected = HyperwalletException.class)
    public void testGetUserBalanceMissingUserTokenNull() {
        hyperwallet.listUserBalances(null);
    }

    @Test(expected = HyperwalletException.class)
    public void testGetUserBalanceMissingUserTokenEmpty() {
        hyperwallet.listUserBalances("");
    }

    @Test(expected = HyperwalletException.class)
    public void testGetUserPrepaidCardBalancesUserTokenNull() {
        hyperwallet.listUserPrepaidCardBalances(null, "trm-*");
    }

    @Test(expected = HyperwalletException.class)
    public void testGetUserPrepaidCardBalancesUserTokenEmpty() {
        hyperwallet.listUserPrepaidCardBalances("", "trm-*");
    }

    @Test(expected = HyperwalletException.class)
    public void testGetUserPrepaidCardBalancesAccountTokenNull() {
        hyperwallet.listUserPrepaidCardBalances("usr-*", null);
    }

    @Test(expected = HyperwalletException.class)
    public void testGetUserPrepaidCardBalancesAccountTokenEmpty() {
        hyperwallet.listUserPrepaidCardBalances("usr-*", "");
    }

    @Test(expected = HyperwalletException.class)
    public void testGetUserAccountBalancesProgramTokenNull() {
        hyperwallet.listUserAccountBalances(null, "act-*");
    }

    @Test(expected = HyperwalletException.class)
    public void testGetUserAccountBalancesProgramTokenEmpty() {
        hyperwallet.listUserAccountBalances("", "act-*");
    }

    @Test(expected = HyperwalletException.class)
    public void testGetUserAccountBalancesAccountTokenNull() {
        hyperwallet.listUserAccountBalances("prg-*", null);
    }

    @Test(expected = HyperwalletException.class)
    public void testGetUserAccountBalancesAccountTokenEmpty() {
        hyperwallet.listUserAccountBalances("prg-*", "");
    }

    @Test
    public void testGetMockedUserBalanceNullReturn() {
        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.listUserBalances("user-token")).thenReturn(null);
        assertNull(hw.listUserBalances("usr-token"));
    }

    @Test
    public void testGetMockedUserBalanceNormal() {
        HyperwalletList<HyperwalletBalance> list = new HyperwalletList<HyperwalletBalance>();
        HyperwalletBalance balOne = new HyperwalletBalance();
        balOne.setAmount(10.00).setCurrency("CAD");
        list.data.add(balOne);

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.listUserBalances("user-token")).thenReturn(list);

        HyperwalletList<HyperwalletBalance> response = hw.listUserBalances("user-token");
        assertNotNull(response);
        assertTrue(response.data.size() > 0);
        HyperwalletBalance responseBalance = response.data.get(0);
        assertNotNull(responseBalance);
        assertEquals(balOne.amount, responseBalance.amount);
        assertEquals(balOne.currency, responseBalance.currency);
    }

    @Test
    public void testGetMockedUserPrepaidCardBalancesNullReturn() {
        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.listUserPrepaidCardBalances("user-token", "trm-token")).thenReturn(null);
        assertNull(hw.listUserPrepaidCardBalances("usr-token", "trm-token"));
    }

    @Test
    public void testGetMockedUserPrepaidCardBalancesNormal() {
        HyperwalletList<HyperwalletBalance> list = new HyperwalletList<HyperwalletBalance>();
        HyperwalletBalance balOne = new HyperwalletBalance();
        balOne.setAmount(11.00).setCurrency("CAD");
        list.data.add(balOne);

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.listUserPrepaidCardBalances("user-token", "trm-token")).thenReturn(list);

        HyperwalletList<HyperwalletBalance> response = hw.listUserPrepaidCardBalances("user-token", "trm-token");
        assertNotNull(response);
        assertTrue(response.data.size() > 0);
        HyperwalletBalance responseBalance = response.data.get(0);
        assertNotNull(responseBalance);
        assertEquals(balOne.amount, responseBalance.amount);
        assertEquals(balOne.currency, responseBalance.currency);
    }

    @Test
    public void testGetMockedAccountBalancesNullReturn() {
        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.listUserAccountBalances("user-token", "act-token")).thenReturn(null);
        assertNull(hw.listUserAccountBalances("prg-token", "act-token"));
    }

    @Test
    public void testGetMockedAccountBalancesNormal() {
        HyperwalletList<HyperwalletBalance> list = new HyperwalletList<HyperwalletBalance>();
        HyperwalletBalance balOne = new HyperwalletBalance();
        balOne.setAmount(12.00).setCurrency("CAD");
        list.data.add(balOne);

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.listUserAccountBalances("user-token", "act-token")).thenReturn(list);

        HyperwalletList<HyperwalletBalance> response = hw.listUserAccountBalances("user-token", "act-token");
        assertNotNull(response);
        assertTrue(response.data.size() > 0);
        HyperwalletBalance responseBalance = response.data.get(0);
        assertNotNull(responseBalance);
        assertEquals(balOne.amount, responseBalance.amount);
        assertEquals(balOne.currency, responseBalance.currency);
    }
}