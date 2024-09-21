package ru.skillbox.rest_news_service.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.web.model.NewsFilter;

public interface NewsSpecification {
    static Specification<News> withFilter(NewsFilter newsFilter) {
        return Specification.where(byAuthorId(newsFilter.getAuthorId()))
                .and(byCategoryId(newsFilter.getCategoryId()));
    }

    static Specification<News> byAuthorId(Long authorId) {
        return (root, query, criteriaBuilder) -> {
            if (authorId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("author").get("id"), authorId);
        };
    }

    static Specification<News> byCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        };
    }
}
