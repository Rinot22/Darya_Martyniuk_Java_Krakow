package com.martyniuk.tests;

import com.martyniuk.testTask.model.Order;
import com.martyniuk.testTask.model.PaymentMethod;
import com.martyniuk.testTask.util.JsonReader;
import com.martyniuk.testTask.util.ListType;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    public void testReadValidOrderList() throws Exception {
        String json = "[{\"id\":\"ORDER1\",\"value\":123.45,\"promotions\":[\"CARD\"]}]";
        File temp = File.createTempFile("orders", ".json");
        try (FileWriter writer = new FileWriter(temp)) {
            writer.write(json);
        }

        List<Order> orders = JsonReader.readList(temp.getAbsolutePath(), ListType.ORDER_LIST);
        assertEquals(1, orders.size());
        assertEquals("ORDER1", orders.get(0).getId());
        assertEquals(123.45, orders.get(0).getValue());
        assertEquals(List.of("CARD"), orders.get(0).getPromotions());
    }

    @Test
    public void testReadValidPaymentMethodList() throws Exception {
        String json = "[{\"id\":\"CARD\",\"discount\":10,\"limit\":500.0}]";
        File temp = File.createTempFile("methods", ".json");
        try (FileWriter writer = new FileWriter(temp)) {
            writer.write(json);
        }

        List<PaymentMethod> methods = JsonReader.readList(temp.getAbsolutePath(), ListType.PAYMENT_METHOD_LIST);
        assertEquals(1, methods.size());
        assertEquals("CARD", methods.get(0).getId());
        assertEquals(10, methods.get(0).getDiscount());
        assertEquals(500.0, methods.get(0).getLimit());
    }

    @Test
    public void testInvalidFileThrowsException() {
        String fakePath = "non_existent.json";
        assertThrows(Exception.class, () -> {
            JsonReader.readList(fakePath, ListType.ORDER_LIST);
        });
    }

    @Test
    public void testMalformedJsonThrowsException() throws Exception {
        File temp = File.createTempFile("invalid", ".json");
        try (FileWriter writer = new FileWriter(temp)) {
            writer.write("{ this is not valid json ]");
        }

        assertThrows(Exception.class, () -> {
            JsonReader.readList(temp.getAbsolutePath(), ListType.ORDER_LIST);
        });
    }
}

