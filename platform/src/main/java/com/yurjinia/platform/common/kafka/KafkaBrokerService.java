package com.yurjinia.platform.common.kafka;

import com.yurjinia.platform.migration.enums.JobStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaBrokerService {

    private final KafkaProducer kafkaProducer;

    public void send(String topic, JobStatus message) {
        kafkaProducer.send(topic, message);
    }

}
