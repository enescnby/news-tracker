package org.enes.newsapi.util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.enes.newsapi.dto.ErrorResponse;
import org.enes.newsapi.exception.NewsApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponseBuilder {

    public ErrorResponse buildErrorResponse(
        HttpStatus status,
        String error,
        String message,
        String path,
        String errorCode,
        List<String> details
    ) {
        return new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            error,
            message,
            path,
            errorCode,
            details,
            Map.of("version", "1.0.0", "timestamp", System.currentTimeMillis())
        );
    }

    public ErrorResponse buildFromException(
        NewsApiException ex,
        String path
    ) {
        return new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            ex.getMessage(),
            path,
            ex.getErrorCode(),
            ex.getDetails(),
            Map.of("version", "1.0.0", "timestamp", System.currentTimeMillis())
        );
    }

}
