package org.enes.newsprocessor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.enes.newsprocessor.entity.NewsEntity;
import org.enes.newsprocessor.repository.NewsRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NewsConsumerService {

    private final NewsRepository newsRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NewsConsumerService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @KafkaListener(topics = "news-feed", groupId = "news-processor-group")
    public void consume(String message) {
        try {
            NewsEntity news = objectMapper.readValue(message, NewsEntity.class);
            newsRepository.save(news);
            System.out.println("News added: " + news.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
