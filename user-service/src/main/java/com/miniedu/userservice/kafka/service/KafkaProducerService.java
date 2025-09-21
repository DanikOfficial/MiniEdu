package com.miniedu.userservice.kafka.service;

import com.miniedu.model.kafka.events.EmailEvent;

public interface KafkaProducerService {
    void sendEmailEvent(EmailEvent event);
}
