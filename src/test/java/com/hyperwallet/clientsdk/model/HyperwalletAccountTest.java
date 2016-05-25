package com.hyperwallet.clientsdk.model;

import com.hyperwallet.clientsdk.Hyperwallet;
import com.hyperwallet.clientsdk.HyperwalletException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by rbao on 5/20/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class HyperwalletAccountTest {


    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void testGet(){

        Hyperwallet hyperwallet = mock(Hyperwallet.class);
        HyperwalletAccount account_mock = new HyperwalletAccount();
        account_mock.setEmail("email");
        Date date = new Date();
        account_mock.setCreatedOn(date);
        account_mock.setType(HyperwalletAccount.EType.FUNDING);
        account_mock.setToken("token");

        when(hyperwallet.getProgramAccount(anyString(),anyString())).thenReturn(account_mock);

        HyperwalletAccount account = hyperwallet.getProgramAccount("any", "any");

        assertNotNull(account);
        assertEquals(account_mock.getEmail(),account.getEmail());
        assertEquals(account_mock.getToken(),account.getToken());
        assertEquals(account_mock.getCreatedOn(),account.getCreatedOn());
        assertEquals(account_mock.getType(),account.getType());

        verify(hyperwallet,times(1)).getProgramAccount(anyString(),anyString());
        verifyNoMoreInteractions(hyperwallet);

    }

    @Test
    public void testGet_Empty_ProgramToken(){
        thrown.expect(HyperwalletException.class);
        Hyperwallet hyperwallet = new Hyperwallet("","");
        hyperwallet.getProgramAccount("","accountToken");
    }

    @Test
    public void testGet_Empty_AccountToken(){
        thrown.expect(HyperwalletException.class);
        Hyperwallet hyperwallet = new Hyperwallet("","");
        hyperwallet.getProgramAccount("","accountToken");
    }


}