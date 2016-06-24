package com.hyperwallet.clientsdk;

import com.hyperwallet.clientsdk.model.HyperwalletProgram;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HyperwalletProgramTest {

    Hyperwallet hyperwallet;

    @Before
    public void initialize() {
        hyperwallet = new Hyperwallet("username", "password", "programToken", "url");
    }

    @Test(expected = HyperwalletException.class)
    public void testGetProgramNullToken() {
        hyperwallet.getProgram(null);
    }

    @Test(expected = HyperwalletException.class)
    public void testGetProgramEmptyToken() {
        hyperwallet.getProgram(" ");
    }

    @Test
    public void testGetProgramNull() {
        Hyperwallet hw = mock(Hyperwallet.class);
        when(hw.getProgram("prg-token")).thenReturn(null);
        assertNull(hw.getProgram("prg-token"));
    }

    @Test
    public void testGetProgramNormal() {
        Date now = new Date(System.currentTimeMillis());
        HyperwalletProgram orig = new HyperwalletProgram();
        orig.setCreatedOn(now);
        orig.setParentToken("prg-parent-token");
        orig.setName("Test program");
        orig.setToken("prg-token");

        HyperwalletApiClient client = mock(HyperwalletApiClient.class);
        Hyperwallet hw = new Hyperwallet("username", "password");
        hw.setClientService(client);
        when(client.get(anyString(), any(Class.class))).thenReturn(orig);
        HyperwalletProgram response = hw.getProgram("prg-token");

        assertNotNull(response);
        assertEquals(now, response.getCreatedOn());
        assertEquals(orig.getToken(), response.getToken());
        assertEquals(orig.getName(), response.getName());
        assertEquals(orig.getParentToken(), response.getParentToken());
    }
}