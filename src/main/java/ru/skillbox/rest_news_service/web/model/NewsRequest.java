package ru.skillbox.rest_news_service.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsRequest {

    @NotBlank(message = "Текст новости должен быть указан!")
    private String newsText;

    @NotNull(message = "ID категории должно быть указано")
    private Long categoryId;
}