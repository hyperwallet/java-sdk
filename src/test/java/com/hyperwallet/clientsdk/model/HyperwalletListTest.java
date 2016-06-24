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
public class HyperwalletListTest {

    @Test
    public void testHyperwalletList() {
        HyperwalletList<String> list = new HyperwalletList<String>();
        assertThat(list.getCount(), is(equalTo(0)));
        assertThat(list.getLimit(), is(equalTo(0)));
        assertThat(list.getOffset(), is(equalTo(0)));
        assertThat(list.getData(), is(Matchers.<String>empty()));

        list.setCount(1);
        list.setLimit(2);
        list.setOffset(3);
        List<String> strList = new ArrayList<String>();
        strList.add("test");
        list.setData(strList);

        assertThat(list.getCount(), is(equalTo(1)));
        assertThat(list.getLimit(), is(equalTo(2)));
        assertThat(list.getOffset(), is(equalTo(3)));
        assertThat(list.getData(), is(equalTo(strList)));
    }

}
