package ru.skillbox.rest_news_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NewsFilterValidValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface NewsFilterValid {
    String message() default "Поля пагинации должны быть указаны! Должно быть указано хотя бы одно ID автора или категории или оба";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}