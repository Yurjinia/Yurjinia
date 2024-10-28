package com.yurjinia.platform.common.configuration;

import com.yurjinia.platform.migration.enums.JobStatus;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfiguration {

    @Value("${KAFKA.BOOTSTRAP_SERVERS}")
    public String bootstrapServers;

    @Value("${KAFKA.CONSUMER.GROUP_ID}")
    public String groupId;

    @Bean
    public ConsumerFactory<String, JobStatus> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());

        StringDeserializer stringDeserializer = new StringDeserializer();
        JsonDeserializer<JobStatus> jobStatusJsonDeserializer = new JsonDeserializer<>(JobStatus.class, false);
        return new DefaultKafkaConsumerFactory<>(configProps, stringDeserializer, jobStatusJsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, JobStatus> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, JobStatus> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}
