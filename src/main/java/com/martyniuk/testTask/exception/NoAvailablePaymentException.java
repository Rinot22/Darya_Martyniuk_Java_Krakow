package com.martyniuk.testTask.exception;

public class NoAvailablePaymentException extends RuntimeException {
    public NoAvailablePaymentException(String orderId) {
        super("No available payment method for " + orderId);
    }
}
