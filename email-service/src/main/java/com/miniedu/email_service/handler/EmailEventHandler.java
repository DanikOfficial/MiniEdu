package com.miniedu.email_service.handler;


import com.miniedu.model.kafka.events.EmailEvent;

public interface EmailEventHandler<T extends EmailEvent> {
    void handle(T event);
    Class<T> getHandledType();
}
