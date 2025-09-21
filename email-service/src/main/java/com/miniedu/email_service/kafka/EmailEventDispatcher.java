package com.miniedu.email_service.kafka;


import com.miniedu.email_service.handler.EmailEventHandler;
import com.miniedu.model.kafka.events.EmailEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EmailEventDispatcher {

    private final Map<Class<? extends EmailEvent>, EmailEventHandler<? extends EmailEvent>> handlers;

    public EmailEventDispatcher(List<EmailEventHandler<? extends EmailEvent>> handlerList) {
        this.handlers = handlerList.stream()
                .collect(Collectors.toMap(
                        EmailEventHandler::getHandledType,
                        handler -> handler));
    }

    @SuppressWarnings("unchecked")
    public <T extends EmailEvent> void dispatch(T event) {
        EmailEventHandler<T> handler = (EmailEventHandler<T>) handlers.get(event.getClass());
        if (handler != null) {
            handler.handle(event);
        } else {
            throw new IllegalArgumentException("No handler for type: " + event.getClass());
        }
    }
}
