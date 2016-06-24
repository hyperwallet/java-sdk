package com.hyperwallet.clientsdk.model;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * @author fkrauthan
 */
public class HyperwalletErrorListTest {

    @Test
    public void testHyperwalletErrorList() {
        HyperwalletErrorList errorList = new HyperwalletErrorList();
        assertThat(errorList.getErrors(), is(Matchers.<HyperwalletError>empty()));

        List<HyperwalletError> myErrorList = new ArrayList<HyperwalletError>();
        errorList.setErrors(myErrorList);

        assertThat(errorList.getErrors(), is(equalTo(myErrorList)));
    }
}
