package org.enes.newscollector.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NewsProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topicName;

    public NewsProducerService(KafkaTemplate<String, String> kafkaTemplate,
                               @Value("${spring.kafka.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendNews(String newJson) {
        kafkaTemplate.send(topicName, newJson);
    }
}
