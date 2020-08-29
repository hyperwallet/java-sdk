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
        assertThat(list.isHasNextPage(), is(equalTo(false)));
        assertThat(list.isHasPreviousPage(), is(equalTo(false)));
        assertThat(list.getLimit(), is(equalTo(0)));
        assertThat(list.getData(), is(Matchers.<String>empty()));

        assertThat(list.isHasNextPage(), is(equalTo(false)));
        assertThat(list.isHasPreviousPage(), is(equalTo(false)));
        list.setLimit(2);
        List<String> strList = new ArrayList<String>();
        strList.add("test");
        list.setData(strList);

        assertThat(list.isHasNextPage(), is(equalTo(false)));
        assertThat(list.isHasPreviousPage(), is(equalTo(false)));
        assertThat(list.getLimit(), is(equalTo(2)));
        assertThat(list.getData(), is(equalTo(strList)));

        assertThat(list.isHasNextPage(), is(equalTo(false)));
        assertThat(list.isHasPreviousPage(), is(equalTo(false)));
        assertThat(list.getLimit(), is(equalTo(2)));
        assertThat(list.getData(), is(equalTo(strList)));

        for(int i=0; i < 200; i++){
            strList.add("test" + i);
        }
        list.setData(strList);
        System.out.println("Size of list = " + strList.size() );
//        assertThat(list.isHasNextPage(), is(equalTo(true)));
//        assertThat(list.isHasPreviousPage(), is(equalTo(false)));
//        assertThat(list.getLimit(), is(equalTo(100)));
//        assertThat(list.getData(), is(equalTo(strList)));


    }

}
