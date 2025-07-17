package org.example.bishopprototype;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;

@Configuration
@Slf4j
@Data
public class Consumer {

    @KafkaListener(topics = "${spring.kafka.topicName}", groupId = "1", containerFactory = "kafkaListenerContainerFactory")
    void listener(String message) {
        log.info(message);
    }
}
