package ru.skillbox.rest_news_service.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import ru.skillbox.rest_news_service.entity.Comment;
import ru.skillbox.rest_news_service.entity.News;
import ru.skillbox.rest_news_service.entity.RoleType;
import ru.skillbox.rest_news_service.exception.AccessDeniedException;
import ru.skillbox.rest_news_service.service.AuthorService;
import ru.skillbox.rest_news_service.service.CommentService;
import ru.skillbox.rest_news_service.service.NewsService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class CheckOwnershipAspect {

    private final AuthorService authorService;
    private final NewsService newsService;
    private final CommentService commentService;

    @Around("@annotation(ru.skillbox.rest_news_service.aop.CheckOwnership)")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        String methodName = proceedingJoinPoint.getSignature().getName();

        // Получаем аргументы метода
        Object[] args = proceedingJoinPoint.getArgs();
        UserDetails userDetails = getUserDetailsFromArgs(args);

        if (userDetails == null) {
            log.warn("UserDetails not found in method: {}", methodName);
            throw new AccessDeniedException("Access Denied: User is not authenticated");
        }

        log.info("Method called by user: {}. Role is: {}", userDetails.getUsername(),
                userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")));

        log.info("Начало выполнения метода: {}", methodName);

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String requestPath = request.getRequestURI();  // Получаем путь запроса
            log.info("Путь запроса: {}", requestPath);

            Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            String targetIdStr = Optional.ofNullable(pathVariables.get("id"))
                    .orElseThrow(() -> new IllegalArgumentException("ID parameter is missing in the request"));

            Long targetId = Long.valueOf(targetIdStr);
            log.info("ID объекта: {}", targetId);

            List<String> authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            if (!((requestPath.contains("news") || (requestPath.contains("comment")) && methodName.equals("update")))) {
                // Администратор и модератор могут выполнять любые действия кроме
                // обновления новости, там только владелец в независимости от роли
                if (authorities.contains(RoleType.ROLE_ADMIN.name()) || authorities.contains(RoleType.ROLE_MODERATOR.name())) {
                    log.info("Выполнение метода разрешено для {} (Администратор или Модератор)", authorities);
                    Object result = proceedingJoinPoint.proceed();
                    log.info("Завершение выполнения метода: {}", methodName);
                    return result;
                }
            }


            if (requestPath.contains("/author") && authorities.contains(RoleType.ROLE_USER.name())) {
                log.info("Вызов authorService для обработки запроса");
                Long authorId = authorService.findByUsername(userDetails.getUsername()).getId();

                if (targetId.equals(authorId)) {
                    log.info("Пользователь {} успешно прошел проверку доступа к авторам {}", authorId, targetId);
                    Object result = proceedingJoinPoint.proceed();
                    log.info("Завершение выполнения метода: {}", methodName);
                    return result;
                } else {
                    log.warn("Пользователь {} не прошел проверку доступа к авторам {}", authorId, targetId);
                    throw new AccessDeniedException("Вы не прошли проверку доступа к авторам.");
                }
            } else if (requestPath.contains("/news")) {
                log.info("Вызов newsService для обработки запроса");
                News news = newsService.findNewsById(targetId);
                Long newsAuthorId = news.getAuthor().getId();
                Long authorId = authorService.findByUsername(userDetails.getUsername()).getId();

                if (newsAuthorId.equals(authorId)) {
                    log.info("Пользователь {} успешно прошел проверку владения для новости {}", authorId, targetId);
                    Object result = proceedingJoinPoint.proceed();
                    log.info("Завершение выполнения метода: {}", methodName);
                    return result;
                } else {
                    log.warn("Пользователь {} не прошел проверку владения для новости {}", authorId, targetId);
                    throw new AccessDeniedException("Вы не являетесь владельцем данной новости.");
                }
            } else if (requestPath.contains("/comment")) {
                log.info("Вызов commentService для обработки запроса");
                Comment comment = commentService.findCommentById(targetId);
                Long commentAuthorId = comment.getAuthor().getId();
                Long authorId = authorService.findByUsername(userDetails.getUsername()).getId();

                if (commentAuthorId.equals(authorId)) {
                    log.info("Пользователь {} успешно прошел проверку владения коментарием {}", authorId, targetId);
                    Object result = proceedingJoinPoint.proceed();
                    log.info("Завершение выполнения метода: {}", methodName);
                    return result;
                } else {
                    log.warn("Пользователь {} не прошел проверку владения коментарием {}", authorId, targetId);
                    throw new AccessDeniedException("Вы не являетесь владельцем данного коментария.");
                }
            }
        }

        throw new AccessDeniedException("Request attributes not found");
    }

    private UserDetails getUserDetailsFromArgs(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof UserDetails) {
                return (UserDetails) arg;
            }
        }
        return null;
    }
}