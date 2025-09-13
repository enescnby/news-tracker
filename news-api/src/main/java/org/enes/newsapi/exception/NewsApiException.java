package org.enes.newsapi.exception;

import java.util.List;

public class NewsApiException extends RuntimeException {
    private final String errorCode;
    private final List<String> details;

    protected NewsApiException(
        String message,
        String errorCode,
        List<String> details
    ) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public String getErrorCode() { return errorCode; }
    public List<String> getDetails() { return details; }
}
