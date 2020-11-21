package com.hyperwallet.clientsdk.util;

import com.hyperwallet.clientsdk.model.HyperwalletVerificationDocument;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class HyperwalletMultipartUtilsTest  {

   @Test
   public void testConvertSucessfull() throws Exception {
       HyperwalletVerificationDocument hyperwalletVerificationDocument = new HyperwalletVerificationDocument();;
       List<HyperwalletVerificationDocument> documentList = new ArrayList<>();
       hyperwalletVerificationDocument.setType("DRIVERS_LICENSE");
       hyperwalletVerificationDocument.setCategory("IDENTIFICATION");
       hyperwalletVerificationDocument.setCountry("US");
       Map<String, String> fileList =  new HashMap<>();
       fileList.put("drivers_license_front",  "src/test/resources/integration/test.png");
       fileList.put("drivers_license_back",  "src/test/resources/integration/test1.png");
       hyperwalletVerificationDocument.setUploadFiles(fileList);
       documentList.add(hyperwalletVerificationDocument);
       Multipart multipart = HyperwalletMultipartUtils.convert(documentList);
       assertThat(multipart.getMultipartList(), hasSize(3));
   }
}
