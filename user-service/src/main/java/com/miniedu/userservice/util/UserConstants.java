package com.miniedu.userservice.util;

public final class UserConstants {

    public static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_EMAIL = "email";
    public static final String CLAIM_USER_ID = "userId";

    public static final String ERROR_USER_NOT_EXIST = "The specified user does not exist!";
    public static final String ERROR_EMAIL_ALREADY_EXISTS = "There's already a user with this email";

    public static final String MSG_USER_REGISTERED =
            "User created successfully! Please check your email and click verification link to finish the registration process.";
    public static final String USER_PASSWORD_REQUEST_CHANGED = "The password change link was sent successfully! " +
            "Please check your email and click the link to finish password change!";

    private UserConstants() {
        // Private constructor to prevent instantiation
    }
}
