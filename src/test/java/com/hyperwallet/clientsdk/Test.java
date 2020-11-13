package com.hyperwallet.clientsdk;

import com.hyperwallet.clientsdk.model.HyperwalletVerificationDocument;

import javax.net.ssl.*;
import javax.xml.bind.DatatypeConverter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    static class DefaultTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public static void main(String agrs[]) throws Exception {

        Hyperwallet client = new Hyperwallet("selrestuser@1861681",
                "Password1!", "prg-82499161-57d3-4160-8209-85db18d62c02",
                "https://localhost-hyperwallet.aws.paylution.net:8181");

        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
            SSLContext.setDefault(ctx);
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> hostname.contains("localhost"));
            String pair = "selrestuser@1861681" + ":" + "Password1!";
            final String base64 = DatatypeConverter.printBase64Binary(pair.getBytes());

            String userToken = "usr-c725f318-bbf6-46ac-84c3-dabfb33eef82";
            List<HyperwalletVerificationDocument> documentList = new ArrayList<>();
            HyperwalletVerificationDocument hyperwalletVerificationDocument1 = new HyperwalletVerificationDocument();;
            hyperwalletVerificationDocument1.setType("DRIVERS_LICENSE");
            hyperwalletVerificationDocument1.setCategory("IDENTIFICATION");
            hyperwalletVerificationDocument1.setCountry("US");
            Map<String, String> fileList =  new HashMap<>();
            fileList.put("drivers_license_front",  "/Users/ramahalingam/Desktop/L1.png");
            fileList.put("drivers_license_back",  "/Users/ramahalingam/Desktop/L2.png");
            hyperwalletVerificationDocument1.setUploadFiles(fileList);
            documentList.add(hyperwalletVerificationDocument1);
            client.uploadUserDocuments(userToken, documentList);
        } catch (HyperwalletException e) {
            System.out.println("Exception occured::" + e.getErrorMessage());
            System.out.println("Exception occured::" + e.getErrorCode());
            System.out.println("Exception occured::" + e.getCause());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}

