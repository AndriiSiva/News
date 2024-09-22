package ru.skillbox.rest_news_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.rest_news_service.entity.Role;
import ru.skillbox.rest_news_service.exception.EntityNotFoundException;
import ru.skillbox.rest_news_service.mapper.AuthorMapper;
import ru.skillbox.rest_news_service.entity.Author;
import ru.skillbox.rest_news_service.entity.News;
import ru.skillbox.rest_news_service.repository.AuthorRepository;
import ru.skillbox.rest_news_service.repository.NewsRepository;
import ru.skillbox.rest_news_service.service.AuthorService;
import ru.skillbox.rest_news_service.service.CategoryService;
import ru.skillbox.rest_news_service.utils.BeanUtils;
import ru.skillbox.rest_news_service.web.model.*;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final NewsRepository newsRepository;
    private final AuthorMapper authorMapper;
    private final CategoryService categoryService;
    private final PasswordEncoder passwordEncoder;


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
    public Author findByUsername(String name) {
        return authorRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Author not found" + name));
    }

    @Override
    public Author findAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Автор с ID {0} не найден", id)));
    }

    @Override
    public AuthorResponse create(UpsertAuthorRequest request, Role role) {
        Author author = authorMapper.requestToAuthor(request);

        author.setRoles(Collections.singletonList(role));

        author.setPassword(passwordEncoder.encode(author.getPassword()));

        role.setAuthor(author);

        return authorMapper.authorToResponse(authorRepository.save(author));
    }


    @Override
    @Transactional
    public AuthorResponse saveWithNews(UpsertAuthorRequestWithNews request, Role role) {
        // Validate that all news have a categoryId
        boolean hasNullCategoryId = request.getNewsList().stream()
                .map(NewsRequest::getCategoryId)
                .anyMatch(Objects::isNull);

        if (hasNullCategoryId) {
            throw new EntityNotFoundException("categoryId must be filled");
        }

        // Map request to Author entity
        Author author = authorMapper.requestToAuthorWithNews(request);

        // Set role and encode password
        author.setRoles(Collections.singletonList(role));
        author.setPassword(passwordEncoder.encode(author.getPassword()));
        role.setAuthor(author);

        // Prepare news list, but do not save them yet
        List<News> newsList = request.getNewsList().stream()
                .map(newsRequest -> News.builder()
                        .newsText(newsRequest.getNewsText())
                        .category(categoryService.findCategoryById(newsRequest.getCategoryId())) // Set category
                        .author(author)  // Set author directly to avoid nulls
                        .build())
                .collect(Collectors.toList());

        // Add news to author, but avoid cascading operations
        author.setNewsList(newsList);

        // Now save both author and news together after all data is set
        Author savedAuthor = authorRepository.save(author);

        // Return the mapped response with the saved author and news
        return authorMapper.authorToResponse(savedAuthor);
    }


    @Override
    public AuthorResponse update(UpdateAuthorRequest request, Long authorId, Role role) {
        Author updatedAuthor = authorMapper.requestToAuthor(authorId, request);
        Author existedAuthor = findAuthorById(authorId);

        BeanUtils.copyAuthorNotNullProperties(existedAuthor, updatedAuthor);

        updatedAuthor.setPassword(passwordEncoder.encode(request.getPassword()));

        if (role.getAuthority() != null && updatedAuthor.getRoles().stream()
                .map(r -> r.getAuthority().name())
                .noneMatch(r -> r.equals(role.getAuthority().name()))) {
            updatedAuthor.getRoles().add(role);
            role.setAuthor(updatedAuthor);
        }

        authorRepository.save(updatedAuthor);

        return authorMapper.authorToResponse(updatedAuthor);
    }

    @Override
    public void deleteById(Long id) {
        findById(id);
        authorRepository.deleteById(id);
    }
}