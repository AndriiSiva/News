package ru.skillbox.rest_news_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.rest_news_service.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
