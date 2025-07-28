package com.smm.adapterservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public final class RestTemplateUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String toJson(final Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }

    public static HttpEntity<Object> buildHttpEntity(final Object requestBody) {
        return new HttpEntity<>(requestBody, buildHeaders());
    }

    private static HttpHeaders buildHeaders() {
        return new HttpHeaders();
    }
}
