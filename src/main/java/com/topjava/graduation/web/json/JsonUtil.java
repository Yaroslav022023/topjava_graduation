package com.topjava.graduation.web.json;

import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;
import java.util.List;

import static com.topjava.graduation.web.json.JacksonObjectMapper.getMapper;

public class JsonUtil {

    public static <T> List<T> readValues(String json, Class<T> clazz) {
        ObjectReader reader = getMapper().readerFor(clazz);
        try {
            return reader.<T>readValues(json).readAll();
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read array from JSON:\n'" + json + "'", e);
        }
    }

    public static <T> T readValue(String json, Class<T> clazz) {
        try {
            return getMapper().readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid read from JSON:\n'" + json + "'", e);
        }
    }
}