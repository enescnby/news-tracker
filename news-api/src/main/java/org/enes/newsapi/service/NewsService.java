package org.enes.newsapi.service;

import org.enes.newsapi.entity.NewsEntity;
import org.enes.newsapi.repository.NewsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public Page<NewsEntity> findAll(Pageable pageable) {
        log.debug("Finding all news with pagination: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        
        Page<NewsEntity> result = newsRepository.findAll(pageable);

        log.info("Found {} news items out of {} total", result.getNumberOfElements(), result.getTotalElements());

        return result;
    }

    public Optional<NewsEntity> findById(String id) {
        log.debug("Finding news by id: {}", id);

        Optional<NewsEntity> result = newsRepository.findById(id);

        if(result.isPresent()) {
            log.debug("News found with id: {}", id);
        } else {
            log.debug("News not found with id: {}", id);
        }

        return result;
    }

    public Page<NewsEntity> findByTitleContainingWord(String title, Pageable pageable) {
        log.debug("Finding news by title containing word: {} with pagination: page={}, size={}, sort={}",
            title, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<NewsEntity> result = newsRepository.findByTitleWordMatch(title, pageable);

        log.info("Found {} news items matching title '{}' out of {} total", result.getNumberOfElements(), title, result.getTotalElements());

        return result;
    }

    public Page<NewsEntity> findBySources(List<String> sources, Pageable pageable) {
        log.debug("Finding news by sources: {} with pagination: page={}, size={}, sort={}",
                 sources, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        
        Page<NewsEntity> result = newsRepository.findBySourceIn(sources, pageable);

        log.info("Found {} news items matching sources '{}' out of {} total", result.getNumberOfElements(), sources, result.getTotalElements());

        return result;
    }

    public Page<NewsEntity> findByTitleAndSources(String title, List<String> sources, Pageable pageable) {
        log.debug("Finding news by title: {} and sources: {} with pagination: page={}, size={}, sort={}",
                 title, sources, pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        
        Page<NewsEntity> result = newsRepository.findByTitleContainingIgnoreCaseAndSourceIn(title, sources, pageable);

        log.info("Found {} news items matching title '{}' and sources '{}' out of {} total", result.getNumberOfElements(), title, sources, result.getTotalElements());

        return result;
    }
}
