package com.hyperwallet.clientsdk.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fkrauthan
 */
public class HyperwalletProgramTest extends BaseModelTest<HyperwalletProgram> {
    protected HyperwalletProgram createBaseModel() {
        List<HyperwalletLink> hyperwalletLinkList = new ArrayList<>();
        HyperwalletLink hyperwalletLink = new HyperwalletLink();
        hyperwalletLinkList.add(hyperwalletLink);
        HyperwalletProgram program = new HyperwalletProgram();
        program
                .token("test-token")
                .createdOn(new Date())
                .name("test-name")
                .parentToken("test-parent-token")
                .links(hyperwalletLinkList);
        return program;
    }

    protected Class<HyperwalletProgram> createModelClass() {
        return HyperwalletProgram.class;
    }
}
