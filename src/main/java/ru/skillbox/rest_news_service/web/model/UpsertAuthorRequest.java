package ru.skillbox.rest_news_service.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.rest_news_service.model.Comment;
import ru.skillbox.rest_news_service.model.News;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpsertAuthorRequest {
    @NotBlank(message = "Имя автора должно быть заполнено!")
    @Size(min = 3, max = 30, message = "Имя автора не может быть меньше {min} и больше {max}!")
    private String name;

    private List<News> newsList;

    private List<Comment> comments;

}
