package com.andhraempower.exception;

public class MissingBearerTokenException extends RuntimeException {
    public MissingBearerTokenException(String message) {
        super(message);
    }
}
