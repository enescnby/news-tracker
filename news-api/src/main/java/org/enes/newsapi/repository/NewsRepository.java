package org.enes.newsapi.repository;

import org.enes.newsapi.entity.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends ElasticsearchRepository<NewsEntity, String> {
    Page<NewsEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
