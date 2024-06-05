package com.github.davidherdu.microservices.integration.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produce")
public class ProducerController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "example-topic";

    @PostMapping("/{message}")
    public String produceMessage(@PathVariable String message) {
        kafkaTemplate.send(TOPIC, message);
        return "Message sent to Kafka topic: " + message;
    }
}
