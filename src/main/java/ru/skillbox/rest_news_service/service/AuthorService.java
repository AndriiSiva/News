package ru.skillbox.rest_news_service.service;

import ru.skillbox.rest_news_service.model.Author;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.web.model.AuthorListResponse;
import ru.skillbox.rest_news_service.web.model.AuthorResponse;
import ru.skillbox.rest_news_service.web.model.CreateAuthorWithNewsRequest;
import ru.skillbox.rest_news_service.web.model.UpsertAuthorRequest;

import java.util.List;

public interface AuthorService {
    AuthorListResponse findAll(int page, int size);

    AuthorResponse findById(Long id);

    Author findAuthorById(Long id);

    AuthorResponse save(UpsertAuthorRequest request);

    AuthorResponse update(UpsertAuthorRequest request, Long authorId);

    void deleteById(Long id);

    AuthorResponse saveWithNews(CreateAuthorWithNewsRequest request);
}
