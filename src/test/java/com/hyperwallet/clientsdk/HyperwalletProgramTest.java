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
        Hyperwallet hw = mock(Hyperwallet.class);
        HyperwalletProgram orig = new HyperwalletProgram();
        orig.createdOn = now;
        orig.parentToken = "prg-parent-token";
        orig.name = "Test program";
        orig.token = "prg-token";

        when(hw.getProgram("prg-token")).thenReturn(orig);
        HyperwalletProgram response = hw.getProgram("prg-token");

        assertNotNull(response);
        assertEquals(now, response.createdOn);
        assertEquals(orig.token, response.token);
        assertEquals(orig.name, response.name);
        assertEquals(orig.parentToken, response.parentToken);
    }
}