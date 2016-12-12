package com.hyperwallet.clientsdk.util;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.hyperwallet.clientsdk.HyperwalletException;
import com.hyperwallet.clientsdk.model.HyperwalletBaseMonitor;
import org.testng.annotations.Test;

import javax.xml.bind.Element;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author fkrauthan
 */
public class HyperwalletJsonUtilTest {

    @JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestBody {
        public String test;
        public Double amount;
    }

    @JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestBodyWithMonitor extends HyperwalletBaseMonitor {
        public String test;
        public String test2;
        public String test3;
        public String test4;

        public void setTest(String test) {
            addField("test", test);
            this.test = test;
        }

        public void clearTest3() {
            clearField("test3");
            this.test3 = null;
        }
    }

    @JsonFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER)
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TestWithError extends HyperwalletBaseMonitor {
        public Element element;

        public TestWithError() {
            element = new Element() {
            };
            addField("element", element);
        }
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void testInstanceCreation() {
        new HyperwalletJsonUtil();
    }

    @Test
    public void testFromJson_byClassReference_nullContent() {
        TestBody body = HyperwalletJsonUtil.fromJson(null, TestBody.class);
        assertThat(body, is(nullValue()));
    }

    @Test(expectedExceptions = HyperwalletException.class)
    public void testFromJson_byClassReference_Invalid_JSON_Content() {
        HyperwalletJsonUtil.fromJson("{\"amount\": \"1,023.37\" }", TestBody.class);
    }

    @Test
    public void testFromJson_byClassReference_Valid_JSON_Content() {
        TestBody body = HyperwalletJsonUtil.fromJson("{\"amount\": \"1023.37\" }", TestBody.class);
        assertThat(body.amount, is(1023.37));
    }

    @Test
    public void testFromJson_byClassReference_emptyContent() {
        TestBody body = HyperwalletJsonUtil.fromJson("", TestBody.class);
        assertThat(body, is(nullValue()));
    }

    @Test
    public void testFromJson_byClassReference_successful() {
        TestBody body = HyperwalletJsonUtil.fromJson("{\"test\": \"value\"}", TestBody.class);
        assertThat(body, is(notNullValue()));
        assertThat(body.test, is(equalTo("value")));
    }

    @Test
    public void testFromJson_byTypeReference_nullContent() {
        TestBody body = HyperwalletJsonUtil.fromJson(null, new TypeReference<TestBody>() {});
        assertThat(body, is(nullValue()));
    }

    @Test
    public void testFromJson_byTypeReference_emptyContent() {
        TestBody body = HyperwalletJsonUtil.fromJson("", new TypeReference<TestBody>() {});
        assertThat(body, is(nullValue()));
    }

    @Test(expectedExceptions = HyperwalletException.class)
    public void testFromJson_byTypeReference_Invalid_JSON_Content() {
        HyperwalletJsonUtil.fromJson("{\"amount\" : }", new TypeReference<TestBody>() {});
    }

    @Test
    public void testFromJson_byTypeReference_successful() {
        TestBody body = HyperwalletJsonUtil.fromJson("{\"test\": \"value\"}", new TypeReference<TestBody>() {});
        assertThat(body, is(notNullValue()));
        assertThat(body.test, is(equalTo("value")));
    }

    @Test
    public void testToJson_nullObject() {
        String json = HyperwalletJsonUtil.toJson(null);
        assertThat(json, is(nullValue()));
    }

    @Test
    public void testToJson_invalidObject() {
        TestWithError testWithError = new TestWithError();

        String json = HyperwalletJsonUtil.toJson(testWithError);
        assertThat(json, is(nullValue()));
    }

    @Test
    public void testToJson_successful() {
        TestBodyWithMonitor body = new TestBodyWithMonitor();
        body.setTest("value1");
        body.test2 = "value2";
        body.clearTest3();

        String json = HyperwalletJsonUtil.toJson(body);
        assertThat(json, is(equalTo("{\"test\":\"value1\",\"test3\":null}")));
    }

}
