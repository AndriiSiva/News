package ru.skillbox.rest_news_service.web.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.rest_news_service.service.NewsService;
import ru.skillbox.rest_news_service.web.model.*;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping("/filter")
    public ResponseEntity<NewsListResponse> filterBy(@Valid NewsFilter filter) {
        return ResponseEntity.ok(newsService.filterBy(filter));
    }

    @GetMapping
    public ResponseEntity<NewsListResponse> findAll(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(newsService.findAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponseWithComments> findById(@PathVariable Long id) {

        return ResponseEntity.ok(newsService.findById(id));
    }

    @PostMapping
    public ResponseEntity<NewsResponse> create(@RequestBody @Valid UpsertNewsRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(newsService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsResponse> update(@PathVariable("id") Long newsId,@RequestParam Long authorId,
                                               @RequestBody @Valid UpsertNewsRequest request) {

        return ResponseEntity.ok(newsService.update(newsId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestParam Long authorId) {
        newsService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}