package org.enes.newsapi.repository;

import org.enes.newsapi.entity.NewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NewsRepository extends ElasticsearchRepository<NewsEntity, String> {
    Page<NewsEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<NewsEntity> findBySourceIgnoreCase(String source, Pageable pageable);
    Page<NewsEntity> findBySourceIn(List<String> sources, Pageable pageable);
    Page<NewsEntity> findByTitleContainingIgnoreCaseAndSourceIn(String title, List<String> sources, Pageable pageable);

    @Query("{\"match\": {\"title\": {\"query\": \"?0\", \"operator\": \"and\"}}}")
    Page<NewsEntity> findByTitleWordMatch(String title, Pageable pageable);
}
