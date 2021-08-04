package com.hyperwallet.clientsdk.model;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author fkrauthan
 */
public class HyperwalletStatusTransitionTest extends BaseModelTest<HyperwalletStatusTransition> {

    @Override
    protected HyperwalletStatusTransition createBaseModel() {
        List<HyperwalletLink> hyperwalletLinkList = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLinkList.add(hyperwalletLink);
        HyperwalletStatusTransition transition = new HyperwalletStatusTransition();
        transition
                .token("test-token")
                .transition(HyperwalletStatusTransition.Status.ACTIVATED)
                .fromStatus(HyperwalletStatusTransition.Status.LOCKED)
                .toStatus(HyperwalletStatusTransition.Status.ACTIVATED)
                .createdOn(new Date())
                .notes("test-notes")
                .links(hyperwalletLinkList);
        return transition;
    }

    @Override
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
