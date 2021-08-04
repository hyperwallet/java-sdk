package com.hyperwallet.clientsdk.model;


public class HyperwalletPaperCheckListPaginationOptionsTest extends BaseModelTest<HyperwalletPaperCheckListPaginationOptions> {

    protected HyperwalletPaperCheckListPaginationOptions createBaseModel() {
        HyperwalletPaperCheckListPaginationOptions options = new HyperwalletPaperCheckListPaginationOptions();
        options
                .status(HyperwalletPaperCheck.Status.ACTIVATED);

        return options;
    }

    protected Class<HyperwalletPaperCheckListPaginationOptions> createModelClass() {
        return HyperwalletPaperCheckListPaginationOptions.class;
    }
}

