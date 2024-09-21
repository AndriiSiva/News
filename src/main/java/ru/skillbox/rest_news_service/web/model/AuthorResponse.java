package ru.skillbox.rest_news_service.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorResponse {
    private long id;
    private String name;
    private Long newsList;
    private Long comments;
}
