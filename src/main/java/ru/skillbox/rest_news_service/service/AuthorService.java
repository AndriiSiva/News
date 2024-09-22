package ru.skillbox.rest_news_service.service;

import ru.skillbox.rest_news_service.entity.Author;
import ru.skillbox.rest_news_service.entity.Role;
import ru.skillbox.rest_news_service.web.model.*;

public interface AuthorService {
    AuthorListResponse findAll(int page, int size);

    AuthorResponse findById(Long id);

    Author findByUsername(String username);

    Author findAuthorById(Long id);

    AuthorResponse create(UpsertAuthorRequest request, Role role);

    AuthorResponse update(UpdateAuthorRequest request, Long authorId, Role from);

    void deleteById(Long id);

    AuthorResponse saveWithNews(UpsertAuthorRequestWithNews request, Role role);
}