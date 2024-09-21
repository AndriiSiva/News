package ru.skillbox.rest_news_service.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsResponse {
    private Long id;
    private String newsText;
    private Long authorId;
    private Long categoryId;
    private Long comments;
}