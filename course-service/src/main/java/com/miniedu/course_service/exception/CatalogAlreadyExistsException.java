package com.miniedu.course_service.exception;

public class CatalogAlreadyExistsException extends RuntimeException{
    public CatalogAlreadyExistsException(String message) {
        super(message);
    }
}
