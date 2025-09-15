package org.enes.newsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record NewsDto(

        @Schema(description = "News ID")
        String id,
        @Schema(description = "News title")
        String title,
        @Schema(description = "News link")
        String link,
        @Schema(description = "News description")
        String description,
        @Schema(description = "News publication date")
        String pubDate,
        @Schema(description = "News source")
        String source,
        @Schema(description = "News thumbnail URL")
        String thumbnailUrl,
        @Schema(description = "News content URL")
        String contentUrl
) {
}
