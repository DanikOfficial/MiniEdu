package com.miniedu.userservice.controller.advice;

import com.miniedu.userservice.api.ApiResponseObject;
import com.miniedu.userservice.exception.EmailAlreadyExistsException;
import com.miniedu.userservice.exception.UserAlreadyExistsException;
import com.miniedu.userservice.exception.UserNotExistException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestControllerAdvisor extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,  // Changed from HttpStatus
            WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String message = "Invalid field(s)!";

        Map<String, Object> res = new ApiResponseObject().buildValidationError(message, "fail", errors);

        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserNotExistException.class})
    public ResponseEntity<Object> handleNotFoundException(
            RuntimeException ex,
            WebRequest request) {

        Map<String, Object> res = new ApiResponseObject().buildSimpleError(ex.getMessage(), "fail");

        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {EmailAlreadyExistsException.class, UserAlreadyExistsException.class})
    public ResponseEntity<Object> handleAlreadyExistException (RuntimeException ex) {
        Map<String, Object> res = new ApiResponseObject().buildSimpleError(ex.getMessage(), "fail");
        return new ResponseEntity<>(res, HttpStatus.CONFLICT);
    }

}
