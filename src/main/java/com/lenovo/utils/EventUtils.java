package com.lenovo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class EventUtils {
    public  <T> T deserialize(String value, Class<T> clazz) {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(value, clazz);
        } catch (IOException e) {
            throw new RuntimeException("can't read value from event", e);
        }
    }
}
