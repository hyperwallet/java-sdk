package com.hyperwallet.clientsdk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.hyperwallet.clientsdk.model.HyperwalletBaseMonitor;

import java.io.IOException;

public class HyperwalletJsonUtil {

    private static ObjectMapper objectMapper = new HyperwalletJsonConfiguration().getContext(Void.class);
    private static ObjectMapper parser = new HyperwalletJsonConfiguration().getParser();

    public HyperwalletJsonUtil() {
        throw new UnsupportedOperationException("This is a util class!");
    }

    public static <T> T fromJson(final String content, final Class<T> valueType) {
        if (content == null) {
            return null;
        }
        try {
            return HyperwalletJsonUtil.parser.readValue(content, valueType);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T fromJson(final String content, final TypeReference<T> valueType) {
        if (content == null) {
            return null;
        }
        try {
            return HyperwalletJsonUtil.parser.readValue(content, valueType);
        } catch (IOException e) {
            return null;
        }
    }

    public static String toJson(final Object object) {
        if (object == null) {
            return null;
        }
        try {
            HyperwalletBaseMonitor base = (HyperwalletBaseMonitor) object;
            FilterProvider filters = new SimpleFilterProvider()
                    .addFilter(HyperwalletJsonConfiguration.INCLUSION_FILTER,
                            SimpleBeanPropertyFilter.filterOutAllExcept(base.getInclusions()));

            ObjectWriter writer = HyperwalletJsonUtil.objectMapper.writer(filters);
            return writer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
