package com.yurjinia.jobmanager.kafka;

import com.yurjinia.jobmanager.job.enums.JobStatus;
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
