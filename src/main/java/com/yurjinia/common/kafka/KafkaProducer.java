package com.yurjinia.common.kafka;


import com.yurjinia.project_structure.project.dto.ProjectDTO;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, ProjectDTO> kafkaTemplate;

    public void send(String topic, ProjectDTO message) {
        kafkaTemplate.send(topic, message);
    }

}
