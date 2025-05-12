package com.martyniuk.testTask.model;

public class PaymentMethod {
    private String id;
    private int discount;
    private double limit;

    public PaymentMethod() {}

    public String getId() {
        return id;
    }

    public int getDiscount() {
        return discount;
    }

    public double getLimit() {
        return limit;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }
}
