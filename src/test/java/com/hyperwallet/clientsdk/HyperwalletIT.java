package com.hyperwallet.clientsdk;

import com.hyperwallet.clientsdk.model.*;
import com.hyperwallet.clientsdk.model.HyperwalletPrepaidCard.Brand;
import com.hyperwallet.clientsdk.model.HyperwalletPrepaidCard.EReplacePrepaidCardReason;
import com.hyperwallet.clientsdk.model.HyperwalletStatusTransition.Status;
import com.hyperwallet.clientsdk.model.HyperwalletTransfer.ForeignExchange;
import com.hyperwallet.clientsdk.model.HyperwalletTransferMethod.CardType;
import com.hyperwallet.clientsdk.model.HyperwalletTransferMethod.Type;
import com.hyperwallet.clientsdk.model.HyperwalletTransferMethod.VerificationStatus;
import com.hyperwallet.clientsdk.model.HyperwalletUser.Gender;
import com.hyperwallet.clientsdk.model.HyperwalletUser.GovernmentIdType;
import com.hyperwallet.clientsdk.model.HyperwalletUser.ProfileType;
import com.hyperwallet.clientsdk.model.HyperwalletVerificationDocumentReason.RejectReason;
import com.hyperwallet.clientsdk.util.Multipart;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hyperwallet.clientsdk.model.HyperwalletStatusTransition.Status.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.JsonBody.json;
import static org.testng.Assert.*;

public class HyperwalletIT {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z");

    private static ClientAndServer mockServer;
    private Hyperwallet client;

    @BeforeClass
    public static void startServer() {
        mockServer = startClientAndServer(Integer.parseInt(System.getProperty("MOCK_SERVER_PORT", "2016")));
    }

    @AfterClass
    public static void stopServer() {
        mockServer.stop();
    }

    @BeforeMethod
    public void setUp() {
        String baseUrl = "http://localhost:" + mockServer.getPort();
        client = new Hyperwallet("test-username", "test-password", "test-program-token", baseUrl);
    }

    //
    // User
    //

    @Test
    public void testUploadUserDocuments() throws Exception {
        String functionality = "uploadUserDocuments";
        initMockServer(functionality);

        HyperwalletUser returnValue;
        HyperwalletVerificationDocument hyperwalletVerificationDocument = new HyperwalletVerificationDocument();;
        try {
            String userToken = "usr-62f24150-5756-4234-9154-90ee4eed328b";
            Multipart multipart = new Multipart();
            List<HyperwalletVerificationDocument> documentList = new ArrayList<>();
            hyperwalletVerificationDocument.setType("DRIVERS_LICENSE");
            hyperwalletVerificationDocument.setCategory("IDENTIFICATION");
            hyperwalletVerificationDocument.setCountry("US");
            Map<String, String> fileList =  new HashMap<>();
            fileList.put("drivers_license_front",  "src/test/resources/integration/test.png");
            fileList.put("drivers_license_back",  "src/test/resources/integration/test.png");
            hyperwalletVerificationDocument.setUploadFiles(fileList);
            documentList.add(hyperwalletVerificationDocument);
            returnValue=client.uploadUserDocuments(userToken, documentList);

        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        HyperwalletVerificationDocument document = new HyperwalletVerificationDocument();
        assertThat(returnValue.getToken(), is(equalTo("usr-62f24150-5756-4234-9154-90ee4eed328b")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletUser.Status.PRE_ACTIVATED)));
        assertThat(returnValue.getVerificationStatus(), is(equalTo(HyperwalletUser.VerificationStatus.UNDER_REVIEW)));
        assertThat(hyperwalletVerificationDocument.getCategory(), is(equalTo("IDENTIFICATION")));
        assertThat(hyperwalletVerificationDocument.getType(), is(equalTo("DRIVERS_LICENSE")));
        assertThat(hyperwalletVerificationDocument.getCountry(), is(equalTo("US")));
    }

    @Test
    public void uploadUserDocuments_invalidDocumentStatus() throws Exception {
        String functionality = "uploadUserDocumentsWithInvalidStatus";
        initMockServer(functionality);

        HyperwalletUser returnValue;
        HyperwalletVerificationDocument hyperwalletVerificationDocument = new HyperwalletVerificationDocument();
        try {
            String userToken = "usr-62f24150-5756-4234-9154-90ee4eed328b";
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
            returnValue = client.uploadUserDocuments(userToken, documentList);

        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        assertThat(returnValue.getToken(), is(equalTo("usr-62f24150-5756-4234-9154-90ee4eed328b")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletUser.Status.PRE_ACTIVATED)));
        assertThat(returnValue.getVerificationStatus(), is(equalTo(HyperwalletUser.VerificationStatus.UNDER_REVIEW)));
        assertThat(returnValue.getDocuments().get(0).getCategory(), is(equalTo("IDENTIFICATION")));
        assertThat(returnValue.getDocuments().get(0).getType(), is(equalTo("DRIVERS_LICENSE")));
        assertThat(returnValue.getDocuments().get(0).getCountry(), is(equalTo("US")));
        assertThat(returnValue.getDocuments().get(0).getStatus(), is("INVALID"));
        assertThat(returnValue.getDocuments().get(0).getReasons().size(), is(2));
        assertThat(returnValue.getDocuments().get(0).getReasons().get(0).getName(), is(RejectReason.DOCUMENT_CORRECTION_REQUIRED));
        assertThat(returnValue.getDocuments().get(0).getReasons().get(0).getDescription(), is("Document requires correction"));
        assertThat(returnValue.getDocuments().get(0).getReasons().get(1).getName(), is(RejectReason.DOCUMENT_NOT_DECISIVE));
        assertThat(returnValue.getDocuments().get(0).getReasons().get(1).getDescription(),
                is("Decision cannot be made based on document. Alternative document required"));
        assertThat(returnValue.getDocuments().get(0).getCreatedOn(), is(dateFormat.parse("2020-11-24T19:05:02 UTC")));
    }

    @Test
    public void testListUserStatusTransitions() throws Exception {
        String functionality = "listUserStatusTransitions";
        initMockServer(functionality);

        HyperwalletList<HyperwalletStatusTransition> returnValue;
        try {
            returnValue = client.listUserStatusTransitions("usr-2c059341-8281-4d30-a65d-a49d8e2a9b0f");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.hasNextPage(), is(equalTo(false)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("sts-1f7f58a9-22e8-4fef-8d6e-a17e2c71db33")));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T01:55:34 UTC"))));
        assertThat(returnValue.getData().get(0).getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getData().get(0).getToStatus(), is(equalTo(DE_ACTIVATED)));
    }

    @Test
    public void testGetUserStatusTransition() throws Exception {
        String functionality = "getUserStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setNotes("Closing this account.");
        transition.setTransition(DE_ACTIVATED);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.getUserStatusTransition("usr-2c059341-8281-4d30-a65d-a49d8e2a9b0f",
                                                            "sts-1f7f58a9-22e8-4fef-8d6e-a17e2c71db33");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("sts-1f7f58a9-22e8-4fef-8d6e-a17e2c71db33")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T02:04:17 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
    }

    @Test
    public void testUpdateUserVerificationStatus() throws Exception {
        String functionality = "updateUserVerificationStatus";
        initMockServer(functionality);
        HyperwalletUser hyperwalletUser = new HyperwalletUser();
            hyperwalletUser.token("usr-b8e7ff1d-a3c6-45a0-ae0a-62b74580caca");
            hyperwalletUser.verificationStatus(HyperwalletUser.VerificationStatus.REQUESTED);

        HyperwalletUser returnValue;
            try {
            returnValue = client.updateUser(hyperwalletUser);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("usr-b8e7ff1d-a3c6-45a0-ae0a-62b74580caca")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletUser.Status.PRE_ACTIVATED)));
        assertThat(returnValue.getVerificationStatus(), is(equalTo(HyperwalletUser.VerificationStatus.REQUIRED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2020-10-01T22:22:01 UTC"))));
        assertThat(returnValue.getClientUserId(), is(equalTo("sf1601571120")));
        assertThat(returnValue.getProfileType(), is(equalTo(ProfileType.BUSINESS)));
        assertThat(returnValue.getBusinessName(), is(equalTo("Big Baller Payments")));
        assertThat(returnValue.getBusinessRegistrationId(), is(equalTo("12341212")));
        assertThat(returnValue.getBusinessRegistrationStateProvince(), is(equalTo("test")));
        assertThat(returnValue.getBusinessRegistrationCountry(), is(equalTo("CN")));
        assertThat(returnValue.getBusinessRegistrationCountry(), is(equalTo("CN")));
        assertThat(returnValue.getBusinessContactRole(), is(equalTo(HyperwalletUser.BusinessContactRole.OWNER)));
        assertThat(returnValue.getBusinessOperatingName(), is(equalTo("Big Baller Payments Operating")));
        assertThat(returnValue.getDateOfBirth(), is(equalTo(dateFormat.parse("1980-01-01T00:00:00 UTC"))));
        assertThat(returnValue.getGender(), is(equalTo(Gender.MALE)));
        assertThat(returnValue.getPhoneNumber(), is(equalTo("647-90531")));
        assertThat(returnValue.getMobileNumber(), is(equalTo("605-555-1323")));
        assertThat(returnValue.getEmail(), is(equalTo("sf1601571120@sink.sendgrid.net")));
        assertThat(returnValue.getGovernmentId(), is(equalTo("333333333")));
        assertThat(returnValue.getEmployerId(), is(equalTo("222222222")));
        assertThat(returnValue.getProgramToken(), is(equalTo("prg-eedaf875-01f1-4524-8b94-d4936255af78")));
    }

    //
    // Prepaid Card
    //
    @Test
    public void testCreatePrepaidCard() throws Exception {
        String functionality = "createPrepaidCard";
        initMockServer(functionality);

        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard()
                .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .type(HyperwalletTransferMethod.Type.PREPAID_CARD);

        HyperwalletPrepaidCard returnValue;
        try {
            returnValue = client.createOrReplacePrepaidCard(prepaidCard);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/prepaid-cards/trm-7e915660-8c97-47bf"
                        + "-8a4f-0c1bc890d46f");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletTransferMethod.Type.BANK_CARD)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testGetPrepaidCard() throws Exception {
        String functionality = "getPrepaidCard";
        initMockServer(functionality);

        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard()
                .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .token("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f");

        HyperwalletPrepaidCard returnValue;
        try {
            returnValue = client.getPrepaidCard(prepaidCard.getUserToken(), prepaidCard.getToken());
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-d868bf2d-acf1-48de-9806-8cefcb6a40ad/prepaid-cards/trm-38e07e59-69d1-40bc"
                        + "-bebc-af639b847410");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletTransferMethod.Type.PREPAID_CARD)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2020-09-07T18:05:09 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getCardPackage(), is(equalTo("L1")));
        assertThat(returnValue.getCardType(), is(equalTo(HyperwalletPrepaidCard.CardType.PERSONALIZED)));
        assertThat(returnValue.getCardNumber(), is(equalTo("************0727")));
        assertThat(returnValue.getCardBrand(), is(equalTo(HyperwalletPrepaidCard.Brand.VISA)));
        assertThat(returnValue.getDateOfExpiry(), is(equalTo(dateFormat.parse("2024-09-01T00:00:00 UTC"))));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testUpdatePrepaidCard() throws Exception {
        String functionality = "updatePrepaidCard";
        initMockServer(functionality);

        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard()
                .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .token("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")
                .cardPackage("US8419889B2");

        HyperwalletPrepaidCard returnValue;
        try {
            returnValue = client.updatePrepaidCard(prepaidCard);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletTransferMethod.Type.BANK_CARD)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-09T22:50:14 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getCardPackage(), is(equalTo("US8419889B2")));
        assertThat(returnValue.getCardType(), is(equalTo(HyperwalletPrepaidCard.CardType.VIRTUAL)));
        assertThat(returnValue.getCardNumber(), is(equalTo("************0114")));
        assertThat(returnValue.getCardBrand(), is(equalTo(HyperwalletPrepaidCard.Brand.MASTERCARD)));
        assertThat(returnValue.getDateOfExpiry(), is(nullValue()));
    }


    @Test
    public void testListPrepaidCard() throws Exception {
        String functionality = "listPrepaidCards";
        initMockServer(functionality);
        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard()
                .userToken("usr-c4292f1a-866f-4310-a289-b916853939de");
        HyperwalletList<HyperwalletPrepaidCard> returnValue;

        HyperwalletListPaginationOptions options = new HyperwalletListPaginationOptions();
        options.status(HyperwalletTransferMethod.Status.DE_ACTIVATED)
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T19:58:26Z"));

        try {
            returnValue = client.listPrepaidCards(prepaidCard.getUserToken(),options);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref("https://api.sandbox.hyperwallet.com/rest/v4/users/usr-d868bf2d-acf1-48de-9806-8cefcb6a40ad/prepaid-cards?limit=100");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        HyperwalletPrepaidCard hyperwalletPrepaidCardResponse = returnValue.getData().get(0);
        assertThat(hyperwalletPrepaidCardResponse.getToken(), is(equalTo("trm-38e07e59-69d1-40bc-bebc-af639b847410")));
        assertThat(hyperwalletPrepaidCardResponse.getType(), is(equalTo(HyperwalletTransferMethod.Type.PREPAID_CARD)));
        assertThat(hyperwalletPrepaidCardResponse.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(hyperwalletPrepaidCardResponse.getCreatedOn(), is(equalTo(dateFormat.parse("2020-09-07T18:05:09 UTC"))));
        assertThat(hyperwalletPrepaidCardResponse.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(hyperwalletPrepaidCardResponse.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(hyperwalletPrepaidCardResponse.getCardPackage(), is(equalTo("L1")));
        assertThat(hyperwalletPrepaidCardResponse.getCardType(), is(equalTo(HyperwalletPrepaidCard.CardType.PERSONALIZED)));
        assertThat(hyperwalletPrepaidCardResponse.getCardNumber(), is(equalTo("************0727")));
        assertThat(hyperwalletPrepaidCardResponse.getCardBrand(), is(equalTo(HyperwalletPrepaidCard.Brand.VISA)));
        assertThat(hyperwalletPrepaidCardResponse.getDateOfExpiry(), is(equalTo(dateFormat.parse("2024-09-01T00:00:00 UTC"))));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testReplacePrepaidCard() throws Exception {
        String functionality = "replacePrepaidCard";
        initMockServer(functionality);

        HyperwalletPrepaidCard prepaidCard = new HyperwalletPrepaidCard()
                .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .type(HyperwalletTransferMethod.Type.PREPAID_CARD)
                .replacementOf("trm-3e23841c-34fd-41b1-9b81-ac2c3ad5ab84")
                .replacementReason(EReplacePrepaidCardReason.DAMAGED)
                .cardPackage("L1");

        HyperwalletPrepaidCard returnValue;
        try {
            returnValue = client.createOrReplacePrepaidCard(prepaidCard);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://localhost-hyperwallet.aws.paylution.net:8181/rest/v4/users/usr-85e743a2-beec-4817-9a10-2e0aede9afd7/prepaid-cards/trm"
                        + "-3e23841c-34fd-41b1-9b81-ac2c3ad5ab84");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-3e23841c-34fd-41b1-9b81-ac2c3ad5ab84")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletTransferMethod.Type.PREPAID_CARD)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.PRE_ACTIVATED)));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getCardType(), is(equalTo(HyperwalletPrepaidCard.CardType.PERSONALIZED)));
        assertThat(returnValue.getCardPackage(), is(equalTo("L1")));
        assertThat(returnValue.getCardNumber(), is(equalTo("************0843")));
        assertThat(returnValue.getCardBrand(), is(equalTo(Brand.VISA)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2020-09-10T14:31:43 UTC"))));
        assertThat(returnValue.getDateOfExpiry(), is(equalTo(dateFormat.parse("2024-09-01T00:00:00 UTC"))));
        assertThat(returnValue.getUserToken(), is(equalTo("usr-85e743a2-beec-4817-9a10-2e0aede9afd7")));
        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());

    }

    //
    // Bank Cards
    //

    @Test
    public void testCreateBankCard() throws Exception {
        String functionality = "createBankCard";
        initMockServer(functionality);

        HyperwalletBankCard bankCard = new HyperwalletBankCard()
            .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
            .cardNumber("4216701111100114")
            .dateOfExpiry(dateFormat.parse("2018-01-01T00:00:00 UTC"))
            .cvv("123")
                .transferMethodCountry("US")
                .transferMethodCurrency("USD");

        HyperwalletBankCard returnValue;
        try {
            returnValue = client.createBankCard(bankCard);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/bank-cards/trm-7e915660-8c97-47bf-8a4f"
                        + "-0c1bc890d46f");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletBankCard.Type.BANK_CARD)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletBankCard.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-09T22:50:14 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getCardType(), is(equalTo(HyperwalletBankCard.CardType.DEBIT)));
        assertThat(returnValue.getCardNumber(), is(equalTo("************0114")));
        assertThat(returnValue.getCardBrand(), is(equalTo(HyperwalletBankCard.Brand.VISA)));
        assertThat(returnValue.getDateOfExpiry(), is(equalTo(dateFormat.parse("2018-01-01T00:00:00 UTC"))));
        assertThat(returnValue.getCvv(), is(nullValue()));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }

    @Test
    public void testUpdateBankCard() throws Exception {
        String functionality = "updateBankCard";
        initMockServer(functionality);

        HyperwalletBankCard bankCard = new HyperwalletBankCard()
            .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
            .token("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")
                .dateOfExpiry(dateFormat.parse("2018-11-01T00:00:00 UTC"))
                .cvv("123");

        HyperwalletBankCard returnValue;
        try {
            returnValue = client.updateBankCard(bankCard);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/bank-cards/trm-7e915660-8c97-47bf-8a4f"
                        + "-0c1bc890d46f");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletBankCard.Type.BANK_CARD)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletBankCard.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-09T22:50:14 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getCardType(), is(equalTo(HyperwalletBankCard.CardType.DEBIT)));
        assertThat(returnValue.getCardNumber(), is(equalTo("************0114")));
        assertThat(returnValue.getCardBrand(), is(equalTo(HyperwalletBankCard.Brand.VISA)));
        assertThat(returnValue.getDateOfExpiry(), is(equalTo(dateFormat.parse("2018-11-01T00:00:00 UTC"))));
        assertThat(returnValue.getCvv(), is(nullValue()));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }

    @Test
    public void testGetBankCard() throws Exception {
        String functionality = "getBankCard";
        initMockServer(functionality);

        HyperwalletBankCard returnValue;
        try {
            returnValue = client.getBankCard("usr-c4292f1a-866f-4310-a289-b916853939de",
                    "trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/bank-cards/trm-7e915660-8c97-47bf-8a4f"
                        + "-0c1bc890d46f");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletBankCard.Type.BANK_CARD)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletBankCard.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-09T22:50:14 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getCardType(), is(equalTo(HyperwalletBankCard.CardType.DEBIT)));
        assertThat(returnValue.getCardNumber(), is(equalTo("************0114")));
        assertThat(returnValue.getCardBrand(), is(equalTo(HyperwalletBankCard.Brand.VISA)));
        assertThat(returnValue.getDateOfExpiry(), is(equalTo(dateFormat.parse("2018-12-01T00:00:00 UTC"))));
        assertThat(returnValue.getCvv(), is(nullValue()));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }

    @Test
    public void testListBankCard() throws Exception {
        String functionality = "listBankCards";
        initMockServer(functionality);

        HyperwalletList<HyperwalletBankCard> returnValue;

        HyperwalletBankCardListPaginationOptions options = new HyperwalletBankCardListPaginationOptions();
        options.status(HyperwalletBankCard.Status.ACTIVATED)
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2016-06-29T19:58:26Z"));


        try {
            returnValue = client.listBankCards("usr-c4292f1a-866f-4310-a289-b916853939de",options);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/bank-cards?limit=10");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.hasNextPage(), is(equalTo(true)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(true)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")));
        assertThat(returnValue.getData().get(0).getType(), is(equalTo(HyperwalletBankCard.Type.BANK_CARD)));
        assertThat(returnValue.getData().get(0).getStatus(), is(equalTo(HyperwalletBankCard.Status.ACTIVATED)));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-09T22:50:14 UTC"))));
        assertThat(returnValue.getData().get(0).getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getData().get(0).getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getData().get(0).getCardType(), is(equalTo(HyperwalletBankCard.CardType.DEBIT)));
        assertThat(returnValue.getData().get(0).getCardNumber(), is(equalTo("************0114")));
        assertThat(returnValue.getData().get(0).getCardBrand(), is(equalTo(HyperwalletBankCard.Brand.VISA)));
        assertThat(returnValue.getData().get(0).getDateOfExpiry(), is(equalTo(dateFormat.parse("2018-11-01T00:00:00 UTC"))));
        assertThat(returnValue.getData().get(0).getCvv(), is(nullValue()));
        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    /*
    Following test is not returning response. This is dissabled timebeing. To be rerun after enabling.
     */
    @Test
    public void testListBankAccount() throws Exception {
        String functionality = "listBankAccounts";
        initMockServer(functionality);

        HyperwalletList<HyperwalletBankAccount> returnValue;
        HyperwalletBankAccountsListPaginationOptions options = new HyperwalletBankAccountsListPaginationOptions();
        options.type(HyperwalletBankAccount.Type.BANK_ACCOUNT)
                .status(HyperwalletBankAccount.Status.ACTIVATED)
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2016-06-29T17:58:26Z"))
                .createdBefore(convertStringToDate("2019-06-29T19:58:26Z"));


        try {
            //returnValue = client.listBankAccounts("usr-c4292f1a-866f-4310-a289-b916853939de",options);
            returnValue = client.listBankAccounts("usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad",options);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.hasNextPage(), is(equalTo(false)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("trm-2bd0b56d-e111-4d12-bd24-d77fc02b9f4f")));
        assertThat(returnValue.getData().get(0).getType(), is(equalTo(HyperwalletBankAccount.Type.BANK_ACCOUNT)));
        assertThat(returnValue.getData().get(0).getStatus(), is(equalTo(HyperwalletBankAccount.Status.DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2020-09-08T15:01:07 UTC"))));
        assertThat(returnValue.getData().get(0).getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getData().get(0).getTransferMethodCurrency(), is(equalTo("USD")));
        assertEquals(returnValue.getData().get(0).getBranchId(),"021000021");
        assertEquals(returnValue.getData().get(0).getBankAccountId(),"1599557466");
        assertEquals(returnValue.getData().get(0).getBankAccountPurpose(),"SAVINGS");
        assertEquals(returnValue.getData().get(0).getProfileType().toString(),"INDIVIDUAL");
        assertEquals(returnValue.getData().get(0).getFirstName(),"firstName");
        assertEquals(returnValue.getData().get(0).getLastName(),"lastName");
        assertEquals(returnValue.getData().get(0).getDateOfBirth(),dateFormat.parse("2000-09-08T15:01:07 UTC"));
        assertEquals(returnValue.getData().get(0).getAddressLine1(),"1234 IndividualAddress St");
        assertEquals(returnValue.getData().get(0).getCity(),"Test1111");
        assertEquals(returnValue.getData().get(0).getStateProvince(),"CA");
        assertEquals(returnValue.getData().get(0).getCountry(),"US");
        assertEquals(returnValue.getData().get(0).getPostalCode(),"12345");
    }

    @Test
    public void testDeactivateBankCard() throws Exception {
        String functionality = "deactivateBankCard";
        initMockServer(functionality);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.deactivateBankCard("usr-f695ef43-9614-4e17-9269-902c234616c3",
                                                    "trm-d69300ef-5011-486b-bd2e-bfd8b20fef26",
                                                    "Closing this account.");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("sts-1825afa2-61f1-4860-aa69-a65b9d14f556")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T00:55:57 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Closing this account.")));
    }

    @Test
    public void testListBankCardStatusTransitions() throws Exception {
        String functionality = "listBankCardStatusTransitions";
        initMockServer(functionality);

        HyperwalletList<HyperwalletStatusTransition> returnValue;
        try {
            returnValue = client.listBankCardStatusTransitions("usr-f695ef43-9614-4e17-9269-902c234616c3",
                    "trm-d69300ef-5011-486b-bd2e-bfd8b20fef26");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.hasNextPage(), is(equalTo(false)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("sts-1825afa2-61f1-4860-aa69-a65b9d14f556")));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T00:55:57 UTC"))));
        assertThat(returnValue.getData().get(0).getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getData().get(0).getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getNotes(), is(equalTo("Closing this account.")));
    }

    @Test
    public void testCreateBankCardStatusTransition() throws Exception {
        String functionality = "createBankCardStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setNotes("Closing this account.");
        transition.setTransition(DE_ACTIVATED);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.createBankCardStatusTransition("usr-f695ef43-9614-4e17-9269-902c234616c3",
                                                                "trm-d69300ef-5011-486b-bd2e-bfd8b20fef26",
                                                                transition);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("sts-1825afa2-61f1-4860-aa69-a65b9d14f556")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T00:55:57 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Closing this account.")));
    }

    @Test
    public void testGetBankCardStatusTransition() throws Exception {
        String functionality = "getBankCardStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setNotes("Closing this account.");
        transition.setTransition(DE_ACTIVATED);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.getBankCardStatusTransition("usr-f695ef43-9614-4e17-9269-902c234616c3",
                                                                "trm-d69300ef-5011-486b-bd2e-bfd8b20fef26",
                                                             "sts-1825afa2-61f1-4860-aa69-a65b9d14f556");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("sts-1825afa2-61f1-4860-aa69-a65b9d14f556")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T00:55:57 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Closing this account.")));
    }

    //
    // Paper Checks
    //

    @Test
    public void testCreatePaperCheck() throws Exception {
        String functionality = "createPaperCheck";
        initMockServer(functionality);

        HyperwalletPaperCheck paperCheck = new HyperwalletPaperCheck()
                .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .transferMethodCountry("US")
                .transferMethodCurrency("USD");

        HyperwalletPaperCheck returnValue;
        try {
            returnValue = client.createPaperCheck(paperCheck);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/paper-checks/trm-59f67c62-fd06-497e"
                        + "-a9ea-99d6eb38b12b");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);
        assertThat(returnValue.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletTransferMethod.Type.PAPER_CHECK)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getProfileType(), is(equalTo(HyperwalletUser.ProfileType.BUSINESS)));
        assertThat(returnValue.getFirstName(), is(equalTo("Some")));
        assertThat(returnValue.getLastName(), is(equalTo("Guy")));
        assertThat(returnValue.getBusinessOperatingName(), is(equalTo("ABC Framing")));
        assertThat(returnValue.getDateOfBirth(), is(equalTo(dateFormat.parse("1991-01-01T00:00:00 UTC"))));
        assertThat(returnValue.getAddressLine1(), is(equalTo("575 Market Street")));
        assertThat(returnValue.getCity(), is(equalTo("San Francisco")));
        assertThat(returnValue.getStateProvince(), is(equalTo("CA")));
        assertThat(returnValue.getCountry(), is(equalTo("US")));
        assertThat(returnValue.getPostalCode(), is(equalTo("94105")));
        assertThat(returnValue.getShippingMethod(), is(equalTo(HyperwalletPaperCheck.ShippingMethod.STANDARD)));
        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertThat(actualHyperwalletLink.getParams(), is(expectedHyperwalletLink.getParams()));
    }

    @Test
    public void testUpdatePaperCheck() throws Exception {
        String functionality = "updatePaperCheck";
        initMockServer(functionality);

        HyperwalletPaperCheck paperCheck = new HyperwalletPaperCheck()
                .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .token("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")
                .addressLine1("123 Other Street")
                .city("Far Away Land");

        HyperwalletPaperCheck returnValue;
        try {
            returnValue = client.updatePaperCheck(paperCheck);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/paper-checks/trm-59f67c62-fd06-497e"
                        + "-a9ea-99d6eb38b12b");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);
        assertThat(returnValue.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletTransferMethod.Type.PAPER_CHECK)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:42:46 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getProfileType(), is(equalTo(HyperwalletUser.ProfileType.BUSINESS)));
        assertThat(returnValue.getFirstName(), is(equalTo("Some")));
        assertThat(returnValue.getLastName(), is(equalTo("Guy")));
        assertThat(returnValue.getBusinessOperatingName(), is(equalTo("ABC Framing")));
        assertThat(returnValue.getDateOfBirth(), is(equalTo(dateFormat.parse("1991-01-01T00:00:00 UTC"))));
        assertThat(returnValue.getAddressLine1(), is(equalTo("123 Other Street")));
        assertThat(returnValue.getCity(), is(equalTo("Far Away Land")));
        assertThat(returnValue.getStateProvince(), is(equalTo("CA")));
        assertThat(returnValue.getCountry(), is(equalTo("US")));
        assertThat(returnValue.getPostalCode(), is(equalTo("94105")));
        assertThat(returnValue.getShippingMethod(), is(equalTo(HyperwalletPaperCheck.ShippingMethod.STANDARD)));
        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertThat(actualHyperwalletLink.getParams(), is(expectedHyperwalletLink.getParams()));
    }

    @Test
    public void testGetPaperCheck() throws Exception {
        String functionality = "getPaperCheck";
        initMockServer(functionality);

        HyperwalletPaperCheck returnValue;
        try {
            returnValue = client.getPaperCheck("usr-c4292f1a-866f-4310-a289-b916853939de",
                    "trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/paper-checks/trm-59f67c62-fd06-497e"
                        + "-a9ea-99d6eb38b12b");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);
        assertThat(returnValue.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletTransferMethod.Type.PAPER_CHECK)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getProfileType(), is(equalTo(HyperwalletUser.ProfileType.BUSINESS)));
        assertThat(returnValue.getFirstName(), is(equalTo("Some")));
        assertThat(returnValue.getLastName(), is(equalTo("Guy")));
        assertThat(returnValue.getBusinessOperatingName(), is(equalTo("ABC Framing")));
        assertThat(returnValue.getDateOfBirth(), is(equalTo(dateFormat.parse("1991-01-01T00:00:00 UTC"))));
        assertThat(returnValue.getAddressLine1(), is(equalTo("575 Market Street")));
        assertThat(returnValue.getCity(), is(equalTo("San Francisco")));
        assertThat(returnValue.getStateProvince(), is(equalTo("CA")));
        assertThat(returnValue.getCountry(), is(equalTo("US")));
        assertThat(returnValue.getPostalCode(), is(equalTo("94105")));
        assertThat(returnValue.getShippingMethod(), is(equalTo(HyperwalletPaperCheck.ShippingMethod.STANDARD)));
        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertThat(actualHyperwalletLink.getParams(), is(expectedHyperwalletLink.getParams()));
    }

    @Test
    public void testListPaperCheck() throws Exception {
        String functionality = "listPaperChecks";
        initMockServer(functionality);

        HyperwalletList<HyperwalletPaperCheck> returnValue;
        try {
            returnValue = client.listPaperChecks("usr-c4292f1a-866f-4310-a289-b916853939de");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink
                .setHref("https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/paper-checks?limit=10");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.hasNextPage(), is(equalTo(false)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getData().get(0).getType(), is(equalTo(HyperwalletTransferMethod.Type.PAPER_CHECK)));
        assertThat(returnValue.getData().get(0).getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getData().get(0).getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getData().get(0).getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getData().get(0).getProfileType(), is(equalTo(HyperwalletUser.ProfileType.BUSINESS)));
        assertThat(returnValue.getData().get(0).getFirstName(), is(equalTo("Some")));
        assertThat(returnValue.getData().get(0).getLastName(), is(equalTo("Guy")));
        assertThat(returnValue.getData().get(0).getBusinessOperatingName(), is(equalTo("ABC Framing")));
        assertThat(returnValue.getData().get(0).getDateOfBirth(), is(equalTo(dateFormat.parse("1991-01-01T00:00:00 UTC"))));
        assertThat(returnValue.getData().get(0).getAddressLine1(), is(equalTo("575 Market Street")));
        assertThat(returnValue.getData().get(0).getCity(), is(equalTo("San Francisco")));
        assertThat(returnValue.getData().get(0).getStateProvince(), is(equalTo("CA")));
        assertThat(returnValue.getData().get(0).getCountry(), is(equalTo("US")));
        assertThat(returnValue.getData().get(0).getPostalCode(), is(equalTo("94105")));
        assertThat(returnValue.getData().get(0).getShippingMethod(), is(equalTo(HyperwalletPaperCheck.ShippingMethod.STANDARD)));
        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testDeactivatePaperCheck() throws Exception {
        String functionality = "deactivatePaperCheck";
        initMockServer(functionality);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.deactivatePaperCheck("usr-1dea80c9-c73e-4490-91b7-097d4a07550f",
                                                      "trm-9e2e1a06-a33b-4c2f-9933-893ae21db442",
                                                    "Closing check.");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("sts-ed2207f0-39cc-493f-9cd0-24998de0c0f7")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-30T19:50:49 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Closing check.")));
    }

    @Test
    public void testListPaperCheckStatusTransitions() throws Exception {
        String functionality = "listPaperCheckStatusTransitions";
        initMockServer(functionality);

        HyperwalletList<HyperwalletStatusTransition> returnValue;
        try {
            returnValue = client.listPaperCheckStatusTransitions("usr-1dea80c9-c73e-4490-91b7-097d4a07550f",
                    "trm-9e2e1a06-a33b-4c2f-9933-893ae21db442");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink
                .setHref(
                        "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-1dea80c9-c73e-4490-91b7-097d4a07550f/paper-checks/trm-9e2e1a06-a33b"
                        + "-4c2f-9933-893ae21db442/status-transitions?limit=10");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.hasNextPage(), is(equalTo(false)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("sts-ed2207f0-39cc-493f-9cd0-24998de0c0f7")));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-30T19:50:49 UTC"))));
        assertThat(returnValue.getData().get(0).getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getData().get(0).getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getNotes(), is(equalTo("Closing check.")));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testCreatePaperCheckStatusTransition() throws Exception {
        String functionality = "createPaperCheckStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setNotes("Closing check.");
        transition.setTransition(DE_ACTIVATED);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.createPaperCheckStatusTransition("usr-1dea80c9-c73e-4490-91b7-097d4a07550f",
                    "trm-9e2e1a06-a33b-4c2f-9933-893ae21db442",
                    transition);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink
                .setHref("https://api.sandbox.hyperwallet.com/rest/v4/users/usr-1dea80c9-c73e-4490-91b7-097d4a07550f/paper-checks/trm-9e2e1a06"
                        + "-a33b-4c2f-9933-893ae21db442/status-transitions/sts-ed2207f0-39cc-493f-9cd0-24998de0c0f7");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("sts-ed2207f0-39cc-493f-9cd0-24998de0c0f7")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-30T19:50:49 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Closing check.")));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertThat(actualHyperwalletLink.getParams(), is(expectedHyperwalletLink.getParams()));
    }

    @Test
    public void testGetPaperCheckStatusTransition() throws Exception {
        String functionality = "getPaperCheckStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setNotes("Closing this account.");
        transition.setTransition(DE_ACTIVATED);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.getPaperCheckStatusTransition("usr-1dea80c9-c73e-4490-91b7-097d4a07550f",
                    "trm-9e2e1a06-a33b-4c2f-9933-893ae21db442",
                    "sts-ed2207f0-39cc-493f-9cd0-24998de0c0f7");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink
                .setHref("https://api.sandbox.hyperwallet.com/rest/v4/users/usr-1dea80c9-c73e-4490-91b7-097d4a07550f/paper-checks/trm-9e2e1a06"
                        + "-a33b-4c2f-9933-893ae21db442/status-transitions/sts-ed2207f0-39cc-493f-9cd0-24998de0c0f7");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("sts-ed2207f0-39cc-493f-9cd0-24998de0c0f7")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-30T19:50:49 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Closing check.")));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertThat(actualHyperwalletLink.getParams(), is(expectedHyperwalletLink.getParams()));
    }

    //
    // Transfers
    //
    @Test
    public void testCreateTransfer() throws Exception {
        String functionality = "createTransfer";
        initMockServer(functionality);

        HyperwalletTransfer transfer = new HyperwalletTransfer()
                .sourceToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .destinationToken("trm-ff53d939-49c3-412f-8d83-ab4f7e83d553")
                .clientTransferId("clientTransferId");

        HyperwalletTransfer returnValue;
        try {
            returnValue = client.createTransfer(transfer);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/transfers/trf-59f67c62-fd06-497e-a9ea-99d6eb38b12b");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransfer.Status.QUOTED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getClientTransferId(), is(equalTo("clientTransferId")));
        assertThat(returnValue.getSourceToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getSourceAmount(), is(equalTo("2100.4")));
        assertThat(returnValue.getSourceFeeAmount(), is(equalTo("20.3")));
        assertThat(returnValue.getSourceCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getDestinationToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getDestinationAmount(), is(equalTo("2100.2")));
        assertThat(returnValue.getDestinationFeeAmount(), is(equalTo("30.5")));
        assertThat(returnValue.getDestinationCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getNotes(), is(equalTo("notes")));
        assertThat(returnValue.getMemo(), is(equalTo("memo")));
        assertThat(returnValue.getExpiresOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));

        ForeignExchange foreignExchange = returnValue.getForeignExchanges().get(0);
        assertThat(foreignExchange.getSourceAmount(), is(equalTo(100.00)));
        assertThat(foreignExchange.getSourceCurrency(), is(equalTo("USD")));
        assertThat(foreignExchange.getDestinationAmount(), is(equalTo(63.49)));
        assertThat(foreignExchange.getDestinationCurrency(), is(equalTo("CAD")));
        assertThat(foreignExchange.getRate(), is(equalTo(0.79)));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testCreateTransferWithoutForeignExchange() throws Exception {
        String functionality = "createTransferWithoutForeignExchange";
        initMockServer(functionality);

        HyperwalletTransfer transfer = new HyperwalletTransfer()
                .sourceToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .destinationToken("trm-ff53d939-49c3-412f-8d83-ab4f7e83d553")
                .clientTransferId("clientTransferId");

        HyperwalletTransfer returnValue;
        try {
            returnValue = client.createTransfer(transfer);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/transfers/trf-59f67c62-fd06-497e-a9ea-99d6eb38b12b");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransfer.Status.QUOTED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getClientTransferId(), is(equalTo("clientTransferId")));
        assertThat(returnValue.getSourceToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getSourceAmount(), is(equalTo("2200.4")));
        assertThat(returnValue.getSourceFeeAmount(), is(equalTo("20.3")));
        assertThat(returnValue.getSourceCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getDestinationToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getDestinationAmount(), is(equalTo("2100.2")));
        assertThat(returnValue.getDestinationFeeAmount(), is(equalTo("30.5")));
        assertThat(returnValue.getDestinationCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getNotes(), is(equalTo("notes")));
        assertThat(returnValue.getMemo(), is(equalTo("memo")));
        assertThat(returnValue.getExpiresOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getForeignExchanges(), is(nullValue()));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testGetTransfer() throws Exception {
        String functionality = "getTransfer";
        initMockServer(functionality);

        HyperwalletTransfer returnValue;
        try {
            returnValue = client.getTransfer("usr-c4292f1a-866f-4310-a289-b916853939de");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/transfers/trf-59f67c62-fd06-497e-a9ea-99d6eb38b12b");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransfer.Status.QUOTED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getClientTransferId(), is(equalTo("clientTransferId")));
        assertThat(returnValue.getSourceToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getSourceAmount(), is(equalTo("200.4")));
        assertThat(returnValue.getSourceFeeAmount(), is(equalTo("20.3")));
        assertThat(returnValue.getSourceCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getDestinationToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getDestinationAmount(), is(equalTo("100.2")));
        assertThat(returnValue.getDestinationFeeAmount(), is(equalTo("30.5")));
        assertThat(returnValue.getDestinationCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getNotes(), is(equalTo("notes")));
        assertThat(returnValue.getMemo(), is(equalTo("memo")));
        assertThat(returnValue.getExpiresOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));

        ForeignExchange foreignExchange = returnValue.getForeignExchanges().get(0);
        assertThat(foreignExchange.getSourceAmount(), is(equalTo(100.00)));
        assertThat(foreignExchange.getSourceCurrency(), is(equalTo("USD")));
        assertThat(foreignExchange.getDestinationAmount(), is(equalTo(63.49)));
        assertThat(foreignExchange.getDestinationCurrency(), is(equalTo("CAD")));
        assertThat(foreignExchange.getRate(), is(equalTo(0.79)));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testGetTransferWithoutForeignExchange() throws Exception {
        String functionality = "getTransferWithoutForeignExchange";
        initMockServer(functionality);

        HyperwalletTransfer returnValue;
        try {
            returnValue = client.getTransfer("usr-c4292f1a-866f-4310-a289-b916853939de");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/transfers/trf-59f67c62-fd06-497e-a9ea-99d6eb38b12b");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransfer.Status.QUOTED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getClientTransferId(), is(equalTo("clientTransferId")));
        assertThat(returnValue.getSourceToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getSourceAmount(), is(equalTo(String.valueOf(200.4))));
        assertThat(returnValue.getSourceFeeAmount(), is(equalTo(String.valueOf(20.3))));
        assertThat(returnValue.getSourceCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getDestinationToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getDestinationAmount(), is(equalTo(String.valueOf(100.2))));
        assertThat(returnValue.getDestinationFeeAmount(), is(equalTo(String.valueOf(30.5))));
        assertThat(returnValue.getDestinationCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getNotes(), is(equalTo("notes")));
        assertThat(returnValue.getMemo(), is(equalTo("memo")));
        assertThat(returnValue.getExpiresOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getForeignExchanges(), is(nullValue()));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testListTransfer() throws Exception {
        String functionality = "listTransfers";
        initMockServer(functionality);

        HyperwalletList<HyperwalletTransfer> returnValue;
        try {
            returnValue = client.listTransfers();
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/transfers?offset=0&limit=10");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.hasNextPage(), is(equalTo(false)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));

        HyperwalletTransfer hyperwalletTransfer = returnValue.getData().get(0);
        assertThat(hyperwalletTransfer.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(hyperwalletTransfer.getStatus(), is(equalTo(HyperwalletTransfer.Status.QUOTED)));
        assertThat(hyperwalletTransfer.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(hyperwalletTransfer.getClientTransferId(), is(equalTo("clientTransferId")));
        assertThat(hyperwalletTransfer.getSourceToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(hyperwalletTransfer.getSourceAmount(), is(equalTo(String.valueOf(200.4))));
        assertThat(hyperwalletTransfer.getSourceFeeAmount(), is(equalTo(String.valueOf(20.3))));
        assertThat(hyperwalletTransfer.getSourceCurrency(), is(equalTo("USD")));
        assertThat(hyperwalletTransfer.getDestinationToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(hyperwalletTransfer.getDestinationAmount(), is(equalTo(String.valueOf(100.2))));
        assertThat(hyperwalletTransfer.getDestinationFeeAmount(), is(equalTo(String.valueOf(30.5))));
        assertThat(hyperwalletTransfer.getDestinationCurrency(), is(equalTo("USD")));
        assertThat(hyperwalletTransfer.getNotes(), is(equalTo("notes")));
        assertThat(hyperwalletTransfer.getMemo(), is(equalTo("memo")));
        assertThat(hyperwalletTransfer.getExpiresOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));

        ForeignExchange foreignExchange = hyperwalletTransfer.getForeignExchanges().get(0);
        assertThat(foreignExchange.getSourceAmount(), is(equalTo(100.00)));
        assertThat(foreignExchange.getSourceCurrency(), is(equalTo("USD")));
        assertThat(foreignExchange.getDestinationAmount(), is(equalTo(63.49)));
        assertThat(foreignExchange.getDestinationCurrency(), is(equalTo("CAD")));
        assertThat(foreignExchange.getRate(), is(equalTo(0.79)));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testListTransferWithoutForeignExchange() throws Exception {
        String functionality = "listTransfersWithoutForeignExchange";
        initMockServer(functionality);

        HyperwalletList<HyperwalletTransfer> returnValue;
        try {
            returnValue = client.listTransfers();
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/transfers?offset=0&limit=10");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.hasNextPage(), is(equalTo(false)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));

        HyperwalletTransfer hyperwalletTransfer = returnValue.getData().get(0);
        assertThat(hyperwalletTransfer.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(hyperwalletTransfer.getStatus(), is(equalTo(HyperwalletTransfer.Status.QUOTED)));
        assertThat(hyperwalletTransfer.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(hyperwalletTransfer.getClientTransferId(), is(equalTo("clientTransferId")));
        assertThat(hyperwalletTransfer.getSourceToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(hyperwalletTransfer.getSourceAmount(), is(equalTo(String.valueOf(2200.4))));
        assertThat(hyperwalletTransfer.getSourceFeeAmount(), is(equalTo(String.valueOf(20.3))));
        assertThat(hyperwalletTransfer.getSourceCurrency(), is(equalTo("USD")));
        assertThat(hyperwalletTransfer.getDestinationToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(hyperwalletTransfer.getDestinationAmount(), is(equalTo(String.valueOf(100.2))));
        assertThat(hyperwalletTransfer.getDestinationFeeAmount(), is(equalTo(String.valueOf(30.5))));
        assertThat(hyperwalletTransfer.getDestinationCurrency(), is(equalTo("USD")));
        assertThat(hyperwalletTransfer.getNotes(), is(equalTo("notes")));
        assertThat(hyperwalletTransfer.getMemo(), is(equalTo("memo")));
        assertThat(hyperwalletTransfer.getExpiresOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getData().get(0).getForeignExchanges(), is(nullValue()));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testCreateTransferStatusTransition() throws Exception {
        String functionality = "createTransferStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setNotes("Closing check.");
        transition.setTransition(SCHEDULED);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.createTransferStatusTransition("usr-1dea80c9-c73e-4490-91b7-097d4a07550f", transition);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/transfers/trr-9e2e1a06-a33b-4c2f-9933-893ae21db442/status-transitions/sts-ed2207f0"
                        + "-39cc-493f-9cd0-24998de0c0f7");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("sts-ed2207f0-39cc-493f-9cd0-24998de0c0f7")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-30T19:50:49 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(SCHEDULED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(QUOTED)));
        assertThat(returnValue.getToStatus(), is(equalTo(SCHEDULED)));
        assertThat(returnValue.getNotes(), is(equalTo("Closing check.")));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    //
    // Transfer Refunds
    //
    @Test
    public void testCreateTransferRefund() throws Exception {
        String functionality = "createTransferRefund";
        initMockServer(functionality);

        HyperwalletTransferRefund transferRefund = new HyperwalletTransferRefund()
                .clientRefundId("clientRefundId")
                .notes("Merchant Payment return to Wallet Balance")
                .memo("TransferReturn123456");

        HyperwalletTransferRefund returnValue;
        try {
            returnValue = client.createTransferRefund("trf-dc6a19f7-1d24-434d-87ce-f1a960f3fbce", transferRefund);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/transfers/trf-dc6a19f7-1d24-434d-87ce-f1a960f3fbce/refunds/trd-a159dc18-eb29-4530-8733"
                        + "-060c7feaad0f");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        HyperwalletTransferRefund expectedValue = new HyperwalletTransferRefund()
                .token("trd-a159dc18-eb29-4530-8733-060c7feaad0f")
                .status(HyperwalletTransferRefund.Status.COMPLETED)
                .clientRefundId("clientRefundId")
                .sourceToken("act-ba4e8fdd-614b-11e5-af23-0faa28ca7c0f")
                .sourceAmount(20.0)
                .sourceCurrency("USD")
                .destinationToken("usr-3deb34a0-ffd1-487d-8860-6d69435cea6c")
                .destinationAmount(20.0)
                .destinationCurrency("USD")
                .notes("Merchant Payment return to Wallet Balance")
                .memo("TransferReturn123456")
                .createdOn(dateFormat.parse("2019-11-11T19:04:43 UTC"))
                .links(hyperwalletLinks);

        checkTransferRefund(returnValue, expectedValue);
        checkForeignExchange(returnValue, expectedValue);
    }

    private void checkTransferRefund(HyperwalletTransferRefund actual, HyperwalletTransferRefund expected) {
        assertThat(actual.getToken(), is(expected.getToken()));
        assertThat(actual.getStatus(), is(expected.getStatus()));
        assertThat(actual.getClientRefundId(), is(expected.getClientRefundId()));
        assertThat(actual.getSourceToken(), is(expected.getSourceToken()));
        assertThat(actual.getSourceAmount(), is(expected.getSourceAmount()));
        assertThat(actual.getSourceCurrency(), is(expected.getSourceCurrency()));
        assertThat(actual.getDestinationToken(), is(expected.getDestinationToken()));
        assertThat(actual.getDestinationAmount(), is(expected.getDestinationAmount()));
        assertThat(actual.getDestinationCurrency(), is(expected.getDestinationCurrency()));
        assertThat(actual.getNotes(), is(expected.getNotes()));
        assertThat(actual.getMemo(), is(expected.getMemo()));
        assertThat(actual.getCreatedOn(), is(expected.getCreatedOn()));

        HyperwalletLink actualHyperwalletLink = actual.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = expected.getLinks().get(0);
        assertEquals(actualHyperwalletLink.getHref(), expectedHyperwalletLink.getHref());
        assertEquals(actualHyperwalletLink.getParams().keySet(), expectedHyperwalletLink.getParams().keySet());
        assertEquals(actualHyperwalletLink.getParams().values(), expectedHyperwalletLink.getParams().values());
    }

    private void checkForeignExchange(HyperwalletTransferRefund actual, HyperwalletTransferRefund expected) {
        ForeignExchange foreignExchange = actual.getForeignExchanges().get(0);
        assertThat(foreignExchange.getSourceAmount(), is(equalTo(100.00)));
        assertThat(foreignExchange.getSourceCurrency(), is(equalTo("USD")));
        assertThat(foreignExchange.getDestinationAmount(), is(equalTo(63.49)));
        assertThat(foreignExchange.getDestinationCurrency(), is(equalTo("CAD")));
        assertThat(foreignExchange.getRate(), is(equalTo(0.79)));
    }

    @Test
    public void testGetTransferRefund() throws Exception {
        String functionality = "getTransferRefund";
        initMockServer(functionality);

        HyperwalletTransferRefund returnValue;
        try {
            returnValue = client.getTransferRefund("trf-639579d9-4fe8-4fbf-8e34-827d27697f64", "trd-19156720-01e8-4f1c-8ef3-7ced80672128");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/transfers/trf-639579d9-4fe8-4fbf-8e34-827d27697f64/refunds/trd-19156720-01e8-4f1c-8ef3"
                        + "-7ced80672128");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        HyperwalletTransferRefund expectedValue = new HyperwalletTransferRefund()
                .token("trd-19156720-01e8-4f1c-8ef3-7ced80672128")
                .status(HyperwalletTransferRefund.Status.COMPLETED)
                .clientRefundId("1573548663")
                .sourceToken("act-ba4e8fdd-614b-11e5-af23-0faa28ca7c0f")
                .sourceAmount(50.0)
                .sourceCurrency("USD")
                .destinationToken("usr-3deb34a0-ffd1-487d-8860-6d69435cea6c")
                .destinationAmount(50.0)
                .destinationCurrency("USD")
                .notes("Merchant Payment return to Wallet Balance")
                .memo("TransferReturn123456")
                .createdOn(dateFormat.parse("2019-11-12T11:51:05 UTC"))
                .links(hyperwalletLinks);

        checkTransferRefund(returnValue, expectedValue);
        checkForeignExchange(returnValue, expectedValue);
    }

    @Test
    public void testListTransferRefunds() throws Exception {
        String functionality = "listTransferRefunds";
        initMockServer(functionality);

        HyperwalletList<HyperwalletTransferRefund> returnValue;
        try {
            returnValue = client.listTransferRefunds("trf-fdbc1f59-ef5f-4b9f-94e5-7e15797bcefb", null);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://localhost:8181/rest/v4/transfers/trf-fdbc1f59-ef5f-4b9f-94e5-7e15797bcefb/refunds/trd-e59d19d4-eccb-4160-b04c-4f11c83f99f0");
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("rel", "self");
        hyperwalletLink.setParams(paramsMap);
        hyperwalletLinks.add(hyperwalletLink);
        HyperwalletTransferRefund expectedValue = new HyperwalletTransferRefund()
                .token("trd-e59d19d4-eccb-4160-b04c-4f11c83f99f0")
                .status(HyperwalletTransferRefund.Status.COMPLETED)
                .clientRefundId("1573566270")
                .sourceToken("act-ba4e8fdd-614b-11e5-af23-0faa28ca7c0f")
                .sourceAmount(50.0)
                .sourceCurrency("USD")
                .destinationToken("usr-3deb34a0-ffd1-487d-8860-6d69435cea6c")
                .destinationAmount(50.0)
                .destinationCurrency("USD")
                .notes("Merchant Payment return to Wallet Balance")
                .memo("TransferReturn123456")
                .createdOn(dateFormat.parse("2019-11-12T16:44:30 UTC"))
                .links(hyperwalletLinks);

        assertEquals(returnValue.getLinks().size(), 1);
        checkTransferRefund(returnValue.getData().get(0), expectedValue);
        checkForeignExchange(returnValue.getData().get(0), expectedValue);
    }

    @Test
    public void testGetTransferStatusTransition() throws Exception {
        String functionality = "getTransferStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.getTransferStatusTransition("trf-c57dad1b-9d18-4b06-8919-a4d429b39baa", "sts-6b90b063-fc87-466f-8aae-9f9572d786d5");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/transfers/trf-c57dad1b-9d18-4b06-8919-a4d429b39baa/status-transitions/sts-1f7f58a9"
                + "-22e8-4fef-8d6e-a17e2c71db33");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("sts-1f7f58a9-22e8-4fef-8d6e-a17e2c71db33")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T02:04:17 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testlistTransferStatusTransition() throws Exception {
        String functionality = "listTransferStatusTransition";
        initMockServer(functionality);

        HyperwalletList<HyperwalletStatusTransition> returnValue;
        try {
            returnValue = client.listTransferStatusTransition("trf-c57dad1b-9d18-4b06-8919-a4d429b39baa", null);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/transfers/trf-c57dad1b-9d18-4b06-8919-a4d429b39baa/status-transitions/sts-7048eb5d"
                + "-01b5-4bb8-94e3-cf8cdb1d42df");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        HyperwalletStatusTransition hyperwalletStatusTransition = returnValue.getData().get(0);

        assertThat(hyperwalletStatusTransition.getToken(), is(equalTo("sts-7048eb5d-01b5-4bb8-94e3-cf8cdb1d42df")));
        assertThat(hyperwalletStatusTransition.getCreatedOn(), is(equalTo(dateFormat.parse("2020-09-11T21:02:33 UTC"))));
        assertThat(hyperwalletStatusTransition.getTransition(), is(equalTo(COMPLETED)));
        assertThat(hyperwalletStatusTransition.getFromStatus(), is(equalTo(QUOTED)));
        assertThat(hyperwalletStatusTransition.getToStatus(), is(equalTo(COMPLETED)));

        HyperwalletLink actualHyperwalletLink = hyperwalletStatusTransition.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    //
    // PayPal Accounts
    //

    @Test
    public void testCreatePayPalAccount() throws Exception {
        String functionality = "createPayPalAccount";
        initMockServer(functionality);

        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount()
                .userToken("usr-e7b61829-a73a-45dc-930e-afa8a56b923b")
                .transferMethodCountry("US")
                .transferMethodCurrency("USD")
                .type(HyperwalletPayPalAccount.Type.PAYPAL_ACCOUNT)
                .email("user@domain.com");

        HyperwalletPayPalAccount returnValue;
        try {
            returnValue = client.createPayPalAccount(payPalAccount);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-e7b61829-a73a-45dc-930e-afa8a56b923b/paypal-accounts/trm-54b0db9c-5565-47f7"
                        + "-aee6-685e713595f3");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-54b0db9c-5565-47f7-aee6-685e713595f3")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletPayPalAccount.Status.ACTIVATED)));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletPayPalAccount.Type.PAYPAL_ACCOUNT)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2018-05-01T00:00:00 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getEmail(), is(equalTo("user@domain.com")));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }

    @Test
    public void testGetPayPalAccount() throws Exception {
        String functionality = "getPayPalAccount";
        initMockServer(functionality);

        HyperwalletPayPalAccount returnValue;
        try {
            returnValue = client.getPayPalAccount("usr-e7b61829-a73a-45dc-930e-afa8a56b923b", "trm-54b0db9c-5565-47f7-aee6-685e713595f3");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-e7b61829-a73a-45dc-930e-afa8a56b923b/paypal-accounts/trm-54b0db9c-5565-47f7"
                        + "-aee6-685e713595f3");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-54b0db9c-5565-47f7-aee6-685e713595f3")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletPayPalAccount.Status.ACTIVATED)));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletPayPalAccount.Type.PAYPAL_ACCOUNT)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2018-05-01T00:00:00 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getEmail(), is(equalTo("user@domain.com")));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }

    @Test
    public void testListPayPalAccount() throws Exception {
        String functionality = "listPayPalAccounts";
        initMockServer(functionality);

        HyperwalletList<HyperwalletPayPalAccount> returnValue;
        try {
            returnValue = client.listPayPalAccounts("usr-e7b61829-a73a-45dc-930e-afa8a56b923b");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-e7b61829-a73a-45dc-930e-afa8a56b923b/paypal-accounts?limit=10");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.hasNextPage(), is(equalTo(false)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
        assertThat(returnValue.getLimit(), is(equalTo(10)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("trm-54b0db9c-5565-47f7-aee6-685e713595f3")));
        assertThat(returnValue.getData().get(0).getStatus(), is(equalTo(HyperwalletPayPalAccount.Status.ACTIVATED)));
        assertThat(returnValue.getData().get(0).getType(), is(equalTo(HyperwalletPayPalAccount.Type.PAYPAL_ACCOUNT)));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2018-05-01T00:00:00 UTC"))));
        assertThat(returnValue.getData().get(0).getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getData().get(0).getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getData().get(0).getEmail(), is(equalTo("user@domain.com")));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }

    @Test
    public void testDeactivatePayPalAccount() throws Exception {
        String functionality = "deactivatePayPalAccount";
        initMockServer(functionality);

        HyperwalletStatusTransition returnValue;

        try {
            returnValue = client.deactivatePayPalAccount("usr-f695ef43-9614-4e17-9269-902c234616c3",
                    "trm-da21954a-3910-4d70-b83d-0c2d96104394", "PayPal account removed.");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("sts-70ddc78a-0c14-4a72-8390-75d49ff376f2")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2018-10-30T18:50:20 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("PayPal account removed.")));
    }

    @Test
    public void testListPayPalAccountStatusTransitions() throws Exception {
        String functionality = "listPayPalAccountStatusTransitions";
        initMockServer(functionality);

        HyperwalletList<HyperwalletStatusTransition> returnValue;
        try {
            returnValue = client.listPayPalAccountStatusTransitions("usr-f695ef43-9614-4e17-9269-902c234616c3",
                    "trm-da21954a-3910-4d70-b83d-0c2d96104394");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.hasNextPage(), is(equalTo(false)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
        assertThat(returnValue.getLimit(), is(equalTo(10)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("sts-70ddc78a-0c14-4a72-8390-75d49ff376f2")));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2018-10-30T18:50:20 UTC"))));
        assertThat(returnValue.getData().get(0).getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getData().get(0).getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getNotes(), is(equalTo("PayPal account is removed.")));
    }

    @Test
    public void testCreatePayPalAccountStatusTransition() throws Exception {
        String functionality = "createPayPalAccountStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setNotes("PayPal account removed.");
        transition.setTransition(DE_ACTIVATED);


        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.createPayPalAccountStatusTransition("usr-f695ef43-9614-4e17-9269-902c234616c3",
                    "trm-da21954a-3910-4d70-b83d-0c2d96104394",
                    transition);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("sts-70ddc78a-0c14-4a72-8390-75d49ff376f2")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2018-10-30T18:50:20 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("PayPal account removed.")));
    }

    @Test
    public void testGetPayPalAccountStatusTransition() throws Exception {
        String functionality = "getPayPalAccountStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.getPayPalAccountStatusTransition("usr-f695ef43-9614-4e17-9269-902c234616c3",
                    "trm-da21954a-3910-4d70-b83d-0c2d96104394",
                    "sts-70ddc78a-0c14-4a72-8390-75d49ff376f2");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("sts-70ddc78a-0c14-4a72-8390-75d49ff376f2")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2018-10-30T18:50:20 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("PayPal account is removed.")));
    }

    @Test
    public void testUpdatePayPalAccount() throws Exception {
        String functionality = "updatePayPalAccount";
        initMockServer(functionality);

        HyperwalletPayPalAccount payPalAccount = new HyperwalletPayPalAccount();
        payPalAccount
                .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .token("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b")
                .email("user1@domain.com");

        HyperwalletPayPalAccount returnValue;
        try {
            returnValue = client.updatePayPalAccount(payPalAccount);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/paypal-accounts/trm-ac5727ac-8fe7-42fb"
                        + "-b69d-977ebdd7b48b");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletPayPalAccount.Status.ACTIVATED)));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletPayPalAccount.Type.PAYPAL_ACCOUNT)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2019-01-09T22:50:14 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getEmail(), is(equalTo("user1@domain.com")));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }


    //
    // Venmo Accounts
    //
    @Test
    public void testCreateVenmoAccount() throws Exception {
        String functionality = "createVenmoAccount";
        initMockServer(functionality);

        HyperwalletVenmoAccount venmoAccount = new HyperwalletVenmoAccount()
                .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .transferMethodCountry("US")
                .transferMethodCurrency("USD")
                .type(HyperwalletVenmoAccount.Type.VENMO_ACCOUNT)
                .accountId("9876543210");

        HyperwalletVenmoAccount returnValue;
        try {
            returnValue = client.createVenmoAccount(venmoAccount);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/venmo-accounts/trm-ac5727ac-8fe7-42fb"
                        + "-b69d-977ebdd7b48b");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);
        assertThat(returnValue.getToken(), is(equalTo("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletVenmoAccount.Status.ACTIVATED)));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletVenmoAccount.Type.VENMO_ACCOUNT)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2019-01-09T22:50:14 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getAccountId(), is(equalTo("9876543210")));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }


    @Test
    public void testGetVenmoAccount() throws Exception {
        String functionality = "getVenmoAccount";
        initMockServer(functionality);

        HyperwalletVenmoAccount returnValue;
        try {
            returnValue = client.getVenmoAccount("usr-c4292f1a-866f-4310-a289-b916853939de", "trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/venmo-accounts/trm-ac5727ac-8fe7-42fb"
                        + "-b69d-977ebdd7b48b");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);
        assertThat(returnValue.getToken(), is(equalTo("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletVenmoAccount.Status.ACTIVATED)));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletVenmoAccount.Type.VENMO_ACCOUNT)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2019-01-09T22:50:14 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getAccountId(), is(equalTo("9876543210")));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }

    @Test
    public void testListVenmoAccount() throws Exception {
        String functionality = "listVenmoAccounts";
        initMockServer(functionality);

        HyperwalletList<HyperwalletVenmoAccount> returnValue;
        try {
            returnValue = client.listVenmoAccounts("usr-c4292f1a-866f-4310-a289-b916853939de");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/venmo-accounts?limit=10");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);
        assertThat(returnValue.hasNextPage(), is(equalTo(false)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
        assertThat(returnValue.getLimit(), is(equalTo(10)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b")));
        assertThat(returnValue.getData().get(0).getStatus(), is(equalTo(HyperwalletVenmoAccount.Status.ACTIVATED)));
        assertThat(returnValue.getData().get(0).getType(), is(equalTo(HyperwalletVenmoAccount.Type.VENMO_ACCOUNT)));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2019-01-09T22:50:14 UTC"))));
        assertThat(returnValue.getData().get(0).getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getData().get(0).getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getData().get(0).getAccountId(), is(equalTo("9876543210")));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }

    @Test
    public void testUpdateVenmoAccount() throws Exception {
        String functionality = "updateVenmoAccount";
        initMockServer(functionality);
        HyperwalletVenmoAccount updateRequest = new HyperwalletVenmoAccount();
        updateRequest
                .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .token("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b")
                .accountId("9620766696");
        HyperwalletVenmoAccount returnValue;
        try {
            returnValue = client.updateVenmoAccount(updateRequest);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-c4292f1a-866f-4310-a289-b916853939de/venmo-accounts/trm-ac5727ac-8fe7-42fb"
                        + "-b69d-977ebdd7b48b");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);
        assertThat(returnValue.getToken(), is(equalTo("trm-ac5727ac-8fe7-42fb-b69d-977ebdd7b48b")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletVenmoAccount.Status.ACTIVATED)));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletVenmoAccount.Type.VENMO_ACCOUNT)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2019-01-09T22:50:14 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getAccountId(), is(equalTo("9620766696")));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }

    @Test
    public void testDeactivateVenmoAccount() throws Exception {
        String functionality = "deactivateVenmoAccount";
        initMockServer(functionality);

        HyperwalletStatusTransition returnValue;

        try {
            returnValue = client.deactivateVenmoAccount("usr-6838bf6a-4917-46c5-b924-b9fa4153d645",
                    "trm-f6dd5ef3-c534-4d28-bc2e-ddd15822e20f", "Venmo account removed.");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("sts-6a8c70b6-3634-4d83-beac-ea7050311ed1")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2019-01-09T22:50:14 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Venmo account removed.")));
    }

    @Test
    public void testListVenmoAccountStatusTransitions() throws Exception {
        String functionality = "listVenmoAccountStatusTransitions";
        initMockServer(functionality);

        HyperwalletList<HyperwalletStatusTransition> returnValue;
        try {
            returnValue = client.listVenmoAccountStatusTransitions("usr-6838bf6a-4917-46c5-b924-b9fa4153d645",
                    "trm-f6dd5ef3-c534-4d28-bc2e-ddd15822e20f");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        assertThat(returnValue.hasNextPage(), is(equalTo(false)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
        assertThat(returnValue.getLimit(), is(equalTo(10)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("sts-6a8c70b6-3634-4d83-beac-ea7050311ed1")));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2019-01-09T22:50:14 UTC"))));
        assertThat(returnValue.getData().get(0).getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getData().get(0).getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getNotes(), is(equalTo("Venmo account is removed.")));
    }

    @Test
    public void testCreateVenmoAccountStatusTransition() throws Exception {
        String functionality = "createVenmoAccountStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setNotes("Venmo account removed.");
        transition.setTransition(DE_ACTIVATED);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.createVenmoAccountStatusTransition("usr-6838bf6a-4917-46c5-b924-b9fa4153d645",
                    "trm-f6dd5ef3-c534-4d28-bc2e-ddd15822e20f",
                    transition);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("sts-6a8c70b6-3634-4d83-beac-ea7050311ed1")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2019-01-09T22:50:14 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Venmo account removed.")));
    }

    @Test
    public void testGetVenmoAccountStatusTransition() throws Exception {
        String functionality = "getVenmoAccountStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.getVenmoAccountStatusTransition("usr-6838bf6a-4917-46c5-b924-b9fa4153d645",
                    "trm-f6dd5ef3-c534-4d28-bc2e-ddd15822e20f",
                    "sts-6a8c70b6-3634-4d83-beac-ea7050311ed1");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("sts-6a8c70b6-3634-4d83-beac-ea7050311ed1")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2019-01-09T22:50:14 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Venmo account is removed.")));
    }

    //
    // Payments
    //
    @Test
    public void testListPaymentStatusTransitions() throws Exception {
        String functionality = "listPaymentStatusTransitions";
        initMockServer(functionality);

        HyperwalletList<HyperwalletStatusTransition> returnValue;
        try {
            returnValue = client.listPaymentStatusTransitions("pmt-2c059341-8281-4d30-a65d-a49d8e2a9b0f");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://uat-api.paylution.com/rest/v4/payments/pmt-2c059341-8281-4d30-a65d-a49d8e2a9b0f/status-transitions?limit=10");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.hasNextPage(), is(equalTo(false)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
        assertThat(returnValue.getData().get(1).getToken(), is(equalTo("sts-1f7f58a9-22e8-4fef-8d6e-a17e2c71db33")));
        assertThat(returnValue.getData().get(1).getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T02:04:17 UTC"))));
        assertThat(returnValue.getData().get(1).getTransition(), is(equalTo(RECALLED)));
        assertThat(returnValue.getData().get(1).getFromStatus(), is(equalTo(COMPLETED)));
        assertThat(returnValue.getData().get(1).getToStatus(), is(equalTo(RECALLED)));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }


    @Test
    public void testListPayments() throws Exception {
        String functionality = "listPayments";
        initMockServer(functionality);


        HyperwalletPaymentListOptions options = new HyperwalletPaymentListOptions();
        options.clientPaymentId("gv47LDuf")
                .sortBy("test-sort-by")
                .limit(10)
                .createdAfter(convertStringToDate("2017-10-06T15:03:13Z"))
                .createdBefore(convertStringToDate("2017-10-06T15:03:13Z"));

        HyperwalletList<HyperwalletPayment> returnValue;
        try {
            returnValue = client.listPayments(options);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink
                .setHref("https://api.sandbox.hyperwallet.com/rest/v4/payments?after=pmt-92739c73-ff0a-4011-970e-3de855347ea9&limit=10");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);
        assertThat(returnValue.hasNextPage(), is(equalTo(true)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(true)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("pmt-3ffb5fcc-1c98-48ce-9a6c-e4759933a037")));
        assertThat(returnValue.getData().get(0).getStatus(), is(equalTo("SCHEDULED")));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-06T15:03:13 UTC"))));
        assertThat(returnValue.getData().get(0).getAmount(), is(equalTo(50.00)));
        assertThat(returnValue.getData().get(0).getCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getData().get(0).getClientPaymentId(), is(equalTo("gv47LDuf")));
        assertThat(returnValue.getData().get(0).getPurpose(), is(equalTo("OTHER")));
        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());

    }

    @Test
    public void testCreatePaymentStatusTransition() throws Exception {
        String functionality = "createPaymentStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setNotes("Cancel a payment upon customer request.");
        transition.setTransition(CANCELLED);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.createPaymentStatusTransition("pmt-2c059341-8281-4d30-a65d-a49d8e2a9b0f",
                    transition);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/payments/pmt-8716ee36-8b8f-4572-bf68-b9d1f956a3a2/status-transitions/sts-46592b6c-7d8b"
                        + "-4e00-abf6-ca5a03880d31");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("sts-46592b6c-7d8b-4e00-abf6-ca5a03880d31")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-12-21T11:35:43 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(CANCELLED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(SCHEDULED)));
        assertThat(returnValue.getToStatus(), is(equalTo(CANCELLED)));
        assertThat(returnValue.getNotes(), is(equalTo("Cancel a payment upon customer request.")));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }

    @Test
    public void testGetPaymentStatusTransition() throws Exception {
        String functionality = "getPaymentStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition.setNotes("Closing this account.");
        transition.setTransition(DE_ACTIVATED);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.getPaymentStatusTransition("pmt-2c059341-8281-4d30-a65d-a49d8e2a9b0f",
                    "sts-1f7f58a9-22e8-4fef-8d6e-a17e2c71db33");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://uat-api.paylution.com/rest/v4/payments/pmt-2c059341-8281-4d30-a65d-a49d8e2a9b0f/status-transitions/sts-1f7f58a9-22e8-4fef"
                        + "-8d6e-a17e2c71db33");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("sts-1f7f58a9-22e8-4fef-8d6e-a17e2c71db33")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T02:04:17 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(COMPLETED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(CREATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(COMPLETED)));
        if (returnValue.getLinks() != null) {
            HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
            HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
            assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
            assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
        }
    }

    @Test
    public void testListTransferMethods() throws Exception {
        String functionality = "listTransferMethods";
        initMockServer(functionality);

        HyperwalletList<HyperwalletTransferMethod> returnValue;
        try {
            String userToken = "usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad";
            returnValue = client.listTransferMethods(userToken, null);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://localhost-hyperwallet.aws.paylution.net:8181/rest/v4/users/usr-539f5e81-a52e-4bc5-aba7-f17de183c900/transfer-methods?limit"
                        + "=100");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertFalse(returnValue.hasNextPage());
        assertFalse(returnValue.hasPreviousPage());
        HyperwalletTransferMethod bandTransferMethod = returnValue.getData().get(0);
        assertThat(bandTransferMethod.getToken(), is(equalTo("trm-a43b1064-da94-457f-ae56-e4f85bd36aec")));
        assertThat(bandTransferMethod.getType(), is(equalTo(HyperwalletTransferMethod.Type.BANK_ACCOUNT)));
        assertThat(bandTransferMethod.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(bandTransferMethod.getVerificationStatus(), is(equalTo(VerificationStatus.NOT_REQUIRED)));
        assertThat(bandTransferMethod.getCreatedOn(), is(equalTo(dateFormat.parse("2020-09-09T18:43:10 UTC"))));
        assertThat(bandTransferMethod.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(bandTransferMethod.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(bandTransferMethod.getBranchId(), is(equalTo("021000021")));
        assertThat(bandTransferMethod.getBankAccountId(), is(equalTo("1599657189")));
        assertThat(bandTransferMethod.getBankAccountPurpose(), is(equalTo("SAVINGS")));
        assertThat(bandTransferMethod.getUserToken(), is(equalTo("usr-539f5e81-a52e-4bc5-aba7-f17de183c900")));
        assertThat(bandTransferMethod.getProfileType(), is(equalTo(ProfileType.INDIVIDUAL)));
        assertThat(bandTransferMethod.getFirstName(), is(equalTo("FirstName")));
        assertThat(bandTransferMethod.getLastName(), is(equalTo("LastName")));
        assertThat(bandTransferMethod.getDateOfBirth(), is(equalTo(dateFormat.parse("2000-09-09T18:43:10 UTC"))));
        assertThat(bandTransferMethod.getGender(), is(equalTo(Gender.MALE)));
        assertThat(bandTransferMethod.getPhoneNumber(), is(equalTo("605-555-1323")));
        assertThat(bandTransferMethod.getMobileNumber(), is(equalTo("605-555-1323")));
        assertThat(bandTransferMethod.getGovernmentId(), is(equalTo("444444444")));
        assertThat(bandTransferMethod.getAddressLine1(), is(equalTo("1234 IndividualAddress St")));
        assertThat(bandTransferMethod.getAddressLine2(), is(equalTo("1234 AddressLineTwo St")));
        assertThat(bandTransferMethod.getCity(), is(equalTo("TestCity")));
        assertThat(bandTransferMethod.getStateProvince(), is(equalTo("CA")));
        assertThat(bandTransferMethod.getCountry(), is(equalTo("US")));
        assertThat(bandTransferMethod.getPostalCode(), is(equalTo("12345")));

        HyperwalletTransferMethod cardTransferMethod = returnValue.getData().get(1);

        assertThat(cardTransferMethod.getToken(), is(equalTo("trm-c8e4c164-7d5b-4b5b-8b6a-9c4d68c2a9fe")));
        assertThat(cardTransferMethod.getType(), is(equalTo(HyperwalletTransferMethod.Type.PREPAID_CARD)));
        assertThat(cardTransferMethod.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.PRE_ACTIVATED)));
        assertThat(cardTransferMethod.getVerificationStatus(), is(equalTo(VerificationStatus.NOT_REQUIRED)));
        assertThat(cardTransferMethod.getCreatedOn(), is(equalTo(dateFormat.parse("2020-09-09T18:43:34 UTC"))));
        assertThat(cardTransferMethod.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(cardTransferMethod.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(cardTransferMethod.getCardType(), is(equalTo(CardType.PERSONALIZED)));
        assertThat(cardTransferMethod.getCardPackage(), is(equalTo("L1")));
        assertThat(cardTransferMethod.getCardNumber(), is(equalTo("************4194")));
        assertThat(cardTransferMethod.getCardBrand(), is(equalTo(Brand.VISA)));
        assertThat(cardTransferMethod.getDateOfExpiry(), is(equalTo(dateFormat.parse("2024-09-01T00:00:00 UTC"))));
        assertThat(cardTransferMethod.getUserToken(), is(equalTo("usr-539f5e81-a52e-4bc5-aba7-f17de183c900")));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    //
    // Program Accounts
    //
    @Test
    public void testGetProgramAccount() throws Exception {
        String functionality = "getProgramAccount";
        initMockServer(functionality);
        HyperwalletAccount returnValue;
        try {
            returnValue = client.getProgramAccount("prg-83836cdf-2ce2-4696-8bc5-f1b86077238c", "act-da5795ed-d1c9-4231-9e71-611ab8fe9f66");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/programs/prg-83836cdf-2ce2-4696-8bc5-f1b86077238c/accounts/act-da5795ed-d1c9-4231-9e71"
                        + "-611ab8fe9f66");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("act-da5795ed-d1c9-4231-9e71-611ab8fe9f66")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletAccount.EType.FUNDING)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-04T21:19:24 UTC"))));
        assertThat(returnValue.getEmail(), is(equalTo("8715201615-fundingaccount@hyperwallet.com")));
        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    //
    // Webhook Notification
    //
    @Test
    public void testGetWebhookEvent() throws Exception {
        String functionality = "getWebhookEvent";
        initMockServer(functionality);

        HyperwalletWebhookNotification returnValue;
        try {
            returnValue = client.getWebhookEvent("wbh-53010937-fbe4-4040-9a87-9fa0065f79bb");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref("https://uat-api.paylution.com/rest/v4/webhook-notifications/wbh-53010937-fbe4-4040-9a87-9fa0065f79bb");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);
        HashMap<String, String> map = (HashMap) returnValue.getObject();
        assertThat(returnValue.getToken(), is(equalTo("wbh-53010937-fbe4-4040-9a87-9fa0065f79bb")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletWebhookNotification.Type.USER_CREATED.toString())));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2019-12-21T11:35:43 UTC"))));
        assertThat(returnValue.getObject(), is(instanceOf(HashMap.class)));
        assertThat(map.get("token"), is("usr-23e4d66a-879d-419b-950b-3f8d344c9cc7"));
        assertThat(map.get("status"), is("PRE_ACTIVATED"));
        assertThat(map.get("createdOn"), is("2019-12-21T11:35:43"));
        assertThat(map.get("clientUserId"), is("webhook-cleint"));
        assertThat(map.get("profileType"), is("INDIVIDUAL"));
        assertThat(map.get("firstName"), is("John"));
        assertThat(map.get("lastName"), is("Smith"));
        assertThat(map.get("dateOfBirth"), is("1991-01-01"));
        assertThat(map.get("email"), is("XKOSdXRs@hyperwallet.com"));
        assertThat(map.get("addressLine1"), is("123 Main Street"));
        assertThat(map.get("city"), is("New York"));
        assertThat(map.get("stateProvince"), is("NY"));
        assertThat(map.get("country"), is("US"));
        assertThat(map.get("postalCode"), is("10016"));
        assertThat(map.get("language"), is("en"));
        assertThat(map.get("programToken"), is("prg-31507b29-95df-4b37-95ca-ecba6c06f995"));
        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());

    }

    @Test
    public void testListWebhookEvents() throws Exception {
        String functionality = "listWebhookEvent";
        initMockServer(functionality);

        HyperwalletList<HyperwalletWebhookNotification> returnValue;
        try {
            returnValue = client.listWebhookEvents();
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref("https://api.sandbox.hyperwallet.com/rest/v4/webhook-notifications?limit=5");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);
        assertThat(returnValue.hasNextPage(), is(equalTo(true)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(true)));
        assertThat(returnValue.getLimit(), is(equalTo(5)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("wbh-946472ab-4e17-4224-9abe-e91f9e1d4031")));
        assertThat(returnValue.getData().get(0).getType(), is(equalTo(HyperwalletWebhookNotification.Type.USER_CREATED.toString())));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-10T21:51:00 UTC"))));
        HashMap<String, String> map = (HashMap) returnValue.getData().get(0).getObject();
        assertThat(returnValue.getData().get(0).getObject(), is(instanceOf(HashMap.class)));
        assertThat(map.get("token"), is("usr-23e4d66a-879d-419b-950b-3f8d344c9cc7"));
        assertThat(map.get("status"), is("PRE_ACTIVATED"));
        assertThat(map.get("createdOn"), is("2019-12-21T11:35:43"));
        assertThat(map.get("clientUserId"), is("webhook-cleint"));
        assertThat(map.get("profileType"), is("INDIVIDUAL"));
        assertThat(map.get("firstName"), is("John"));
        assertThat(map.get("lastName"), is("Smith"));
        assertThat(map.get("dateOfBirth"), is("1991-01-01"));
        assertThat(map.get("email"), is("XKOSdXRs@hyperwallet.com"));
        assertThat(map.get("addressLine1"), is("123 Main Street"));
        assertThat(map.get("city"), is("New York"));
        assertThat(map.get("stateProvince"), is("NY"));
        assertThat(map.get("country"), is("US"));
        assertThat(map.get("postalCode"), is("10016"));
        assertThat(map.get("language"), is("en"));
        assertThat(map.get("programToken"), is("prg-31507b29-95df-4b37-95ca-ecba6c06f995"));
        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());

    }

    @Test
    public void testCreateUser() throws Exception {
        String functionality = "createUser";
        initMockServer(functionality);
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
                .profileType(ProfileType.INDIVIDUAL)
                .governmentId("333333333")
                .phoneNumber("605-555-1323")
                .programToken("prg-362d09bd-1d3c-48fe-8209-c42708cd0bf7")
                .stateProvince("CA")
                .governmentIdType(GovernmentIdType.PASSPORT);

        HyperwalletUser returnValue;
        try {
            returnValue = client.createUser(hyperwalletUser);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("usr-be3dbae6-7f3d-4cf2-ab03-9d794764c943")));
        assertThat(returnValue.getLinks(), is(notNullValue()));
        assertEquals(returnValue.getLinks().get(0).getHref(),
                "https://localhost:8181/rest/v4/users/usr-be3dbae6-7f3d-4cf2-ab03-9d794764c943");
        assertThat(returnValue.getLinks().get(0).getParams(), is(notNullValue()));
        assertEquals(returnValue.getLinks().get(0).getParams().get("rel"), "self");
        assertEquals(returnValue.getFirstName(), "John");
        assertEquals(returnValue.getLastName(), "Smith");
        assertEquals(returnValue.getGovernmentId(), "333333333");
        assertEquals(returnValue.getProgramToken(), "prg-362d09bd-1d3c-48fe-8209-c42708cd0bf7");
        assertEquals(returnValue.getVerificationStatus().toString(), "NOT_REQUIRED");
        assertEquals(returnValue.getGovernmentIdType(), GovernmentIdType.PASSPORT);
    }

    @Test
    public void testUpdateUser() throws Exception {
        String functionality = "updateUser";
        initMockServer(functionality);

        HyperwalletUser hyperwalletUser = new HyperwalletUser()
                .firstName("Jim")
                .token("usr-f122bc13-d6b0-4da0-9631-8b74f6364299");

        HyperwalletUser returnValue;
        try {
            returnValue = client.updateUser(hyperwalletUser);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("usr-f122bc13-d6b0-4da0-9631-8b74f6364299")));
        assertThat(returnValue.getLinks(), is(notNullValue()));
        assertEquals(returnValue.getLinks().get(0).getHref(),
                "https://localhost:8181/rest/v4/users/usr-46e3e5c9-6dde-4321-b4f7-20b975aa8124");
        assertThat(returnValue.getLinks().get(0).getParams(), is(notNullValue()));
        assertEquals(returnValue.getLinks().get(0).getParams().get("rel"), "self");
        assertEquals(returnValue.getFirstName(), "Jim");
        assertEquals(returnValue.getGovernmentIdType(), GovernmentIdType.PASSPORT);
    }

    //
    // Response with error
    //

    @Test
    public void testCreateBankCardWithErrorResponse() throws Exception {
        String functionality = "createBankCardWithError";
        initMockServerWithErrorResponse(functionality);

        HyperwalletBankCard bankCard = new HyperwalletBankCard()
                .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .cardNumber("4216701111100114")
                .dateOfExpiry(dateFormat.parse("2018-01-01T00:00:00 UTC"))
                .cvv("123")
                .transferMethodCountry("US")
                .transferMethodCurrency("USD");

        try {
            client.createBankCard(bankCard);
            fail("Expect HyperwalletException");
        } catch (HyperwalletException e) {
            assertThat(e.getErrorCode(), is(equalTo("DUPLICATE_EXTERNAL_ACCOUNT_CREATION")));
            assertThat(e.getMessage(),
                    is(equalTo("The information you provided is already registered with this user.")));
            assertThat(e.getRelatedResources(), hasSize(5));
            assertThat(e.getRelatedResources(), hasItems("trm-f3d38df1-adb7-4127-9858-e72ebe682a79",
                    "trm-601b1401-4464-4f3f-97b3-09079ee7723b", "trm-66166e6e-c0cb-4161-b1cd-a7ec3f83dc17",
                    "trm-41c559b6-c803-4213-b8b4-c54f0e72df17", "trm-76558de0-5703-41e0-bf3f-a335a5cf58be"));
            assertThat(e.getHyperwalletErrors(), hasSize(1));
            HyperwalletError error = e.getHyperwalletErrors().get(0);
            assertThat(error.getCode(), is(equalTo("DUPLICATE_EXTERNAL_ACCOUNT_CREATION")));
            assertThat(error.getFieldName(), is(nullValue()));
            assertThat(error.getMessage(),
                    is(equalTo("The information you provided is already registered with this user.")));
            assertThat(error.getRelatedResources(), hasSize(5));
            assertThat(error.getRelatedResources(), hasItems("trm-f3d38df1-adb7-4127-9858-e72ebe682a79",
                    "trm-601b1401-4464-4f3f-97b3-09079ee7723b", "trm-66166e6e-c0cb-4161-b1cd-a7ec3f83dc17",
                    "trm-41c559b6-c803-4213-b8b4-c54f0e72df17", "trm-76558de0-5703-41e0-bf3f-a335a5cf58be"));
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
    }

    //
    // Bank Accounts
    //

    @Test
    public void testCreateBankAccount() throws Exception {
        String functionality = "createBankAccount";
        initMockServer(functionality);

        HyperwalletBankAccount bankAccount = new HyperwalletBankAccount()
                .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .transferMethodCountry("US")
                .transferMethodCurrency("USD")
                .type(HyperwalletBankAccount.Type.BANK_ACCOUNT)
                .branchId("021000021")
                .bankAccountId("1234")
                .bankAccountPurpose("SAVINGS");
        HyperwalletBankAccount returnValue;
        try {
            returnValue = client.createBankAccount(bankAccount);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad/bank-accounts/trm-2bd0b56d-e111-4d12"
                        + "-bd24-d77fc02b9f4f");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-2bd0b56d-e111-4d12-bd24-d77fc02b9f4f")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletBankAccount.Type.BANK_ACCOUNT)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletBankAccount.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2020-09-08T15:01:07 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getBankName(), is(equalTo("JPMORGAN CHASE BANK")));
        assertThat(returnValue.getBranchId(), is(equalTo("021000021")));
        assertThat(returnValue.getBankAccountId(), is(equalTo("1599557466")));
        assertThat(returnValue.getBankAccountPurpose(), is(equalTo("SAVINGS")));
        assertThat(returnValue.getUserToken(), is(equalTo("usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad")));
        assertThat(returnValue.getProfileType(), is(equalTo(ProfileType.INDIVIDUAL)));
        assertThat(returnValue.getFirstName(), is(equalTo("firstName")));
        assertThat(returnValue.getLastName(), is(equalTo("lastName")));
        assertThat(returnValue.getDateOfBirth(), is(equalTo(dateFormat.parse("2000-09-08T15:01:07 UTC"))));
        assertThat(returnValue.getGender(), is(equalTo(Gender.MALE)));
        assertThat(returnValue.getPhoneNumber(), is(equalTo("605-555-1323")));
        assertThat(returnValue.getMobileNumber(), is(equalTo("605-555-1323")));
        assertThat(returnValue.getGovernmentId(), is(equalTo("444444444")));
        assertThat(returnValue.getAddressLine1(), is(equalTo("1234 IndividualAddress St")));
        assertThat(returnValue.getAddressLine2(), is(equalTo("1234 AddressLineTwo St")));
        assertThat(returnValue.getCity(), is(equalTo("Test1111")));
        assertThat(returnValue.getStateProvince(), is(equalTo("CA")));
        assertThat(returnValue.getCountry(), is(equalTo("US")));
        assertThat(returnValue.getPostalCode(), is(equalTo("12345")));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testGetBankAccount() throws Exception {
        String functionality = "getBankAccount";
        initMockServer(functionality);

        HyperwalletBankAccount returnValue;

        try {
            String userToken = "usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad";
            String transferMethodToken = "trm-2bd0b56d-e111-4d12-bd24-d77fc02b9f4f";
            returnValue = client.getBankAccount(userToken, transferMethodToken);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad/bank-accounts/trm-2bd0b56d-e111-4d12"
                        + "-bd24-d77fc02b9f4f");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-2bd0b56d-e111-4d12-bd24-d77fc02b9f4f")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletBankAccount.Type.BANK_ACCOUNT)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletBankAccount.Status.DE_ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2020-09-08T15:01:07 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getBankName(), is(equalTo("JPMORGAN CHASE BANK")));
        assertThat(returnValue.getBranchId(), is(equalTo("021000021")));
        assertThat(returnValue.getBankAccountId(), is(equalTo("1599557466")));
        assertThat(returnValue.getBankAccountPurpose(), is(equalTo("SAVINGS")));
        assertThat(returnValue.getUserToken(), is(equalTo("usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad")));
        assertThat(returnValue.getProfileType(), is(equalTo(ProfileType.INDIVIDUAL)));
        assertThat(returnValue.getFirstName(), is(equalTo("firstName")));
        assertThat(returnValue.getLastName(), is(equalTo("lastName")));
        assertThat(returnValue.getDateOfBirth(), is(equalTo(dateFormat.parse("2000-09-08T15:01:07 UTC"))));
        assertThat(returnValue.getGender(), is(equalTo(Gender.MALE)));
        assertThat(returnValue.getPhoneNumber(), is(equalTo("605-555-1323")));
        assertThat(returnValue.getMobileNumber(), is(equalTo("605-555-1323")));
        assertThat(returnValue.getGovernmentId(), is(equalTo("444444444")));
        assertThat(returnValue.getAddressLine1(), is(equalTo("1234 IndividualAddress St")));
        assertThat(returnValue.getAddressLine2(), is(equalTo("1234 AddressLineTwo St")));
        assertThat(returnValue.getCity(), is(equalTo("Test1111")));
        assertThat(returnValue.getStateProvince(), is(equalTo("CA")));
        assertThat(returnValue.getCountry(), is(equalTo("US")));
        assertThat(returnValue.getPostalCode(), is(equalTo("12345")));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testGetBankAccountLists() throws Exception {
        String functionality = "listBankAccounts";
        initMockServer(functionality);

        HyperwalletList<HyperwalletBankAccount> bankAccountHyperwalletList;

        try {
            String userToken = "usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad";
            bankAccountHyperwalletList = client.listBankAccounts(userToken);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad/bank-accounts/trm-2bd0b56d-e111-4d12"
                        + "-bd24-d77fc02b9f4f");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        HyperwalletBankAccount returnValue = bankAccountHyperwalletList.getData().get(0);
        assertThat(returnValue.getToken(), is(equalTo("trm-2bd0b56d-e111-4d12-bd24-d77fc02b9f4f")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletBankAccount.Type.BANK_ACCOUNT)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletBankAccount.Status.DE_ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2020-09-08T15:01:07 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getBankName(), is(equalTo("JPMORGAN CHASE BANK")));
        assertThat(returnValue.getBranchId(), is(equalTo("021000021")));
        assertThat(returnValue.getBankAccountId(), is(equalTo("1599557466")));
        assertThat(returnValue.getBankAccountPurpose(), is(equalTo("SAVINGS")));
        assertThat(returnValue.getUserToken(), is(equalTo("usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad")));
        assertThat(returnValue.getProfileType(), is(equalTo(ProfileType.INDIVIDUAL)));
        assertThat(returnValue.getFirstName(), is(equalTo("firstName")));
        assertThat(returnValue.getLastName(), is(equalTo("lastName")));
        assertThat(returnValue.getDateOfBirth(), is(equalTo(dateFormat.parse("2000-09-08T15:01:07 UTC"))));
        assertThat(returnValue.getGender(), is(equalTo(Gender.MALE)));
        assertThat(returnValue.getPhoneNumber(), is(equalTo("605-555-1323")));
        assertThat(returnValue.getMobileNumber(), is(equalTo("605-555-1323")));
        assertThat(returnValue.getGovernmentId(), is(equalTo("444444444")));
        assertThat(returnValue.getAddressLine1(), is(equalTo("1234 IndividualAddress St")));
        assertThat(returnValue.getAddressLine2(), is(equalTo("1234 AddressLineTwo St")));
        assertThat(returnValue.getCity(), is(equalTo("Test1111")));
        assertThat(returnValue.getStateProvince(), is(equalTo("CA")));
        assertThat(returnValue.getCountry(), is(equalTo("US")));
        assertThat(returnValue.getPostalCode(), is(equalTo("12345")));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());

        HyperwalletLink hyperwalletLink1 = bankAccountHyperwalletList.getLinks().get(0);
        assertThat(hyperwalletLink1.getHref(),
                is(equalTo("https://api.sandbox.hyperwallet.com/rest/v4/users/usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad/bank-accounts?limit=100")));
        assertEquals(hyperwalletLink1.getParams(), hyperwalletLinks.get(0).getParams());
    }

    @Test
    public void testDeactivateBankAccount() throws Exception {
        String functionality = "deactivateBankAccount";
        initMockServer(functionality);

        HyperwalletStatusTransition returnValue;
        try {
            String userToken = "usr-c4292f1a-866f-4310-a289-b916853939de";
            String bankAccountToken = "trm-d69300ef-5011-486b-bd2e-bfd8b20fef26";
            returnValue = client.deactivateBankAccount(userToken, bankAccountToken);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-f695ef43-9614-4e17-9269-902c234616c3/bank-accounts/trm-d69300ef-5011-486b"
                        + "-bd2e-bfd8b20fef26/status-transitions/sts-1825afa2-61f1-4860-aa69-a65b9d14f556");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testGetBankAccountStatusTransition() throws Exception {
        String functionality = "getBankAccountStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.getBankAccountStatusTransition("usr-f695ef43-9614-4e17-9269-902c234616c3",
                    "trm-d69300ef-5011-486b-bd2e-bfd8b20fef26",
                    "sts-1825afa2-61f1-4860-aa69-a65b9d14f556");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-f695ef43-9614-4e17-9269-902c234616c3/bank-accounts/trm-d69300ef-5011-486b-bd2e"
                + "-bfd8b20fef26/status-transitions/sts-1825afa2-61f1-4860-aa69-a65b9d14f556");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);
        assertThat(returnValue.getToken(), is(equalTo("sts-1825afa2-61f1-4860-aa69-a65b9d14f556")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T00:55:57 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Closing this account.")));
        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }


    @Test
    public void testListBankAccountStatusTransitions() throws Exception {
        String functionality = "listBankAccountStatusTransitions";
        initMockServer(functionality);

        HyperwalletList<HyperwalletStatusTransition> hyperwalletStatusTransitionHyperwalletList;
        try {
            String userToken = "usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad";
            String bankAccountToken = "trm-2bd0b56d-e111-4d12-bd24-d77fc02b9f4f";
            hyperwalletStatusTransitionHyperwalletList = client.listBankAccountStatusTransitions(userToken, bankAccountToken);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad/bank-accounts/trm-2bd0b56d-e111-4d12"
                        + "-bd24-d77fc02b9f4f/status-transitions/sts-1834b075-ee3c-4c00-b1dc-a517fe8a87ab");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        HyperwalletStatusTransition returnValue = hyperwalletStatusTransitionHyperwalletList.getData().get(0);
        assertThat(returnValue.getToken(), is(equalTo("sts-1834b075-ee3c-4c00-b1dc-a517fe8a87ab")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2020-09-08T15:01:58 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Deactivating card")));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());

        HyperwalletLink hyperwalletLink1 = hyperwalletStatusTransitionHyperwalletList.getLinks().get(0);
        assertThat(hyperwalletLink1.getHref(), is(equalTo(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad/bank-accounts/trm-2bd0b56d-e111-4d12"
                        + "-bd24-d77fc02b9f4f/status-transitions?limit=100")));
        assertEquals(hyperwalletLink1.getParams(), hyperwalletLinks.get(0).getParams());
    }

    @Test
    public void updateBankAccount() throws Exception {
        String functionality = "updateBankAccount";
        initMockServer(functionality);

        HyperwalletBankAccount bankAccount = new HyperwalletBankAccount()
                .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
                .token("trm-950a3055-e54c-456f-beb2-c6f6d42eddce")
                .firstName("FirstNameChanged");

        HyperwalletBankAccount returnValue;
        try {
            returnValue = client.updateBankAccount(bankAccount);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad/bank-accounts/trm-2bd0b56d-e111-4d12"
                        + "-bd24-d77fc02b9f4f");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("trm-2bd0b56d-e111-4d12-bd24-d77fc02b9f4f")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletBankAccount.Type.BANK_ACCOUNT)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletBankAccount.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2020-09-08T15:01:07 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getBankName(), is(equalTo("JPMORGAN CHASE BANK")));
        assertThat(returnValue.getBranchId(), is(equalTo("021000021")));
        assertThat(returnValue.getBankAccountId(), is(equalTo("1599557466")));
        assertThat(returnValue.getBankAccountPurpose(), is(equalTo("SAVINGS")));
        assertThat(returnValue.getUserToken(), is(equalTo("usr-321ad2c1-df3f-4a7a-bce4-3e88416b54ad")));
        assertThat(returnValue.getProfileType(), is(equalTo(ProfileType.INDIVIDUAL)));
        assertThat(returnValue.getFirstName(), is(equalTo("FirstNameChanged")));
        assertThat(returnValue.getLastName(), is(equalTo("lastName")));
        assertThat(returnValue.getDateOfBirth(), is(equalTo(dateFormat.parse("2000-09-08T15:01:07 UTC"))));
        assertThat(returnValue.getGender(), is(equalTo(Gender.MALE)));
        assertThat(returnValue.getPhoneNumber(), is(equalTo("605-555-1323")));
        assertThat(returnValue.getMobileNumber(), is(equalTo("605-555-1323")));
        assertThat(returnValue.getGovernmentId(), is(equalTo("444444444")));
        assertThat(returnValue.getAddressLine1(), is(equalTo("1234 IndividualAddress St")));
        assertThat(returnValue.getAddressLine2(), is(equalTo("1234 AddressLineTwo St")));
        assertThat(returnValue.getCity(), is(equalTo("Test1111")));
        assertThat(returnValue.getStateProvince(), is(equalTo("CA")));
        assertThat(returnValue.getCountry(), is(equalTo("US")));
        assertThat(returnValue.getPostalCode(), is(equalTo("12345")));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    //
    // Programs
    //
    @Test
    public void testGetProgram() throws Exception {
        String functionality = "getProgram";
        initMockServer(functionality);

        HyperwalletProgram returnValue;
        try {
            returnValue = client.getProgram("prg-83836cdf-2ce2-4696-8bc5-f1b86077238c");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/programs/prg-83836cdf-2ce2-4696-8bc5-f1b86077238c");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);

        assertThat(returnValue.getToken(), is(equalTo("prg-83836cdf-2ce2-4696-8bc5-f1b86077238c")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-04T21:19:24 UTC"))));
        assertThat(returnValue.getName(), is(equalTo("Hyperwallet - Portal")));
        assertThat(returnValue.getParentToken(), is(equalTo("prg-a4b617e2-24c9-4891-9ee5-741489613f73")));

        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    //
    // Business Stakeholders
    //

    @Test
    public void testGetBusinessStakeholderStatusTransition() throws Exception {
        String functionality = "getBusinessStakeholderStatusTransition";
        initMockServer(functionality);

        HyperwalletStatusTransition returnValue;
        try {
            returnValue = client.getBusinessStakeholderStatusTransition("usr-9b0e6dfe-c26b-4ddf-9d79-96b332c1dc8a",
                    "stk-6829da02-7258-4b2b-bcb8-4bb97d4187d2", "sts-16d9ae38-f29f-4360-a1c0-0bceafb12c27");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://localhost-hyperwallet.aws.paylution.net:8181/rest/v4/users/usr-9b0e6dfe-c26b-4ddf-9d79-96b332c1dc8a/business-stakeholders"
                + "/stk-6829da02-7258-4b2b-bcb8-4bb97d4187d2/status-transitions/sts-16d9ae38-f29f-4360-a1c0-0bceafb12c27");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);
        assertThat(returnValue.getToken(), is(equalTo("sts-16d9ae38-f29f-4360-a1c0-0bceafb12c27")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2020-11-13T00:41:03 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testListBusinessStakeholderStatusTransition() throws Exception {
        String functionality = "listBusinessStakeholderStatusTransition";
        initMockServer(functionality);

        HyperwalletList<HyperwalletStatusTransition> returnValue;
        try {
            returnValue = client.listBusinessStakeholderStatusTransition("usr-f695ef43-9614-4e17-9269-902c234616c3",
                    "stk-6829da02-7258-4b2b-bcb8-4bb97d4187d2");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        List<HyperwalletLink> hyperwalletLinks = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLink.setHref(
                "https://api.sandbox.hyperwallet.com/rest/v4/users/usr-f695ef43-9614-4e17-9269-902c234616c3/business-stakeholders/stk-6829da02-7258"
                + "-4b2b-bcb8-4bb97d4187d2/status-transitions?limit=10");
        Map<String, String> mapParams = new HashMap<>();
        mapParams.put("rel", "self");
        hyperwalletLink.setParams(mapParams);
        hyperwalletLinks.add(hyperwalletLink);
        assertThat(returnValue.hasNextPage(), is(equalTo(false)));
        assertThat(returnValue.hasPreviousPage(), is(equalTo(false)));
        assertThat(returnValue.getLimit(), is(equalTo(10)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("sts-1825afa2-61f1-4860-aa69-a65b9d14f556")));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T00:55:57 UTC"))));
        assertThat(returnValue.getData().get(0).getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getData().get(0).getToStatus(), is(equalTo(DE_ACTIVATED)));
        HyperwalletLink actualHyperwalletLink = returnValue.getLinks().get(0);
        HyperwalletLink expectedHyperwalletLink = hyperwalletLinks.get(0);
        assertThat(actualHyperwalletLink.getHref(), is(equalTo(expectedHyperwalletLink.getHref())));
        assertEquals(actualHyperwalletLink.getParams(), expectedHyperwalletLink.getParams());
    }

    @Test
    public void testUploadStakeholderDocuments() throws Exception {
        String functionality = "uploadStakeholderDocuments";
        initMockServer(functionality);

        HyperwalletBusinessStakeholder returnValue;
        HyperwalletVerificationDocument hyperwalletVerificationDocument = new HyperwalletVerificationDocument();;
        try {
            String userToken = "usr-490848fb-8e1f-4f7c-9a18-a5b7a372e602";
            String stkToken = "stk-e08f13b8-0e54-43d2-a587-67d513633275";
            Multipart multipart = new Multipart();
            List<HyperwalletVerificationDocument> documentList = new ArrayList<>();
            hyperwalletVerificationDocument.setType("DRIVERS_LICENSE");
            hyperwalletVerificationDocument.setCategory("IDENTIFICATION");
            hyperwalletVerificationDocument.setCountry("US");
            Map<String, String> fileList =  new HashMap<>();
            fileList.put("drivers_license_front",  "src/test/resources/integration/test.png");
            fileList.put("drivers_license_back",  "src/test/resources/integration/test.png");
            hyperwalletVerificationDocument.setUploadFiles(fileList);
            documentList.add(hyperwalletVerificationDocument);
            returnValue = client.uploadStakeholderDocuments(userToken, stkToken, documentList);

        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        HyperwalletVerificationDocument document = new HyperwalletVerificationDocument();
        assertThat(returnValue.getToken(), is(equalTo("stk-e08f13b8-0e54-43d2-a587-67d513633275")));
        assertThat(returnValue.getVerificationStatus(), is(equalTo(HyperwalletBusinessStakeholder.VerificationStatus.UNDER_REVIEW)));
        assertThat(hyperwalletVerificationDocument.getCategory(), is(equalTo("IDENTIFICATION")));
        assertThat(hyperwalletVerificationDocument.getType(), is(equalTo("DRIVERS_LICENSE")));
        assertThat(hyperwalletVerificationDocument.getCountry(), is(equalTo("US")));
    }

    @Test
    public void uploadStakeholderDocuments_invalidDocumentStatus() throws Exception {
        String functionality = "uploadStakeholderDocumentsInvalidStatus";
        initMockServer(functionality);

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
            mockServer.verify(parseRequest(functionality));
            throw e;
        }
        assertThat(returnValue.getToken(), is(equalTo("stk-e08f13b8-0e54-43d2-a587-67d513633275")));
        assertThat(returnValue.getVerificationStatus(), is(equalTo(HyperwalletBusinessStakeholder.VerificationStatus.UNDER_REVIEW)));
        assertThat(returnValue.getDocuments().get(0).getCategory(), is(equalTo("IDENTIFICATION")));
        assertThat(returnValue.getDocuments().get(0).getType(), is(equalTo("DRIVERS_LICENSE")));
        assertThat(returnValue.getDocuments().get(0).getCountry(), is(equalTo("US")));
        assertThat(returnValue.getDocuments().get(0).getReasons().size(), is(2));
        assertThat(returnValue.getDocuments().get(0).getReasons().get(0).getName(), is(RejectReason.DOCUMENT_CORRECTION_REQUIRED));
        assertThat(returnValue.getDocuments().get(0).getReasons().get(0).getDescription(), is("Document requires correction"));
        assertThat(returnValue.getDocuments().get(0).getReasons().get(1).getName(), is(RejectReason.DOCUMENT_NOT_DECISIVE));
        assertThat(returnValue.getDocuments().get(0).getReasons().get(1).getDescription(),
                is("Decision cannot be made based on document. Alternative document required"));
        assertThat(returnValue.getDocuments().get(0).getCreatedOn(), is(dateFormat.parse("2020-11-24T19:05:02 UTC")));
    }

    //
    // Authentication Token
    //
    @Test
    public void testCreateAuthenticationToken() throws Exception {
        String functionality = "createAuthenticationToken";
        initMockServer(functionality);

        HyperwalletAuthenticationToken returnValue;
        try {
            returnValue = client.getAuthenticationToken("usr-c4292f1a-866f-4310-a289-b916853939de");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getValue(), is(equalTo(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3ItMmQyZGNlYWMtNDlmNi00YWQwLTk0N2YtMTIwOTIzNzhhMmQyIiwiaWF0IjoxNTQ0ODI5ODA2LCJleHAiOjE1NDQ4MzA0MDYsImF1ZCI6InBndS03YTEyMzJlOC0xNDc5LTQzNzAtOWY1NC03ODc1ZjdiMTg2NmMiLCJpc3MiOiJwcmctY2NhODAyNWUtODVhMy0xMWU2LTg2MGEtNThhZDVlY2NlNjFkIiwicmVzdC11cmkiOiJodHRwczovL3FhbWFzdGVyLWh5cGVyd2FsbGV0LmF3cy5wYXlsdXRpb24ubmV0L3Jlc3QvdjMvIiwiZ3JhcGhxbC11cmkiOiJodHRwczovL3FhbWFzdGVyLWh5cGVyd2FsbGV0LmF3cy5wYXlsdXRpb24ubmV0L2dyYXBocWwifQ.pGOdbYermGhiON5IFKSnXZd6Zj hktMd3WEDOMplYyAeiqVeZGck04eVpsBaXEqYp78NJIs7J5kMX-rPgFYxHpw")));
    }


    private void initMockServerWithErrorResponse(String functionality) throws IOException {
        mockServer.reset();
        mockServer
                .when(parseRequest(functionality))
                .respond(parseResponseWithErrorResponse(functionality));
    }

    private void initMockServer(String functionality) throws IOException {
        mockServer.reset();
        mockServer
            .when(parseRequest(functionality))
            .respond(parseResponse(functionality));
    }

    private HttpResponse parseResponse(String functionality) throws IOException {
        return HttpResponse.response()
            .withHeader("Content-Type", "application/json")
            .withStatusCode(HttpStatusCode.OK_200.code())
            .withBody(org.apache.commons.io.IOUtils.toString(getClass().getResourceAsStream("/integration/" + functionality + "-response.json")));
    }

    private HttpResponse parseResponseWithErrorResponse(String functionality) throws IOException {
        return HttpResponse.response()
                .withStatusCode(HttpStatusCode.NOT_FOUND_404.code())
                .withBody(org.apache.commons.io.IOUtils.toString(
                        getClass().getResourceAsStream("/integration/" + functionality + "-response.json")));
    }

    private HttpRequest parseRequest(String functionality) {
        Scanner scanner = new Scanner(getClass().getResourceAsStream("/integration/" + functionality + "-request.txt"));
        String method = scanner.findInLine("(POST|PUT|DELETE|GET)");
        String url = scanner.findInLine("(https[^\"]*)").replace("https://api.sandbox.hyperwallet.com", "");
        scanner.nextLine();

        if(scanner.hasNext("-u")){
            scanner.nextLine();
        }

        Map<String, String> headers = new HashMap<String, String>();
        while (scanner.hasNext("-H")){
            String[] headerAndValue = scanner.findInLine("[^\"]*:[^\"]*").split(":");
            headers.put(headerAndValue[0].trim(), headerAndValue[1].trim());
            scanner.nextLine();
        }

        String body = null;
        if(scanner.hasNext("-d")) {
            StringBuilder bodyStringBuilder = new StringBuilder();
            scanner.next("-d");
            while (scanner.hasNextLine()) {
                bodyStringBuilder.append(scanner.nextLine());
            }
            body = bodyStringBuilder.toString().trim();
            body = body.substring(1);
            body = body.substring(0, body.length() - 1);
        }

        HttpRequest request = HttpRequest.request()
            .withHeader(header("Authorization", ".*" ))
            .withMethod(method)
            .withPath(url);
        for(Map.Entry<String, String> header: headers.entrySet()){
            request = request.withHeader(header.getKey(), header.getValue());
        }
        return body == null? request : request.withBody(json(body));
    }

    private Date convertStringToDate(String date) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.parse(date);
    }

}
