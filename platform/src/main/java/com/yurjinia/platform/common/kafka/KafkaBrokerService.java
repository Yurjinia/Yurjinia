package com.yurjinia.platform.common.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaBrokerService {

    private final KafkaProducer kafkaProducer;

    public void send(String topic, String message) {
        kafkaProducer.send(topic, message);
    }

}
