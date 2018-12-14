package com.hyperwallet.clientsdk.model;

import org.testng.annotations.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author fkrauthan
 */
public class HyperwalletErrorTest {

    @Test
    public void testConstructorWithoutArguments() {
        HyperwalletError error = new HyperwalletError();
        assertThat(error.getCode(), is(nullValue()));
        assertThat(error.getFieldName(), is(nullValue()));
        assertThat(error.getMessage(), is(nullValue()));
        assertThat(error.getRelatedResources(), is(nullValue()));
    }

    @Test
    public void testConstructorWithTwoArguments() {
        HyperwalletError error = new HyperwalletError("test1", "test2");
        assertThat(error.getCode(), is(equalTo("test1")));
        assertThat(error.getFieldName(), is(nullValue()));
        assertThat(error.getMessage(), is(equalTo("test2")));
        assertThat(error.getRelatedResources(), is(nullValue()));
    }

    @Test
    public void testConstructorWithThreeArguments() {
        HyperwalletError error = new HyperwalletError("test1", "test2", "test3");
        assertThat(error.getCode(), is(equalTo("test1")));
        assertThat(error.getFieldName(), is(equalTo("test2")));
        assertThat(error.getMessage(), is(equalTo("test3")));
        assertThat(error.getRelatedResources(), is(nullValue()));
    }

    @Test
    public void testConstructorWithFourArguments() {
        HyperwalletError error = new HyperwalletError("test1", "test2", "test3",
                Arrays.asList("test4", "test5", "test6"));
        assertThat(error.getCode(), is(equalTo("test1")));
        assertThat(error.getFieldName(), is(equalTo("test2")));
        assertThat(error.getMessage(), is(equalTo("test3")));
        assertThat(error.getRelatedResources(), hasSize(3));
        assertThat(error.getRelatedResources(), hasItems("test4", "test5", "test6"));
    }

}
