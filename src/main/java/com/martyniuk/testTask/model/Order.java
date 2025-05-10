package com.martyniuk.testTask.model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    public String id;
    public double value;
    public List<String> promotions = new ArrayList<>();

    public Order() {}

}
