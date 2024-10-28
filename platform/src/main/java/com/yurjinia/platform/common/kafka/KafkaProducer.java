package com.yurjinia.platform.common.kafka;


import com.yurjinia.platform.migration.enums.JobStatus;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, JobStatus> kafkaTemplate;

    public void send(String topic, JobStatus message) {
        kafkaTemplate.send(topic, message);
    }

}
