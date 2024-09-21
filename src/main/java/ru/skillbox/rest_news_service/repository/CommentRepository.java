package ru.skillbox.rest_news_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.rest_news_service.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
