package com.miniedu.model.kafka.events;

public record PasswordResetEvent(String email, String userId, String passwordResetToken) implements EmailEvent {
}
