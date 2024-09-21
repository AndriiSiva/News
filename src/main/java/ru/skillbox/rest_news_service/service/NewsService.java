package ru.skillbox.rest_news_service.service;

import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.web.model.*;


public interface NewsService {
    NewsListResponse findAll(int page, int size);

    NewsResponseWithComments findById(Long id);

    News findNewsById(Long id);

    NewsResponse save(UpsertNewsRequest request);

    NewsResponse update(Long newsId, UpsertNewsRequest request);

    void deleteById(Long id);

    NewsListResponse filterBy(NewsFilter filter);
}
