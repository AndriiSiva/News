package ru.skillbox.rest_news_service.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpsertNewsRequest {

    @NotBlank(message = "Текст новости должен быть указан!")
    private String newsText;

    @NotNull(message = "ID категории должно быть указано")
    private Long categoryId;
}