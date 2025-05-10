package com.martyniuk.testTask.util;
import com.fasterxml.jackson.core.type.TypeReference;
import com.martyniuk.testTask.model.Order;
import com.martyniuk.testTask.model.PaymentMethod;

import java.util.List;

public class ListType {
    public static final TypeReference<List<Order>> ORDER_LIST = new TypeReference<>() {};
    public static final TypeReference<List<PaymentMethod>> PAYMENT_METHOD_LIST = new TypeReference<>() {};
}
