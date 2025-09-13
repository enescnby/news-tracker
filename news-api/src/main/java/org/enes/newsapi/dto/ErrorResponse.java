package org.enes.newsapi.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public record ErrorResponse (
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    String path,
    String errorCode,
    List<String> details,
    Map<String, Object> metadata
) {}
