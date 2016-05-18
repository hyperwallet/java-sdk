package com.hyperwallet.clientsdk.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class HyperwalletErrorListTest {

    @Test
    public void testGetErrorsEmpty() throws Exception {
        HyperwalletErrorList errorList = new HyperwalletErrorList();
        assertNotNull(errorList.getErrors());
        assertTrue(errorList.getErrors().isEmpty());
    }

    @Test
    public void testGetErrorsWithErrors() throws Exception {

        HyperwalletErrorList errorList = new HyperwalletErrorList();
        HyperwalletError error = new HyperwalletError();
        error.fieldName = "error_field";
        error.message = "Test error message";
        error.code = "TEST_ERROR_CODE";
        errorList.getErrors().add(error);

        assertNotNull(errorList.getErrors());
        assertFalse(errorList.getErrors().isEmpty());
        assertTrue(errorList.getErrors().size() == 1);

        HyperwalletError e = errorList.getErrors().get(0);
        assertEquals(e.getFieldName(), error.fieldName);
        assertEquals(e.getMessage(), error.message);
        assertEquals(e.getCode(), error.code);
    }
}