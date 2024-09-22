package ru.skillbox.rest_news_service.web.model;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateNewsRequest {

    private String newsText;

    private Long categoryId;
}