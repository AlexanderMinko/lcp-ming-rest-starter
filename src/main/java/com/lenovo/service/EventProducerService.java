package com.lenovo.service;

import com.lenovo.model.events.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
public class EventProducerService {

    private final KafkaTemplate<String, Event> kafkaTemplate;

    public void sendEvent(Event event) {
        log.debug("Order event send at: {}", event.getCreatedDate());
        kafkaTemplate.send(event.getTopicName(), event);
        //kafka-console-consumer --bootstrap-server localhost:9092 --topic send-email-event --from-beginning
    }
}
