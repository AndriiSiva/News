package ru.skillbox.rest_news_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.skillbox.rest_news_service.model.Category;
import ru.skillbox.rest_news_service.web.model.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    Category requestToCategory(UpsertCategoryRequest request);

    @Mapping(source = "categoryId", target = "id")
    Category requestToCategory(Long categoryId, UpsertCategoryRequest request);

    CategoryResponse categoryToResponse(Category category);

    List<CategoryResponse> categoryListToReponseList(List<Category> categories);

    default CategoryListResponse categoryListToCategoryListResponse(Page<Category> categoriesPage) {
        CategoryListResponse response = new CategoryListResponse();
        response.setCategories(categoriesPage.getContent().stream()
                .map(this::categoryToResponse)
                .toList());

        response.setTotalElements(categoriesPage.getTotalElements());
        response.setTotalPages(categoriesPage.getTotalPages());
        response.setCurrentPage(categoriesPage.getNumber());
        response.setPageSize(categoriesPage.getSize());

        return response;
    }
}