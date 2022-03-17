package com.hyperwallet.clientsdk;

import com.hyperwallet.clientsdk.model.*;
import com.hyperwallet.clientsdk.util.Multipart;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import com.hyperwallet.clientsdk.util.HyperwalletEncryption;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
//import com.nimbusds.jose.jwk.JWKSet;
import java.net.Proxy;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class HyperwalletFT {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z");

    private Hyperwallet client;
    // Fill in with appropriate data to perform functional tests
    private final String username = "selrestuser@1861681";
    private final String password = "Password1!";
    private final String baseURL = "https://qamaster-hyperwallet.aws.paylution.net";
    private final String prgmToken = "prg-eedaf875-01f1-4524-8b94-d4936255af78";


    @BeforeMethod
    public void setUp() {
        if (!username.isEmpty()) {
            client = new Hyperwallet(username, password, prgmToken, baseURL);
        }
    }

    // NOTE: To run these JWK tests, make loadKeySet from a private func to public
    @Test
    public void testJWKLoad() throws Exception {
        if (!username.isEmpty()) {

            HyperwalletEncryption hwEnc;

            hwEnc = new HyperwalletEncryption.HyperwalletEncryptionBuilder()
                    .encryptionAlgorithm(JWEAlgorithm.RSA_OAEP_256)
                    .encryptionMethod(EncryptionMethod.A256CBC_HS512)
                    .signAlgorithm(JWSAlgorithm.RS256)
                    .hyperwalletKeySetLocation("")
                    .clientPrivateKeySetLocation("").build();
            client = new Hyperwallet(username, password, prgmToken, baseURL, hwEnc);
            Proxy testval = hwEnc.getProxy();
//            JWKSet keys = hwEnc.loadKeySet("http://localhost:8081/mockserver/static/jwks/client-rsa-public.json");
//
//            assertThat(keys.getKeys().size(), is(equalTo(2)));
        }
    }

    @Test
    public void testJWKLoadProxyFail() throws Exception {
        if (!username.isEmpty()) {

            HyperwalletEncryption hwEnc;

            hwEnc = new HyperwalletEncryption.HyperwalletEncryptionBuilder()
                    .encryptionAlgorithm(JWEAlgorithm.RSA_OAEP_256)
                    .encryptionMethod(EncryptionMethod.A256CBC_HS512)
                    .signAlgorithm(JWSAlgorithm.RS256)
                    .hyperwalletKeySetLocation("")
                    .clientPrivateKeySetLocation("")
                    .build();
            hwEnc.setProxy("localhost", 3128);
            client = new Hyperwallet(username, password, prgmToken, baseURL, hwEnc);
            Proxy testval = hwEnc.getProxy();

//            try {
//                JWKSet keys = hwEnc.loadKeySet("http://localhost:8081/mockserver/static/jwks/client-rsa-public.json");
//            } catch (Exception e) {
//                assertThat(e.getMessage(), is(containsString("Server returned HTTP response code: 407 for URL:")));
//            }
        }
    }

    @Test
    public void testJWKLoadProxyAuthFail() throws Exception {
        if (!username.isEmpty()) {

            HyperwalletEncryption hwEnc;

            hwEnc = new HyperwalletEncryption.HyperwalletEncryptionBuilder()
                    .encryptionAlgorithm(JWEAlgorithm.RSA_OAEP_256)
                    .encryptionMethod(EncryptionMethod.A256CBC_HS512)
                    .signAlgorithm(JWSAlgorithm.RS256)
                    .hyperwalletKeySetLocation("")
                    .clientPrivateKeySetLocation("").build();
            hwEnc.setProxy("localhost", 3128);
            hwEnc.setProxyUsername("test1");
            hwEnc.setProxyPassword("test1");
            client = new Hyperwallet(username, password, prgmToken, baseURL, hwEnc);

            Proxy testval = hwEnc.getProxy();

//            try {
//                JWKSet keys = hwEnc.loadKeySet("http://localhost:8081/mockserver/static/jwks/client-rsa-public.json");
//            } catch (Exception e) {
//                assertThat(e.getMessage(), is(containsString("Server redirected too many  times (20)")));
//            }
        }
    }

    @Test
    public void testJWKLoadProxyAuth() throws Exception {
        if (!username.isEmpty()) {

            HyperwalletEncryption hwEnc;

            hwEnc = new HyperwalletEncryption.HyperwalletEncryptionBuilder()
                    .encryptionAlgorithm(JWEAlgorithm.RSA_OAEP_256)
                    .encryptionMethod(EncryptionMethod.A256CBC_HS512)
                    .signAlgorithm(JWSAlgorithm.RS256)
                    .hyperwalletKeySetLocation("")
                    .clientPrivateKeySetLocation("").build();
            hwEnc.setProxy("localhost", 3128);
            hwEnc.setProxyUsername("test");
            hwEnc.setProxyPassword("test");
            client = new Hyperwallet(username, password, prgmToken, baseURL, hwEnc);
            Proxy testval = hwEnc.getProxy();
//            JWKSet keys = hwEnc.loadKeySet("http://localhost:8081/mockserver/static/jwks/client-rsa-public.json");
//
//            assertThat(keys.getKeys().size(), is(equalTo(2)));
        }
    }


    @Test
    public void testListWebhookEvents() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletList<HyperwalletWebhookNotification> returnValue;

            try {
                returnValue = client.listWebhookEvents();
            } catch (Exception e) {
                throw e;
            }

            assertThat(returnValue.hasNextPage(), is(equalTo(true)));
            assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
            assertThat(returnValue.getLimit(), is(equalTo(100)));
        }
    }

    @Test
    public void testProxyListWebhookEvents() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletList<HyperwalletWebhookNotification> returnValue;

            try {
                client.setHyperwalletProxy("localhost", 9090);
                returnValue = client.listWebhookEvents();
            } catch (Exception e) {
                throw e;
            }

            assertThat(returnValue.hasNextPage(), is(equalTo(true)));
            assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
            assertThat(returnValue.getLimit(), is(equalTo(100)));
        }
    }

    @Test
    public void testProxyAuthListWebhookEvents() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletList<HyperwalletWebhookNotification> returnValue;

            try {
                client.setHyperwalletProxy("localhost", 3128);
                client.setHyperwalletProxyUsername("test");
                client.setHyperwalletProxyPassword("test");
                returnValue = client.listWebhookEvents();
            } catch (Exception e) {
                throw e;
            }

            assertThat(returnValue.hasNextPage(), is(equalTo(true)));
            assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
            assertThat(returnValue.getLimit(), is(equalTo(100)));
        }
    }

    @Test
    public void testProxyAuthFailListWebhookEvents() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletList<HyperwalletWebhookNotification> returnValue;

            try {
                client.setHyperwalletProxy("localhost", 3128);
                client.setHyperwalletProxyUsername("test1");
                client.setHyperwalletProxyPassword("test1");
                returnValue = client.listWebhookEvents();
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
                    .stateProvince("CA")
                    .governmentIdType(HyperwalletUser.GovernmentIdType.PASSPORT);

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
                    .stateProvince("CA")
                    .governmentIdType(HyperwalletUser.GovernmentIdType.PASSPORT);

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
                    .stateProvince("CA")
                    .governmentIdType(HyperwalletUser.GovernmentIdType.PASSPORT);

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
                    .stateProvince("CA")
                    .governmentIdType(HyperwalletUser.GovernmentIdType.PASSPORT);

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
    public void testUploadStakeholderDocumentsWithInvalidData() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletBusinessStakeholder returnValue;
            HyperwalletVerificationDocument hyperwalletVerificationDocument = new HyperwalletVerificationDocument();
            try {
                String userToken = "usr-490848fb-8e1f-4f7c-9a18-a5b7a372e602";
                String stkToken = "stk-e08f13b8-0e54-43d2-a587-67d513633275";
                Multipart multipart = new Multipart();
                List<HyperwalletVerificationDocument> documentList = new ArrayList<>();
                hyperwalletVerificationDocument.setType("DRIVERS_LICENSE");
                hyperwalletVerificationDocument.setCategory("IDENTIFICATION");
                hyperwalletVerificationDocument.setCountry("US");
                Map<String, String> fileList = new HashMap<>();
                fileList.put("drivers_license_front", "src/test/resources/integration/test.png");
                fileList.put("drivers_license_back", "src/test/resources/integration/test.png");
                hyperwalletVerificationDocument.setUploadFiles(fileList);
                documentList.add(hyperwalletVerificationDocument);
                returnValue = client.uploadStakeholderDocuments(userToken, stkToken, documentList);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("User not found for token usr-490848fb-8e1f-4f7c-9a18-a5b7a372e602")));
            }
        }
    }

    @Test
    public void testProxyUploadStakeholderDocumentsWithInvalidData() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletBusinessStakeholder returnValue;
            HyperwalletVerificationDocument hyperwalletVerificationDocument = new HyperwalletVerificationDocument();
            try {
                String userToken = "usr-490848fb-8e1f-4f7c-9a18-a5b7a372e602";
                String stkToken = "stk-e08f13b8-0e54-43d2-a587-67d513633275";
                Multipart multipart = new Multipart();
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
                returnValue = client.uploadStakeholderDocuments(userToken, stkToken, documentList);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("User not found for token usr-490848fb-8e1f-4f7c-9a18-a5b7a372e602")));
            }
        }
    }

    @Test
    public void testProxyAuthUploadStakeholderDocumentsWithInvalidData() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletBusinessStakeholder returnValue;
            HyperwalletVerificationDocument hyperwalletVerificationDocument = new HyperwalletVerificationDocument();
            try {
                String userToken = "usr-490848fb-8e1f-4f7c-9a18-a5b7a372e602";
                String stkToken = "stk-e08f13b8-0e54-43d2-a587-67d513633275";
                Multipart multipart = new Multipart();
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
                returnValue = client.uploadStakeholderDocuments(userToken, stkToken, documentList);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("User not found for token usr-490848fb-8e1f-4f7c-9a18-a5b7a372e602")));
            }
        }
    }

    @Test
    public void testProxyAuthFailUploadStakeholderDocumentsWithInvalidData() throws Exception {
        if (!username.isEmpty()) {
            HyperwalletBusinessStakeholder returnValue;
            HyperwalletVerificationDocument hyperwalletVerificationDocument = new HyperwalletVerificationDocument();
            try {
                String userToken = "usr-490848fb-8e1f-4f7c-9a18-a5b7a372e602";
                String stkToken = "stk-e08f13b8-0e54-43d2-a587-67d513633275";
                Multipart multipart = new Multipart();
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
                returnValue = client.uploadStakeholderDocuments(userToken, stkToken, documentList);
            } catch (Exception e) {
                assertThat(e.getMessage(), is(containsString("Proxy Authentication Required")));
            }
        }
    }
}
