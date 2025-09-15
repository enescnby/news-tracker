package org.enes.newsapi.controller;

import org.enes.newsapi.annotation.ValidateRequestParams;
import org.enes.newsapi.dto.NewsDto;
import org.enes.newsapi.entity.NewsEntity;
import org.enes.newsapi.exception.InvalidPageSizeException;
import org.enes.newsapi.exception.InvalidSortDirectionException;
import org.enes.newsapi.exception.InvalidSortFieldException;
import org.enes.newsapi.exception.NewsNotFoundException;
import org.enes.newsapi.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.enes.newsapi.dto.ErrorResponse;

import java.util.List;
import org.enes.common.util.PaginationUtils;

@RestController
@RequestMapping("/api/news")
@Slf4j
@Tag(name = "News")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Operation(summary = "Get all news", description = "Get all news with pagination and sorting")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved news"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters (ErrorResponse)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error (ErrorResponse)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    @ValidateRequestParams(allowed = {"page", "size", "sortBy", "sortDir"})
    public ResponseEntity<Page<NewsDto>> getAllNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "pubDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        log.info("GET /api/news - page={}, size={}, sortBy={}, sortDir={}", page, size, sortBy, sortDir);

        validatePageSize(size);
        validateSortDirection(sortDir);
        validateSortField(sortBy);

        Sort sort = createSort(sortBy, sortDir);

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<NewsEntity> newsPage = newsService.findAll(pageable);
        Page<NewsDto> dtoPage = newsPage.map(this::toDto);

        log.info("GET /api/news completed - returned {} items out of {} total", 
                 dtoPage.getNumberOfElements(), dtoPage.getTotalElements());

        return ResponseEntity.ok(dtoPage);
    }

    @Operation(summary = "Get news by id", description = "Get news by id if it exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved news"),
        @ApiResponse(responseCode = "404", description = "News not found (ErrorResponse)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error (ErrorResponse)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> getNewsById(@PathVariable String id) {
        log.info("GET /api/news/{} - retrieving news by id", id);
        
        NewsEntity entity = newsService.findById(id)
                .orElseThrow(() -> new NewsNotFoundException(id));

        log.info("GET /api/news/{} completed - found news: title='{}'", id, entity.getTitle());
        return ResponseEntity.ok(this.toDto(entity));
    }

    @Operation(summary = "Get news by filtering", description = "Get news by filtering about title or sources. Returns all news if no filters are provided.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved news"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters (ErrorResponse)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error (ErrorResponse)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Parameters(value = {
        @Parameter(name = "title", description = "News title (optional)"),
        @Parameter(name = "sources", description = "News sources (optional)"),
        @Parameter(name = "page", description = "Page number (0-indexed) (optional)"),
        @Parameter(name = "size", description = "Page size (1-100) (optional)"),
        @Parameter(name = "sortBy", description = "Sort by field (pubDate or source) (optional)"),
        @Parameter(name = "sortDir", description = "Sort direction (asc or desc) (optional)"),
    })
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
        log.info("GET /api/news/search - title='{}', sources={}, page={}, size={}, sortBy={}, sortDir={}", 
                 title, sources, page, size, sortBy, sortDir);

        validatePageSize(size);
        validateSortDirection(sortDir);
        validateSortField(sortBy);
        
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<NewsEntity> result;

        if (title != null && sources != null && !sources.isEmpty()){
            log.debug("Searching by title and sources");
            result = newsService.findByTitleAndSources(title, sources, pageable);
        } else if (title != null) {
            log.debug("Searching by title only");
            result = newsService.findByTitleContainingWord(title, pageable);
        } else if (sources != null && !sources.isEmpty()){
            log.debug("Searching by sources only");
            result = newsService.findBySources(sources, pageable);
        } else {
            log.debug("No search criteria provided, returning all news");
            result = newsService.findAll(pageable);
        }

        Page<NewsDto> dtoPage = result.map(this::toDto);

        log.info("GET /api/news/search completed - returned {} items out of {} total", 
                 dtoPage.getNumberOfElements(), dtoPage.getTotalElements());

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

    private void validatePageSize(int size) {
        if (size < 1 || size > 100) {
            throw new InvalidPageSizeException(size);
        }
    }

    private void validateSortField(String sortBy) {
        if (!sortBy.equalsIgnoreCase("pubDate") && !sortBy.equalsIgnoreCase("source")) {
            throw new InvalidSortFieldException(sortBy);
        }
    }

    private void validateSortDirection(String sortDir) {
        if (!sortDir.equalsIgnoreCase("asc") && !sortDir.equalsIgnoreCase("desc")) {
            throw new InvalidSortDirectionException(sortDir);
        }
    }
}
