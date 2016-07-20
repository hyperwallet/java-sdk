package com.hyperwallet.clientsdk.model;

import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author fkrauthan
 */
public class HyperwalletReceiptTest {

    @Test
    public void testHyperwalletReceipt() {
        HyperwalletReceipt receipt = new HyperwalletReceipt();
        assertThat(receipt.getJournalId(), is(nullValue()));
        assertThat(receipt.getType(), is(nullValue()));
        assertThat(receipt.getCreatedOn(), is(nullValue()));
        assertThat(receipt.getEntry(), is(nullValue()));
        assertThat(receipt.getSourceToken(), is(nullValue()));
        assertThat(receipt.getDestinationToken(), is(nullValue()));
        assertThat(receipt.getAmount(), is(nullValue()));
        assertThat(receipt.getFee(), is(nullValue()));
        assertThat(receipt.getCurrency(), is(nullValue()));
        assertThat(receipt.getForeignExchangeRate(), is(nullValue()));
        assertThat(receipt.getForeignExchangeCurrency(), is(nullValue()));
        assertThat(receipt.getDetails(), is(notNullValue()));
        assertThat(receipt.getDetails().isEmpty(), is(equalTo(true)));

        Map<String, String> detailsMap = new HashMap<String, String>();
        detailsMap.put(HyperwalletReceipt.DetailFieldKey.BANK_NAME.key(), "test");

        Date creationDate = new Date();

        receipt.setJournalId("test-journal-id");
        receipt.setType(HyperwalletReceipt.Type.ANNUAL_FEE);
        receipt.setCreatedOn(creationDate);
        receipt.setEntry(HyperwalletReceipt.Entry.DEBIT);
        receipt.setSourceToken("test-source-token");
        receipt.setDestinationToken("test-destination-token");
        receipt.setAmount(10.99);
        receipt.setFee(1.99);
        receipt.setCurrency("CAD");
        receipt.setForeignExchangeRate(0.99);
        receipt.setForeignExchangeCurrency("USD");
        receipt.setDetails(detailsMap);

        assertThat(receipt.getJournalId(), is(equalTo("test-journal-id")));
        assertThat(receipt.getType(), is(equalTo(HyperwalletReceipt.Type.ANNUAL_FEE)));
        assertThat(receipt.getCreatedOn(), is(equalTo(creationDate)));
        assertThat(receipt.getEntry(), is(equalTo(HyperwalletReceipt.Entry.DEBIT)));
        assertThat(receipt.getSourceToken(), is(equalTo("test-source-token")));
        assertThat(receipt.getDestinationToken(), is(equalTo("test-destination-token")));
        assertThat(receipt.getAmount(), is(equalTo(10.99)));
        assertThat(receipt.getFee(), is(equalTo(1.99)));
        assertThat(receipt.getCurrency(), is(equalTo("CAD")));
        assertThat(receipt.getForeignExchangeRate(), is(equalTo(0.99)));
        assertThat(receipt.getForeignExchangeCurrency(), is(equalTo("USD")));
        assertThat(receipt.getDetails(), is(equalTo(detailsMap)));
    }

}
