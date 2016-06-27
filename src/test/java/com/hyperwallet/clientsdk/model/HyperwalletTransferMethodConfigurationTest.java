package com.hyperwallet.clientsdk.model;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author fkrauthan
 */
public class HyperwalletTransferMethodConfigurationTest {

    @Test
    public void testSelectionChoice() {
        HyperwalletTransferMethodConfiguration.SelectionChoice selectionChoice = new HyperwalletTransferMethodConfiguration.SelectionChoice();
        assertThat(selectionChoice.getLabel(), is(nullValue()));
        assertThat(selectionChoice.getValue(), is(nullValue()));

        selectionChoice.setLabel("test-label");
        selectionChoice.setValue("test-value");

        assertThat(selectionChoice.getLabel(), is(equalTo("test-label")));
        assertThat(selectionChoice.getValue(), is(equalTo("test-value")));
    }

    @Test
    public void testField() {
        HyperwalletTransferMethodConfiguration.Field field = new HyperwalletTransferMethodConfiguration.Field();
        assertThat(field.getLabel(), is(nullValue()));
        assertThat(field.getCategory(), is(nullValue()));
        assertThat(field.getDataType(), is(nullValue()));
        assertThat(field.getMaxLength(), is(nullValue()));
        assertThat(field.getMinLength(), is(nullValue()));
        assertThat(field.getName(), is(nullValue()));
        assertThat(field.getRegularExpression(), is(nullValue()));
        assertThat(field.isRequired(), is(equalTo(false)));
        assertThat(field.getSelectionChoices(), is(Matchers.<HyperwalletTransferMethodConfiguration.SelectionChoice>empty()));

        List<HyperwalletTransferMethodConfiguration.SelectionChoice> list = new ArrayList<HyperwalletTransferMethodConfiguration.SelectionChoice>();
        list.add(new HyperwalletTransferMethodConfiguration.SelectionChoice());

        field.setLabel("test-label");
        field.setCategory(HyperwalletTransferMethodConfiguration.Field.Category.ACCOUNT);
        field.setDataType(HyperwalletTransferMethodConfiguration.Field.DataType.BOOLEAN);
        field.setMaxLength(100);
        field.setMinLength(50);
        field.setName("test-name");
        field.setRegularExpression("test-regular-expression");
        field.setRequired(true);
        field.setSelectionChoices(list);

        assertThat(field.getLabel(), is(equalTo("test-label")));
        assertThat(field.getCategory(), is(equalTo(HyperwalletTransferMethodConfiguration.Field.Category.ACCOUNT)));
        assertThat(field.getDataType(), is(equalTo(HyperwalletTransferMethodConfiguration.Field.DataType.BOOLEAN)));
        assertThat(field.getMaxLength(), is(equalTo(100)));
        assertThat(field.getMinLength(), is(equalTo(50)));
        assertThat(field.getName(), is(equalTo("test-name")));
        assertThat(field.getRegularExpression(), is(equalTo("test-regular-expression")));
        assertThat(field.isRequired(), is(equalTo(true)));
        assertThat(field.getSelectionChoices(), is(equalTo(list)));
    }

    @Test
    public void testHyperwalletTransferMethodConfiguration() {
        HyperwalletTransferMethodConfiguration configuration = new HyperwalletTransferMethodConfiguration();
        assertThat(configuration.getCountries(), is(Matchers.<String>empty()));
        assertThat(configuration.getCurrencies(), is(Matchers.<String>empty()));
        assertThat(configuration.getFields(), is(Matchers.<HyperwalletTransferMethodConfiguration.Field>empty()));
        assertThat(configuration.getProfileType(), is(nullValue()));
        assertThat(configuration.getType(), is(nullValue()));

        List<String> countryList = new ArrayList<String>();
        countryList.add("test-country");

        List<String> currencyList = new ArrayList<String>();
        currencyList.add("test-currency");

        List<HyperwalletTransferMethodConfiguration.Field> fieldList = new ArrayList<HyperwalletTransferMethodConfiguration.Field>();
        fieldList.add(new HyperwalletTransferMethodConfiguration.Field());

        configuration.setCountries(countryList);
        configuration.setCurrencies(currencyList);
        configuration.setFields(fieldList);
        configuration.setProfileType(HyperwalletUser.ProfileType.INDIVIDUAL);
        configuration.setType(HyperwalletTransferMethod.Type.BANK_ACCOUNT);

        assertThat(configuration.getCountries(), is(equalTo(countryList)));
        assertThat(configuration.getCurrencies(), is(equalTo(currencyList)));
        assertThat(configuration.getFields(), is(equalTo(fieldList)));
        assertThat(configuration.getProfileType(), is(equalTo(HyperwalletUser.ProfileType.INDIVIDUAL)));
        assertThat(configuration.getType(), is(equalTo(HyperwalletTransferMethod.Type.BANK_ACCOUNT)));
    }

}
