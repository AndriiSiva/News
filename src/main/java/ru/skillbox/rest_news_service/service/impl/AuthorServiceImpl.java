package ru.skillbox.rest_news_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.rest_news_service.exception.EntityNotFoundException;
import ru.skillbox.rest_news_service.mapper.AuthorMapper;
import ru.skillbox.rest_news_service.model.Author;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.repository.AuthorRepository;
import ru.skillbox.rest_news_service.repository.CommentRepository;
import ru.skillbox.rest_news_service.repository.NewsRepository;
import ru.skillbox.rest_news_service.service.AuthorService;
import ru.skillbox.rest_news_service.service.CategoryService;
import ru.skillbox.rest_news_service.utils.BeanUtils;
import ru.skillbox.rest_news_service.web.model.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final NewsRepository newsRepository;
    private final AuthorMapper authorMapper;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public AuthorResponse saveWithNews(CreateAuthorWithNewsRequest request) {
        if (request.getNewsList().stream()
                .map(NewsRequest::getCategoryId)
                .anyMatch(Objects::isNull)) {
            throw new EntityNotFoundException("categoryId должно быть заполенено");
        }
        Author author = Author
                .builder()
                .name(request.getName())
                .build();

        List<News> newsList = request.getNewsList().stream().map(newsRequest ->
                News.builder()
                        .newsText(newsRequest.getNewsText())
                        .category(categoryService.findCategoryById(newsRequest.getCategoryId()))
                        .build()).toList();
        Author savedAuthor = authorRepository.save(author);

        for (News news : newsList) {
            news.setAuthor(savedAuthor);
            var savedNews = newsRepository.save(news);
            savedAuthor.addNews(savedNews);
        }
        return authorMapper.authorToResponse(savedAuthor);
    }

    @Override
    public AuthorListResponse findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return authorMapper.authorListToAuthorResponseList(authorRepository.findAll(pageable));
    }

    @Override
    public AuthorResponse findById(Long id) {
        return authorMapper.authorToResponse(authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Автор с ID {0} не найден", id))));
    }

    @Override
    public Author findAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Автор с ID {0} не найден", id)));
    }

    @Override
    public AuthorResponse save(UpsertAuthorRequest request) {
        Author author = authorMapper.requestToAuthor(request);
        return authorMapper.authorToResponse(authorRepository.save(author));
    }

    @Override
    public AuthorResponse update(UpsertAuthorRequest request, Long authorId) {
        Author updatedAuthor = authorMapper.requestToAuthor(authorId, request);
        Author existedAuthor = findAuthorById(authorId);

        BeanUtils.copyAuthorNotNullProperties(updatedAuthor, existedAuthor);

        authorRepository.save(existedAuthor);

        return authorMapper.authorToResponse(updatedAuthor);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        authorRepository.deleteById(id);
    }
}