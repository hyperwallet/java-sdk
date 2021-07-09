package com.hyperwallet.clientsdk.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author fkrauthan
 */
public class HyperwalletPaymentListOptionsTest extends BaseModelTest<HyperwalletPaymentListOptions> {
    protected HyperwalletPaymentListOptions createBaseModel()  {
        HyperwalletPaymentListOptions options = new HyperwalletPaymentListOptions();
        options
                .clientPaymentId("test-clientPaymentId");

        return options;
    }

    protected Class<HyperwalletPaymentListOptions> createModelClass() {
        return HyperwalletPaymentListOptions.class;
    }
}
