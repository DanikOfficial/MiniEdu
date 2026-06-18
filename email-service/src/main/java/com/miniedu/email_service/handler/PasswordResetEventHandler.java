package com.miniedu.email_service.handler;

import com.miniedu.email_service.model.EmailTokenRequest;
import com.miniedu.email_service.service.EmailService;
import com.miniedu.model.kafka.events.PasswordResetEvent;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetEventHandler implements EmailEventHandler<PasswordResetEvent>{

    private final EmailService emailService;

    public PasswordResetEventHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void handle(PasswordResetEvent event) {
        emailService.sendResetPasswordEmail(new EmailTokenRequest(event.email(), event.passwordResetToken()));
    }

    @Override
    public Class<PasswordResetEvent> getHandledType() {
        return PasswordResetEvent.class;
    }
}
