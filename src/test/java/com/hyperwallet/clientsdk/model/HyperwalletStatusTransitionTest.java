package com.hyperwallet.clientsdk.model;

import org.testng.annotations.Test;

import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author fkrauthan
 */
public class HyperwalletStatusTransitionTest extends BaseModelTest<HyperwalletStatusTransition> {
    protected HyperwalletStatusTransition createBaseModel() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition
                .token("test-token")
                .transition(HyperwalletStatusTransition.Status.ACTIVATED)
                .fromStatus(HyperwalletStatusTransition.Status.LOCKED)
                .toStatus(HyperwalletStatusTransition.Status.ACTIVATED)
                .createdOn(new Date())
                .notes("test-notes");
        return transition;
    }

    protected Class<HyperwalletStatusTransition> createModelClass() {
        return HyperwalletStatusTransition.class;
    }

    @Test
    public void testConstructorWithStatusArgument() {
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition(HyperwalletStatusTransition.Status.ACTIVATED);
        assertThat(transition.getTransition(), is(equalTo(HyperwalletStatusTransition.Status.ACTIVATED)));

        assertThat(transition.getInclusions(), containsInAnyOrder("transition"));
    }

}
