package com.martyniuk.testTask;

import com.martyniuk.testTask.model.Order;
import com.martyniuk.testTask.model.PaymentMethod;
import com.martyniuk.testTask.util.JsonReader;
import com.martyniuk.testTask.util.ListType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TestMain {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Usage: java -jar app.jar <orders.json> <paymentmethods.json>");
            System.exit(1);
        }

        try {
            List<Order> orders = JsonReader.readList(args[0], ListType.ORDER_LIST);
            List<PaymentMethod> methods = JsonReader.readList(args[1], ListType.PAYMENT_METHOD_LIST);
            Optimizer optimizer = new Optimizer(orders, methods);
            Map<String, Double> result = optimizer.optimize();

            result.forEach((id, amount) -> System.out.printf("%s %.2f%n", id, amount));
        } catch (IOException e) {
            System.err.println("Failed to read input files: " + e.getMessage());
            System.exit(2);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.exit(3);
        }

    }
}
