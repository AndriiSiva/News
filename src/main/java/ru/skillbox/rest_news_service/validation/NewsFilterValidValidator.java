package ru.skillbox.rest_news_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ObjectUtils;
import ru.skillbox.rest_news_service.web.model.NewsFilter;

public class NewsFilterValidValidator implements ConstraintValidator<NewsFilterValid, NewsFilter> {
    @Override
    public boolean isValid(NewsFilter value, ConstraintValidatorContext context) {
        if (ObjectUtils.anyNull(value.getPage(), value.getSize())) {
            return false;
        }

        return !ObjectUtils.allNull(value.getAuthorId(), value.getCategoryId());
    }
}
