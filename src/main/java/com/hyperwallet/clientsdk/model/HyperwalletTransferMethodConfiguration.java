package com.hyperwallet.clientsdk.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HyperwalletTransferMethodConfiguration {

    public static class SelectionChoice {

        private String value;
        private String label;

        public String getValue() {
            return value;
        }

        public SelectionChoice setValue(String value) {
            this.value = value;
            return this;
        }

        public String getLabel() {
            return label;
        }

        public SelectionChoice setLabel(String label) {
            this.label = label;
            return this;
        }
    }

    public static class Field {

        public enum Category {ACCOUNT, INTERMEDIARY_ACCOUNT, PROFILE, ADDRESS}

        public enum DataType {TEXT, SELECTION, BOOLEAN, NUMBER}

        private String name;
        private String label;
        private Category category;
        private DataType dataType;
        private boolean isRequired;
        private String regularExpression;
        private Integer minLength;
        private Integer maxLength;
        private List<SelectionChoice> selectionChoices = new ArrayList<SelectionChoice>();

        public String getName() {
            return name;
        }

        public Field setName(String name) {
            this.name = name;
            return this;
        }

        public String getLabel() {
            return label;
        }

        public Field setLabel(String label) {
            this.label = label;
            return this;
        }

        public Category getCategory() {
            return category;
        }

        public Field setCategory(Category category) {
            this.category = category;
            return this;
        }

        public DataType getDataType() {
            return dataType;
        }

        public Field setDataType(DataType dataType) {
            this.dataType = dataType;
            return this;
        }

        public boolean isRequired() {
            return isRequired;
        }

        public Field setRequired(boolean required) {
            isRequired = required;
            return this;
        }

        public String getRegularExpression() {
            return regularExpression;
        }

        public Field setRegularExpression(String regularExpression) {
            this.regularExpression = regularExpression;
            return this;
        }

        public Integer getMinLength() {
            return minLength;
        }

        public Field setMinLength(Integer minLength) {
            this.minLength = minLength;
            return this;
        }

        public Integer getMaxLength() {
            return maxLength;
        }

        public Field setMaxLength(Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public List<SelectionChoice> getSelectionChoices() {
            return selectionChoices;
        }

        public Field setSelectionChoices(List<SelectionChoice> selectionChoices) {
            this.selectionChoices = selectionChoices;
            return this;
        }
    }

    private List<String> countries = new ArrayList<String>();
    private List<String> currencies = new ArrayList<String>();
    private HyperwalletTransferMethod.Type type;
    private HyperwalletUser.ProfileType profileType;
    private List<Field> fields = new ArrayList<Field>();

    public List<String> getCountries() {
        return countries;
    }

    public HyperwalletTransferMethodConfiguration setCountries(List<String> countries) {
        this.countries = countries;
        return this;
    }

    public List<String> getCurrencies() {
        return currencies;
    }

    public HyperwalletTransferMethodConfiguration setCurrencies(List<String> currencies) {
        this.currencies = currencies;
        return this;
    }

    public HyperwalletTransferMethod.Type getType() {
        return type;
    }

    public HyperwalletTransferMethodConfiguration setType(HyperwalletTransferMethod.Type type) {
        this.type = type;
        return this;
    }

    public HyperwalletUser.ProfileType getProfileType() {
        return profileType;
    }

    public HyperwalletTransferMethodConfiguration setProfileType(HyperwalletUser.ProfileType profileType) {
        this.profileType = profileType;
        return this;
    }

    public List<Field> getFields() {
        return fields;
    }

    public HyperwalletTransferMethodConfiguration setFields(List<Field> fields) {
        this.fields = fields;
        return this;
    }
}
