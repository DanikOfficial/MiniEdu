package com.miniedu.model.kafka.events;

public record UserRegisteredEvent(String email, String userId, String verificationToken) implements EmailEvent {
}
