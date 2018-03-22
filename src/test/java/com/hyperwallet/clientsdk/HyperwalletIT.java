package com.hyperwallet.clientsdk;

import static com.hyperwallet.clientsdk.model.HyperwalletStatusTransition.Status.DE_ACTIVATED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.JsonBody.json;

import com.hyperwallet.clientsdk.model.HyperwalletBankCard;
import com.hyperwallet.clientsdk.model.HyperwalletList;
import com.hyperwallet.clientsdk.model.HyperwalletPaperCheck;
import com.hyperwallet.clientsdk.model.HyperwalletStatusTransition;
import com.hyperwallet.clientsdk.model.HyperwalletTransferMethod;
import com.hyperwallet.clientsdk.model.HyperwalletUser;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
            .transferMethodCountry("US")
            .transferMethodCurrency("USD");

        HyperwalletBankCard returnValue;
        try {
            returnValue = client.createBankCard(bankCard);
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
        assertThat(returnValue.getCardType(), is(equalTo(HyperwalletBankCard.CardType.DEBIT)));
        assertThat(returnValue.getCardNumber(), is(equalTo("************0114")));
        assertThat(returnValue.getCardBrand(), is(equalTo(HyperwalletBankCard.Brand.VISA)));
        assertThat(returnValue.getDateOfExpiry(), is(equalTo(dateFormat.parse("2018-01-01T00:00:00 UTC"))));
    }

    @Test
    public void testUpdateBankCard() throws Exception {
        String functionality = "updateBankCard";
        initMockServer(functionality);

        HyperwalletBankCard bankCard = new HyperwalletBankCard()
            .userToken("usr-c4292f1a-866f-4310-a289-b916853939de")
            .token("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")
            .dateOfExpiry(dateFormat.parse("2018-11-01T00:00:00 UTC"));

        HyperwalletBankCard returnValue;
        try {
            returnValue = client.updateBankCard(bankCard);
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
        assertThat(returnValue.getCardType(), is(equalTo(HyperwalletBankCard.CardType.DEBIT)));
        assertThat(returnValue.getCardNumber(), is(equalTo("************0114")));
        assertThat(returnValue.getCardBrand(), is(equalTo(HyperwalletBankCard.Brand.VISA)));
        assertThat(returnValue.getDateOfExpiry(), is(equalTo(dateFormat.parse("2018-11-01T00:00:00 UTC"))));
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

        assertThat(returnValue.getToken(), is(equalTo("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletTransferMethod.Type.BANK_CARD)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-09T22:50:14 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getCardType(), is(equalTo(HyperwalletBankCard.CardType.DEBIT)));
        assertThat(returnValue.getCardNumber(), is(equalTo("************0114")));
        assertThat(returnValue.getCardBrand(), is(equalTo(HyperwalletBankCard.Brand.VISA)));
        assertThat(returnValue.getDateOfExpiry(), is(equalTo(dateFormat.parse("2018-12-01T00:00:00 UTC"))));
    }

    @Test
    public void testListBankCard() throws Exception {
        String functionality = "listBankCards";
        initMockServer(functionality);

        HyperwalletList<HyperwalletBankCard> returnValue;
        try {
            returnValue = client.listBankCards("usr-c4292f1a-866f-4310-a289-b916853939de");
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getCount(), is(equalTo(2)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("trm-7e915660-8c97-47bf-8a4f-0c1bc890d46f")));
        assertThat(returnValue.getData().get(0).getType(), is(equalTo(HyperwalletTransferMethod.Type.BANK_CARD)));
        assertThat(returnValue.getData().get(0).getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-09T22:50:14 UTC"))));
        assertThat(returnValue.getData().get(0).getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getData().get(0).getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getData().get(0).getCardType(), is(equalTo(HyperwalletBankCard.CardType.DEBIT)));
        assertThat(returnValue.getData().get(0).getCardNumber(), is(equalTo("************0114")));
        assertThat(returnValue.getData().get(0).getCardBrand(), is(equalTo(HyperwalletBankCard.Brand.VISA)));
        assertThat(returnValue.getData().get(0).getDateOfExpiry(), is(equalTo(dateFormat.parse("2018-11-01T00:00:00 UTC"))));
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
        assertThat(returnValue.getFromStatus(), is(equalTo(HyperwalletStatusTransition.Status.ACTIVATED)));
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

        assertThat(returnValue.getCount(), is(equalTo(1)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("sts-1825afa2-61f1-4860-aa69-a65b9d14f556")));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T00:55:57 UTC"))));
        assertThat(returnValue.getData().get(0).getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getFromStatus(), is(equalTo(HyperwalletStatusTransition.Status.ACTIVATED)));
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
        assertThat(returnValue.getFromStatus(), is(equalTo(HyperwalletStatusTransition.Status.ACTIVATED)));
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
        assertThat(returnValue.getFromStatus(), is(equalTo(HyperwalletStatusTransition.Status.ACTIVATED)));
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

        assertThat(returnValue.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletTransferMethod.Type.PAPER_CHECK)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getBankAccountRelationship(), is(equalTo(HyperwalletTransferMethod.BankAccountRelationship.SELF)));
        assertThat(returnValue.getProfileType(), is(equalTo(HyperwalletUser.ProfileType.INDIVIDUAL)));
        assertThat(returnValue.getFirstName(), is(equalTo("Some")));
        assertThat(returnValue.getLastName(), is(equalTo("Guy")));
        assertThat(returnValue.getDateOfBirth(), is(equalTo(dateFormat.parse("1991-01-01T00:00:00 UTC"))));
        assertThat(returnValue.getAddressLine1(), is(equalTo("575 Market Street")));
        assertThat(returnValue.getCity(), is(equalTo("San Francisco")));
        assertThat(returnValue.getStateProvince(), is(equalTo("CA")));
        assertThat(returnValue.getCountry(), is(equalTo("US")));
        assertThat(returnValue.getPostalCode(), is(equalTo("94105")));
        assertThat(returnValue.getShippingMethod(), is(equalTo(HyperwalletPaperCheck.ShippingMethod.STANDARD)));
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

        assertThat(returnValue.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletTransferMethod.Type.PAPER_CHECK)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:42:46 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getBankAccountRelationship(), is(equalTo(HyperwalletTransferMethod.BankAccountRelationship.SELF)));
        assertThat(returnValue.getProfileType(), is(equalTo(HyperwalletUser.ProfileType.INDIVIDUAL)));
        assertThat(returnValue.getFirstName(), is(equalTo("Some")));
        assertThat(returnValue.getLastName(), is(equalTo("Guy")));
        assertThat(returnValue.getDateOfBirth(), is(equalTo(dateFormat.parse("1991-01-01T00:00:00 UTC"))));
        assertThat(returnValue.getAddressLine1(), is(equalTo("123 Other Street")));
        assertThat(returnValue.getCity(), is(equalTo("Far Away Land")));
        assertThat(returnValue.getStateProvince(), is(equalTo("CA")));
        assertThat(returnValue.getCountry(), is(equalTo("US")));
        assertThat(returnValue.getPostalCode(), is(equalTo("94105")));
        assertThat(returnValue.getShippingMethod(), is(equalTo(HyperwalletPaperCheck.ShippingMethod.STANDARD)));
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

        assertThat(returnValue.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletTransferMethod.Type.PAPER_CHECK)));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getBankAccountRelationship(), is(equalTo(HyperwalletTransferMethod.BankAccountRelationship.SELF)));
        assertThat(returnValue.getProfileType(), is(equalTo(HyperwalletUser.ProfileType.INDIVIDUAL)));
        assertThat(returnValue.getFirstName(), is(equalTo("Some")));
        assertThat(returnValue.getLastName(), is(equalTo("Guy")));
        assertThat(returnValue.getDateOfBirth(), is(equalTo(dateFormat.parse("1991-01-01T00:00:00 UTC"))));
        assertThat(returnValue.getAddressLine1(), is(equalTo("575 Market Street")));
        assertThat(returnValue.getCity(), is(equalTo("San Francisco")));
        assertThat(returnValue.getStateProvince(), is(equalTo("CA")));
        assertThat(returnValue.getCountry(), is(equalTo("US")));
        assertThat(returnValue.getPostalCode(), is(equalTo("94105")));
        assertThat(returnValue.getShippingMethod(), is(equalTo(HyperwalletPaperCheck.ShippingMethod.STANDARD)));
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

        assertThat(returnValue.getCount(), is(equalTo(1)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getData().get(0).getType(), is(equalTo(HyperwalletTransferMethod.Type.PAPER_CHECK)));
        assertThat(returnValue.getData().get(0).getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getData().get(0).getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getData().get(0).getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getData().get(0).getBankAccountRelationship(), is(equalTo(HyperwalletTransferMethod.BankAccountRelationship.SELF)));
        assertThat(returnValue.getData().get(0).getProfileType(), is(equalTo(HyperwalletUser.ProfileType.INDIVIDUAL)));
        assertThat(returnValue.getData().get(0).getFirstName(), is(equalTo("Some")));
        assertThat(returnValue.getData().get(0).getLastName(), is(equalTo("Guy")));
        assertThat(returnValue.getData().get(0).getDateOfBirth(), is(equalTo(dateFormat.parse("1991-01-01T00:00:00 UTC"))));
        assertThat(returnValue.getData().get(0).getAddressLine1(), is(equalTo("575 Market Street")));
        assertThat(returnValue.getData().get(0).getCity(), is(equalTo("San Francisco")));
        assertThat(returnValue.getData().get(0).getStateProvince(), is(equalTo("CA")));
        assertThat(returnValue.getData().get(0).getCountry(), is(equalTo("US")));
        assertThat(returnValue.getData().get(0).getPostalCode(), is(equalTo("94105")));
        assertThat(returnValue.getData().get(0).getShippingMethod(), is(equalTo(HyperwalletPaperCheck.ShippingMethod.STANDARD)));
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
        assertThat(returnValue.getFromStatus(), is(equalTo(HyperwalletStatusTransition.Status.ACTIVATED)));
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

        assertThat(returnValue.getCount(), is(equalTo(1)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("sts-ed2207f0-39cc-493f-9cd0-24998de0c0f7")));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-30T19:50:49 UTC"))));
        assertThat(returnValue.getData().get(0).getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getFromStatus(), is(equalTo(HyperwalletStatusTransition.Status.ACTIVATED)));
        assertThat(returnValue.getData().get(0).getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getNotes(), is(equalTo("Closing check.")));
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

        assertThat(returnValue.getToken(), is(equalTo("sts-ed2207f0-39cc-493f-9cd0-24998de0c0f7")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-30T19:50:49 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(HyperwalletStatusTransition.Status.ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Closing check.")));
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

        assertThat(returnValue.getToken(), is(equalTo("sts-ed2207f0-39cc-493f-9cd0-24998de0c0f7")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-30T19:50:49 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(HyperwalletStatusTransition.Status.ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Closing check.")));
    }

    private void initMockServer(String functionality) throws IOException {
        mockServer.reset();
        mockServer
            .when(parseRequest(functionality))
            .respond(parseResponse(functionality));
    }

    private HttpResponse parseResponse(String functionality) throws IOException {
        return HttpResponse.response()
            .withStatusCode(HttpStatusCode.OK_200.code())
            .withBody(org.apache.commons.io.IOUtils.toString(getClass().getResourceAsStream("/integration/" + functionality + "-response.json")));
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

}
