package ru.clevertec.news.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverterUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJsonString(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
}
