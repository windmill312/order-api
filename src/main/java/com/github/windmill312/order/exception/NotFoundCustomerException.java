package com.github.windmill312.order.exception;

public class NotFoundCustomerException extends RuntimeException {

    private static final long serialVersionUID = -7123305301975975219L;

    public NotFoundCustomerException() {
    }

    public NotFoundCustomerException(String message) {
        super(message);
    }
}
