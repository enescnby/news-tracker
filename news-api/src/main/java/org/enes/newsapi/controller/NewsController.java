package org.enes.newsapi.controller;

import org.enes.newsapi.entity.NewsEntity;
import org.enes.newsapi.repository.NewsRepository;
import org.enes.newsapi.service.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService, NewsRepository newsRepository) {
        this.newsService = newsService;
    }

    @GetMapping
    public ResponseEntity<Page<NewsEntity>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "pubDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<NewsEntity> newsPage = newsService.findAll(pageable);

        return ResponseEntity.ok(newsPage);
    }

    @GetMapping("/{id}")
    public Optional<NewsEntity> getNewsById(@PathVariable String id) {
        return newsService.findById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<NewsEntity>> getNewsByTitleContaining(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NewsEntity> pageResult = newsService.findByTitleContaining(title, pageable);
        return ResponseEntity.ok(pageResult.getContent());
    }
}
