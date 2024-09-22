package ru.skillbox.rest_news_service.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.skillbox.rest_news_service.entity.Comment;
import ru.skillbox.rest_news_service.web.model.CommentResponse;
import ru.skillbox.rest_news_service.web.model.UpsertCommentRequest;


public interface CommentService {
    CommentResponse findById(Long id);

    Comment findCommentById(Long id);

    CommentResponse save(UpsertCommentRequest request, UserDetails userDetails);

    CommentResponse update(Long commentId, UpsertCommentRequest request, UserDetails userDetails);

    void deleteById(Long id);
}