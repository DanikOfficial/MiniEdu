package com.miniedu.userservice.exception;

public class UserNotExistException extends RuntimeException {

    public UserNotExistException(String message) {
        super(message);
    }

    public UserNotExistException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
