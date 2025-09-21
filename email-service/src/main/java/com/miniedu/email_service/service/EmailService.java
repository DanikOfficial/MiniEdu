package com.miniedu.email_service.service;

import com.miniedu.email_service.model.EmailTokenRequest;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendVerificationEmail(EmailTokenRequest request);

    void sendResetPasswordEmail(EmailTokenRequest request);
}
