package com.miniedu.email_service.constants;

public final class EmailConstants {

    // Email Types
    public static final String VERIFICATION_EMAIL = "VERIFICATION";
    public static final String PASSWORD_RESET_EMAIL = "PASSWORD_RESET";

    // Template Paths
    public static final String EMAIL_VERIFICATION_TEMPLATE_PATH = "templates/email-verification.html";
    public static final String EMAIL_PASSWORD_RESET_TEMPLATE_PATH = "templates/password-reset.html";

    // Placeholder Keys
    public static final String VERIFICATION_LINK_PLACEHOLDER = "{{verification_link}}";
    public static final String PASSWORD_RESET_LINK_PLACEHOLDER = "{{reset_link}}";

    // Email Subjects
    public static final String VERIFICATION_EMAIL_SUBJECT = "Verify your MiniEdu account";
    public static final String PASSWORD_RESET_EMAIL_SUBJECT = "MiniEdu Account – Password Reset Instructions";

    private EmailConstants() {}
}
