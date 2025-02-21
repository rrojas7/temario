package com.chapterntt.temario.model.exception;

public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }
}
