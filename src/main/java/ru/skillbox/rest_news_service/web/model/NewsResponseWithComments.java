package ru.skillbox.rest_news_service.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponseWithComments {
    private Long id;
    private String newsText;
    private Long authorId;
    private Long categoryId;
    private List<CommentResponse> comments;
}