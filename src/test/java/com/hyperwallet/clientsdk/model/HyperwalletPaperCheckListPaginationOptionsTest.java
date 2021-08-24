package com.hyperwallet.clientsdk.model;


public class HyperwalletPaperCheckListPaginationOptionsTest extends BaseModelTest<HyperwalletPaperCheckListPaginationOptions> {

    @Override
    protected HyperwalletPaperCheckListPaginationOptions createBaseModel() {
        HyperwalletPaperCheckListPaginationOptions options = new HyperwalletPaperCheckListPaginationOptions();
        options
                .status(HyperwalletPaperCheck.Status.ACTIVATED);

        return options;
    }

    @Override
    protected Class<HyperwalletPaperCheckListPaginationOptions> createModelClass() {
        return HyperwalletPaperCheckListPaginationOptions.class;
    }
}

