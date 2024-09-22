package ru.skillbox.rest_news_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skillbox.rest_news_service.aop.CheckOwnership;
import ru.skillbox.rest_news_service.exception.EntityNotFoundException;
import ru.skillbox.rest_news_service.mapper.NewsMapper;
import ru.skillbox.rest_news_service.entity.Author;
import ru.skillbox.rest_news_service.entity.News;
import ru.skillbox.rest_news_service.repository.NewsRepository;
import ru.skillbox.rest_news_service.repository.NewsSpecification;
import ru.skillbox.rest_news_service.service.AuthorService;
import ru.skillbox.rest_news_service.service.CategoryService;
import ru.skillbox.rest_news_service.service.NewsService;
import ru.skillbox.rest_news_service.utils.BeanUtils;
import ru.skillbox.rest_news_service.web.model.*;

import java.text.MessageFormat;


@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;
    private final NewsMapper newsMapper;

    @Override
    public NewsListResponse findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsMapper.newsListToNewsListResponse(newsRepository.findAll(pageable));
    }

    @Override
    public NewsResponseWithComments findById(Long id) {
        return newsMapper.newsToResponseWithComments(newsRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Новость с ID {0} не найдена", id))));
    }

    @Override
    public News findNewsById(Long id) {
        return newsRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Новость с ID {0} не найдена", id)));
    }

    @Override
    public NewsResponse save(UpsertNewsRequest request, UserDetails userDetails) {
        News news = newsMapper.requestToNews(request, categoryService, authorService, userDetails);
        return newsMapper.newsToResponse(newsRepository.save(news));
    }

    @Override
    public NewsResponse update(Long newsId, UpdateNewsRequest request, UserDetails userDetails) {

        News exictedNews = findNewsById(newsId);
        if (request.getCategoryId() == null) {
            request.setCategoryId(exictedNews.getCategory().getId());
        }
        if (request.getNewsText() == null) {
            request.setNewsText(exictedNews.getNewsText());
        }

        News updatedNews = newsMapper.requestToNews(newsId, request, categoryService, authorService, userDetails);
        Author author = authorService.findAuthorById(updatedNews.getAuthor().getId());

        BeanUtils.copyNewsNonNullProperties(updatedNews, exictedNews);
        exictedNews.setAuthor(author);
        return newsMapper.newsToResponse(newsRepository.save(exictedNews));
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        newsRepository.deleteById(id);
    }

    @Override
    public NewsListResponse filterBy(NewsFilter filter) {
        return newsMapper.newsListToNewsListResponse(newsRepository.findAll(NewsSpecification.withFilter(filter), PageRequest.of(
                filter.getPage(), filter.getSize())));
    }
}





