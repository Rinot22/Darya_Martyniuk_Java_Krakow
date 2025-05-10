package com.martyniuk.testTask.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martyniuk.testTask.model.Order;
import com.martyniuk.testTask.model.PaymentMethod;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonReader {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> List<T> readList(String path, TypeReference<List<T>> typeRef) throws IOException {
        return mapper.readValue(new File(path), typeRef);
    }
}
