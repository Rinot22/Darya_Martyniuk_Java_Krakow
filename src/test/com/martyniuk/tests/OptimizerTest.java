package com.martyniuk.tests;

import com.martyniuk.testTask.Optimizer;
import com.martyniuk.testTask.exception.NoAvailablePaymentException;
import com.martyniuk.testTask.model.Order;
import com.martyniuk.testTask.model.PaymentMethod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class OptimizerTest {

    @Test
    public void testFullPunktyAppliedWhenEnoughLimit() {
        List<Order> orders = List.of(
                createOrder("ORDER1", 100.0, null)
        );
        List<PaymentMethod> methods = List.of(
                createMethod("PUNKTY", 15, 100.0)
        );

        Optimizer optimizer = new Optimizer(orders, methods);
        Map<String, Double> result = optimizer.optimize();

        assertEquals(85.0, result.get("PUNKTY")); // 15% discount
    }

    @Test
    public void testCardUsedWhenPunktyUnavailable() {
        List<Order> orders = List.of(
                createOrder("ORDER1", 100.0, List.of("CARD1"))
        );
        List<PaymentMethod> methods = List.of(
                createMethod("PUNKTY", 15, 0.0),
                createMethod("CARD1", 10, 100.0)
        );

        Optimizer optimizer = new Optimizer(orders, methods);
        Map<String, Double> result = optimizer.optimize();

        assertEquals(90.0, result.get("CARD1")); // 10% discount
    }

    @Test
    public void testMixedPaymentUsedWhenPunktyPartial() {
        List<Order> orders = List.of(
                createOrder("ORDER1", 100.0, null)
        );
        List<PaymentMethod> methods = List.of(
                createMethod("PUNKTY", 15, 10.0),  // can't fully cover → mixed
                createMethod("CARD1", 0, 100.0)
        );

        Optimizer optimizer = new Optimizer(orders, methods);
        Map<String, Double> result = optimizer.optimize();

        assertEquals(10.0, result.get("PUNKTY")); // 10 zł = 10% of 100 after 10% discount = 90
        assertEquals(80.0, result.get("CARD1"));  // remaining 90 - 10
    }

    @Test
    public void testExceptionWhenNoMethodCanPay() {
        List<Order> orders = List.of(
                createOrder("ORDER1", 100.0, null)
        );
        List<PaymentMethod> methods = List.of(
                createMethod("CARD1", 0, 0.0),
                createMethod("PUNKTY", 15, 5.0)
        );

        assertThrows(NoAvailablePaymentException.class, () -> {
            new Optimizer(orders, methods).optimize();
        });
    }

    // Helpers

    private Order createOrder(String id, double value, List<String> promotions) {
        Order o = new Order();
        o.setId(id);
        o.setValue(value);
        o.setPromotions(promotions);
        return o;
    }

    private PaymentMethod createMethod(String id, int discount, double limit) {
        PaymentMethod p = new PaymentMethod();
        p.setId(id);
        p.setDiscount(discount);
        p.setLimit(limit);
        return p;
    }
}
