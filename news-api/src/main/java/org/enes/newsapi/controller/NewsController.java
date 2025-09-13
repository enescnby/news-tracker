package org.enes.newsapi.controller;

import org.enes.newsapi.annotation.ValidateRequestParams;
import org.enes.newsapi.dto.NewsDto;
import org.enes.newsapi.entity.NewsEntity;
import org.enes.newsapi.exception.NewsNotFoundException;
import org.enes.newsapi.service.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import org.enes.common.util.PaginationUtils;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    @ValidateRequestParams(allowed = {"page", "size", "sortBy", "sortDir"})
    public ResponseEntity<Page<NewsDto>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "pubDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = createSort(sortBy, sortDir);

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<NewsEntity> newsPage = newsService.findAll(pageable);
        Page<NewsDto> dtoPage = newsPage.map(this::toDto);

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> getNewsById(@PathVariable String id) {
        NewsEntity entity = newsService.findById(id)
                .orElseThrow(() -> new NewsNotFoundException(id));

        return ResponseEntity.ok(this.toDto(entity));
    }

    @GetMapping("/search")
    @ValidateRequestParams(allowed = {"title", "sources", "page", "size", "sortBy", "sortDir"})
    public ResponseEntity<Page<NewsDto>> getNewsByTitleContaining(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<String> sources,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "pubDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<NewsEntity> result;

        if (title != null && sources != null && !sources.isEmpty()){
            result = newsService.findByTitleAndSources(title, sources, pageable);
        } else if (title != null) {
            result = newsService.findByTitleContainingWord(title, pageable);
        } else if (sources != null && !sources.isEmpty()){
            result = newsService.findBySources(sources, pageable);
        } else {
            result = newsService.findAll(pageable);
        }

        Page<NewsDto> dtoPage = result.map(this::toDto);

        return ResponseEntity.ok(dtoPage);
    }

    private Sort createSort(String sortBy, String sortDir) {
        return PaginationUtils.isAscending(sortDir)
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
    }

    private NewsDto toDto(NewsEntity entity) {
        return new NewsDto(
                entity.getId(),
                entity.getTitle(),
                entity.getLink(),
                entity.getDescription(),
                entity.getPubDate(),
                entity.getSource(),
                entity.getThumbnailUrl(),
                entity.getContentUrl()
        );
    }
}
