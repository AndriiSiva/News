package ru.skillbox.rest_news_service.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpsertCommentRequest {
    @NotNull(message = "ID автора должно быть указано")
    @Positive(message = "ID автора должно быть больше 0!")
    private Long authorId;

    @NotBlank(message = "Текст комментария должен быть указан!")
    private String commentText;

    @NotNull(message = "ID новости должно быть указано")
    @Positive(message = "ID новости должно быть больше 0!")
    private Long newsId;
}
