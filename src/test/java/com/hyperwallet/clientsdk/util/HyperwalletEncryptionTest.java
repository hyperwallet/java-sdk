package com.hyperwallet.clientsdk.util;

import com.hyperwallet.clientsdk.HyperwalletException;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;

import org.apache.commons.io.IOUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.fail;

public class HyperwalletEncryptionTest {
    private HyperwalletEncryption hyperwalletEncryption;
    private List<String> fieldNames;

    @BeforeMethod
    public void setUp() {
        this.hyperwalletEncryption = new HyperwalletEncryption.HyperwalletEncryptionBuilder()
                .clientPrivateKeySetLocation("").hyperwalletKeySetLocation("").build();
        this.fieldNames = collectFieldNames();
    }

    @Test(dataProvider = "fieldNames")
    public void testBuilderMethod(String fieldName) throws Exception {
        Method getter = findGetter(fieldName);
        Method builderMethod = HyperwalletEncryption.HyperwalletEncryptionBuilder.class.getMethod(fieldName, getter.getReturnType());

        Object oldValue = getter.invoke(hyperwalletEncryption);
        assertThat(oldValue, is(notNullValue()));
        HyperwalletEncryption.HyperwalletEncryptionBuilder builder = new HyperwalletEncryption.HyperwalletEncryptionBuilder();
        builderMethod.invoke(builder, (Object) null);
        assertThat(getter.invoke(builder.build()), is(getDefaultValue(fieldName)));

        HyperwalletEncryption.HyperwalletEncryptionBuilder ret = (HyperwalletEncryption.HyperwalletEncryptionBuilder)builderMethod.invoke(builder, oldValue);
        assertThat(getter.invoke(ret.build()), is(equalTo(oldValue)));
    }

    @Test
    public void shouldCorrectlyEncryptAndDecryptInputText() throws IOException, ParseException, JOSEException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        String hyperwalletKeysPath = new File(classLoader.getResource("encryption/public-jwkset").toURI()).getAbsolutePath();
        String clientPrivateKeysPath = new File(classLoader.getResource("encryption/private-jwkset").toURI()).getAbsolutePath();
        String testPayload = IOUtils.toString(classLoader.getResourceAsStream("encryption/test-payload.json"));

        HyperwalletEncryption hyperwalletEncryption = new HyperwalletEncryption.HyperwalletEncryptionBuilder()
                .clientPrivateKeySetLocation(clientPrivateKeysPath).hyperwalletKeySetLocation(hyperwalletKeysPath).build();
        String encryptedPayload = hyperwalletEncryption.encrypt(testPayload);
        String payloadAfterDescription = hyperwalletEncryption.decrypt(encryptedPayload);

        assertThat("Payload text is the same after decryption" + testPayload,
                payloadAfterDescription, is(testPayload));
    }

    @Test
    public void shouldThrowExceptionWhenDecryptionIsMadeByKeyOtherThanUsedForEncryption()
            throws IOException, ParseException, JOSEException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        String hyperwalletKeysPath = "https://uat-api.paylution.com/jwkset";
        String clientPrivateKeysPath = new File(classLoader.getResource("encryption/private-jwkset").toURI()).getAbsolutePath();
        String testPayload = IOUtils.toString(classLoader.getResourceAsStream("encryption/test-payload.json"));

        HyperwalletEncryption hyperwalletEncryption = new HyperwalletEncryption.HyperwalletEncryptionBuilder()
                .clientPrivateKeySetLocation(clientPrivateKeysPath).hyperwalletKeySetLocation(hyperwalletKeysPath).build();
        String encryptedPayload = hyperwalletEncryption.encrypt(testPayload);
        try {
            hyperwalletEncryption.decrypt(encryptedPayload);
            fail("Expected JOSEException");
        } catch (JOSEException e) {
            assertThat(e.getMessage(), is(containsString("Decryption error")));
        }
    }

    @Test
    public void shouldThrowExceptionWhenEncryptionAlgorithmIsNotFoundInKeySet()
            throws URISyntaxException, IOException, ParseException, JOSEException {
        ClassLoader classLoader = getClass().getClassLoader();
        String hyperwalletKeysPath = new File(classLoader.getResource("encryption/public-jwkset").toURI()).getAbsolutePath();
        String clientPrivateKeysPath = new File(classLoader.getResource("encryption/private-jwkset").toURI()).getAbsolutePath();
        String testPayload = IOUtils.toString(classLoader.getResourceAsStream("encryption/test-payload.json"));

        HyperwalletEncryption hyperwalletEncryption = new HyperwalletEncryption.HyperwalletEncryptionBuilder()
                .clientPrivateKeySetLocation(clientPrivateKeysPath).hyperwalletKeySetLocation(hyperwalletKeysPath)
                .encryptionAlgorithm(JWEAlgorithm.A256GCMKW).build();

        try {
            hyperwalletEncryption.encrypt(testPayload);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertThat(e.getMessage(), is(containsString("Algorithm = A256GCMKW is not found in client or Hyperwallet key set")));
        }
    }

    @Test
    public void shouldThrowExceptionWhenKeySetFileIsNotFound()
            throws URISyntaxException, IOException, ParseException, JOSEException {
        ClassLoader classLoader = getClass().getClassLoader();
        String hyperwalletKeysPath = new File(classLoader.getResource("encryption/public-jwkset").toURI()).getAbsolutePath();
        String clientPrivateKeysPath = "/encryption/public-jwkset/keyset.json";
        String testPayload = IOUtils.toString(classLoader.getResourceAsStream("encryption/test-payload.json"));

        HyperwalletEncryption hyperwalletEncryption = new HyperwalletEncryption.HyperwalletEncryptionBuilder()
                .clientPrivateKeySetLocation(clientPrivateKeysPath).hyperwalletKeySetLocation(hyperwalletKeysPath).build();

        try {
            hyperwalletEncryption.encrypt(testPayload);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is(containsString("Wrong client JWK set location")));
        }
    }

    @Test
    public void shouldThrowExceptionWhenJWSSignatureExpirationDateIsNull() throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        String hyperwalletKeysPath = new File(classLoader.getResource("encryption/public-jwkset").toURI()).getAbsolutePath();
        String clientPrivateKeysPath = new File(classLoader.getResource("encryption/private-jwkset").toURI()).getAbsolutePath();

        HyperwalletEncryption hyperwalletEncryption = new HyperwalletEncryption.HyperwalletEncryptionBuilder()
                .clientPrivateKeySetLocation(clientPrivateKeysPath).hyperwalletKeySetLocation(hyperwalletKeysPath).build();

        try {
            hyperwalletEncryption.verifySignatureExpirationDate(null);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("exp JWS header param was null")));
        }
    }

    @Test
    public void shouldThrowExceptionWhenJWSSignatureExpirationDateHasIncorrectType() throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        String hyperwalletKeysPath = new File(classLoader.getResource("encryption/public-jwkset").toURI()).getAbsolutePath();
        String clientPrivateKeysPath = new File(classLoader.getResource("encryption/private-jwkset").toURI()).getAbsolutePath();

        HyperwalletEncryption hyperwalletEncryption = new HyperwalletEncryption.HyperwalletEncryptionBuilder()
                .clientPrivateKeySetLocation(clientPrivateKeysPath).hyperwalletKeySetLocation(hyperwalletKeysPath).build();

        try {
            hyperwalletEncryption.verifySignatureExpirationDate("123123");
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("exp JWS header must be of type Long")));
        }
    }

    @Test
    public void shouldThrowExceptionWhenJWSSignatureExpirationDateIsBeforeCurrentDate() throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        String hyperwalletKeysPath = new File(classLoader.getResource("encryption/public-jwkset").toURI()).getAbsolutePath();
        String clientPrivateKeysPath = new File(classLoader.getResource("encryption/private-jwkset").toURI()).getAbsolutePath();

        HyperwalletEncryption hyperwalletEncryption = new HyperwalletEncryption.HyperwalletEncryptionBuilder()
                .clientPrivateKeySetLocation(clientPrivateKeysPath).hyperwalletKeySetLocation(hyperwalletKeysPath).build();

        try {
            hyperwalletEncryption.verifySignatureExpirationDate(0L);
            fail("Expected HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getMessage(), is(containsString("Response message signature(JWS) has expired")));
        }
    }

    private Method findGetter(String fieldName) throws Exception {
        String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        return HyperwalletEncryption.class.getMethod(getterName);
    }

    private List<String> collectFieldNames() {

        List<String> fieldNames = new ArrayList<String>();
        for (Field field : HyperwalletEncryption.class.getDeclaredFields()) {
            if (!Modifier.isPrivate(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            fieldNames.add(field.getName());
        }

        return fieldNames;
    }

    @DataProvider(name = "fieldNames")
    public Iterator<Object[]> createFieldNamesProvider() {
        List<String> fieldNames = collectFieldNames();
        List<Object[]> objects = new ArrayList<Object[]>(fieldNames.size());
        for (String fieldName : fieldNames) {
            objects.add(new Object[]{fieldName});
        }
        return objects.iterator();
    }

    private Object getDefaultValue(String fieldName) {
        switch(fieldName) {
            case "encryptionAlgorithm":
                return JWEAlgorithm.RSA_OAEP_256;
            case "signAlgorithm":
                return JWSAlgorithm.RS256;
            case "encryptionMethod":
                return EncryptionMethod.A256CBC_HS512;
            case "jwsExpirationMinutes":
                return 5;
            default:
                return null;
        }
    }
}
