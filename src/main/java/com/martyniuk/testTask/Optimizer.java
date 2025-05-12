package com.martyniuk.testTask;

import com.martyniuk.testTask.exception.NoAvailablePaymentException;
import com.martyniuk.testTask.model.Order;
import com.martyniuk.testTask.model.PaymentMethod;

import java.util.*;

public class Optimizer {

    private final List<Order> orders;
    private final Map<String, PaymentMethod> methodMap = new HashMap<>();
    private final Map<String, Double> spent = new HashMap<>();

    public Optimizer(List<Order> orders, List<PaymentMethod> methods) {
        if (orders == null || orders.isEmpty())
            throw new IllegalArgumentException("Order list is empty or null");
        if (methods == null || methods.isEmpty())
            throw new IllegalArgumentException("Payment method list is empty or null");

        this.orders = orders;

        for (PaymentMethod m : methods) {
            if (m.getDiscount() < 0 || m.getDiscount() > 100)
                throw new IllegalArgumentException("Invalid discount: " + m.getId());
            if (m.getLimit() < 0)
                throw new IllegalArgumentException("Negative limit: " + m.getId());

            methodMap.put(m.getId(), m);
            spent.put(m.getId(), 0.0);
        }
    }

    public Map<String, Double> optimize() {
        List<Order> noPromoOrders = new ArrayList<>();

        List<Order> promoOrders = new ArrayList<>();
        for (Order order : orders) {
            if (order.getPromotions() == null || order.getPromotions().isEmpty()) {
                noPromoOrders.add(order);
            } else {
                promoOrders.add(order);
            }
        }

        Map<String, List<Order>> grouped = new HashMap<>();
        for (Order order : promoOrders) {
            for (String promo : order.getPromotions()) {
                grouped.computeIfAbsent(promo, k -> new ArrayList<>()).add(order);
            }
        }

        Set<String> processedOrders = new HashSet<>();

        for (Order order : promoOrders) {
            if (processedOrders.contains(order.getId())) continue;

            double bestCost = Double.MAX_VALUE;
            PaymentMethod bestMethod = null;

            for (String promoId : order.getPromotions()) {
                PaymentMethod method = methodMap.get(promoId);
                if (method == null) continue;

                double cardCost = round(order.getValue() * (1 - method.getDiscount() / 100.0));
                if (method.getLimit() >= cardCost && cardCost < bestCost) {
                    bestCost = cardCost;
                    bestMethod = method;
                }
            }

            PaymentMethod punkty = methodMap.get("PUNKTY");
            if (punkty != null && punkty.getLimit() >= order.getValue()) {
                double punktyCost = round(order.getValue() * (1 - punkty.getDiscount() / 100.0));
                if (punktyCost < bestCost && punkty.getLimit() >= punktyCost) {
                    bestMethod = punkty;
                    bestCost = punktyCost;
                }
            }

            if (bestMethod != null && bestMethod.getLimit() >= bestCost) {
                applyDiscount(bestMethod, bestCost);
                processedOrders.add(order.getId());
            }

            if (bestMethod == null) {
                throw new NoAvailablePaymentException("No available method for order: " + order.getId());
            }
        }


        noPromoOrders.sort(Comparator.comparingDouble(Order::getValue));

        for (Order order : noPromoOrders) {
            PaymentMethod punkty = methodMap.get("PUNKTY");
            if (punkty == null) continue;

            double fullDiscounted = round(order.getValue() * (1 - punkty.getDiscount() / 100.0));

            if (punkty.getLimit() >= fullDiscounted) {
                // PUNKTY can fully cover the discounted price
                punkty.setLimit(punkty.getLimit() - fullDiscounted);
                spent.put(punkty.getId(), spent.get(punkty.getId()) + fullDiscounted);
                continue; // order paid — skip to next
            }

            double discounted = round(order.getValue() * 0.90);
            double minPointsRequired = round(order.getValue() * 0.10);
            double pointsUsed = Math.min(punkty.getLimit(), discounted);
            double cardPart = discounted - pointsUsed;

            if (punkty.getLimit() >= minPointsRequired) {
                for (PaymentMethod pm : methodMap.values()) {
                    if (!pm.getId().equals("PUNKTY") && pm.getLimit() >= cardPart) {
                        punkty.setLimit(punkty.getLimit() - pointsUsed);
                        spent.put(punkty.getId(), spent.get(punkty.getId()) + pointsUsed);

                        pm.setLimit(pm.getLimit() - cardPart);
                        spent.put(pm.getId(), spent.get(pm.getId()) + cardPart);
                        break;
                    }
                }
            }
        }

        return spent;
    }

    private void applyDiscount(PaymentMethod method, double cost) {
        method.setLimit(method.getLimit() - cost);
        spent.put(method.getId(), spent.get(method.getId()) + cost);
    }

    private double round(double x) {
        return Math.round(x * 100.0) / 100.0;
    }
}
