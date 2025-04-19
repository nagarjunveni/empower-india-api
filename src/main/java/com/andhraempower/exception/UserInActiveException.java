package com.andhraempower.exception;

public class UserInActiveException extends RuntimeException {

    public UserInActiveException(String message) {
        super(message);
    }
}
