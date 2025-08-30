package org.enes.newsprocessor.entity;

import org.enes.common.model.News;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "news")
public class NewsEntity extends News{
    @Id
    private String id;

    @Override
    @Field(type = FieldType.Text)
    public String getTitle() { return super.getTitle(); }

    @Override
    @Field(type = FieldType.Keyword)
    public String getLink() { return super.getLink();}

    @Override
    @Field(type = FieldType.Text, analyzer = "standard")
    public String getDescription() { return super.getDescription(); }

    @Override
    @Field(type = FieldType.Date)
    public String getPubDate() { return super.getPubDate(); }

    @Override
    @Field(type = FieldType.Keyword)
    public String getSource() { return super.getSource(); }

    @Override
    @Field(type = FieldType.Keyword)
    public String getThumbnailUrl() { return super.getThumbnailUrl(); }

    @Override
    @Field(type = FieldType.Keyword)
    public String getContentUrl() { return super.getContentUrl(); }
}
