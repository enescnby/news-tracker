package org.enes.newscollector.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Component
@Slf4j
public class RssScheduler {

    private final RssFeedService rssFeedService;

    @Value("${rss.fetch-interval}")
    private long interval;

    private final Map<String, String> feedUrls = Map.of(
            "https://rss.nytimes.com/services/xml/rss/nyt/Technology.xml", "nytimes",
            "https://www.theverge.com/rss/index.xml", "theverge",
            "https://feeds.bbci.co.uk/news/technology/rss.xml", "bbcnews"
    );

    public RssScheduler(RssFeedService rssFeedService) {
        this.rssFeedService = rssFeedService;
    }

    @Scheduled(fixedDelayString = "${rss.fetch-interval}")
    public void fetchFeeds() {
        log.info("Starting scheduled RSS feed fetch - interval: {}ms, feeds count: {}", interval, feedUrls.size());
        
        long startTime = System.currentTimeMillis();
        int successCount = 0;
        int errorCount = 0;
        
        for (Map.Entry<String, String> entry : feedUrls.entrySet()) {
            try {
                log.debug("Fetching RSS feed: {} -> {}", entry.getValue(), entry.getKey());
                rssFeedService.fetchAndSend(entry.getKey(), entry.getValue());
                successCount++;
            } catch (Exception e) {
                log.error("Failed to fetch RSS feed: {} -> {}", entry.getValue(), entry.getKey(), e);
                errorCount++;
            }
        }
        
        long duration = System.currentTimeMillis() - startTime;
        log.info("Scheduled RSS feed fetch completed - duration: {}ms, success: {}, errors: {}", 
                 duration, successCount, errorCount);
    }
}
