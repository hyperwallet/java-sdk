package com.hyperwallet.clientsdk.model;

import java.util.Date;

/**
 * @author fkrauthan
 */
public class HyperwalletProgramTest extends BaseModelTest<HyperwalletProgram> {
    protected HyperwalletProgram createBaseModel() {
        HyperwalletProgram program = new HyperwalletProgram();
        program
                .token("test-token")
                .createdOn(new Date())
                .name("test-name")
                .parentToken("test-parent-token");
        return program;
    }

    protected Class<HyperwalletProgram> createModelClass() {
        return HyperwalletProgram.class;
    }
}
