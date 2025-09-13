package org.enes.newsapi.exception;

import java.util.List;

public class NewsNotFoundException extends NewsApiException {
    public NewsNotFoundException(String id) {
        super("News not found with id: " + id, "NEWS_NOT_FOUND", 
              List.of("Please verify the news ID", "Check if news exists"));
    }
}
