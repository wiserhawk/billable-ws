package com.indhawk.billable.billablews.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {

    private static ObjectMapper JSON_MAPPER = new ObjectMapper();

    static {
        JSON_MAPPER.registerModule(new JavaTimeModule());
    }

    public static String objToJson(Object obj) {
        try {
            return JSON_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error while converting object to json", e);
            throw new RuntimeException("Error while converting object to json", e);
        }
    }

    public static  <T> T jsonToObj(String json, Class<T> clazz) {
        try {
            T obj = JSON_MAPPER.readValue(json, clazz);
            return obj;
        } catch (JsonProcessingException e) {
            log.error("Error while converting json to object", e);
            throw new RuntimeException("Error while converting json to object", e);
        }
    }


}
