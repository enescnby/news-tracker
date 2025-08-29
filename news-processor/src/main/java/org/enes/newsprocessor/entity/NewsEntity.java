package org.enes.newsprocessor.entity;

import org.enes.common.model.News;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "news")
public class NewsEntity extends News{
    @Id
    private String id;
}
