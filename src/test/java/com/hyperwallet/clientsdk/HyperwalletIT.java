package com.hyperwallet.clientsdk;

import static com.hyperwallet.clientsdk.model.HyperwalletStatusTransition.Status.ACTIVATED;
import static com.hyperwallet.clientsdk.model.HyperwalletStatusTransition.Status.CANCELLED;
import static com.hyperwallet.clientsdk.model.HyperwalletStatusTransition.Status.COMPLETED;
import static com.hyperwallet.clientsdk.model.HyperwalletStatusTransition.Status.CREATED;
import static com.hyperwallet.clientsdk.model.HyperwalletStatusTransition.Status.DE_ACTIVATED;
import static com.hyperwallet.clientsdk.model.HyperwalletStatusTransition.Status.QUOTED;
import static com.hyperwallet.clientsdk.model.HyperwalletStatusTransition.Status.RECALLED;
import static com.hyperwallet.clientsdk.model.HyperwalletStatusTransition.Status.SCHEDULED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.JsonBody.json;
import static org.testng.Assert.fail;

import com.hyperwallet.clientsdk.model.*;
import com.hyperwallet.clientsdk.model.HyperwalletTransfer.ForeignExchange;
import com.hyperwallet.clientsdk.model.HyperwalletTransferRefund.Status;
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
    // User
    //
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

        assertThat(returnValue.getCount(), is(equalTo(1)));
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

    //
    // Prepaid Card
    //

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
        assertThat(returnValue.getCvv(), is(nullValue()));
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
        assertThat(returnValue.getCvv(), is(nullValue()));
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
        assertThat(returnValue.getCvv(), is(nullValue()));
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
        assertThat(returnValue.getData().get(0).getCvv(), is(nullValue()));
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

        assertThat(returnValue.getCount(), is(equalTo(1)));
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

        assertThat(returnValue.getCount(), is(equalTo(1)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("sts-ed2207f0-39cc-493f-9cd0-24998de0c0f7")));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-30T19:50:49 UTC"))));
        assertThat(returnValue.getData().get(0).getTransition(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getData().get(0).getFromStatus(), is(equalTo(ACTIVATED)));
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
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
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
        assertThat(returnValue.getFromStatus(), is(equalTo(ACTIVATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(DE_ACTIVATED)));
        assertThat(returnValue.getNotes(), is(equalTo("Closing check.")));
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

        assertThat(returnValue.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransfer.Status.QUOTED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getClientTransferId(), is(equalTo("clientTransferId")));
        assertThat(returnValue.getSourceToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getSourceAmount(), is(equalTo(200.4)));
        assertThat(returnValue.getSourceFeeAmount(), is(equalTo(20.3)));
        assertThat(returnValue.getSourceCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getDestinationToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getDestinationAmount(), is(equalTo(100.2)));
        assertThat(returnValue.getDestinationFeeAmount(), is(equalTo(30.5)));
        assertThat(returnValue.getDestinationCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getNotes(), is(equalTo("notes")));
        assertThat(returnValue.getMemo(), is(equalTo("memo")));
        assertThat(returnValue.getExpiresOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getForeignExchanges().get(0).getSourceAmount(), is(equalTo(100.00)));
        assertThat(returnValue.getForeignExchanges().get(0).getSourceCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getForeignExchanges().get(0).getDestinationAmount(), is(equalTo(63.49)));
        assertThat(returnValue.getForeignExchanges().get(0).getDestinationCurrency(), is(equalTo("CAD")));
        assertThat(returnValue.getForeignExchanges().get(0).getRate(), is(equalTo(0.79)));
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

        assertThat(returnValue.getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransfer.Status.QUOTED)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getClientTransferId(), is(equalTo("clientTransferId")));
        assertThat(returnValue.getSourceToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getSourceAmount(), is(equalTo(200.4)));
        assertThat(returnValue.getSourceFeeAmount(), is(equalTo(20.3)));
        assertThat(returnValue.getSourceCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getDestinationToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getDestinationAmount(), is(equalTo(100.2)));
        assertThat(returnValue.getDestinationFeeAmount(), is(equalTo(30.5)));
        assertThat(returnValue.getDestinationCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getNotes(), is(equalTo("notes")));
        assertThat(returnValue.getMemo(), is(equalTo("memo")));
        assertThat(returnValue.getExpiresOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getForeignExchanges().get(0).getSourceAmount(), is(equalTo(100.00)));
        assertThat(returnValue.getForeignExchanges().get(0).getSourceCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getForeignExchanges().get(0).getDestinationAmount(), is(equalTo(63.49)));
        assertThat(returnValue.getForeignExchanges().get(0).getDestinationCurrency(), is(equalTo("CAD")));
        assertThat(returnValue.getForeignExchanges().get(0).getRate(), is(equalTo(0.79)));
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

        assertThat(returnValue.getCount(), is(equalTo(1)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("trm-59f67c62-fd06-497e-a9ea-99d6eb38b12b")));
        assertThat(returnValue.getData().get(0).getStatus(), is(equalTo(HyperwalletTransfer.Status.QUOTED)));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getData().get(0).getClientTransferId(), is(equalTo("clientTransferId")));
        assertThat(returnValue.getData().get(0).getSourceToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getData().get(0).getSourceAmount(), is(equalTo(200.4)));
        assertThat(returnValue.getData().get(0).getSourceFeeAmount(), is(equalTo(20.3)));
        assertThat(returnValue.getData().get(0).getSourceCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getData().get(0).getDestinationToken(), is(equalTo("usr-c4292f1a-866f-4310-a289-b916853939de")));
        assertThat(returnValue.getData().get(0).getDestinationAmount(), is(equalTo(100.2)));
        assertThat(returnValue.getData().get(0).getDestinationFeeAmount(), is(equalTo(30.5)));
        assertThat(returnValue.getData().get(0).getDestinationCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getData().get(0).getNotes(), is(equalTo("notes")));
        assertThat(returnValue.getData().get(0).getMemo(), is(equalTo("memo")));
        assertThat(returnValue.getData().get(0).getExpiresOn(), is(equalTo(dateFormat.parse("2017-10-31T22:32:57 UTC"))));
        assertThat(returnValue.getData().get(0).getForeignExchanges().get(0).getSourceAmount(), is(equalTo(100.00)));
        assertThat(returnValue.getData().get(0).getForeignExchanges().get(0).getSourceCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getData().get(0).getForeignExchanges().get(0).getDestinationAmount(), is(equalTo(63.49)));
        assertThat(returnValue.getData().get(0).getForeignExchanges().get(0).getDestinationCurrency(), is(equalTo("CAD")));
        assertThat(returnValue.getData().get(0).getForeignExchanges().get(0).getRate(), is(equalTo(0.79)));
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

        assertThat(returnValue.getToken(), is(equalTo("sts-ed2207f0-39cc-493f-9cd0-24998de0c0f7")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-10-30T19:50:49 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(SCHEDULED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(QUOTED)));
        assertThat(returnValue.getToStatus(), is(equalTo(SCHEDULED)));
        assertThat(returnValue.getNotes(), is(equalTo("Closing check.")));
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

        HyperwalletTransferRefund expectedValue = new HyperwalletTransferRefund()
                .token("trd-a159dc18-eb29-4530-8733-060c7feaad0f")
                .status(Status.COMPLETED)
                .clientRefundId("clientRefundId")
                .sourceToken("act-ba4e8fdd-614b-11e5-af23-0faa28ca7c0f")
                .sourceAmount(20.0)
                .sourceCurrency("USD")
                .destinationToken("usr-3deb34a0-ffd1-487d-8860-6d69435cea6c")
                .destinationAmount(20.0)
                .destinationCurrency("USD")
                .notes("Merchant Payment return to Wallet Balance")
                .memo("TransferReturn123456")
                .createdOn(dateFormat.parse("2019-11-11T19:04:43 UTC"));

        checkTransferRefund(returnValue, expectedValue);
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

        HyperwalletTransferRefund expectedValue = new HyperwalletTransferRefund()
                .token("trd-19156720-01e8-4f1c-8ef3-7ced80672128")
                .status(Status.COMPLETED)
                .clientRefundId("1573548663")
                .sourceToken("act-ba4e8fdd-614b-11e5-af23-0faa28ca7c0f")
                .sourceAmount(50.0)
                .sourceCurrency("USD")
                .destinationToken("usr-3deb34a0-ffd1-487d-8860-6d69435cea6c")
                .destinationAmount(50.0)
                .destinationCurrency("USD")
                .notes("Merchant Payment return to Wallet Balance")
                .memo("TransferReturn123456")
                .createdOn(dateFormat.parse("2019-11-12T11:51:05 UTC"));

        checkTransferRefund(returnValue, expectedValue);
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

        assertThat(returnValue.getCount(), is(equalTo(2)));

        HyperwalletTransferRefund expectedValue = new HyperwalletTransferRefund()
                .token("trd-e59d19d4-eccb-4160-b04c-4f11c83f99f0")
                .status(Status.COMPLETED)
                .clientRefundId("1573566270")
                .sourceToken("act-ba4e8fdd-614b-11e5-af23-0faa28ca7c0f")
                .sourceAmount(50.0)
                .sourceCurrency("USD")
                .destinationToken("usr-3deb34a0-ffd1-487d-8860-6d69435cea6c")
                .destinationAmount(50.0)
                .destinationCurrency("USD")
                .notes("Merchant Payment return to Wallet Balance")
                .memo("TransferReturn123456")
                .createdOn(dateFormat.parse("2019-11-12T16:44:30 UTC"));

        checkTransferRefund(returnValue.getData().get(0), expectedValue);
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
                .type(HyperwalletTransferMethod.Type.PAYPAL_ACCOUNT)
                .email("user@domain.com");

        HyperwalletPayPalAccount returnValue;
        try {
            returnValue = client.createPayPalAccount(payPalAccount);
        } catch (Exception e) {
            mockServer.verify(parseRequest(functionality));
            throw e;
        }

        assertThat(returnValue.getToken(), is(equalTo("trm-54b0db9c-5565-47f7-aee6-685e713595f3")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletTransferMethod.Type.PAYPAL_ACCOUNT)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2018-05-01T00:00:00 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getEmail(), is(equalTo("user@domain.com")));
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

        assertThat(returnValue.getToken(), is(equalTo("trm-54b0db9c-5565-47f7-aee6-685e713595f3")));
        assertThat(returnValue.getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getType(), is(equalTo(HyperwalletTransferMethod.Type.PAYPAL_ACCOUNT)));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2018-05-01T00:00:00 UTC"))));
        assertThat(returnValue.getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getEmail(), is(equalTo("user@domain.com")));
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

        assertThat(returnValue.getCount(), is(equalTo(1)));
        assertThat(returnValue.getData().get(0).getToken(), is(equalTo("trm-54b0db9c-5565-47f7-aee6-685e713595f3")));
        assertThat(returnValue.getData().get(0).getStatus(), is(equalTo(HyperwalletTransferMethod.Status.ACTIVATED)));
        assertThat(returnValue.getData().get(0).getType(), is(equalTo(HyperwalletTransferMethod.Type.PAYPAL_ACCOUNT)));
        assertThat(returnValue.getData().get(0).getCreatedOn(), is(equalTo(dateFormat.parse("2018-05-01T00:00:00 UTC"))));
        assertThat(returnValue.getData().get(0).getTransferMethodCountry(), is(equalTo("US")));
        assertThat(returnValue.getData().get(0).getTransferMethodCurrency(), is(equalTo("USD")));
        assertThat(returnValue.getData().get(0).getEmail(), is(equalTo("user@domain.com")));
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

        assertThat(returnValue.getCount(), is(equalTo(1)));
        assertThat(returnValue.getOffset(), is(equalTo(0)));
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

        assertThat(returnValue.getCount(), is(equalTo(2)));
        assertThat(returnValue.getData().get(1).getToken(), is(equalTo("sts-1f7f58a9-22e8-4fef-8d6e-a17e2c71db33")));
        assertThat(returnValue.getData().get(1).getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T02:04:17 UTC"))));
        assertThat(returnValue.getData().get(1).getTransition(), is(equalTo(RECALLED)));
        assertThat(returnValue.getData().get(1).getFromStatus(), is(equalTo(COMPLETED)));
        assertThat(returnValue.getData().get(1).getToStatus(), is(equalTo(RECALLED)));
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

        assertThat(returnValue.getToken(), is(equalTo("sts-46592b6c-7d8b-4e00-abf6-ca5a03880d31")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-12-21T11:35:43 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(CANCELLED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(SCHEDULED)));
        assertThat(returnValue.getToStatus(), is(equalTo(CANCELLED)));
        assertThat(returnValue.getNotes(), is(equalTo("Cancel a payment upon customer request.")));
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

        assertThat(returnValue.getToken(), is(equalTo("sts-1f7f58a9-22e8-4fef-8d6e-a17e2c71db33")));
        assertThat(returnValue.getCreatedOn(), is(equalTo(dateFormat.parse("2017-11-16T02:04:17 UTC"))));
        assertThat(returnValue.getTransition(), is(equalTo(COMPLETED)));
        assertThat(returnValue.getFromStatus(), is(equalTo(CREATED)));
        assertThat(returnValue.getToStatus(), is(equalTo(COMPLETED)));
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

}
