package com.lenovo.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaConfig {

    @Bean
    public NewTopic computerEvent() {
        return TopicBuilder.name("create-order-event")
            .partitions(1)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic emailEvent() {
        return TopicBuilder.name("send-email-event")
            .partitions(1)
            .replicas(1)
            .build();
    }

}
