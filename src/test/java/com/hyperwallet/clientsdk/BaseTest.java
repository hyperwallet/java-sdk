package com.hyperwallet.clientsdk;

import org.junit.Before;

/**
 * Created by rbao on 5/20/16.
 */
public class BaseTest {

    protected Hyperwallet hyperwallet;
    protected String programToken = "prg-a150eb06-bf46-43b8-966f-8fb5ecb88ed0";
    protected String accountToken = "act-a150eb06-bf46-43b8-112f-8fb5ecb88ed0";

    @Before
    public void before() {
        hyperwallet = HyperwalletTest.getHyperwallet();
    }

    public static Hyperwallet getHyperwallet() {
        final String environmentKey = getEnvironmentKey();
        final String apiSecret = getApiSecret();
        if (environmentKey == null || apiSecret == null) {
            return null;
        }
        return new Hyperwallet(environmentKey, apiSecret);
    }

    private static String getEnvironmentKey() {
        return System.getenv("HYPERWALLET_USERNAME");
    }

    private static String getApiSecret() {
        return System.getenv("HYPERWALLET_PASSWORD");
    }
}
