package ru.skillbox.rest_news_service.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import ru.skillbox.rest_news_service.exception.AccessDeniedException;
import ru.skillbox.rest_news_service.model.Comment;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.service.CommentService;
import ru.skillbox.rest_news_service.service.NewsService;

import java.util.Map;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CheckOwnershipAspect {

    private final NewsService newsService;
    private final CommentService commentService;

    @Before("@annotation(ru.skillbox.rest_news_service.aop.CheckOwnership)")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Before execution of: {}", joinPoint.getSignature().getName());
    }

    @After("@annotation(ru.skillbox.rest_news_service.aop.CheckOwnership)")
    public void logAfter(JoinPoint joinPoint) {
        log.info("After execution of: {}", joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "@annotation(ru.skillbox.rest_news_service.aop.CheckOwnership)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("After returning from: {} with result: {}", joinPoint.getSignature().getName(), result);
    }

    @AfterThrowing(pointcut = "@annotation(ru.skillbox.rest_news_service.aop.CheckOwnership)", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Exception exception) {
        log.info("After throwing from: {} with exception: ", joinPoint.getSignature().getName(), exception);
    }

    @Around("@annotation(ru.skillbox.rest_news_service.aop.CheckOwnership)")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String methodName = proceedingJoinPoint.getSignature().getName();
        log.info("Начало выполнения метода: {}", methodName);

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String requestPath = request.getRequestURI();  // Получаем путь запроса
            log.info("Путь запроса: {}", requestPath);

            Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            String targetIdStr = pathVariables.get("id");
            log.info("ID объекта: {}", targetIdStr);

            String authorIdStr = request.getParameter("authorId");
            log.info("ID автора: {}", authorIdStr);

            Long targetId = Long.valueOf(targetIdStr);
            Long authorId = Long.valueOf(authorIdStr);

            if (requestPath.contains("/comment")) {
                log.info("Вызов commentService для обработки запроса");
                Comment comment = commentService.findCommentById(targetId);
                Long commentAuthorId = comment.getAuthor().getId();

                if (commentAuthorId.equals(authorId)) {
                    log.info("Пользователь {} успешно прошел проверку владения для комментария {}", authorId, targetId);
                    Object result = proceedingJoinPoint.proceed();
                    log.info("Завершение выполнения метода: {}", methodName);
                    return result;
                } else {
                    log.warn("Пользователь {} не прошел проверку владения для комментария {}", authorId, targetId);
                    throw new AccessDeniedException("Вы не являетесь владельцем данного комментария.");
                }
            } else if (requestPath.contains("/news")) {
                log.info("Вызов newsService для обработки запроса");
                News news = newsService.findNewsById(targetId);
                Long newsAuthorId = news.getAuthor().getId();

                if (newsAuthorId.equals(authorId)) {
                    log.info("Пользователь {} успешно прошел проверку владения для новости {}", authorId, targetId);
                    Object result = proceedingJoinPoint.proceed();
                    log.info("Завершение выполнения метода: {}", methodName);
                    return result;
                } else {
                    log.warn("Пользователь {} не прошел проверку владения для новости {}", authorId, targetId);
                    throw new AccessDeniedException("Вы не являетесь владельцем данной новости.");
                }
            } else {
                log.error("Неподдерживаемый путь запроса: {}", requestPath);
                throw new UnsupportedOperationException("Неподдерживаемый тип запроса.");
            }
        }

        return null;
    }
}
