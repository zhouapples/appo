package com.meeting.appo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DesearializerUtils {
    public static <T> T getEntity(String jsonStr,Class<T> prototype){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (T) objectMapper.readValue(jsonStr,prototype);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
