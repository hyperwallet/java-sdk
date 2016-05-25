package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.Hyperwallet;
import com.hyperwallet.clientsdk.HyperwalletException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by rbao on 5/20/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class HyperwalletAccountTest {

    @Mock
    Hyperwallet hyperwallet;
    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void testGet(){

       when(hyperwallet.getAccount(anyString(),anyString())).thenReturn(new HyperwalletAccount());

        HyperwalletAccount account = hyperwallet.getAccount("any", "any");

        assertNotNull(account);
        verify(hyperwallet,times(1)).getAccount(anyString(),anyString());
        verifyNoMoreInteractions(hyperwallet);
    }

    @Test
    public void testGet_Empty_ProgramToken(){
        thrown.expect(HyperwalletException.class);
        hyperwallet.getAccount("","accountToken");

    }
}