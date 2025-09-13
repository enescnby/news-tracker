package org.enes.newsapi.exception;

public class NewsNotFoundException extends RuntimeException{
    public NewsNotFoundException(String id) {
        super("News not found with id: " + id);
    }
}
