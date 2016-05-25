package com.hyperwallet.clientsdk;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HyperwalletUserTest {

    Hyperwallet hyperwallet;

    @Before
    public void initialize() {
        hyperwallet = new Hyperwallet("username", "password", "programToken", "url");
    }

    @Test
    public void testCreateUser() {
        Hyperwallet hw = mock(Hyperwallet.class);
    }
}