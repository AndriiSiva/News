package ru.skillbox.rest_news_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skillbox.rest_news_service.model.Author;
import ru.skillbox.rest_news_service.model.Category;
import ru.skillbox.rest_news_service.model.Comment;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.web.model.AuthorResponse;
import ru.skillbox.rest_news_service.web.model.NewsResponse;

import java.time.Instant;
import java.util.ArrayList;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AbstractTestController {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    protected Author createAuthor(Long id, News news, Comment comment) {
        Author author = new Author(id,
                "Author " + id, new ArrayList<>(), new ArrayList<>());
        if (news != null) {
            news.setAuthor(author);
            author.addNews(news);
        }
        if (comment != null) {
            comment.setAuthor(author);
            author.addComment(comment);
        }
        return author;
    }

    protected News createNews(Long id, String newsText, Author author, Category category) {
        return new News(id, newsText, author, new ArrayList<>(),category, Instant.now(), Instant.now());
    }

    protected Comment createComment(Long id, String commentText, Author author) {
        return new Comment(id, commentText, author, null, Instant.now(), Instant.now());
    }

    protected AuthorResponse createAuthorResponse(Long id, NewsResponse newsResponse) {
        AuthorResponse authorResponse = new AuthorResponse(id, "Author " + id, 2L,2L);
        return authorResponse;
    }

    protected NewsResponse createNewsResponse(Long id, String newsText) {
        return new NewsResponse(id, newsText,null, null,null);
    }
}
