package ru.skillbox.rest_news_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.skillbox.rest_news_service.aop.CheckOwnership;
import ru.skillbox.rest_news_service.exception.EntityNotFoundException;
import ru.skillbox.rest_news_service.mapper.CommentMapper;
import ru.skillbox.rest_news_service.mapper.NewsMapper;
import ru.skillbox.rest_news_service.entity.Author;
import ru.skillbox.rest_news_service.entity.Comment;
import ru.skillbox.rest_news_service.entity.News;
import ru.skillbox.rest_news_service.repository.AuthorRepository;
import ru.skillbox.rest_news_service.repository.CommentRepository;
import ru.skillbox.rest_news_service.repository.NewsRepository;
import ru.skillbox.rest_news_service.service.CommentService;
import ru.skillbox.rest_news_service.service.NewsService;
import ru.skillbox.rest_news_service.utils.BeanUtils;
import ru.skillbox.rest_news_service.web.model.CommentResponse;
import ru.skillbox.rest_news_service.web.model.UpsertCommentRequest;
import ru.skillbox.rest_news_service.web.model.UpsertNewsRequest;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final NewsService newsService;
    private final AuthorRepository authorRepository;
    private final CommentMapper commentMapper;
    private final NewsMapper newsMapper;
    private final NewsRepository newsRepository;

//    @Override
//    public List<Comment> findAll() {
//        return commentRepository.findAll();

//    }

    @Override
    public CommentResponse findById(Long id) {
        return commentMapper.commentToResponse(commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Комментарий с ID {0} не найден", id))));
    }

    @Override
    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Комментарий с ID {0} не найден", id)));
    }

    @Override
    public CommentResponse save(UpsertCommentRequest request, UserDetails userDetails) {

        Author author = authorRepository.findByName(userDetails.getUsername()).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Автор с именем {0} не найден", userDetails.getUsername())));

        News news = newsService.findNewsById(request.getNewsId());

        Comment comment = commentMapper.requestToComment(request);

        news.setComment(comment);
        comment.setNews(news);
        comment.setAuthor(author);
        return commentMapper.commentToResponse(commentRepository.save(comment));
    }

    @Override
    public CommentResponse update(Long commentId, UpsertCommentRequest request, UserDetails userDetails) {

        // Найдем существующий комментарий по commentId
        Comment exictedComment = findCommentById(commentId);

        if (request.getCommentText() == null) {
            request.setCommentText(exictedComment.getCommentText());
        }
        if (request.getNewsId() == null) {
            request.setNewsId(exictedComment.getNews().getId());
        }


        // Обновим поля комментария на основе запроса
        Comment comment = commentMapper.requestToComment(commentId, request);
        BeanUtils.copyCommentNonNullProperties(comment, exictedComment);

        // Найдем автора комментария по ID
        Author author = authorRepository.findByName(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Автор с именем {0} не найден", userDetails.getUsername())));

        exictedComment.setAuthor(author);


        News news = newsService.findNewsById(request.getNewsId());
        exictedComment.setNews(news);

        return commentMapper.commentToResponse(commentRepository.save(exictedComment));
    }


    @Override
    public void deleteById(Long id) {
        findById(id);
        commentRepository.deleteById(id);
    }
}
