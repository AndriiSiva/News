package ru.skillbox.rest_news_service.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.skillbox.rest_news_service.entity.News;
import ru.skillbox.rest_news_service.web.model.*;


public interface NewsService {
    NewsListResponse findAll(int page, int size);

    NewsResponseWithComments findById(Long id);

    News findNewsById(Long id);

    NewsResponse save(UpsertNewsRequest request, UserDetails userDetails);

    NewsResponse update(Long newsId, UpdateNewsRequest request, UserDetails userDetails);

    void deleteById(Long id);

    NewsListResponse filterBy(NewsFilter filter);
}
