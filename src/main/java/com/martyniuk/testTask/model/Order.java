package com.martyniuk.testTask.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id;
    private double value;
    private List<String> promotions = new ArrayList<>();

    public Order() {}

    public String getId() {
        return id;
    }

    public double getValue() {
        return value;
    }

    public List<String> getPromotions() {
        return promotions;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setPromotions(List<String> promotions) {
        this.promotions = promotions;
    }
}
