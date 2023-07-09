package com.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String serializeObject(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T deserializeJsonToObject(String json, Class<T> objectType) {
        try {
            return objectMapper.readValue(json, objectType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> deserializeJsonToList(String json, Class<T> objectType) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, objectType));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T deserializeAnonymousType(String json, T anonymousTypeObject) {
        try {
            return objectMapper.readerForUpdating(anonymousTypeObject).readValue(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
