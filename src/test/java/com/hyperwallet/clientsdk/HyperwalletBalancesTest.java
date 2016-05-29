package com.hyperwallet.clientsdk;

import com.hyperwallet.clientsdk.model.HyperwalletBalance;
import com.hyperwallet.clientsdk.model.HyperwalletList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HyperwalletBalancesTest {

    Hyperwallet hyperwallet;

    @Before
    public void initialize() {
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
    public void testGetProgramAccountBalancesProgramTokenNull() {
        hyperwallet.listProgramAccountBalances(null, "act-*");
    }

    @Test(expected = HyperwalletException.class)
    public void testGetProgramAccountBalancesProgramTokenEmpty() {
        hyperwallet.listProgramAccountBalances("", "act-*");
    }

    @Test(expected = HyperwalletException.class)
    public void testGetProgramAccountBalancesAccountTokenNull() {
        hyperwallet.listProgramAccountBalances("prg-*", null);
    }

    @Test(expected = HyperwalletException.class)
    public void testGetProgramAccountBalancesAccountTokenEmpty() {
        hyperwallet.listProgramAccountBalances("prg-*", "");
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
        balOne.amount(10.00).currency("CAD");
        list.data.add(balOne);

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.listUserBalances("user-token")).thenReturn(list);

        HyperwalletList<HyperwalletBalance> response = hw.listUserBalances("user-token");
        assertNotNull(response);
        assertTrue(response.data.size() > 0);
        HyperwalletBalance responseBalance = response.data.get(0);
        assertNotNull(responseBalance);
        assertEquals(balOne.getAmount(), responseBalance.getAmount());
        assertEquals(balOne.getCurrency(), responseBalance.getCurrency());
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
        balOne.amount(11.00).currency("CAD");
        list.data.add(balOne);

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.listUserPrepaidCardBalances("user-token", "trm-token")).thenReturn(list);

        HyperwalletList<HyperwalletBalance> response = hw.listUserPrepaidCardBalances("user-token", "trm-token");
        assertNotNull(response);
        assertTrue(response.data.size() > 0);
        HyperwalletBalance responseBalance = response.data.get(0);
        assertNotNull(responseBalance);
        assertEquals(balOne.getAmount(), responseBalance.getAmount());
        assertEquals(balOne.getCurrency(), responseBalance.getCurrency());
    }

    @Test
    public void testGetMockedProgramAccountBalancesNullReturn() {
        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.listProgramAccountBalances("user-token", "act-token")).thenReturn(null);
        assertNull(hw.listProgramAccountBalances("prg-token", "act-token"));
    }

    @Test
    public void testGetMockedProgramAccountBalancesNormal() {
        HyperwalletList<HyperwalletBalance> list = new HyperwalletList<HyperwalletBalance>();
        HyperwalletBalance balOne = new HyperwalletBalance();
        balOne.amount(12.00).currency("CAD");
        list.data.add(balOne);

        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.listProgramAccountBalances("user-token", "act-token")).thenReturn(list);

        HyperwalletList<HyperwalletBalance> response = hw.listProgramAccountBalances("user-token", "act-token");
        assertNotNull(response);
        assertTrue(response.data.size() > 0);
        HyperwalletBalance responseBalance = response.data.get(0);
        assertNotNull(responseBalance);
        assertEquals(balOne.getAmount(), responseBalance.getAmount());
        assertEquals(balOne.getCurrency(), responseBalance.getCurrency());
    }
}