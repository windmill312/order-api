package com.github.windmill312.order.exception;

public class InvalidReceiveTimeException extends RuntimeException {

    private static final long serialVersionUID = -1388093425497487224L;

    public InvalidReceiveTimeException() {
    }

    public InvalidReceiveTimeException(String message) {
        super(message);
    }
}
