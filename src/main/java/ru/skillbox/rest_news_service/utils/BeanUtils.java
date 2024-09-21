package ru.skillbox.rest_news_service.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import ru.skillbox.rest_news_service.model.Author;
import ru.skillbox.rest_news_service.model.Category;
import ru.skillbox.rest_news_service.model.Comment;
import ru.skillbox.rest_news_service.model.News;

import java.lang.reflect.Field;

@UtilityClass
public class BeanUtils {


    public void copyAuthorNotNullProperties(Author source, Author destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination must not be null");
        }

        if (source.getId() != null) {
            destination.setId(source.getId());
        }

        if (source.getName() != null) {
            destination.setName(source.getName());
        }

        if (source.getNewsList() != null && !source.getNewsList().isEmpty()) {
            destination.setNewsList(source.getNewsList());
        }

        if (source.getComments() != null && !source.getComments().isEmpty()) {
            destination.setComments(source.getComments());
        }
    }

    public static void copyCategoryNotNullProperties(Category source, Category destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination must not be null");
        }

        if (source.getId() != null) {
            destination.setId(source.getId());
        }

        if (source.getName() != null) {
            destination.setName(source.getName());
        }
    }

    public static void copyCommentNonNullProperties(Comment source, Comment destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination must not be null");
        }

        if (source.getId() != null) {
            destination.setId(source.getId());
        }

        if (source.getCommentText() != null) {
            destination.setCommentText(source.getCommentText());
        }

        if (source.getAuthor() != null) {
            destination.setAuthor(source.getAuthor());
        }

        if (source.getNews() != null) {
            destination.setNews(source.getNews());
        }
    }

    public static void copyNewsNonNullProperties(News source, News destination) {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Source and destination must not be null");
        }

        if (source.getId() != null) {
            destination.setId(source.getId());
        }

        if (source.getNewsText() != null) {
            destination.setNewsText(source.getNewsText());
        }

        if (source.getAuthor() != null) {
            destination.setAuthor(source.getAuthor());
        }


        if (source.getCategory() != null) {
            destination.setCategory(source.getCategory());
        }

        if (source.getComments() != null && !source.getComments().isEmpty()) {
            destination.setComments(source.getComments());
        }
    }

    //Для примера через рефлексию
    @SneakyThrows
    public void copyNonNullProperties(Object source, Object destination) {
        Class<?> sourceClass = source.getClass();
        Class<?> destinationClass = destination.getClass();

        // Проверяем, что классы совпадают
        if (!sourceClass.equals(destinationClass)) {
            throw new IllegalArgumentException("Source and destination must be of the same class");
        }

        Field[] fields = sourceClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Делаем поле доступным для рефлексии
            Object value = field.get(source);
            if (value != null) {
                // Устанавливаем значение в поле назначения, если оно не null
                Field destinationField = destinationClass.getDeclaredField(field.getName());
                destinationField.setAccessible(true);
                destinationField.set(destination, value);
            }
        }
    }
}