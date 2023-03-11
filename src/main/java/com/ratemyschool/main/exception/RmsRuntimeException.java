package com.ratemyschool.main.exception;

public class RmsRuntimeException extends RuntimeException {
    public RmsRuntimeException() {
    }

    public RmsRuntimeException(String message) {
        super(message);
    }
}
