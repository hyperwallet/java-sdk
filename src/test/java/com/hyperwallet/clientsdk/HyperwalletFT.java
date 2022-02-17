package com.hyperwallet.clientsdk;

import com.hyperwallet.clientsdk.model.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class HyperwalletFT {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z");

    private Hyperwallet client;
    // Fill in with appropriate data to perform functional tests
    private final String username = "";
    private final String password = "";
    private final String baseURL = "";
    private final String prgmToken = "";


    @BeforeMethod
    public void setUp() {
        if (!username.isEmpty())
            client = new Hyperwallet(username, password, prgmToken, baseURL);
    }

    @Test
    public void testListUsers() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletList<HyperwalletUser> returnValue;

            try {
                returnValue = client.listUsers();
            } catch (Exception e) {
                throw e;
            }

            assertThat(returnValue.getLimit(), is(equalTo(10)));
            assertThat(returnValue.getOffset(), is(equalTo(0)));
            assertThat(returnValue.getData().size(), is(greaterThan(0)));
        }
    }

    @Test
    public void testProxyListUsers() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletList<HyperwalletUser> returnValue;

            try {
                client.setHyperwalletProxy("localhost", 9090);
                returnValue = client.listUsers();
            } catch (Exception e) {
                throw e;
            }

            assertThat(returnValue.getLimit(), is(equalTo(10)));
            assertThat(returnValue.getOffset(), is(equalTo(0)));
            assertThat(returnValue.getData().size(), is(greaterThan(0)));

        }
    }

    @Test
    public void testProxyAuthListUsers() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletList<HyperwalletUser> returnValue;

            try {
                client.setHyperwalletProxy("localhost", 3128);
                client.setHyperwalletProxyUsername("test");
                client.setHyperwalletProxyPassword("test");
                returnValue = client.listUsers();
            } catch (Exception e) {
                throw e;
            }

            assertThat(returnValue.getLimit(), is(equalTo(10)));
            assertThat(returnValue.getOffset(), is(equalTo(0)));
            assertThat(returnValue.getData().size(), is(greaterThan(0)));

        }
    }

    @Test
    public void testProxyFailAuthListUsers() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletList<HyperwalletUser> returnValue;

            try {
                client.setHyperwalletProxy("localhost", 3128);
                client.setHyperwalletProxyUsername("test1");
                client.setHyperwalletProxyPassword("test1");
                returnValue = client.listUsers();
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("Proxy Authentication Required")));
            }
        }
    }

    @Test
    public void testCreateUserWithInvalidToken() throws Exception {
        if (!username.isEmpty()) {
            Date dateOfBirth = dateFormat.parse("2000-09-08T15:01:07 UTC");

            HyperwalletUser hyperwalletUser = new HyperwalletUser()
                    .addressLine1("1234 IndividualAddress St")
                    .city("Test")
                    .clientUserId("1234")
                    .country("US")
                    .dateOfBirth(dateOfBirth)
                    .email("abc@company.com")
                    .firstName("John")
                    .lastName("Smith")
                    .postalCode("12345")
                    .profileType(HyperwalletUser.ProfileType.INDIVIDUAL)
                    .governmentId("333333333")
                    .phoneNumber("605-555-1323")
                    .programToken("invalidToken")
                    .stateProvince("CA");

            HyperwalletUser returnValue;
            try {
                returnValue = client.createUser(hyperwalletUser);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("You must provide a valid program token")));
            }
        }
    }

    @Test
    public void testProxyCreateUserWithInvalidToken() throws Exception {
        if (!username.isEmpty()) {
            Date dateOfBirth = dateFormat.parse("2000-09-08T15:01:07 UTC");

            HyperwalletUser hyperwalletUser = new HyperwalletUser()
                    .addressLine1("1234 IndividualAddress St")
                    .city("Test")
                    .clientUserId("1234")
                    .country("US")
                    .dateOfBirth(dateOfBirth)
                    .email("abc@company.com")
                    .firstName("John")
                    .lastName("Smith")
                    .postalCode("12345")
                    .profileType(HyperwalletUser.ProfileType.INDIVIDUAL)
                    .governmentId("333333333")
                    .phoneNumber("605-555-1323")
                    .programToken("invalidToken")
                    .stateProvince("CA");

            HyperwalletUser returnValue;
            try {
                client.setHyperwalletProxy("localhost", 9090);
                returnValue = client.createUser(hyperwalletUser);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("You must provide a valid program token")));
            }
        }
    }

    @Test
    public void testProxyAuthCreateUserWithInvalidToken() throws Exception {
        if (!username.isEmpty()) {
            Date dateOfBirth = dateFormat.parse("2000-09-08T15:01:07 UTC");

            HyperwalletUser hyperwalletUser = new HyperwalletUser()
                    .addressLine1("1234 IndividualAddress St")
                    .city("Test")
                    .clientUserId("1234")
                    .country("US")
                    .dateOfBirth(dateOfBirth)
                    .email("abc@company.com")
                    .firstName("John")
                    .lastName("Smith")
                    .postalCode("12345")
                    .profileType(HyperwalletUser.ProfileType.INDIVIDUAL)
                    .governmentId("333333333")
                    .phoneNumber("605-555-1323")
                    .programToken("invalidToken")
                    .stateProvince("CA");

            HyperwalletUser returnValue;
            try {
                client.setHyperwalletProxy("localhost", 3128);
                client.setHyperwalletProxyUsername("test");
                client.setHyperwalletProxyPassword("test");
                returnValue = client.createUser(hyperwalletUser);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("You must provide a valid program token")));
            }
        }
    }

    @Test
    public void testProxyAuthFailCreateUserWithInvalidToken() throws Exception {
        if (!username.isEmpty()) {
            Date dateOfBirth = dateFormat.parse("2000-09-08T15:01:07 UTC");

            HyperwalletUser hyperwalletUser = new HyperwalletUser()
                    .addressLine1("1234 IndividualAddress St")
                    .city("Test")
                    .clientUserId("1234")
                    .country("US")
                    .dateOfBirth(dateOfBirth)
                    .email("abc@company.com")
                    .firstName("John")
                    .lastName("Smith")
                    .postalCode("12345")
                    .profileType(HyperwalletUser.ProfileType.INDIVIDUAL)
                    .governmentId("333333333")
                    .phoneNumber("605-555-1323")
                    .programToken("invalidToken")
                    .stateProvince("CA");

            HyperwalletUser returnValue;
            try {
                client.setHyperwalletProxy("localhost", 3128);
                client.setHyperwalletProxyUsername("test1");
                client.setHyperwalletProxyPassword("test1");
                returnValue = client.createUser(hyperwalletUser);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("Proxy Authentication Required")));
            }
        }
    }

    @Test
    public void testUploadUserDocumentsWithInvalidData() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletUser returnValue;
            HyperwalletVerificationDocument hyperwalletVerificationDocument = new HyperwalletVerificationDocument();
            try {
                String userToken = "usr-490848fb-8e1f-4f7c-9a18-a5b7a372e602";
                List<HyperwalletVerificationDocument> documentList = new ArrayList<>();
                hyperwalletVerificationDocument.setType("DRIVERS_LICENSE");
                hyperwalletVerificationDocument.setCategory("IDENTIFICATION");
                hyperwalletVerificationDocument.setCountry("US");
                Map<String, String> fileList = new HashMap<>();
                fileList.put("drivers_license_front", "src/test/resources/integration/test.png");
                fileList.put("drivers_license_back", "src/test/resources/integration/test.png");
                hyperwalletVerificationDocument.setUploadFiles(fileList);
                documentList.add(hyperwalletVerificationDocument);
                returnValue = client.uploadUserDocuments(userToken, documentList);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("Invalid entry.")));
            }
        }
    }

    @Test
    public void testProxyUploadUserDocumentsWithInvalidData() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletUser returnValue;
            HyperwalletVerificationDocument hyperwalletVerificationDocument = new HyperwalletVerificationDocument();
            try {
                String userToken = "usr-490848fb-8e1f-4f7c-9a18-a5b7a372e602";
                List<HyperwalletVerificationDocument> documentList = new ArrayList<>();
                hyperwalletVerificationDocument.setType("DRIVERS_LICENSE");
                hyperwalletVerificationDocument.setCategory("IDENTIFICATION");
                hyperwalletVerificationDocument.setCountry("US");
                Map<String, String> fileList = new HashMap<>();
                fileList.put("drivers_license_front", "src/test/resources/integration/test.png");
                fileList.put("drivers_license_back", "src/test/resources/integration/test.png");
                hyperwalletVerificationDocument.setUploadFiles(fileList);
                documentList.add(hyperwalletVerificationDocument);

                client.setHyperwalletProxy("localhost", 9090);
                returnValue = client.uploadUserDocuments(userToken, documentList);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("Invalid entry.")));
            }
        }
    }

    @Test
    public void testProxyAuthUploadUserDocumentsWithInvalidData() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletUser returnValue;
            HyperwalletVerificationDocument hyperwalletVerificationDocument = new HyperwalletVerificationDocument();
            try {
                String userToken = "usr-490848fb-8e1f-4f7c-9a18-a5b7a372e602";
                List<HyperwalletVerificationDocument> documentList = new ArrayList<>();
                hyperwalletVerificationDocument.setType("DRIVERS_LICENSE");
                hyperwalletVerificationDocument.setCategory("IDENTIFICATION");
                hyperwalletVerificationDocument.setCountry("US");
                Map<String, String> fileList = new HashMap<>();
                fileList.put("drivers_license_front", "src/test/resources/integration/test.png");
                fileList.put("drivers_license_back", "src/test/resources/integration/test.png");
                hyperwalletVerificationDocument.setUploadFiles(fileList);
                documentList.add(hyperwalletVerificationDocument);

                client.setHyperwalletProxy("localhost", 3128);
                client.setHyperwalletProxyUsername("test");
                client.setHyperwalletProxyPassword("test");
                returnValue = client.uploadUserDocuments(userToken, documentList);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("Invalid entry.")));
            }
        }
    }

    @Test
    public void testProxyAuthFailUploadUserDocumentsWithInvalidData() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletUser returnValue;
            HyperwalletVerificationDocument hyperwalletVerificationDocument = new HyperwalletVerificationDocument();
            try {
                String userToken = "usr-490848fb-8e1f-4f7c-9a18-a5b7a372e602";
                List<HyperwalletVerificationDocument> documentList = new ArrayList<>();
                hyperwalletVerificationDocument.setType("DRIVERS_LICENSE");
                hyperwalletVerificationDocument.setCategory("IDENTIFICATION");
                hyperwalletVerificationDocument.setCountry("US");
                Map<String, String> fileList = new HashMap<>();
                fileList.put("drivers_license_front", "src/test/resources/integration/test.png");
                fileList.put("drivers_license_back", "src/test/resources/integration/test.png");
                hyperwalletVerificationDocument.setUploadFiles(fileList);
                documentList.add(hyperwalletVerificationDocument);

                client.setHyperwalletProxy("localhost", 3128);
                client.setHyperwalletProxyUsername("test1");
                client.setHyperwalletProxyPassword("test1");
                returnValue = client.uploadUserDocuments(userToken, documentList);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("Proxy Authentication Required")));
            }
        }
    }
}
