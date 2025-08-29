package org.enes.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class News {
    private String title;
    private String link;
    private String description;
    private String pubDate;
    private String source;
    private String thumbnailUrl;
    private String contentUrl;
}
