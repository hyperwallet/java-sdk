package com.hyperwallet.clientsdk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.hyperwallet.clientsdk.HyperwalletException;
import com.hyperwallet.clientsdk.model.HyperwalletBaseMonitor;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;

public class HyperwalletJsonUtil {

    private static ObjectMapper objectMapper = new HyperwalletJsonConfiguration().getContext(Void.class);
    private static ObjectMapper parser = new HyperwalletJsonConfiguration().getParser();

    public HyperwalletJsonUtil() {
        throw new UnsupportedOperationException("This is a util class!");
    }

    public static <T> T fromJson(final String content, final Class<T> valueType) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            return HyperwalletJsonUtil.parser.readValue(changeAmountFormat(content), valueType);
        } catch (IOException e) {
            throw new HyperwalletException(e);
        }
    }

    public static <T> T fromJson(final String content, final TypeReference<T> valueType) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            return HyperwalletJsonUtil.parser.readValue(changeAmountFormat(content), valueType);
        } catch (IOException e) {
            throw new HyperwalletException(e);
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

    /**
     * Remove commas in the numbers returned in json response body to avoid json deserianization errors and to
     * accommodate variances in numbering systems
     *
     * @param responseBody response object
     * @return String responseBody with numbers without commas
     */
    private static String changeAmountFormat(String responseBody) {
        String regexStr = "\\d{1,3}([,]\\d{2,3})*([,]\\d{3})+([.]*\\d+[\\W])?";
        String toReplace = ",";
        String replacementStr = "";  // replacement pattern
        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(responseBody);
        ArrayList<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        for (String oldString : list) {
            String newString = oldString.replace(toReplace, replacementStr);
            responseBody = responseBody.replace(oldString, newString);
        }
        return responseBody;
    }
}
