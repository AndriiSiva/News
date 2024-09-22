package ru.skillbox.rest_news_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.rest_news_service.exception.EntityNotFoundException;
import ru.skillbox.rest_news_service.mapper.CategoryMapper;
import ru.skillbox.rest_news_service.entity.Category;
import ru.skillbox.rest_news_service.repository.CategoryRepository;
import ru.skillbox.rest_news_service.service.CategoryService;
import ru.skillbox.rest_news_service.utils.BeanUtils;
import ru.skillbox.rest_news_service.web.model.CategoryListResponse;
import ru.skillbox.rest_news_service.web.model.CategoryResponse;
import ru.skillbox.rest_news_service.web.model.UpsertCategoryRequest;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryListResponse findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryMapper.categoryListToCategoryListResponse(categoryRepository.findAll(pageable));
    }

    @Override
    public CategoryResponse findById(Long id) {
        return categoryMapper.categoryToResponse(categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Категория с ID {0} не найдена", id))));
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Категория с ID {0} не найдена", id)));
    }

    @Override
    public CategoryResponse save(UpsertCategoryRequest request) {
        Category category = categoryMapper.requestToCategory(request);
        return categoryMapper.categoryToResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(Long categoryId, UpsertCategoryRequest request) {

        Category category = categoryMapper.requestToCategory(categoryId, request);

        Category existedCategory = findCategoryById(categoryId);
        BeanUtils.copyCategoryNotNullProperties(category, existedCategory);
        return categoryMapper.categoryToResponse(categoryRepository.save(existedCategory));
    }

    @Override
    public void deleteById(Long id) {
        findCategoryById(id);
        categoryRepository.deleteById(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        categoryRepository.deleteAllById(ids);
    }
}
