package org.enes.newsapi.service;

import org.enes.newsapi.entity.NewsEntity;
import org.enes.newsapi.repository.NewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public Page<NewsEntity> findAll(Pageable pageable) {
        return newsRepository.findAll(pageable);
    }

    public Optional<NewsEntity> findById(String id) {
        return newsRepository.findById(id);
    }

    public Page<NewsEntity> findByTitleContaining(String title, Pageable pageable) {
        return newsRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Page<NewsEntity> findBySources(List<String> sources, Pageable pageable) {
        return newsRepository.findBySourceIn(sources, pageable);
    }

    public Page<NewsEntity> findByTitleAndSources(String title, List<String> sources, Pageable pageable) {
        return newsRepository.findByTitleContainingIgnoreCaseAndSourceIn(title, sources, pageable);
    }
}
