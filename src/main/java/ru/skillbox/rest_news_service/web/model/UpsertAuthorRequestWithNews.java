package ru.skillbox.rest_news_service.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertAuthorRequestWithNews {

    @NotBlank(message = "Имя автора должно быть заполнено!")
    @Size(min = 3, max = 30, message = "Имя автора не может быть меньше {min} и больше {max}!")
    private String name;


    @NotBlank(message = "Пароль должен быть заполнен!")
    @Size(min = 8, max = 30, message = "Пароль не может быть меньше {min} и больше {max}!")
    private String password;

    @NotNull(message = "Список новостей должен быть заполнен")
    @NotEmpty
    private List<NewsRequest> newsList;
}