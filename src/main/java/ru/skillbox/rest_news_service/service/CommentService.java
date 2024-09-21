package ru.skillbox.rest_news_service.service;

import ru.skillbox.rest_news_service.model.Comment;
import ru.skillbox.rest_news_service.web.model.CommentResponse;
import ru.skillbox.rest_news_service.web.model.UpsertCommentRequest;


public interface CommentService {
    CommentResponse findById(Long id);

    Comment findCommentById(Long id);

    CommentResponse save(UpsertCommentRequest request);

    CommentResponse update(Long commentId, UpsertCommentRequest request);

    void deleteById(Long id);
}