package com.martyniuk.testTask.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    public String id;
    public double value;
    public List<String> promotions = new ArrayList<String>();

    public Order() {}

    public Order(String id, double value, List<String> promotions) {
        this.id = id;
        this.value = value;
        this.promotions = promotions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public List<String> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<String> promotions) {
        this.promotions = promotions;
    }
}
