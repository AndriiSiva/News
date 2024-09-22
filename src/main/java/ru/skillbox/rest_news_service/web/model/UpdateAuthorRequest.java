package ru.skillbox.rest_news_service.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAuthorRequest {

    @Size(min = 3, max = 30, message = "Имя автора не может быть меньше {min} и больше {max}!")
    private String name;

    @NotBlank(message = "Пароль должен быть заполнен!")
    @Size(min = 8, max = 30, message = "Пароль не может быть меньше {min} и больше {max}!")
    private String password;
}
