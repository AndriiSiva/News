package ru.skillbox.rest_news_service.web.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpsertCategoryRequest {

    @NotBlank(message = "Название категории должно быть указано!")
    private String name;
}
