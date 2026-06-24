package com.miniedu.email_service.service;

import com.miniedu.email_service.config.AuthServiceProperties;
import com.miniedu.email_service.constants.EmailConstants;
import com.miniedu.email_service.model.EmailTokenRequest;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private AuthServiceProperties authProps;

    @Override
    public void sendVerificationEmail(EmailTokenRequest request) {
        log.info("Preparing to send verification email to: {}", request.to());
        try {
            String htmlContent = loadTemplateWithToken(request.token(), EmailConstants.VERIFICATION_EMAIL);
            MimeMessage message = buildEmail(request.to(), EmailConstants.VERIFICATION_EMAIL, htmlContent);
            mailSender.send(message);
            log.info("Verification email successfully sent to: {}", request.to());
        } catch (IOException e) {
            log.error("Failed to send verification email to: {}", request.to(), e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    @Override
    public void sendResetPasswordEmail(EmailTokenRequest request) {
        log.info("Preparing to send password reset email to: {}", request.to());
        try {
            String htmlContent = loadTemplateWithToken(request.token(), EmailConstants.PASSWORD_RESET_EMAIL);
            MimeMessage message = buildEmail(request.to(), EmailConstants.PASSWORD_RESET_EMAIL, htmlContent);
            mailSender.send(message);
            log.info("Password reset email successfully sent to: {}", request.to());
        } catch (IOException e) {
            log.error("Failed to send password reset email to: {}", request.to(), e);
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    private MimeMessage buildEmail(String to, String emailType, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);

            String subject = switch (emailType) {
                case EmailConstants.VERIFICATION_EMAIL -> EmailConstants.VERIFICATION_EMAIL_SUBJECT;
                case EmailConstants.PASSWORD_RESET_EMAIL -> EmailConstants.PASSWORD_RESET_EMAIL_SUBJECT;
                default -> throw new IllegalArgumentException("Invalid email type: " + emailType);
            };

            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            return message;
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to build email message", e);
        }
    }

    private String loadTemplateWithToken(String token, String emailType) throws IOException {
        String html;
        String link;
        String placeholder;

        switch (emailType) {
            case EmailConstants.VERIFICATION_EMAIL -> {
                log.debug("Loading email verification template and injecting token...");
                html = loadHtmlTemplate(EmailConstants.EMAIL_VERIFICATION_TEMPLATE_PATH);
                link = authProps.getBaseUrl() + authProps.getPaths().getVerification() + "?token=" + token;
                placeholder = EmailConstants.VERIFICATION_LINK_PLACEHOLDER;
                log.debug("Verification link: {}", link);
            }
            case EmailConstants.PASSWORD_RESET_EMAIL -> {
                log.debug("Loading password reset template and injecting token...");
                html = loadHtmlTemplate(EmailConstants.EMAIL_PASSWORD_RESET_TEMPLATE_PATH);
                link = authProps.getBaseUrl() + authProps.getPaths().getPasswordReset() + "?token=" + token;
                placeholder = EmailConstants.PASSWORD_RESET_LINK_PLACEHOLDER;
                log.debug("Password reset link: {}", link);
            }
            default -> throw new IllegalArgumentException("Invalid email type: " + emailType);
        }

        return html.replace(placeholder, link);
    }

    private String loadHtmlTemplate(String path) throws IOException {
        var resource = new ClassPathResource(path);
        // 🟩 Read directly from the resource's input stream instead of .getFile()
        return org.springframework.util.StreamUtils.copyToString(
                resource.getInputStream(),
                java.nio.charset.StandardCharsets.UTF_8
        );
    }
}
