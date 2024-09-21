package ru.skillbox.rest_news_service.web.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.rest_news_service.validation.NewsFilterValid;

@Data
@NoArgsConstructor
@NewsFilterValid
public class NewsFilter {
    private Integer size;
    private Integer page;

    private Long authorId;
    private Long categoryId;
}
