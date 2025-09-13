package org.enes.newsapi.dto;

public record NewsDto(
        String id,
        String title,
        String link,
        String description,
        String pubDate,
        String source,
        String thumbnailUrl,
        String contentUrl
) {
}
