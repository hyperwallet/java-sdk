package com.hyperwallet.clientsdk.model;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

/**
 * @author fkrauthan
 */
public class HyperwalletBaseMonitorTest {

    @Test
    public void testClearField() {
        HyperwalletBaseMonitor monitor = new HyperwalletBaseMonitor();
        assertThat(monitor.getInclusions(), is(Matchers.<String>empty()));

        monitor.clearField("test");
        assertThat(monitor.getInclusions(), containsInAnyOrder("test"));

        monitor.clearField("test2");
        assertThat(monitor.getInclusions(), containsInAnyOrder("test", "test2"));

        monitor.clearField("test");
        assertThat(monitor.getInclusions(), containsInAnyOrder("test", "test2"));
    }

    @Test
    public void testAddFieldWithoutNullValue() {
        HyperwalletBaseMonitor monitor = new HyperwalletBaseMonitor();
        assertThat(monitor.getInclusions(), is(Matchers.<String>empty()));

        monitor.addField("test", "test-value");
        assertThat(monitor.getInclusions(), containsInAnyOrder("test"));

        monitor.addField("test2", "test-value");
        assertThat(monitor.getInclusions(), containsInAnyOrder("test", "test2"));

        monitor.addField("test", "test-value");
        assertThat(monitor.getInclusions(), containsInAnyOrder("test", "test2"));
    }

    @Test
    public void testAddFieldWithNullValue() {
        HyperwalletBaseMonitor monitor = new HyperwalletBaseMonitor();
        assertThat(monitor.getInclusions(), is(Matchers.<String>empty()));

        monitor.addField("test", "test-value");
        assertThat(monitor.getInclusions(), containsInAnyOrder("test"));

        monitor.addField("test2", "test-value");
        assertThat(monitor.getInclusions(), containsInAnyOrder("test", "test2"));

        monitor.addField("test", null);
        assertThat(monitor.getInclusions(), containsInAnyOrder("test2"));
    }
}
