package java;

import com.martyniuk.testTask.Optimizer;
import com.martyniuk.testTask.model.Order;
import com.martyniuk.testTask.model.PaymentMethod;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class OptimizerTest {
    @Test
    public void testFullPunkty() {
        List<Order> orders = List.of(
                createOrder("ORDER1", 100.0, null)
        );
        List<PaymentMethod> methods = List.of(
                createMethod("PUNKTY", 15, 1000.0)
        );

        Optimizer optimizer = new Optimizer(orders, methods);
        Map<String, Double> result = optimizer.optimize();

        assertEquals(85.0, result.get("PUNKTY"));
    }

    @Test
    public void testCardOnly() {
        List<Order> orders = List.of(
                createOrder("ORDER1", 100.0, List.of("CARD1"))
        );
        List<PaymentMethod> methods = List.of(
                createMethod("PUNKTY", 15, 0.0),
                createMethod("CARD1", 10, 1000.0)
        );

        Optimizer optimizer = new Optimizer(orders, methods);
        Map<String, Double> result = optimizer.optimize();

        assertEquals(90.0, result.get("CARD1"));
    }

    @Test
    public void testMixedStrategy() {
        List<Order> orders = List.of(
                createOrder("ORDER1", 100.0, null)
        );
        List<PaymentMethod> methods = List.of(
                createMethod("PUNKTY", 15, 10.0),
                createMethod("CARD1", 0, 100.0)
        );

        Optimizer optimizer = new Optimizer(orders, methods);
        Map<String, Double> result = optimizer.optimize();

        assertEquals(10.0, result.get("PUNKTY"));
        assertEquals(90.0, result.get("CARD1"));
    }

    private Order createOrder(String id, double value, List<String> promotions) {
        Order o = new Order();
        o.id = id;
        o.value = value;
        o.promotions = promotions;
        return o;
    }

    private PaymentMethod createMethod(String id, int discount, double limit) {
        PaymentMethod p = new PaymentMethod();
        p.id = id;
        p.discount = discount;
        p.limit = limit;
        return p;
    }
}
