package ru.skillbox.rest_news_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.rest_news_service.model.Author;


public interface AuthorRepository extends JpaRepository<Author, Long> {

    //@Override
    //@EntityGraph(attributePaths = {"newsList", "comments"})
    //Page<Author> findAll(Pageable pageable);
}
