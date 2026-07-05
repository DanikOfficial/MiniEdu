package com.miniedu.course_service.exception;

public class UnavailableSeatException extends RuntimeException {
    public UnavailableSeatException(String message) {
        super(message);
    }
}
