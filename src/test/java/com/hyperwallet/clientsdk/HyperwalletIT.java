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
import com.hyperwallet.clientsdk.model.HyperwalletStatusTransition;
import com.hyperwallet.clientsdk.model.HyperwalletTransferMethod;
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
