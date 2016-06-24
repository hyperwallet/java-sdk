package com.hyperwallet.clientsdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.model.HyperwalletBankAccount;
import com.hyperwallet.clientsdk.model.HyperwalletList;
import com.hyperwallet.clientsdk.model.HyperwalletUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class HyperwalletBankAccountTest {

    @Test
    public void testModel() {
        String[] fields = {
                "token", "status", "type",
                "createdOn", "transferMethodCountry", "transferMethodCurrency", "bankName", "bankId", "branchName",
                "branchId", "bankAccountId", "bankAccountRelationship", "bankAccountPurpose",
                "branchAddressLine1", "branchAddressLine2", "branchCity", "branchStateProvince", "branchCountry", "branchPostalCode",
                "wireInstructions", "intermediaryBankId", "intermediaryBankName", "intermediaryBankAccountId", "intermediaryBankAddressLine1",
                "intermediaryBankAddressLine2", "intermediaryBankCity", "intermediaryBankStateProvince", "intermediaryBankPostalCode",
                "intermediaryBankCountry", "userToken", "profileType", "businessName",
                "businessRegistrationId", "businessRegistrationStateProvince", "businessRegistrationCountry",
                "businessContactRole", "firstName", "lastName", "dateOfBirth",
                "countryOfBirth", "countryOfNationality", "gender", "phoneNumber", "mobileNumber",
                "email", "governmentId", "passportId", "driversLicenseId", "addressLine1", "addressLine2",
                "city", "stateProvince", "postalCode", "country", "middleName"};

        HyperwalletBankAccount bank = new HyperwalletBankAccount();

        bank.token("").status(HyperwalletBankAccount.Status.ACTIVATED).type(HyperwalletBankAccount.Type.BANK_ACCOUNT)
                .createdOn(new Date()).transferMethodCountry("").transferMethodCurrency("").bankName("").bankId("").branchName("")
                .branchId("").bankAccountId("").bankAccountRelationship(HyperwalletBankAccount.Relationship.SELF).bankAccountPurpose("")
                .branchAddressLine1("").branchAddressLine2("").branchCity("").branchStateProvince("").branchCountry("").branchPostalCode("")
                .wireInstructions("").intermediaryBankId("").intermediaryBankName("").intermediaryBankAccountId("").intermediaryBankAddressLine1("")
                .intermediaryBankAddressLine2("").intermediaryBankCity("").intermediaryBankStateProvince("").intermediaryBankPostalCode("")
                .intermediaryBankCountry("").userToken("").profileType(HyperwalletUser.ProfileType.INDIVIDUAL).businessName("")
                .businessRegistrationId("").businessRegistrationStateProvince("").businessRegistrationCountry("")
                .businessContactRole(HyperwalletUser.BusinessContactRole.OTHER).firstName("").lastName("").dateOfBirth(new Date())
                .countryOfBirth("").countryOfNationality("").gender(HyperwalletUser.Gender.FEMALE).phoneNumber("").mobileNumber("")
                .email("").governmentId("").passportId("").driversLicenseId("").addressLine1("").addressLine2("")
                .city("").stateProvince("").postalCode("").country("").middleName("");

        Set<String> inclusions = bank.getInclusions();
        assertEquals(fields.length, inclusions.size());

        for(String f : fields) {
            assertTrue(inclusions.contains(f));
        }

        bank.setToken(null);
        assertFalse(inclusions.contains("token"));
        assertTrue(inclusions.size() == fields.length - 1);

        bank.clearToken();
        assertTrue(inclusions.contains("token"));
        assertNull(bank.getToken());
        assertTrue(inclusions.size() == fields.length);
    }

    @Test
    public void testUserCreateBankAccount() {
        HyperwalletBankAccount bank = new HyperwalletBankAccount();
        bank.setType(HyperwalletBankAccount.Type.BANK_ACCOUNT);
        bank.setStatus(HyperwalletBankAccount.Status.ACTIVATED);
        bank.setTransferMethodCountry("CA");
        bank.setTransferMethodCurrency("CAD");
        bank.setProfileType(HyperwalletUser.ProfileType.INDIVIDUAL);
        bank.setBankId("001");
        bank.setBranchId("25039");
        bank.setBankAccountId("000100020003");
        bank.setBankAccountRelationship(HyperwalletBankAccount.Relationship.SELF);
        bank.setUserToken("usr-token");
        bank.setFirstName("Phil");
        bank.setLastName("Jackson");
        bank.setAddressLine1("950 Granbowl");
        bank.setCity("Richmond");
        bank.setStateProvince("BC");
        bank.setCountry("CA");
        bank.setPostalCode("H0H0H0");
        bank.setUserToken("usr-token");
        bank.setCreatedOn(new Date());

        HyperwalletApiClient client = mock(HyperwalletApiClient.class);
        Hyperwallet hw = new Hyperwallet("username", "password");
        hw.setClientService(client);
        when(client.copy(bank)).thenReturn(bank);
        when(client.post(anyString(), anyObject(), any(Class.class))).thenReturn(bank);
        HyperwalletBankAccount created = hw.createUserBankAccount(bank);

        assertNotNull(created);
        assertEquals(bank.getType(), created.getType());
        assertEquals(bank.getStatus(), created.getStatus());
        assertEquals(bank.getTransferMethodCountry(), created.getTransferMethodCountry());
        assertEquals(bank.getTransferMethodCurrency(), created.getTransferMethodCurrency());
        assertEquals(bank.getProfileType(), created.getProfileType());
        assertEquals(bank.getBankId(), created.getBankId());
        assertEquals(bank.getBranchId(), created.getBranchId());
        assertEquals(bank.getBankAccountId(), created.getBankAccountId());
        assertEquals(bank.getBankAccountRelationship(), created.getBankAccountRelationship());
        assertEquals(bank.getUserToken(), created.getUserToken());
        assertEquals(bank.getFirstName(), created.getFirstName());
        assertEquals(bank.getLastName(), created.getLastName());
        assertEquals(bank.getAddressLine1(), created.getAddressLine1());
        assertEquals(bank.getCity(), created.getCity());
        assertEquals(bank.getStateProvince(), created.getStateProvince());
        assertEquals(bank.getCountry(), created.getCountry());
        assertEquals(bank.getPostalCode(), created.getPostalCode());
    }
    
    @Test
    public void testUserUpdateBankAccountClearing() {
    	
        HyperwalletBankAccount bank = new HyperwalletBankAccount();
        bank.setType(HyperwalletBankAccount.Type.BANK_ACCOUNT);
        bank.setStatus(HyperwalletBankAccount.Status.ACTIVATED);
        bank.setTransferMethodCountry("CA");
        bank.setTransferMethodCurrency("CAD");
        bank.setProfileType(HyperwalletUser.ProfileType.INDIVIDUAL);
        bank.setBankId("001");
        bank.setBranchId("25039");
        bank.setBankAccountId("000100020003");
        bank.setBankAccountRelationship(HyperwalletBankAccount.Relationship.SELF);
        bank.setUserToken("usr-token");
        bank.setFirstName("Phil");
        bank.setLastName("Jackson");
        bank.setAddressLine1("950 Granbowl");
        bank.setCity("Richmond");
        bank.setStateProvince("BC");
        bank.setCountry("CA");
        bank.clearPostalCode();
        bank.setUserToken("usr-token");
        bank.setToken("trm-token");

        HyperwalletApiClient client = mock(HyperwalletApiClient.class);
        Hyperwallet hw = new Hyperwallet("username", "password");
        hw.setClientService(client);
        when(client.put(anyString(), anyObject(), any(Class.class))).thenReturn(bank);
        HyperwalletBankAccount created = hw.updateUserBankAccount(bank);

        assertNotNull(created);
        assertEquals(bank.getType(), created.getType());
        assertEquals(bank.getStatus(), created.getStatus());
        assertEquals(bank.getTransferMethodCountry(), created.getTransferMethodCountry());
        assertEquals(bank.getTransferMethodCurrency(), created.getTransferMethodCurrency());
        assertEquals(bank.getProfileType(), created.getProfileType());
        assertEquals(bank.getBankId(), created.getBankId());
        assertEquals(bank.getBranchId(), created.getBranchId());
        assertEquals(bank.getBankAccountId(), created.getBankAccountId());
        assertEquals(bank.getBankAccountRelationship(), created.getBankAccountRelationship());
        assertEquals(bank.getUserToken(), created.getUserToken());
        assertEquals(bank.getFirstName(), created.getFirstName());
        assertEquals(bank.getLastName(), created.getLastName());
        assertEquals(bank.getAddressLine1(), created.getAddressLine1());
        assertEquals(bank.getCity(), created.getCity());
        assertEquals(bank.getStateProvince(), created.getStateProvince());
        assertEquals(bank.getCountry(), created.getCountry());
        assertNull(created.getPostalCode());
    }
    
    @Test
    public void testUserUpdateBankAccount() {
    	
        HyperwalletBankAccount bank = new HyperwalletBankAccount();
        bank.setType(HyperwalletBankAccount.Type.BANK_ACCOUNT);
        bank.setStatus(HyperwalletBankAccount.Status.ACTIVATED);
        bank.setTransferMethodCountry("CA");
        bank.setTransferMethodCurrency("CAD");
        bank.setProfileType(HyperwalletUser.ProfileType.INDIVIDUAL);
        bank.setBankId("001");
        bank.setBranchId("25039");
        bank.setBankAccountId("000100020003");
        bank.setBankAccountRelationship(HyperwalletBankAccount.Relationship.SELF);
        bank.setUserToken("usr-token");
        bank.setFirstName("Phil");
        bank.setLastName("Jackson");
        bank.setAddressLine1("950 Granbowl");
        bank.setCity("Richmond");
        bank.setStateProvince("BC");
        bank.setCountry("CA");
        bank.setPostalCode(null);
        bank.setUserToken("usr-token");
        bank.setToken("trm-token");

        HyperwalletApiClient client = mock(HyperwalletApiClient.class);
        Hyperwallet hw = new Hyperwallet("username", "password");
        hw.setClientService(client);
        when(client.put(anyString(), anyObject(), any(Class.class))).thenReturn(bank);
        HyperwalletBankAccount created = hw.updateUserBankAccount(bank);

        assertNotNull(created);
        assertEquals(bank.getType(), created.getType());
        assertEquals(bank.getStatus(), created.getStatus());
        assertEquals(bank.getTransferMethodCountry(), created.getTransferMethodCountry());
        assertEquals(bank.getTransferMethodCurrency(), created.getTransferMethodCurrency());
        assertEquals(bank.getProfileType(), created.getProfileType());
        assertEquals(bank.getBankId(), created.getBankId());
        assertEquals(bank.getBranchId(), created.getBranchId());
        assertEquals(bank.getBankAccountId(), created.getBankAccountId());
        assertEquals(bank.getBankAccountRelationship(), created.getBankAccountRelationship());
        assertEquals(bank.getUserToken(), created.getUserToken());
        assertEquals(bank.getFirstName(), created.getFirstName());
        assertEquals(bank.getLastName(), created.getLastName());
        assertEquals(bank.getAddressLine1(), created.getAddressLine1());
        assertEquals(bank.getCity(), created.getCity());
        assertEquals(bank.getStateProvince(), created.getStateProvince());
        assertEquals(bank.getCountry(), created.getCountry());
        assertNull(created.getPostalCode());
    }

    @Test
    public void testUserGetBankAccount() {
        HyperwalletBankAccount bank = new HyperwalletBankAccount();
        bank.setType(HyperwalletBankAccount.Type.BANK_ACCOUNT);
        bank.setStatus(HyperwalletBankAccount.Status.ACTIVATED);
        bank.setTransferMethodCountry("CA");
        bank.setTransferMethodCurrency("CAD");
        bank.setProfileType(HyperwalletUser.ProfileType.INDIVIDUAL);
        bank.setBankId("001");
        bank.setBranchId("25039");
        bank.setBankAccountId("000100020003");
        bank.setBankAccountRelationship(HyperwalletBankAccount.Relationship.SELF);
        bank.setUserToken("usr-token");
        bank.setFirstName("Phil");
        bank.setLastName("Jackson");
        bank.setAddressLine1("950 Granbowl");
        bank.setCity("Richmond");
        bank.setStateProvince("BC");
        bank.setCountry("CA");
        bank.setPostalCode("H0H0H0");
        bank.setUserToken("usr-token");
        bank.setToken("trm-token");

        HyperwalletApiClient client = mock(HyperwalletApiClient.class);
        Hyperwallet hw = new Hyperwallet("username", "password");
        hw.setClientService(client);
        when(client.get(anyString(), any(Class.class))).thenReturn(bank);
        HyperwalletBankAccount retreived = hw.getUserBankAccount(bank.getUserToken(), bank.getToken());

        assertNotNull(retreived);
        assertEquals(bank.getType(), retreived.getType());
        assertEquals(bank.getStatus(), retreived.getStatus());
        assertEquals(bank.getTransferMethodCountry(), retreived.getTransferMethodCountry());
        assertEquals(bank.getTransferMethodCurrency(), retreived.getTransferMethodCurrency());
        assertEquals(bank.getProfileType(), retreived.getProfileType());
        assertEquals(bank.getBankId(), retreived.getBankId());
        assertEquals(bank.getBranchId(), retreived.getBranchId());
        assertEquals(bank.getBankAccountId(), retreived.getBankAccountId());
        assertEquals(bank.getBankAccountRelationship(), retreived.getBankAccountRelationship());
        assertEquals(bank.getUserToken(), retreived.getUserToken());
        assertEquals(bank.getFirstName(), retreived.getFirstName());
        assertEquals(bank.getLastName(), retreived.getLastName());
        assertEquals(bank.getAddressLine1(), retreived.getAddressLine1());
        assertEquals(bank.getCity(), retreived.getCity());
        assertEquals(bank.getStateProvince(), retreived.getStateProvince());
        assertEquals(bank.getCountry(), retreived.getCountry());
        assertEquals(bank.getPostalCode(), retreived.getPostalCode());
    }

    @Test
    public void testUserBankAccounts() {
        HyperwalletBankAccount bank = new HyperwalletBankAccount();
        bank.setType(HyperwalletBankAccount.Type.BANK_ACCOUNT);
        bank.setStatus(HyperwalletBankAccount.Status.ACTIVATED);
        bank.setTransferMethodCountry("CA");
        bank.setTransferMethodCurrency("CAD");
        bank.setProfileType(HyperwalletUser.ProfileType.INDIVIDUAL);
        bank.setBankId("001");
        bank.setBranchId("25039");
        bank.setBankAccountId("000100020003");
        bank.setBankAccountRelationship(HyperwalletBankAccount.Relationship.SELF);
        bank.setUserToken("usr-token");
        bank.setFirstName("Phil");
        bank.setLastName("Jackson");
        bank.setAddressLine1("950 Granbowl");
        bank.setCity("Richmond");
        bank.setStateProvince("BC");
        bank.setCountry("CA");
        bank.setPostalCode("H0H0H0");

        HyperwalletList<HyperwalletBankAccount> list = new HyperwalletList<HyperwalletBankAccount>();
        list.data = new ArrayList<HyperwalletBankAccount>();
        list.data.add(bank);

        HyperwalletApiClient client = mock(HyperwalletApiClient.class);
        Hyperwallet hw = new Hyperwallet("username", "password");
        hw.setClientService(client);
        when(client.get(anyString(), any(TypeReference.class))).thenReturn(list);

        HyperwalletList<HyperwalletBankAccount> results = hw.listUserBankAccounts("usr-token");
        assertTrue(results.data.size() > 0);
        HyperwalletBankAccount retreived = results.data.get(0);

        assertNotNull(retreived);
        assertEquals(bank.getType(), retreived.getType());
        assertEquals(bank.getStatus(), retreived.getStatus());
        assertEquals(bank.getTransferMethodCountry(), retreived.getTransferMethodCountry());
        assertEquals(bank.getTransferMethodCurrency(), retreived.getTransferMethodCurrency());
        assertEquals(bank.getProfileType(), retreived.getProfileType());
        assertEquals(bank.getBankId(), retreived.getBankId());
        assertEquals(bank.getBranchId(), retreived.getBranchId());
        assertEquals(bank.getBankAccountId(), retreived.getBankAccountId());
        assertEquals(bank.getBankAccountRelationship(), retreived.getBankAccountRelationship());
        assertEquals(bank.getUserToken(), retreived.getUserToken());
        assertEquals(bank.getFirstName(), retreived.getFirstName());
        assertEquals(bank.getLastName(), retreived.getLastName());
        assertEquals(bank.getAddressLine1(), retreived.getAddressLine1());
        assertEquals(bank.getCity(), retreived.getCity());
        assertEquals(bank.getStateProvince(), retreived.getStateProvince());
        assertEquals(bank.getCountry(), retreived.getCountry());
        assertEquals(bank.getPostalCode(), retreived.getPostalCode());
    }


}
