package com.hyperwallet.clientsdk;

import com.hyperwallet.clientsdk.util.MultipartUtility;
import net.minidev.json.JSONObject;

import javax.net.ssl.*;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import com.hyperwallet.clientsdk.HyperwalletException;

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
   /*     Hyperwallet client = new Hyperwallet("selrestuser@1861681",
                "Password1!", "prg-eedaf875-01f1-4524-8b94-d4936255af78",
                "https://localhost-hyperwallet.aws.paylution.net:8181");*/

        Hyperwallet client = new Hyperwallet("selrestuser@1861681",
                "Password1!", "prg-82499161-57d3-4160-8209-85db18d62c02",
                "https://localhost-hyperwallet.aws.paylution.net:8181");

        try {

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[]{new Test.DefaultTrustManager()}, new SecureRandom());
            SSLContext.setDefault(ctx);
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> hostname.contains("localhost"));
            String pair = "selrestuser@1861681" + ":" + "Password1!";
            final String base64 = DatatypeConverter.printBase64Binary(pair.getBytes());

            String userToken = "usr-d45a3603-1359-4c25-83d4-7b1c496861d3";
            String businessToken = "stk-6d3c7047-2da9-4f55-95cd-80219ef93669";

            String charset = "UTF-8";
            File uploadFile1 = new File("/Users/amylavarapu/Desktop/drivers_license_front.jpg");
            File uploadFile2 = new File("/Users/amylavarapu/Desktop/drivers_license_back.jpg");
//            String requestURL = "https://localhost-hyperwallet.aws.paylution.net:8181" + "/rest/v4/users/" + userToken + "/business-stakeholders/" + businessToken;
            String requestURL = "https://localhost-hyperwallet.aws.paylution.net:8181" + "/rest/v4/users/" + userToken;//  + "/business-stakeholders/" + businessToken;

            MultipartUtility multipart = new MultipartUtility(requestURL,"selrestuser@1861681",
                        "Password1!");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", "DRIVERS_LICENSE");
            jsonObject.put("country", "US");
            jsonObject.put("category", "IDENTIFICATION");
            List<JSONObject> jsonObjectList = new ArrayList<>();
            jsonObjectList.add(jsonObject);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("documents", jsonObjectList);
            //multipart.addHeaderField("Authorization","Basic " + base64);
            multipart.addFormField("data",jsonObject1.toString());
            multipart.addFilePart("drivers_license_front", uploadFile1);
            multipart.addFilePart("drivers_license_back", uploadFile2);

            System.out.println("SERVER REPLIED:");
 //           client.uploadDocumentBusinessStakeholder(userToken,businessToken, multipart);
            client.documentUpload(userToken, multipart);

           // multipart.finish();

            /*
               Get method working fine

            URL url = new URL("https://localhost-hyperwallet.aws.paylution.net:8181/rest/v4/users/usr-ba9ae777-33d4-4ad5-b0f0-ecf46f7109ba");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization","Basic " + base64);
            conn.setRequestProperty("Content-Type", "application/json");
            int code = conn.getResponseCode();
            System.out.println("code>>"+code+"<<");
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                System.err.println(line);
            }
            reader.close();
            conn.disconnect();

*/

        } catch (HyperwalletException e) {
            System.out.println("Exception occure" + e.getErrorMessage());
            System.out.println("Exception occure" + e.getErrorCode());
            System.out.println("Exception occure" + e.getCause());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
