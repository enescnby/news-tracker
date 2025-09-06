package org.enes.newscollector.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
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
        feedUrls.forEach(rssFeedService::fetchAndSend);
    }
}
