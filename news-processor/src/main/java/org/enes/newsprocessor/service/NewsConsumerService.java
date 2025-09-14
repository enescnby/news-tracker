package org.enes.newsprocessor.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.enes.newsprocessor.entity.NewsEntity;
import org.enes.newsprocessor.repository.NewsRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NewsConsumerService {

    private final NewsRepository newsRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NewsConsumerService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @KafkaListener(topics = "news-feed", groupId = "news-processor-group")
    public void consume(String message) {

        log.debug("Received Kafka message: {}", message);

        try {
            NewsEntity news = objectMapper.readValue(message, NewsEntity.class);

            log.debug("Parsed news entity: title='{}', link='{}', source='{}'",
                     news.getTitle(), news.getLink(), news.getSource());

            if(!newsRepository.existsByLink(news.getLink())) {
                newsRepository.save(news);
                
                log.info("News saved successfully: title='{}', source='{}'", news.getTitle(), news.getSource());
            } else {
                log.debug("Duplicate news skipped: title='{}', source='{}'", news.getTitle(), news.getSource());
            }
        } catch (Exception e) {
            log.error("Failed to process Kafka message: error='{}'", e.getMessage());
        }
    }
}
