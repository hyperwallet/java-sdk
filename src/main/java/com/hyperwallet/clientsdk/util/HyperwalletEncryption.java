package com.hyperwallet.clientsdk.util;

import com.hyperwallet.clientsdk.HyperwalletException;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

public class HyperwalletEncryption {

    private static final String EXPIRATION = "exp";
    private static final Integer MILLISECONDS_IN_ONE_MINUTE = 60000;
    private static final Long MILLISECONDS_IN_SECOND = 1000L;
    private static final Integer EXPIRATION_MINUTES = 5;
    private static final JWEAlgorithm ENCRYPTION_ALGORITHM = JWEAlgorithm.RSA_OAEP_256;
    private static final JWSAlgorithm SIGN_ALGORITHM = JWSAlgorithm.RS256;
    private static final EncryptionMethod ENCRYPTION_METHOD = EncryptionMethod.A256CBC_HS512;

    private JWEAlgorithm encryptionAlgorithm;
    private JWSAlgorithm signAlgorithm;
    private EncryptionMethod encryptionMethod;
    private String clientPrivateKeySetLocation;
    private String hyperwalletKeySetLocation;
    private Integer jwsExpirationMinutes;

    public HyperwalletEncryption(JWEAlgorithm encryptionAlgorithm, JWSAlgorithm signAlgorithm, EncryptionMethod encryptionMethod,
                                 String clientPrivateKeySetLocation, String hyperwalletKeySetLocation, Integer jwsExpirationMinutes) {
        this.encryptionAlgorithm = encryptionAlgorithm == null ? ENCRYPTION_ALGORITHM : encryptionAlgorithm;
        this.signAlgorithm = signAlgorithm == null ? SIGN_ALGORITHM : signAlgorithm;
        this.encryptionMethod = encryptionMethod == null ? ENCRYPTION_METHOD : encryptionMethod;
        this.clientPrivateKeySetLocation = clientPrivateKeySetLocation;
        this.hyperwalletKeySetLocation = hyperwalletKeySetLocation;
        this.jwsExpirationMinutes = jwsExpirationMinutes == null ? EXPIRATION_MINUTES : jwsExpirationMinutes;
    }

    public String encrypt(String body) throws JOSEException, IOException, ParseException {

        JWK clientPrivateKey = getKeyByAlgorithm(loadKeySet(clientPrivateKeySetLocation), signAlgorithm);
        JWK hyperwalletPublicKey = getKeyByAlgorithm(loadKeySet(hyperwalletKeySetLocation), encryptionAlgorithm);

        JWSSigner signer = new RSASSASigner((RSAKey)clientPrivateKey);

        JWSObject jwsObject = new JWSObject(
                new JWSHeader.Builder(signAlgorithm)
                        .keyID(clientPrivateKey.getKeyID())
                        .criticalParams(new HashSet<>(Collections.singletonList(EXPIRATION)))
                        .customParam(EXPIRATION, getJWSExpirationMillis()).build(),
                new Payload(body));

        jwsObject.sign(signer);

        JWEObject jweObject = new JWEObject(
                new JWEHeader.Builder(encryptionAlgorithm, encryptionMethod)
                        .keyID(hyperwalletPublicKey.getKeyID()).build(),
                new Payload(jwsObject));

        jweObject.encrypt(new RSAEncrypter((RSAKey)hyperwalletPublicKey));

        return jweObject.serialize();
    }

    public String decrypt(String body) throws ParseException, IOException, JOSEException {

        JWK privateKeyToDecrypt = getKeyByAlgorithm(loadKeySet(clientPrivateKeySetLocation), encryptionAlgorithm);
        RSAPublicKey publicKeyToSign = ((RSAKey)getKeyByAlgorithm(loadKeySet(hyperwalletKeySetLocation), signAlgorithm))
                .toRSAPublicKey();

        JWEObject jweObject = JWEObject.parse(body);
        jweObject.decrypt(new RSADecrypter((RSAKey)privateKeyToDecrypt));
        JWSObject jwsObject = jweObject.getPayload().toJWSObject();
        verifySignatureExpirationDate(jwsObject.getHeader().getCustomParam(EXPIRATION));
        boolean verifyStatus = jwsObject.verify(new RSASSAVerifier(publicKeyToSign,
                new HashSet<>(Collections.singletonList(EXPIRATION))));
        if(!verifyStatus) {
            throw new HyperwalletException("JWS signature is incorrect");
        }
        return jwsObject.getPayload().toString();
    }

    public void verifySignatureExpirationDate(Object signatureExpirationDate) {
        if (signatureExpirationDate == null) {
            throw new HyperwalletException("exp JWS header param was null");
        }
        if (!(signatureExpirationDate instanceof Long)) {
            throw new HyperwalletException("exp JWS header must be of type Long");
        }
        long expirationTimeSeconds = (long)signatureExpirationDate;
        if (new Date().getTime()/MILLISECONDS_IN_SECOND > expirationTimeSeconds) {
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
        return JWKSet.load(url);
    }

    private long getJWSExpirationMillis() {
        return new Date(new Date().getTime() + MILLISECONDS_IN_ONE_MINUTE * jwsExpirationMinutes).getTime()/MILLISECONDS_IN_SECOND;
    }

    private <T extends Algorithm> JWK getKeyByAlgorithm(JWKSet keySet, T algorithm) {
        for(JWK key : keySet.getKeys()) {
            if(key.getAlgorithm().equals(algorithm)) {
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

    public static class HyperwalletEncryptionBuilder {
        private JWEAlgorithm encryptionAlgorithm;
        private JWSAlgorithm signAlgorithm;
        private EncryptionMethod encryptionMethod;
        private String clientPrivateKeySetLocation;
        private String hyperwalletKeySetLocation;
        private Integer jwsExpirationMinutes;

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

        public HyperwalletEncryption build() {
            return new HyperwalletEncryption(encryptionAlgorithm, signAlgorithm, encryptionMethod,
                    clientPrivateKeySetLocation, hyperwalletKeySetLocation, jwsExpirationMinutes);
        }
    }
}