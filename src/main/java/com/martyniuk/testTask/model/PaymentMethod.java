package com.martyniuk.testTask.model;

public class PaymentMethod {
    public String id;
    public int discount;
    public double limit;

    public PaymentMethod() {}

    public PaymentMethod(String id, int discount, double limit) {
        this.id = id;
        this.discount = discount;
        this.limit = limit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }
}
