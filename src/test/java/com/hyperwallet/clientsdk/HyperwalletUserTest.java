package com.hyperwallet.clientsdk;

import com.hyperwallet.clientsdk.model.HyperwalletList;
import com.hyperwallet.clientsdk.model.HyperwalletUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HyperwalletUserTest {

    @Test
    public void testHyperwalletBeanTest() {

        HyperwalletUser user = new HyperwalletUser();
        user.setClientUserId("clientxx001");
        user.setProfileType(HyperwalletUser.ProfileType.INDIVIDUAL);
        user.setFirstName("devfirstname");
        user.setLastName("develastname");
        user.setEmail("dev@mail.com");
        user.setAddressLine1("950 Granville");
        user.setCity("Cebu");
        user.setStateProvince("BC");
        user.setCountry("CA");
        user.setPostalCode("V1V2V3");

        Set<String> inclusions = user.getInclusions();
        assertTrue(inclusions.size() == 10);
        assertTrue(inclusions.contains("clientUserId"));
        assertTrue(inclusions.contains("profileType"));
        assertTrue(inclusions.contains("firstName"));
        assertTrue(inclusions.contains("lastName"));
        assertTrue(inclusions.contains("email"));
        assertTrue(inclusions.contains("addressLine1"));
        assertTrue(inclusions.contains("city"));
        assertTrue(inclusions.contains("stateProvince"));
        assertTrue(inclusions.contains("country"));
        assertTrue(inclusions.contains("postalCode"));
        assertFalse(inclusions.contains("token"));

        assertFalse(inclusions.contains("programToken"));
        assertFalse(inclusions.contains("language"));
        assertFalse(inclusions.contains("status"));
        assertFalse(inclusions.contains("businessType"));
        assertFalse(inclusions.contains("businessName"));
        assertFalse(inclusions.contains("businessRegistrationId"));
        assertFalse(inclusions.contains("businessRegistrationStateProvince"));
        assertFalse(inclusions.contains("businessRegistrationCountry"));
        assertFalse(inclusions.contains("businessContactRole"));
    }

    @Test
    public void testCreateUser() {
        Hyperwallet hw = mock(Hyperwallet.class);
        HyperwalletUser user = new HyperwalletUser();
        user.setClientUserId("clientxx001");
        user.setProfileType(HyperwalletUser.ProfileType.INDIVIDUAL);
        user.setFirstName("devfirstname");
        user.setLastName("develastname");
        user.setEmail("dev@mail.com");
        user.setAddressLine1("950 Granville");
        user.setCity("Cebu");
        user.setStateProvince("BC");
        user.setCountry("CA");
        user.setPostalCode("V1V2V3");

        when(hw.createUser(user)).thenReturn(user);
        HyperwalletUser u = hw.createUser(user);

        assertEquals(user.getClientUserId(), u.getClientUserId());
        assertEquals(user.getProfileType(), u.getProfileType());
        assertEquals(user.getFirstName(), u.getFirstName());
        assertEquals(user.getLastName(), u.getLastName());
        assertEquals(user.getEmail(), u.getEmail());
        assertEquals(user.getAddressLine1(), u.getAddressLine1());
        assertEquals(user.getCity(), u.getCity());
        assertEquals(user.getStateProvince(), u.getStateProvince());
        assertEquals(user.getCountry(), u.getCountry());
        assertEquals(user.getPostalCode(), u.getPostalCode());
    }

    @Test
    public void testGetUser() {
        Hyperwallet hw = mock(Hyperwallet.class);
        HyperwalletUser user = new HyperwalletUser();
        user.setClientUserId("clientxx001");
        user.setProfileType(HyperwalletUser.ProfileType.INDIVIDUAL);
        user.setFirstName("devfirstname");
        user.setLastName("develastname");
        user.setEmail("dev@mail.com");
        user.setAddressLine1("950 Granville");
        user.setCity("Cebu");
        user.setStateProvince("BC");
        user.setCountry("CA");
        user.setPostalCode("V1V2V3");

        when(hw.getUser("user-token")).thenReturn(user);
        HyperwalletUser u = hw.getUser("user-token");

        assertEquals(user.getClientUserId(), u.getClientUserId());
        assertEquals(user.getProfileType(), u.getProfileType());
        assertEquals(user.getFirstName(), u.getFirstName());
        assertEquals(user.getLastName(), u.getLastName());
        assertEquals(user.getEmail(), u.getEmail());
        assertEquals(user.getAddressLine1(), u.getAddressLine1());
        assertEquals(user.getCity(), u.getCity());
        assertEquals(user.getStateProvince(), u.getStateProvince());
        assertEquals(user.getCountry(), u.getCountry());
        assertEquals(user.getPostalCode(), u.getPostalCode());
    }

    @Test
    public void testGetUsers() {
        Hyperwallet hw = mock(Hyperwallet.class);
        HyperwalletUser user = new HyperwalletUser();
        user.setClientUserId("clientxx001");
        user.setProfileType(HyperwalletUser.ProfileType.INDIVIDUAL);
        user.setFirstName("devfirstname");
        user.setLastName("develastname");
        user.setEmail("dev@mail.com");
        user.setAddressLine1("950 Granville");
        user.setCity("Cebu");
        user.setStateProvince("BC");
        user.setCountry("CA");
        user.setPostalCode("V1V2V3");

        HyperwalletList<HyperwalletUser> userList = new HyperwalletList<HyperwalletUser>();
        userList.data = new ArrayList<HyperwalletUser>();
        userList.data.add(user);

        when(hw.listUsers()).thenReturn(userList);

        HyperwalletList<HyperwalletUser> uList = hw.listUsers();
        assertTrue(uList.data.size() > 0);
        for (HyperwalletUser u : uList.data) {
            assertEquals(user.getClientUserId(), u.getClientUserId());
            assertEquals(user.getProfileType(), u.getProfileType());
            assertEquals(user.getFirstName(), u.getFirstName());
            assertEquals(user.getLastName(), u.getLastName());
            assertEquals(user.getEmail(), u.getEmail());
            assertEquals(user.getAddressLine1(), u.getAddressLine1());
            assertEquals(user.getCity(), u.getCity());
            assertEquals(user.getStateProvince(), u.getStateProvince());
            assertEquals(user.getCountry(), u.getCountry());
            assertEquals(user.getPostalCode(), u.getPostalCode());
        }
    }
}