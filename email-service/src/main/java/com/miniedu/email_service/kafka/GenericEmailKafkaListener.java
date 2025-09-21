package com.miniedu.email_service.kafka;

import com.miniedu.model.kafka.events.EmailEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GenericEmailKafkaListener {

    private final EmailEventDispatcher dispatcher;

    public GenericEmailKafkaListener(EmailEventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @KafkaListener(topics = "${kafka.topics.emailEvents}", groupId = "email-service-group")
    public void onReceivedEmailEvent(@Payload EmailEvent event) {
        handleReceivedEvent(event);
    }

    private <T extends EmailEvent> void handleReceivedEvent(T event) {
        log.info("Received event: {}", event);
        try {
            dispatcher.dispatch(event);
        } catch (Exception ex) {
            log.error("Failed to dispatch email event: {}", event, ex);
        }
    }
}
