package com.bank.customerservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "account-creation-topic";

    public void sendMessage(String message) {
        log.info("Sending kafka message to {} : {}", TOPIC, message);
        kafkaTemplate.send(TOPIC, message);
    }
}
