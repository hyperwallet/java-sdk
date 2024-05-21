package com.hyperwallet.clientsdk.util;

import com.hyperwallet.clientsdk.HyperwalletException;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEEncrypter;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.ECDHDecrypter;
import com.nimbusds.jose.crypto.ECDHEncrypter;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyType;
import com.nimbusds.jose.jwk.RSAKey;


import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class HyperwalletEncryption {

    private static final String EXPIRATION = "exp";
    private static final Integer MILLISECONDS_IN_ONE_MINUTE = 60000;
    private static final Long MILLISECONDS_IN_SECOND = 1000L;
    private static final Integer EXPIRATION_MINUTES = 5;
    private static final JWEAlgorithm ENCRYPTION_ALGORITHM = JWEAlgorithm.RSA_OAEP_256;
    private static final JWSAlgorithm SIGN_ALGORITHM = JWSAlgorithm.RS256;
    private static final EncryptionMethod ENCRYPTION_METHOD = EncryptionMethod.A256CBC_HS512;
    private static final String INVALID_KEY_TYPE_STRING = "'kty' not supported = %s";

    private static final List<JWEAlgorithm> SUPPORTED_JWE_ALGORITHMS = Arrays.asList(JWEAlgorithm.RSA_OAEP_256,
            JWEAlgorithm.ECDH_ES,
            JWEAlgorithm.ECDH_ES_A128KW,
            JWEAlgorithm.ECDH_ES_A192KW,
            JWEAlgorithm.ECDH_ES_A256KW);
    private static final List<JWSAlgorithm> SUPPORTED_JWS_ALGORITHMS = Arrays.asList(JWSAlgorithm.RS256,
            JWSAlgorithm.RS384,
            JWSAlgorithm.RS512,
            JWSAlgorithm.PS256,
            JWSAlgorithm.PS384,
            JWSAlgorithm.PS512,
            JWSAlgorithm.ES256,
            JWSAlgorithm.ES384,
            JWSAlgorithm.ES512);
    private static final List<EncryptionMethod> SUPPORTED_ENCRYPTION_METHODS = Arrays.asList(EncryptionMethod.A128CBC_HS256,
            EncryptionMethod.A192CBC_HS384,
            EncryptionMethod.A256CBC_HS512,
            EncryptionMethod.A128GCM,
            EncryptionMethod.A256GCM);

    private final JWEAlgorithm encryptionAlgorithm;
    private final JWSAlgorithm signAlgorithm;
    private final EncryptionMethod encryptionMethod;
    private final String clientPrivateKeySetLocation;
    private final String hyperwalletKeySetLocation;
    private final Integer jwsExpirationMinutes;

    private Proxy proxy;
    private String proxyUsername;
    private String proxyPassword;

    public HyperwalletEncryption(JWEAlgorithm encryptionAlgorithm, JWSAlgorithm signAlgorithm, EncryptionMethod encryptionMethod,
            String clientPrivateKeySetLocation, String hyperwalletKeySetLocation, Integer jwsExpirationMinutes) {
        this.encryptionAlgorithm = encryptionAlgorithm == null ? ENCRYPTION_ALGORITHM : encryptionAlgorithm;
        this.signAlgorithm = signAlgorithm == null ? SIGN_ALGORITHM : signAlgorithm;
        this.encryptionMethod = encryptionMethod == null ? ENCRYPTION_METHOD : encryptionMethod;
        this.clientPrivateKeySetLocation = clientPrivateKeySetLocation;
        this.hyperwalletKeySetLocation = hyperwalletKeySetLocation;
        this.jwsExpirationMinutes = jwsExpirationMinutes == null ? EXPIRATION_MINUTES : jwsExpirationMinutes;

        if (!SUPPORTED_JWS_ALGORITHMS.contains(this.signAlgorithm)) {
            throw new IllegalArgumentException("Unsupported signing algorithm " + this.signAlgorithm);
        }
        if (!SUPPORTED_JWE_ALGORITHMS.contains(this.encryptionAlgorithm)) {
            throw new IllegalArgumentException("Unsupported encryption algorithm " + this.encryptionAlgorithm);
        }
        if (!SUPPORTED_ENCRYPTION_METHODS.contains(this.encryptionMethod)) {
            throw new IllegalArgumentException("Unsupported encryption method " + this.encryptionMethod);
        }
    }

    public String encrypt(String body) throws JOSEException, IOException, ParseException {

        JWK clientPrivateKey = getKeyByAlgorithm(loadClientPrivateKeySet(), signAlgorithm);
        JWK hyperwalletPublicKey = getKeyByAlgorithm(loadHyperwalletKeySet(), encryptionAlgorithm);
        JWSSigner jwsSigner = getJWSSigner(clientPrivateKey);
        JWEEncrypter jweEncrypter = getJWEEncrypter(hyperwalletPublicKey);

        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(signAlgorithm)
                        .keyID(clientPrivateKey.getKeyID())
                        .criticalParams(new HashSet<>(Collections.singletonList(EXPIRATION)))
                        .customParam(EXPIRATION, getJWSExpirationMillis()).build(),
                new Payload(body));

        jwsObject.sign(jwsSigner);

        JWEObject jweObject = new JWEObject(
                new JWEHeader.Builder(encryptionAlgorithm, encryptionMethod)
                        .keyID(hyperwalletPublicKey.getKeyID()).build(),
                new Payload(jwsObject));

        jweObject.encrypt(jweEncrypter);

        return jweObject.serialize();
    }

    public String decrypt(String body) throws ParseException, IOException, JOSEException {

        JWK privateKeyToDecrypt = getKeyByAlgorithm(loadClientPrivateKeySet(), encryptionAlgorithm);
        JWK publicKeyToSign = getKeyByAlgorithm(loadHyperwalletKeySet(), signAlgorithm);
        JWEDecrypter jweDecrypter = getJWEDecrypter(privateKeyToDecrypt);
        JWSVerifier jwsVerifier = getJWSVerifier(publicKeyToSign);

        JWEObject jweObject = JWEObject.parse(body);
        jweObject.decrypt(jweDecrypter);
        JWSObject jwsObject = jweObject.getPayload().toJWSObject();
        verifySignatureExpirationDate(jwsObject.getHeader().getCustomParam(EXPIRATION));
        boolean verifyStatus = jwsObject.verify(jwsVerifier);
        if (!verifyStatus) {
            throw new HyperwalletException("JWS signature is incorrect");
        }
        return jwsObject.getPayload().toString();
    }

    protected JWKSet loadClientPrivateKeySet() throws IOException, ParseException {
        return loadKeySet(clientPrivateKeySetLocation);
    }

    protected JWKSet loadHyperwalletKeySet() throws IOException, ParseException {
        return loadKeySet(hyperwalletKeySetLocation);
    }

    public void verifySignatureExpirationDate(Object signatureExpirationDate) {
        if (signatureExpirationDate == null) {
            throw new HyperwalletException("exp JWS header param was null");
        }
        if (!(signatureExpirationDate instanceof Long)) {
            throw new HyperwalletException("exp JWS header must be of type Long");
        }
        long expirationTimeSeconds = (long) signatureExpirationDate;
        if (new Date().getTime() / MILLISECONDS_IN_SECOND > expirationTimeSeconds) {
            throw new HyperwalletException("Response message signature(JWS) has expired");
        }
    }

    public JWEAlgorithm getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public JWSAlgorithm getSignAlgorithm() {
        return signAlgorithm;
    }

    public EncryptionMethod getEncryptionMethod() {
        return encryptionMethod;
    }

    public String getClientPrivateKeySetLocation() {
        return clientPrivateKeySetLocation;
    }

    public String getHyperwalletKeySetLocation() {
        return hyperwalletKeySetLocation;
    }

    public Integer getJwsExpirationMinutes() {
        return jwsExpirationMinutes;
    }

    private JWKSet loadKeySet(String keySetLocation) throws IOException, ParseException {
        URL url;
        try {
            url = new URL(keySetLocation);
        } catch (MalformedURLException e) {
            checkKeySetLocationIsFile(keySetLocation);
            return JWKSet.load(new File(keySetLocation));
        }

        if (usesProxy()) {
            if (proxyUsername != null && proxyPassword != null) {
                Authenticator authenticator = new Request.DefaultPasswordAuthenticator(
                        proxyUsername, proxyPassword);
                Authenticator.setDefault(authenticator);
            }
            return JWKSet.load(url, 0, 0, 0, getProxy());
        }
        return JWKSet.load(url);
    }

    private long getJWSExpirationMillis() {
        return new Date(new Date().getTime() + MILLISECONDS_IN_ONE_MINUTE * jwsExpirationMinutes).getTime() / MILLISECONDS_IN_SECOND;
    }

    private <T extends Algorithm> JWK getKeyByAlgorithm(JWKSet keySet, T algorithm) {
        for (JWK key : keySet.getKeys()) {
            if (key.getAlgorithm().equals(algorithm)) {
                return key;
            }
        }
        throw new IllegalStateException("Algorithm = " + algorithm + " is not found in client or Hyperwallet key set");
    }

    private void checkKeySetLocationIsFile(String keySetLocation) {
        if (Files.notExists(Paths.get(keySetLocation))) {
            throw new IllegalArgumentException("Wrong client JWK set location");
        }
    }

    private JWSSigner getJWSSigner(JWK jwk) {
        try {
            KeyType kty = jwk.getKeyType();
            if (kty.equals(KeyType.RSA)) {
                return new RSASSASigner((RSAKey) jwk);
            } else if (kty.equals(KeyType.EC)) {
                return new ECDSASigner((ECKey) jwk);
            } else {
                throw new IllegalArgumentException(String.format(INVALID_KEY_TYPE_STRING, kty));
            }
        } catch (JOSEException e) {
            throw new HyperwalletException("Unable to create signer");
        }
    }

    private JWEEncrypter getJWEEncrypter(JWK jwk) {
        try {
            KeyType kty = jwk.getKeyType();
            if (kty.equals(KeyType.RSA)) {
                return new RSAEncrypter((RSAKey) jwk);
            } else if (kty.equals(KeyType.EC)) {
                return new ECDHEncrypter((ECKey) jwk);
            } else {
                throw new IllegalArgumentException(String.format(INVALID_KEY_TYPE_STRING, kty));
            }
        } catch (JOSEException e) {
            throw new HyperwalletException("Unable to create encrypter");
        }
    }

    private JWSVerifier getJWSVerifier(JWK jwk) {
        try {
            KeyType kty = jwk.getKeyType();
            if (kty.equals(KeyType.RSA)) {
                return new RSASSAVerifier(((RSAKey) jwk).toRSAPublicKey(), new HashSet<>(Collections.singletonList(EXPIRATION)));
            } else if (kty.equals(KeyType.EC)) {
                return new ECDSAVerifier(((ECKey) jwk).toECPublicKey(), new HashSet<>(Collections.singletonList(EXPIRATION)));
            } else {
                throw new IllegalArgumentException(String.format(INVALID_KEY_TYPE_STRING, kty));
            }
        } catch (JOSEException e) {
            throw new HyperwalletException("Unable to create verifier");
        }
    }

    private JWEDecrypter getJWEDecrypter(JWK jwk) {
        try {
            KeyType kty = jwk.getKeyType();
            if (kty.equals(KeyType.RSA)) {
                return new RSADecrypter((RSAKey) jwk);
            } else if (kty.equals(KeyType.EC)) {
                return new ECDHDecrypter((ECKey) jwk);
            } else {
                throw new IllegalArgumentException(String.format(INVALID_KEY_TYPE_STRING, kty));
            }
        } catch (JOSEException e) {
            throw new HyperwalletException("Unable to create decrypter");
        }
    }

    public Boolean usesProxy() {
        return proxy != null;
    }

    public void setProxy(String url, Integer port) {
        this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(url, port));
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public static class HyperwalletEncryptionBuilder {

        private JWEAlgorithm encryptionAlgorithm;
        private JWSAlgorithm signAlgorithm;
        private EncryptionMethod encryptionMethod;
        private String clientPrivateKeySetLocation;
        private String hyperwalletKeySetLocation;
        private Integer jwsExpirationMinutes;

        private Proxy proxy;
        private String proxyUsername;
        private String proxyPassword;

        public HyperwalletEncryptionBuilder encryptionAlgorithm(JWEAlgorithm encryptionAlgorithm) {
            this.encryptionAlgorithm = encryptionAlgorithm;
            return this;
        }

        public HyperwalletEncryptionBuilder signAlgorithm(JWSAlgorithm signAlgorithm) {
            this.signAlgorithm = signAlgorithm;
            return this;
        }

        public HyperwalletEncryptionBuilder encryptionMethod(EncryptionMethod encryptionMethod) {
            this.encryptionMethod = encryptionMethod;
            return this;
        }

        public HyperwalletEncryptionBuilder clientPrivateKeySetLocation(String clientPrivateKeySetLocation) {
            this.clientPrivateKeySetLocation = clientPrivateKeySetLocation;
            return this;
        }

        public HyperwalletEncryptionBuilder hyperwalletKeySetLocation(String hyperwalletKeySetLocation) {
            this.hyperwalletKeySetLocation = hyperwalletKeySetLocation;
            return this;
        }

        public HyperwalletEncryptionBuilder jwsExpirationMinutes(Integer jwsExpirationMinutes) {
            this.jwsExpirationMinutes = jwsExpirationMinutes;
            return this;
        }

        public HyperwalletEncryptionBuilder proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public HyperwalletEncryptionBuilder proxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        public HyperwalletEncryptionBuilder proxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        public HyperwalletEncryption build() {
            HyperwalletEncryption hyperwalletEncryption = new HyperwalletEncryption(encryptionAlgorithm, signAlgorithm, encryptionMethod,
                    clientPrivateKeySetLocation, hyperwalletKeySetLocation, jwsExpirationMinutes);
            if (proxy != null) {
                hyperwalletEncryption.setProxy(proxy);
            }
            if (proxyUsername != null) {
                hyperwalletEncryption.setProxyUsername(proxyUsername);
            }
            if (proxyPassword != null) {
                hyperwalletEncryption.setProxyPassword(proxyPassword);
            }
            return hyperwalletEncryption;
        }
    }
}