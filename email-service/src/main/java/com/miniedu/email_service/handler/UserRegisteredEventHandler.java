package com.miniedu.email_service.handler;

import com.miniedu.email_service.model.EmailTokenRequest;
import com.miniedu.email_service.service.EmailService;
import com.miniedu.model.kafka.events.UserRegisteredEvent;
import org.springframework.stereotype.Component;

@Component
public class UserRegisteredEventHandler implements EmailEventHandler<UserRegisteredEvent>  {

    private final EmailService emailService;

    public UserRegisteredEventHandler(EmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void handle(UserRegisteredEvent event) {
        emailService.sendVerificationEmail(new EmailTokenRequest(event.email(), event.verificationToken()));
    }

    @Override
    public Class<UserRegisteredEvent> getHandledType() {
        return UserRegisteredEvent.class;
    }
}
