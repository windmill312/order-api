package com.sychev.order.exception;

public class NotFoundOrderException extends RuntimeException {

    private static final long serialVersionUID = -4699774762055962482L;

    public NotFoundOrderException() {
    }

    public NotFoundOrderException(String message) {
        super(message);
    }
}
