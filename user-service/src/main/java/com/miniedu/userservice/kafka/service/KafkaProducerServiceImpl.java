package com.miniedu.userservice.kafka.service;

import com.miniedu.model.kafka.events.EmailEvent;
import com.miniedu.userservice.kafka.config.KafkaTopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerServiceImpl implements KafkaProducerService {
    private final KafkaTemplate<String, EmailEvent> kafkaTemplate;
    private final KafkaTopicProperties topicProperties;

    private ProducerRecord<String, EmailEvent> createRecord(String topic, EmailEvent event) {
        return new ProducerRecord<>(topic, event.email(), event);
    }

    @Override
    public void sendEmailEvent(EmailEvent event) {
        log.info("Sending event to Kafka topic: {}", topicProperties.getEmailEvents());

        kafkaTemplate.send(createRecord(topicProperties.getEmailEvents(), event))
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info(" Successfully sent to topic: {} partition: {} offset: {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset()
                        );
                    } else {
                        log.error("CRITICAL KAFKA PRODUCER ERROR:", ex);
                    }
                });
    }
}
