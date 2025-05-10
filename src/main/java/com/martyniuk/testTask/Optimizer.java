package com.martyniuk.testTask;

import com.martyniuk.testTask.model.Order;
import com.martyniuk.testTask.model.PaymentMethod;

import java.util.*;

public class Optimizer {
    private final List<Order> orders;
    private final List<PaymentMethod> methods;
    private final Map<String, Double> spent = new HashMap<>();
    private final Map<String, PaymentMethod> methodMap = new HashMap<>();

    public Optimizer(List<Order> orders, List<PaymentMethod> methods) {
        if (orders == null || orders.isEmpty()) {
            throw new IllegalArgumentException("Order list is empty or null");
        }
        if (methods == null || methods.isEmpty()) {
            throw new IllegalArgumentException("Payment method list is empty or null");
        }

        for (Order order : orders) {
            if (order.value <= 0) {
                throw new IllegalArgumentException("Invalid order value: " + order.id);
            }
        }

        for (PaymentMethod method : methods) {
            if (method.discount < 0 || method.discount > 100) {
                throw new IllegalArgumentException("Invalid discount for method: " + method.id);
            }
            if (method.limit < 0) {
                throw new IllegalArgumentException("Negative limit for method: " + method.id);
            }
        }
        this.orders = orders;
        this.methods = methods;
        for (PaymentMethod pm : methods) {
            methodMap.put(pm.id, pm);
            spent.put(pm.id, 0.0);
        }
    }

    public Map<String, Double> optimize() {
        for (int i = 0; i < orders.size(); i++) {
            Order current = orders.get(i);
            if (i + 1 < orders.size()) {
                Order next = orders.get(i + 1);
                double currentCost = simulateCost(current);
                double nextCost = simulateCost(next);

                if (nextCost < currentCost) {
                    applyBest(next);
                    applyBest(current);
                    i++;
                } else {
                    applyBest(current);
                }
            } else {
                applyBest(current);
            }
        }
        return spent;
    }

    private double simulateCost(Order order) {
        double base = order.value;
        double best = Double.MAX_VALUE;

        PaymentMethod punkty = methodMap.get("PUNKTY");
        // only apply full discount if PUNKTY covers full value
        if (punkty != null && punkty.limit >= base) {
            double discounted = round(base * (1 - punkty.discount / 100.0));
            best = Math.min(best, discounted);
        }

        if (order.promotions != null) {
            for (String id : order.promotions) {
                PaymentMethod pm = methodMap.get(id);
                if (pm != null) {
                    double discounted = round(base * (1 - pm.discount / 100.0));
                    if (pm.limit >= discounted) best = Math.min(best, discounted);
                }
            }
        }

        double ten = round(base * 0.10);
        double rest = round(base * 0.90);
        if (punkty != null && punkty.limit >= ten) {
            for (PaymentMethod pm : methods) {
                if (!pm.id.equals("PUNKTY")) {
                    if (pm.limit >= rest) best = Math.min(best, ten + rest);
                }
            }
        }

        return best;
    }

    private void applyBest(Order order) {
        double base = order.value;
        PaymentMethod punkty = methodMap.get("PUNKTY");
        double ten = round(base * 0.10);
        double rest = round(base * 0.90);

        double best = Double.MAX_VALUE;
        Runnable apply = () -> {};

        if (punkty != null) {
            double discounted = round(base * (1 - punkty.discount / 100.0));
            if (punkty.limit >= discounted && discounted < best) {
                best = discounted;
                apply = () -> {
                    punkty.limit -= discounted;
                    spent.put("PUNKTY", spent.get("PUNKTY") + discounted);
                };
            }
        }

        if (order.promotions != null) {
            for (String id : order.promotions) {
                PaymentMethod pm = methodMap.get(id);
                if (pm != null) {
                    double discounted = round(base * (1 - pm.discount / 100.0));
                    if (pm.limit >= discounted && discounted < best) {
                        best = discounted;
                        PaymentMethod card = pm;
                        apply = () -> {
                            card.limit -= discounted;
                            spent.put(card.id, spent.get(card.id) + discounted);
                        };
                    }
                }
            }
        }

        if (punkty != null && punkty.limit >= ten) {
            for (PaymentMethod pm : methods) {
                if (!pm.id.equals("PUNKTY")) {
                    if (pm.limit >= rest && ten + rest < best) {
                        best = ten + rest;
                        PaymentMethod card = pm;
                        apply = () -> {
                            punkty.limit -= ten;
                            card.limit -= rest;
                            spent.put("PUNKTY", spent.get("PUNKTY") + ten);
                            spent.put(card.id, spent.get(card.id) + rest);
                        };
                    }
                }
            }
        }

        apply.run();
    }

    private double round(double x) {
        return Math.round(x * 100.0) / 100.0;
    }
}

