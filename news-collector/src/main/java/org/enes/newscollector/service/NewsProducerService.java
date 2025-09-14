package org.enes.newscollector.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NewsProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topicName;

    public NewsProducerService(KafkaTemplate<String, String> kafkaTemplate,
                               @Value("${spring.kafka.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendNews(String newJson) {
        log.debug("Sending news to Kafka topic: {}", topicName);
        log.trace("News content: {}", newJson);
        
        try {
            kafkaTemplate.send(topicName, newJson);
            log.debug("Successfully sent news to Kafka topic: {}", topicName);
        } catch (Exception e) {
            log.error("Failed to send news to Kafka topic: {}", topicName, e);
            throw e;
        }
    }
}
