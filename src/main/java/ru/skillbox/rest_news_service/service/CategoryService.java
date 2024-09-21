package ru.skillbox.rest_news_service.service;

import ru.skillbox.rest_news_service.model.Category;
import ru.skillbox.rest_news_service.web.model.CategoryListResponse;
import ru.skillbox.rest_news_service.web.model.CategoryResponse;
import ru.skillbox.rest_news_service.web.model.UpsertCategoryRequest;

import java.util.List;

public interface CategoryService {
    CategoryListResponse findAll(int page, int size);

    CategoryResponse findById(Long id);

    Category findCategoryById(Long id);

    CategoryResponse save(UpsertCategoryRequest request);

    CategoryResponse update(Long categoryId, UpsertCategoryRequest request);

    void deleteById(Long id);

    void deleteByIdIn(List<Long> ids);
}
