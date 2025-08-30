package org.enes.newsprocessor.repository;

import org.enes.newsprocessor.entity.NewsEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends ElasticsearchRepository<NewsEntity, String> {
    boolean existsByLink(String link);
}
