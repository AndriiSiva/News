package ru.skillbox.rest_news_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.object.UpdatableSqlQuery;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skillbox.rest_news_service.entity.Comment;
import ru.skillbox.rest_news_service.entity.News;
import ru.skillbox.rest_news_service.service.AuthorService;
import ru.skillbox.rest_news_service.service.CategoryService;
import ru.skillbox.rest_news_service.web.model.*;

import java.util.List;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = CommentMapper.class)
public interface NewsMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "category.id", target = "categoryId")
    NewsResponse newsToResponse(News news);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "comments", target = "comments")
    NewsResponseWithComments newsToResponseWithComments(News news);

    List<NewsResponse> newsListToReponseList(List<News> newsList);

    default NewsListResponse newsListToNewsListResponse(Page<News> newsPage) {
        NewsListResponse response = new NewsListResponse();
        response.setNews(newsPage.getContent().stream()
                .map(this::newsToResponse)
                .toList());

        response.setTotalElements(newsPage.getTotalElements());
        response.setTotalPages(newsPage.getTotalPages());
        response.setCurrentPage(newsPage.getNumber());
        response.setPageSize(newsPage.getSize());
        return response;
    }

    default Long countComments(List<Comment> comments) {
        return (long) comments.size();
    }


    default News requestToNews(UpsertNewsRequest request, CategoryService categoryService,
                               AuthorService authorService, UserDetails userDetails) {
        News news = new News();
        news.setNewsText(request.getNewsText());
        news.setCategory(categoryService.findCategoryById(request.getCategoryId()));
        news.setAuthor(authorService.findByUsername(userDetails.getUsername()));
        return news;
    }

    default News requestToNews(Long orderId, UpdateNewsRequest request, CategoryService categoryService,
                               AuthorService authorService, UserDetails userDetails) {
        UpsertNewsRequest request1 = new UpsertNewsRequest();
        request1.setNewsText(request.getNewsText());
        request1.setCategoryId(request.getCategoryId());
        News news = requestToNews(request1, categoryService, authorService, userDetails);
        news.setId(orderId);
        return news;
    }
}