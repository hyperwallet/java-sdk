package com.hyperwallet.clientsdk.model;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author fkrauthan
 */
public class HyperwalletListTest {

    @Test
    public void testHyperwalletList() {
        HyperwalletList<String> list = new HyperwalletList<String>();
        assertThat(list.hasNextPage(), is(equalTo(false)));
        assertThat(list.hasPreviousPage(), is(equalTo(false)));
        assertThat(list.getLimit(), is(equalTo(0)));
        assertThat(list.getData(), is(nullValue()));
        assertThat(list.getLinks(), is(not(nullValue())));

        assertThat(list.hasNextPage(), is(equalTo(false)));
        assertThat(list.hasPreviousPage(), is(equalTo(false)));
        list.setLimit(2);
        List<String> dataList = new ArrayList<String>();
        dataList.add("test");
        list.setData(dataList);

        assertThat(list.hasNextPage(), is(equalTo(false)));
        assertThat(list.hasPreviousPage(), is(equalTo(false)));
        assertThat(list.getLimit(), is(equalTo(2)));
        assertThat(list.getData(), is(equalTo(dataList)));
        assertThat(list.getData(), Matchers.<String>hasSize(1));

        assertThat(list.hasNextPage(), is(equalTo(false)));
        assertThat(list.hasPreviousPage(), is(equalTo(false)));
        assertThat(list.getLimit(), is(equalTo(2)));
        assertThat(list.getData(), is(equalTo(dataList)));

        list.setLimit(10);
        for(int i=1; i < list.getLimit(); i++) {

            dataList.add("test" + i);
        }
        list.setData(dataList);
        list.setHasNextPage(true);
        HyperwalletLink link = new HyperwalletLink();
        link.setHref("https://localhost:8181/users?limit=10");
        Map<String, String> params = new HashMap<String, String>();
        params.put("rel", "self");
        link.setParams(params);
        list.getLinks().add(link);
        List<HyperwalletLink> hateoasLinks = new ArrayList<HyperwalletLink>();
        hateoasLinks.add(link);

        assertThat(list.hasNextPage(), is(equalTo(true)));
        assertThat(list.hasPreviousPage(), is(equalTo(false)));
        assertThat(list.getLimit(), is(equalTo(10)));
        assertThat(list.getData(), is(equalTo(dataList)));
        assertThat(list.getData(), Matchers.<String>hasSize(10));
        assertThat(list.getLinks().get(0).getHref(), is(equalTo(link.getHref())));
        assertThat(list.getLinks().get(0).getParams().get("rel"), is(equalTo("self")));

        for(int i=0; i < list.getLimit(); i++) {
            dataList.add("test" + i);
        }
        list.setData(dataList);
        list.setHasNextPage(false);
        list.setHasPreviousPage(true);
        link = new HyperwalletLink();
        link.setHref("https://localhost:8181/users?after=trf-w234-2342-234&limit=10");
        params = new HashMap<String, String>();
        params.put("rel", "next");
        link.setParams(params);

        hateoasLinks.add(link);
        list.setLinks(hateoasLinks);
        assertThat(list.hasNextPage(), is(equalTo(false)));
        assertThat(list.hasPreviousPage(), is(equalTo(true)));
        assertThat(list.getLimit(), is(equalTo(10)));
        assertThat(list.getData(), is(equalTo(dataList)));
        assertThat(list.getData(), Matchers.<String>hasSize(20));
    }
}
