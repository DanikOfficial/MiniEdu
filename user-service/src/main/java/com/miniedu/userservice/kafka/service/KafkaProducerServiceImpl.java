package com.miniedu.userservice.kafka.service;

import com.miniedu.model.kafka.events.EmailEvent;
import com.miniedu.userservice.kafka.config.KafkaTopicProperties;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final KafkaTemplate<String, EmailEvent> kafkaTemplate;
    private final KafkaTopicProperties topicProperties;

    private ProducerRecord<String, EmailEvent> createRecord(String topic, EmailEvent event) {
        ProducerRecord<String, EmailEvent> record = new ProducerRecord<>(topic, event.email(), event);
        record.headers().add(new RecordHeader("__TypeId__", event.getClass().getName().getBytes()));
        return record;
    }

    @Override
    public void sendEmailEvent(EmailEvent event) {
        kafkaTemplate.send(createRecord(topicProperties.getEmailEvents(), event));
    }
}
