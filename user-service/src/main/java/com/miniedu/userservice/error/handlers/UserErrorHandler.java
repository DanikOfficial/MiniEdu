package com.miniedu.userservice.error.handlers;

import com.miniedu.userservice.exception.EmailAlreadyExistsException;
import com.miniedu.userservice.exception.UnexpectedErrorException;
import com.miniedu.userservice.exception.UserAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;

import static com.miniedu.userservice.util.UserConstants.*;

public class UserErrorHandler {
    public static void handle(DataIntegrityViolationException ex) {
        var message = ex.getMessage();

        if (message.contains("uk_username")) {
            throw new UserAlreadyExistsException(ERROR_USERNAME_ALREADY_EXISTS);
        } else if (message.contains("uk_user_email")) {
            throw new EmailAlreadyExistsException(ERROR_EMAIL_ALREADY_EXISTS);
        } else {
            throw new UnexpectedErrorException(ERROR_UNEXPECTED_ERROR);
        }
    }
}
